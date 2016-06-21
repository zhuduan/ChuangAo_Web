package ChuangAo.WebSite.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import ChuangAo.WebSite.repository.UserRepository;
import ChuangAo.WebSite.util.OnlineMonitorObserver;
import ChuangAo.WebSite.util.OnlineMonitorSubject;

@Service
@Transactional
public class OnlineMonitorService {
	
	private List<OnlineMonitorObserver> onlineMonitorObservers = new LinkedList<OnlineMonitorObserver>();
	private OnlineMonitorSubject onlineMonitorSubject = OnlineMonitorSubject.getInstance();
	
	@Autowired
	private UserRepository userRepository; 
	
	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	
	
	//---when connect
	//  remove from the OnlineMonitorSubject
	public void delObserver(Integer userID){
		for(OnlineMonitorObserver onlineMonitorObserver : onlineMonitorObservers){
			if(onlineMonitorObserver.equalsToUserID(userID)==true){
				Integer leftNum = onlineMonitorObserver.delOneMonitor();
				if(leftNum<=0){
					onlineMonitorObservers.remove(onlineMonitorObserver);
				}
				//--- whether should profile for GC
				System.out.println("### delete a observer:" + userID +", left:"+leftNum);
			}
		}
	}
	
	
	//---when disconnect
	//  add to the list and OnlineMonitorSubject
	public void addObserver(Integer userID){
		for(OnlineMonitorObserver onlineMonitorObserver : onlineMonitorObservers){
			if(onlineMonitorObserver.equalsToUserID(userID)==true){
				//---no need to add new one, already contains				
				Integer leftNum =onlineMonitorObserver.addToExistMonitor();
				System.out.println("### already has a observer:" + userID + ", left:"+leftNum);
				return;
			}
		}
		//---do not contains, should add a new one
		String ownAccountsJson = userRepository.findByid(userID).getOwnAccounts();
		OnlineMonitorObserver obs = new OnlineMonitorObserver(userID,ownAccountsJson,onlineMonitorSubject,messagingTemplate);
		onlineMonitorObservers.add(obs);
	}
	
	
	
	//---when status change occur
	// notify the defined observer
	public void updateOnlieMonitor(Integer AccountID, Integer Status, Integer AccountType){
		onlineMonitorSubject.setChangedAccountStatus(AccountID,Status,AccountType);
	}
	
	
	//---when first initial the page
	//  actually can just use Ajax request this method to complete the function
	public void initialOnlineMonitor(Integer userID,ModelMap map){
		JSONObject ownAccountsJson = new JSONObject(userRepository.findByid(userID).getOwnAccounts());
		HashMap<Integer,Integer> senderAccountMap = new HashMap<Integer, Integer>();
		HashMap<Integer,Integer> receiverAccountMap = new HashMap<Integer, Integer>();
		HashMap<Integer,Integer> unknowAccountMap = new HashMap<Integer, Integer>();
		Iterator<String> iterator = ownAccountsJson.keys();
		while(iterator.hasNext()){
			Integer key = Integer.parseInt(iterator.next()); 
			Boolean isUnknow = true;
            if(FollowOrderService.senderState.containsKey(key)==true){
            	senderAccountMap.put(key, FollowOrderService.senderState.get(key));
            	isUnknow = false;
            }
            if(FollowOrderService.receiverState.containsKey(key)==true){
            	receiverAccountMap.put(key, FollowOrderService.receiverState.get(key));
            	isUnknow = false;
            } 
            if(isUnknow==true){
            	unknowAccountMap.put(key, 0);
            }
		}
		map.put("senderMap", senderAccountMap);
		map.put("receiverMap", receiverAccountMap);
		map.put("unknowMap", unknowAccountMap);		
	}

}
