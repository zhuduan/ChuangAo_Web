package ChuangAo.WebSite.util;

import java.util.Date;
import java.util.Observable;
import java.util.Observer;

import org.apache.shiro.session.Session;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import ChuangAo.WebSite.model.User;
import ChuangAo.WebSite.service.FollowOrderService;

public class OnlineMonitorObserver implements Observer {
	
	private static final Logger logger = LoggerFactory.getLogger(OnlineMonitorObserver.class);
	
	private SimpMessagingTemplate messagingTemplate;
	private JavaMailSender mailSender;
	private TextMessageSender textMessageSender = new TextMessageSender();
	public Observable observable; 
	
	private User user;
	private JSONObject ownAccounts;
	private Long textUseTimes = 0L;
	private Long mailUseTimes = 0L;
	private Long timeLong;
	private Integer noticePunishment = 10;	//5 times of status changed
	
	private Integer changedAccountID;
	private Integer changedAccountStatus;
	private Integer changedAccountType;
	
	public OnlineMonitorObserver(User user,Observable observable,SimpMessagingTemplate messagingTemplate,JavaMailSender mailSender,Long timeLong){
		this.user = user;
		this.ownAccounts = new JSONObject(user.getOwnAccounts());
		this.observable = observable;
		this.messagingTemplate = messagingTemplate;
		this.mailSender = mailSender;
		this.timeLong = timeLong;
		observable.addObserver(this);
	}
	
	public OnlineMonitorObserver(){
		
	}
	
	public Boolean equalsToUserID(Integer ID){
		if(ID==user.getId()){
			return true;
		}
		return false;
	}

	@Override
	public void update(Observable obs, Object arg) {
		if(obs instanceof OnlineMonitorSubject){
			OnlineMonitorSubject onlineMonitorSubject = (OnlineMonitorSubject)obs;
			changedAccountID = onlineMonitorSubject.getAccountID();
			if(ownAccounts.has(changedAccountID.toString())==false){
				return;		//do not have this account
			}
			changedAccountStatus = onlineMonitorSubject.getStatus();
			changedAccountType = onlineMonitorSubject.getAccountType();
			String desUrl = "/queue/commonService/onlineMonitor/"+user.getId();
			JSONObject jsonValue = new JSONObject();
			if(hasOnlineMonitorAuthority()==true){
				//send to the frontpage
				jsonValue.put("accountID", changedAccountID);
				jsonValue.put("accountStatus", changedAccountStatus);
				jsonValue.put("accountType", changedAccountType);				
			} else{
				jsonValue.put("accountID", changedAccountID);
				jsonValue.put("accountStatus", 0);
				jsonValue.put("accountType", 0);	
			}
			if(hasDataStatisticAuthority()==true){
				if(FollowOrderService.accountDatas.containsKey(changedAccountID)==true){
					JSONObject tempJson = FollowOrderService.accountDatas.get(changedAccountID);
					jsonValue.put("balance", tempJson.get("equity")+"/"+tempJson.get("balance"));
					jsonValue.put("lots", tempJson.get("buyLots")+"/"+tempJson.get("sellLots")+"/"+tempJson.get("totalLots"));
					//System.out.println("balance: "+tempJson);
				} else {
					jsonValue.put("balance", "数据统计中");				
					jsonValue.put("lots", "数据统计中");
				}
			} else {
				jsonValue.put("balance", "未开通");				
				jsonValue.put("lots", "未开通");
			}
			send(desUrl, jsonValue.toString());
			//---notice 
			sendNotice(user.getNoticeType());
			
		}
	}
	
	
	private boolean hasOnlineMonitorAuthority(){
		//--- the last position used as online authority, 
		// which is anything & 0001, the result if equals 1 means has the authority
		// 000, first stand for feedback, second stand for dataStatistic, third stand for onlineMonitor
		if((ownAccounts.getInt(changedAccountID.toString())&1)==1){		//is 001 = 1
			return true;
		}		
		return false;
	}
	
	private boolean hasDataStatisticAuthority(){
		//--- the last position used as online authority, 
		// which is anything & 0001, the result if equals 1 means has the authority
		// 000, first stand for feedback, second stand for dataStatistic, third stand for onlineMonitor
		if((ownAccounts.getInt(changedAccountID.toString())&2)==2){		//is 010 = 2
			return true;
		}
		return false;
	}
	
	
	private void send(String desUrl, String value){
		try{	
			messagingTemplate.convertAndSend(desUrl, value);
		}
		catch(Exception e){
			System.out.println("socket send error message:"+e.getMessage());
			System.out.println("socket send error toString:"+e.toString());
			e.printStackTrace();
		}
	}	
	
	
	private void sendSimpleEmail(String desAddress, String subject, String content){
		 SimpleMailMessage message = new SimpleMailMessage();
	      
	     message.setFrom("2570278383@qq.com");//发送者.
	     message.setTo(desAddress);//接收者.
	     message.setSubject(subject);//邮件主题.
	     message.setText(content);//邮件内容.
	     
	     try{
	    	 mailSender.send(message);//发送邮件
	     }
	     catch(Exception e){
	    	 logger.debug("send mail fail: "+e.getMessage());
	    	 e.printStackTrace();
	     }
	}
	
	private void sendNotice(int noticeType){
		if(changedAccountStatus==4 || changedAccountStatus==7){
			String subject = "您的"+changedAccountID+"账号已掉线，请检查！";			
			String reason;
			if(changedAccountStatus==4 ){
				reason = "与交易商服务器断开";											
			} else{
				reason = "EA访问网络异常";	
			}
			String content = "检测到您的账号"+changedAccountID+"已经掉线，原因为：'"+reason+"'。 建议您尽快检查该账号。";	
			if(FollowOrderService.textNoticeFlag.containsKey(changedAccountID)==true){
				//send email and message	
				switch(noticeType){
					case 0:
						//---do nothing
						break;
					case 1:
						//only email
						sendSimpleEmail(user.getEmail(),subject,content);
						mailUseTimes++;
						break;
					case 2:
						//only text message
						textMessageSender.sendNoticeText(user.getName(), changedAccountID, reason, user.getTelephone());
						textUseTimes++;
						break;
					case 3:
						//both
						sendSimpleEmail(user.getEmail(),subject,content);
						textMessageSender.sendNoticeText(user.getName(), changedAccountID, reason, user.getTelephone());
						mailUseTimes++;
						textUseTimes++;
						break;
					default:
						break;
				}		
				//remove the record
				FollowOrderService.textNoticeFlag.remove(changedAccountID);
			} else {
				noticePunishment = 10;		//recover the puniment 
			}
		} else{
			if(FollowOrderService.textNoticeFlag.containsKey(changedAccountID)==false){
				//add to the notify list
				if(noticePunishment>0){
					noticePunishment--;
				} else {
					FollowOrderService.textNoticeFlag.put(changedAccountID,1);
					noticePunishment = 10;
				}
			}
		}
	}
	
	public Long getTimeLong(){
		return timeLong;
	}
	
	public Long getTextUseTimes(){
		return textUseTimes;
	}
	
	public Long getMailUseTimes(){
		return mailUseTimes;
	}
	
	public void clearComsumeData(){
		timeLong= (new Date()).getTime();
		textUseTimes= 0L;
		mailUseTimes= 0L;
	}
}
