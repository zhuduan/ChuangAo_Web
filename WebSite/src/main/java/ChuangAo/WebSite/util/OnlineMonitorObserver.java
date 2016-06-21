package ChuangAo.WebSite.util;

import java.util.Observable;
import java.util.Observer;

import org.json.JSONObject;
import org.springframework.messaging.simp.SimpMessagingTemplate;

public class OnlineMonitorObserver implements Observer {
	
	private SimpMessagingTemplate messagingTemplate;
	
	private Integer currentUser = 1;
	private Integer userID;
	private JSONObject ownAccounts;
	public Observable observable; 
	
	private Integer changedAccountID;
	private Integer changedAccountStatus;
	private Integer changedAccountType;
	
	public OnlineMonitorObserver(Integer ID, String json, Observable observable, SimpMessagingTemplate messagingTemplate){
		this.userID = ID;
		this.ownAccounts = new JSONObject(json);		//TODO: check the performence of JSON ok or not
		this.observable = observable;
		this.messagingTemplate = messagingTemplate;
		observable.addObserver(this);
	}
	
	public OnlineMonitorObserver(){
		
	}
	
	public Integer getCurrentUser(){
		return currentUser;
	}
	
	public Integer addToExistMonitor(){
		currentUser++;
		return currentUser;
	}
	
	public Integer delOneMonitor(){
		if(currentUser<=1){
			observable.deleteObserver(this);
		}
		currentUser--;
		return currentUser;
	}
	
	public Boolean equalsToUserID(Integer ID){
		if(ID==userID){
			return true;
		}
		return false;
	}

	@Override
	public void update(Observable obs, Object arg) {
		if(obs instanceof OnlineMonitorSubject){
			OnlineMonitorSubject onlineMonitorSubject = (OnlineMonitorSubject)obs;
			changedAccountID = onlineMonitorSubject.getAccountID();
			changedAccountStatus = onlineMonitorSubject.getStatus();
			changedAccountType = onlineMonitorSubject.getAccountType();
			String desUrl = "/ws/topic/commonService/onlineMonitor/"+userID;
			JSONObject jsonValue = new JSONObject();
			if(hasOnlineMonitorAuthority()==true){
				//send to the fontpage
				jsonValue.put("accountID", changedAccountID);
				jsonValue.put("accountStatus", changedAccountStatus);
				jsonValue.put("accountType", changedAccountType);				
			} else{
				jsonValue.put("accountID", changedAccountID);
				jsonValue.put("accountStatus", 0);
				jsonValue.put("accountType", 0);	
			}
			send(desUrl, jsonValue.toString());
		}
	}
	
	
	private boolean hasOnlineMonitorAuthority(){
		if(ownAccounts.has(changedAccountID.toString())==true){
			if(ownAccounts.getInt(changedAccountID.toString())>0){
				//---ok
				return true;
			}
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
		//TODO: see if any exception throws, if disconnect from the client
	}	
	
}
