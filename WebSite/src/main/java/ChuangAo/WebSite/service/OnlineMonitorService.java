package ChuangAo.WebSite.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import ChuangAo.WebSite.model.User;
import ChuangAo.WebSite.repository.ResourceComsumeUserRepository;
import ChuangAo.WebSite.repository.UserRepository;
import ChuangAo.WebSite.util.OnlineMonitorObserver;
import ChuangAo.WebSite.util.OnlineMonitorSubject;
import ChuangAo.WebSite.util.OnlineObserversDaemon;

@Service
@Transactional
public class OnlineMonitorService {
	
	private List<OnlineMonitorObserver> onlineMonitorObservers = new LinkedList<OnlineMonitorObserver>();
	private OnlineMonitorSubject onlineMonitorSubject = OnlineMonitorSubject.getInstance();
	
	@Autowired
	private UserRepository userRepository; 
	
	@Autowired
	private ResourceComsumeUserRepository resourceComsumeUserRepository; 	
	
	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	
	@Autowired
	private JavaMailSender mailSender;
	
	private Boolean activeDaemon = false;
	
	//  add to the list and OnlineMonitorSubject
	public void addObserver(Integer userID){
		OnlineObserversDaemon.getInstance().keepAlive(userID);
		for(OnlineMonitorObserver onlineMonitorObserver : onlineMonitorObservers){
			if(onlineMonitorObserver.equalsToUserID(userID)==true){
				//---no need to add new one, already contains					
				System.out.println("### already has an observer:" + userID);
				return;
			}
		}
		//---do not contains, should add a new one
		User user = userRepository.findByid(userID);
		OnlineMonitorObserver obs = new OnlineMonitorObserver(user,onlineMonitorSubject,messagingTemplate,mailSender,(new Date()).getTime());
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
		//---just set once to the Daemon
		if(activeDaemon==false){
			OnlineObserversDaemon.getInstance().setRepositoryAndList(resourceComsumeUserRepository, onlineMonitorObservers);
			activeDaemon = true;
		}
		JSONObject ownAccountsJson = new JSONObject(userRepository.findByid(userID).getOwnAccounts());
		HashMap<Integer,Integer> senderAccountMap = new HashMap<Integer, Integer>();
		HashMap<Integer,Integer> receiverAccountMap = new HashMap<Integer, Integer>();
		HashMap<Integer,Integer> unknowAccountMap = new HashMap<Integer, Integer>();
		Iterator<String> iterator = ownAccountsJson.keys();
		while(iterator.hasNext()){
			Integer key = Integer.parseInt(iterator.next()); 
			Integer value = ownAccountsJson.getInt(key.toString());
			if(1==(value&1)){
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
			} else {
				unknowAccountMap.put(key, -1);
			}
		}
		map.put("senderMap", senderAccountMap);
		map.put("receiverMap", receiverAccountMap);
		map.put("unknowMap", unknowAccountMap);		
	}

}
