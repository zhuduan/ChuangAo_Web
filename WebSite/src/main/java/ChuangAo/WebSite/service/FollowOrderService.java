package ChuangAo.WebSite.service;

import ChuangAo.WebSite.model.Follow;
import ChuangAo.WebSite.repository.FollowRepository;
import ChuangAo.WebSite.repository.UserRepository;
import ChuangAo.WebSite.util.OnlineMonitorSubject;
import ChuangAo.WebSite.util.senderStatusChangeDaemon;

import java.util.ArrayList;
import java.util.Date;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class FollowOrderService {
	
	@Autowired
	private FollowRepository followRepository;
	
	@Autowired
	private UserRepository userRepository;	
	
	/***
	 * store the list info 
	 * ? -> but not think clearly, whether there is problem about the memory when the sender and list are too big
	 */
	public static ConcurrentHashMap<Integer,ConcurrentHashMap<String,String>> sendOrders = new ConcurrentHashMap<Integer,ConcurrentHashMap<String,String>>();
	
	// 1：鉴权，   2：监听，  3：变化, 4：和trader断开, 5:feedback, 6:无订单, 7:丢失心跳
	public static ConcurrentHashMap<Integer,Integer> receiverState = new ConcurrentHashMap<Integer,Integer>();
	
	// 1: 鉴权，  2：连接，  3：变化，  4：和trader断开, 5:feedback, 6:无订单, 7:丢失心跳
	public static ConcurrentHashMap<Integer,Integer> senderState = new ConcurrentHashMap<Integer,Integer>();
	
	//--- statistic all datas
	public static ConcurrentHashMap<Integer,JSONObject> accountDatas = new ConcurrentHashMap<Integer,JSONObject>();	
	
	//--- inner variables
	private static int maxSleepSeconds = 10;
	public static ConcurrentHashMap<Integer,Integer> senderCountDown = new ConcurrentHashMap<Integer,Integer>();
	
	private static int heartBeatExpireSeconds = 26;
	public static ConcurrentHashMap<Integer,Integer> senderHeartBeat = new ConcurrentHashMap<Integer,Integer>();
	public static ConcurrentHashMap<Integer,Integer> receiverHeartBeat = new ConcurrentHashMap<Integer,Integer>();
		
	public static ConcurrentHashMap<Integer,Integer> textNoticeFlag= new ConcurrentHashMap<Integer,Integer>();
	
	private static Logger logger = Logger.getLogger(FollowOrderService.class);
	/**
	 * @return whether the receiver should return the updated info
	 */
	private String getReceiverCheckResult(Integer senderID, Integer receiverID,JSONObject data){
		//e.g. { "name": "7767180_1449565020_98225687", "priceNow": 1.50258, "lots": 0.21, "symbol": "GBPUSDm", "operationType": -11, "orderType": 0, "nowTicket": 98225896,"timelong": 1449565020,"accountID": 7767180} ;
		
		StringBuffer result = new StringBuffer();
		Integer dealingStats = 0;	// 0:初始值，  1：正常跟单处理，   2：正常单不处理，   -1：跟单号不存在，  -2:喊单号已断连   ，  -99：未知错误
		result.append("{ ");
		if(senderState.containsKey(senderID) == false){
			dealingStats = -1;
		} else {
			switch(senderState.get(senderID)){
				case 1:
					dealingStats = 2;
					break;
				case 2:
					dealingStats = 2;
					receiverState.put(receiverID, 2);
					break;
				case 3:
					dealingStats = 1;
					result.append(jointInfoStr(senderID));
					receiverState.put(receiverID, 3);
					accountDatas.put(receiverID, data);
					OnlineMonitorSubject.getInstance().setChangedAccountStatus(receiverID, 3, 2);
					break;
				case 4:
					dealingStats = -2;
					break;
				case 5:
					dealingStats = 2;
					break;
				case 6:
					dealingStats = 2;
					break;
				default:
					dealingStats = -99;
					break;
			}
		}
		result.append(" \"dealingStats\" : ");
		result.append(dealingStats);
		result.append(" }");
		return result.toString();
	}
	
	private String jointInfoStr(Integer senderID){
		ConcurrentHashMap<String,String> orderDetails = sendOrders.get(senderID);
		StringBuffer tempResult = new StringBuffer();
		tempResult.append("\"totalOrders\":");
		tempResult.append(orderDetails.size());
		tempResult.append(", ");
		Integer tempCount = 0;
		for (Entry<String, String> entry: orderDetails.entrySet()) {
			tempResult.append("\"");
			tempResult.append("order_"+tempCount.toString());
			tempResult.append("\" : ");
			String tempValue = entry.getValue().toString();
			tempResult.append(tempValue);
			tempResult.append(", ");	
			tempCount++;
		}
		return tempResult.toString();
	}
	
	
	public String updateReceiverState(String receiverInfo){
		JSONObject json = new JSONObject(receiverInfo);
		int requestType = json.getInt("RequestType");
		int senderID = json.getInt("SenderID");
		int receiverID = json.getInt("AccountID");			
		String resultStr = "";
		switch(requestType){
			case 1:
				int tempResult = authority(1, receiverID, senderID);
				json.put("dealingStats",tempResult);
				resultStr = json.toString();
				if(tempResult>0){
					receiverState.put(receiverID, 1);
					accountDatas.put(receiverID, json.getJSONObject("accountData"));
					OnlineMonitorSubject.getInstance().setChangedAccountStatus(receiverID, 1, 2);
				}
				break;
			case 2:
				//listing				
				resultStr = getReceiverCheckResult(senderID,receiverID,json.getJSONObject("accountData"));				
				break;
			case 3:
				//listening too
				break;
			case 4:			
				json.put("dealingStats",1);
				resultStr = json.toString();
				receiverState.put(receiverID, 4);
				accountDatas.put(receiverID, json.getJSONObject("accountData"));
				OnlineMonitorSubject.getInstance().setChangedAccountStatus(receiverID, 4, 2);
				break;
			case 5:
				//feedback the statistic datas 
				receiverState.put(receiverID, 5);
				accountDatas.put(receiverID, json.getJSONObject("accountData"));
				OnlineMonitorSubject.getInstance().setChangedAccountStatus(receiverID, 5, 2);
				break;
			case 6:
				//no order - TODO
				break;
			default:
				//log error
				break;
		}
		receiverHeartBeat.put(receiverID, heartBeatExpireSeconds);
		return resultStr;
	}	
	
	
	
	/**
	 * 
	 * @return update the map successfully or not, 
	 * 		   0 for default unknown
	 * 		   1 for request 1 normal return,
	 * 		   2 for old orders and not dealing,
	 * 		   3 for normal close return,
	 * 	       4 for normal request 1 info new open return,
	 * 	       5 for normal request 2 no key return,
	 * 	       6 for normal request 2 state 3 return,
	 * 		   7 for request 4 return,
	 *         8 for request 5 return,
	 * 	       11 for normal request 2 normal return,
	 * 	       12 for normal request 2 clear all return,
	 */
	public String updateSenderState(String senderInfo){
		JSONObject json = new JSONObject(senderInfo);
		Integer requestType = json.getInt("requestType");		//0 for authority, 1 for normal sending,  2 for update info, 3 for feedback, 4 for exit
		Integer totalOrderNum = json.getInt("totalOrderNum");
		Integer senderID = json.getInt("accountID");
		accountDatas.put(senderID,json.getJSONObject("accountData"));
		json.put("result", 0);
		switch(requestType){
			case 0:
				//---authority
				int tempResult = authority(2, senderID, senderID);
				json.put("result",tempResult);
				if(tempResult>0){
					senderState.put(senderID,1);
					OnlineMonitorSubject.getInstance().setChangedAccountStatus(senderID, 1, 1);	
				}
				break;
			case 1:
				//---normal sender
				json.put("result",senderInfoChange(json.getJSONObject("orders"),senderID));				
				break;
			case 2:
				//---update info
				if(senderState.containsKey(senderID)==false){
					json.put("result", 5);
					if(totalOrderNum>0){
						//---somewhere wrong, should update the info
						JSONObject ordersInfo = json.getJSONObject("orders");
						senderInfoPeriodUpdate(ordersInfo,totalOrderNum,senderID);
					}
					break;
				}
				if(senderState.get(senderID)==3){
					//do nothing when the sender state is 3(change)
					json.put("result", 6);
					break;
				}
				JSONObject ordersInfo = json.getJSONObject("orders");
				//System.out.println("ordersInfo: "+ordersInfo.toString());
				json.put("result",senderInfoPeriodUpdate(ordersInfo,totalOrderNum,senderID));
				break;
			case 3:
				//---send too
				break;
			case 4:
				if(senderState.containsKey(senderID)==true){
					senderState.put(senderID,4);
					OnlineMonitorSubject.getInstance().setChangedAccountStatus(senderID, 4, 1);	
					json.put("result",7);
				}
				break;
			case 5:
				if(senderState.containsKey(senderID)==true){
					senderState.put(senderID,5);
					OnlineMonitorSubject.getInstance().setChangedAccountStatus(senderID, 5, 1);
					json.put("result",8);
				}
				break;
			case 6:
				//---no order already did in case 2
				break;
			default:
				break;
		}	
		//---heartBeat continue
		senderHeartBeat.put(senderID, heartBeatExpireSeconds);
		return json.toString();
	}
	
	/*** @Des:
	 *** @return: -2 for authority fail, -1 for unknown err, 1 for add new info, 2 for no need to do, 3 for info close info update, 4 for open info update
	 */
	private Integer senderInfoChange(JSONObject senderInfo,Integer senderID){
		JSONObject json = senderInfo;		
		String orderName = json.getString("name");
		Integer operationType = json.getInt("operationType");
		if(sendOrders.containsKey(senderID)==false){			
			//close operation
			if(operationType<0){
				//do nothing
				return 2;
			}
			
			ConcurrentHashMap<String,String> orderDetails = new ConcurrentHashMap<String,String>();
			orderDetails.put(orderName, senderInfo.toString());
			//System.out.println("senderInfo: "+senderInfo.toString());
			sendOrders.put(senderID, orderDetails);			
			
			// modify the sender status
			senderStatusChangeDaemon fsDaemon = new senderStatusChangeDaemon(senderID, maxSleepSeconds,operationType,orderName, senderCountDown, senderState,orderDetails);
			FutureTask<Integer> futureTask = new FutureTask<Integer>(fsDaemon);
			ExecutorService executor = Executors.newSingleThreadExecutor();
			try {
				executor.execute(futureTask);
				executor.shutdown();
			} catch (Exception e) {
				logger.error(e.getMessage());
				e.printStackTrace();
			}		
			return 1;
		} else{
			ConcurrentHashMap<String,String> orderDetails = sendOrders.get(senderID);
			orderDetails.put(orderName, senderInfo.toString());
							
			// modify the sender status
			senderStatusChangeDaemon fsDaemon = new senderStatusChangeDaemon(senderID, maxSleepSeconds,operationType,orderName, senderCountDown, senderState,orderDetails);
			FutureTask<Integer> futureTask = new FutureTask<Integer>(fsDaemon);
			ExecutorService executor = Executors.newSingleThreadExecutor();
			try {
				executor.execute(futureTask);
				executor.shutdown();				
			} catch (Exception e) {
				logger.error(e.getMessage());
				e.printStackTrace();
			}			
			if((operationType == -1) || (operationType == -2)){
				return 3;
			}
			return 4;
		}
	}
	
	/***
	 * 
	 * @param ordersInfo
	 * @param totalOrderNum
	 * @param senderID
	 * @return 11 for normal, 12 for clear all
	 */
	private Integer senderInfoPeriodUpdate(JSONObject ordersInfo,Integer totalOrderNum,Integer senderID){
		//---
		if(totalOrderNum==0){
			sendOrders.remove(senderID);
			senderState.put(senderID,6);
			OnlineMonitorSubject.getInstance().setChangedAccountStatus(senderID, 6, 1);			
			senderCountDown.put(senderID,0);
			return 12;
		}
		
		if(sendOrders.containsKey(senderID)==false){
			//---if server is disconnect first, or the heartbeat disconnect
			sendOrders.put(senderID,new ConcurrentHashMap<String,String>());
		} 
		ConcurrentHashMap<String,String> orderDetails = sendOrders.get(senderID);
		//---update all info here
		orderDetails.clear();
		for(int i=0;i<totalOrderNum;i++){
			JSONObject tempOrder = ordersInfo.getJSONObject("order_"+i);
			String orderName = tempOrder.getString("name");			
			orderDetails.put(orderName, tempOrder.toString());
		}
		if(senderState.containsKey(senderID)==false){
			senderState.put(senderID,2);
			OnlineMonitorSubject.getInstance().setChangedAccountStatus(senderID, 2, 1);	
		}
		return 11;
	}
	
	/***
	 * 
	 * @param accountType  : 1 for receiver, 2 for send
	 * @param accountID	  : this account id
	 * @param followerID  : the sender which one should follow (when its-self is sender, just set as account id)
	 * @return
	 */
	private int authority(int accountType, int accountID, int followerID){
		//---
		Follow follow =followRepository.findByAccountID(accountID);
		if(follow==null){
			return -1;	//no that account
		}
		if(accountType>follow.getAccountType()){
			return -2;	//the account type is not satisfied
		}
		
		if(accountType==1){
			//--- should do receive
			JSONObject json = new JSONObject(follow.getFollowAccount());
			JSONArray canFollow = json.getJSONArray("ids");
			Boolean inTheFollowList = false;
			for(int i=0;i<canFollow.length();i++){
				if(canFollow.getJSONObject(i).getInt("id")==followerID){
					inTheFollowList = true;
					break;
				}
			}
			if(inTheFollowList==false){
				return -3;	//can not follow the account which is pointed to
			}
		}
		
		Date currentTime = new Date();
		if(follow.getExpireTime().before(currentTime)==true){
			//System.out.println("db:"+follow.getExpireTime());
			//System.out.println("now:"+currentTime);
			return -4;	//exceed the expire time
		}
		
		if(accountType==1){
			//as receiver
			follow.setAsReceiver((follow.getAsReceiver()+1));
		} else {
			//as sender
			follow.setAsSender((follow.getAsSender()+1));
		}
		followRepository.save(follow);		
		return 1;
	}
	
	public List<Follow> getAccountsByUserID(Integer userID){
		JSONObject ownAccountsJson = new JSONObject(userRepository.findByid(userID).getOwnAccounts());
		Iterator<String> iterator = ownAccountsJson.keys();
		List<Integer> ownAccountsList = new ArrayList<Integer>();
		while(iterator.hasNext()){
			Integer key = Integer.parseInt(iterator.next()); 
			ownAccountsList.add(key);
		}
		return followRepository.findInAccountIDList(ownAccountsList);
	}
}
