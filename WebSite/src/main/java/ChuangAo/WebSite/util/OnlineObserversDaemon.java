package ChuangAo.WebSite.util;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import ChuangAo.WebSite.model.ResourceComsumeUser;
import ChuangAo.WebSite.repository.ResourceComsumeUserRepository;



public class OnlineObserversDaemon extends TimerTask  {
	
	private int maxObserversKeepSeconds = 100;
	public ConcurrentHashMap<Integer,Integer> observersCountDown = new ConcurrentHashMap<Integer,Integer>(); 
	
	private OnlineMonitorSubject onlineMonitorSubject = OnlineMonitorSubject.getInstance();
	
	private ResourceComsumeUserRepository resourceComsumeUserRepository; 
	private List<OnlineMonitorObserver> onlineMonitorObservers;
	
	
	private static class OnlineObserversDaemontHolder {  
        private static final OnlineObserversDaemon INSTANCE = new OnlineObserversDaemon();  
    }  
	
	public static final OnlineObserversDaemon getInstance() {  
        return OnlineObserversDaemontHolder.INSTANCE; 
    }  
	
	private OnlineObserversDaemon(){
		//---
		
	};
	
	
	public void keepAlive(Integer userID){
		observersCountDown.put(userID, maxObserversKeepSeconds);
	}
	
	
	public void setRepositoryAndList(ResourceComsumeUserRepository resourceComsumeUserRepository,List<OnlineMonitorObserver> onlineMonitorObservers){
		this.resourceComsumeUserRepository = resourceComsumeUserRepository;
		this.onlineMonitorObservers = onlineMonitorObservers;
	}
	
	
	
	
	
	public void saveUserComsumeData(Integer userID,OnlineMonitorObserver onlineMonitorObserver){
		ResourceComsumeUser resourceComsumeUser =resourceComsumeUserRepository.findByUserid(userID);
		Long passedTime = ((new Date()).getTime() - onlineMonitorObserver.getTimeLong());
		if(resourceComsumeUser==null){
			resourceComsumeUser = new ResourceComsumeUser(userID,onlineMonitorObserver.getMailUseTimes(),
												onlineMonitorObserver.getTextUseTimes(),passedTime);
		} else {
			resourceComsumeUser.setMailUsedTimes(resourceComsumeUser.getMailUsedTimes()+onlineMonitorObserver.getMailUseTimes());
			resourceComsumeUser.setTextUsedTimes(resourceComsumeUser.getTextUsedTimes()+onlineMonitorObserver.getTextUseTimes());
			resourceComsumeUser.setOnlineMonitorTime(resourceComsumeUser.getOnlineMonitorTime()+passedTime);
		}
		resourceComsumeUserRepository.save(resourceComsumeUser);
	}
	
	
	//  remove from the OnlineMonitorSubject
	public void delObserver(Integer userID){
		for(OnlineMonitorObserver onlineMonitorObserver : onlineMonitorObservers){
			if(onlineMonitorObserver.equalsToUserID(userID)==true){
				onlineMonitorObservers.remove(onlineMonitorObserver);	
				onlineMonitorSubject.deleteObserver(onlineMonitorObserver);
				System.out.println("*** remove an observer:" + userID);
				
				//set to the userComsume value
				saveUserComsumeData(userID,onlineMonitorObserver);
			}
		}
	}
	
	
	
	private void decreaseAll(){
		for (Entry<Integer, Integer> entry: observersCountDown.entrySet()) {
			Integer tempValue = entry.getValue();
			Integer tempKey = entry.getKey();
			if(tempValue>0){
				//---still connect
				tempValue=tempValue-30;
				entry.setValue(tempValue);
			} else {
				//---lose heart heartbeat
				delObserver(tempKey);
			}			
		}		
	}

	@Override
	public void run() {
		decreaseAll();
	}
}
