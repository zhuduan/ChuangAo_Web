package ChuangAo.WebSite.util;

import java.util.Date;
import java.util.Map.Entry;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;


public class heartbeatStatusDaemon extends TimerTask  {
	
	// 1：鉴权，   2：监听，  3：变化，  4：断开， 5：feedback, 6:无订单
	private ConcurrentHashMap<Integer,Integer> receiverState;
	
	// 1: 鉴权，  2：连接，  3：变化，  4：断开, 5: feedback, 6:无订单
	private ConcurrentHashMap<Integer,Integer> senderState;
	
	
	private ConcurrentHashMap<Integer,Integer> senderHeartBeat;
	private ConcurrentHashMap<Integer,Integer> receiverHeartBeat;
	
	private ConcurrentHashMap<Integer,ConcurrentHashMap<String,String>> sendOrders;
	private ConcurrentHashMap<Integer,Integer> senderCountDown;

	
	public heartbeatStatusDaemon(ConcurrentHashMap<Integer,Integer> receiverState,
								ConcurrentHashMap<Integer,Integer> senderState,
								ConcurrentHashMap<Integer,Integer> senderHeartBeat,
								ConcurrentHashMap<Integer,Integer> receiverHeartBeat,
								ConcurrentHashMap<Integer,ConcurrentHashMap<String,String>> sendOrders,
								ConcurrentHashMap<Integer,Integer> senderCountDown){
		//---
		this.receiverState = receiverState;
		this.senderState = senderState;
		this.senderHeartBeat = senderHeartBeat;
		this.receiverHeartBeat = receiverHeartBeat;
		this.sendOrders = sendOrders;
		this.senderCountDown = senderCountDown;
	};
	
	
	private void decreaseAll(){
		for (Entry<Integer, Integer> entry: senderHeartBeat.entrySet()) {
			Integer tempValue = entry.getValue();
			Integer tempKey = entry.getKey();
			if(tempValue>0){
				//---still connect
				tempValue--;
				entry.setValue(tempValue);
			} else {
				//---lose heart heartbeat
				if(senderState.containsKey(tempKey)==true){
					//TODO: do not change the status, just write back to database
					//senderState.put(tempKey, 7);		//update the status to disconnect
					OnlineMonitorSubject.getInstance().setChangedAccountStatus(tempKey, 7, 1);
					
					senderHeartBeat.remove(tempKey,tempValue);
					sendOrders.remove(tempKey);
					senderCountDown.remove(tempKey);
					senderState.remove(tempKey);					
					
					Date currentTime = new Date();
					System.out.println("lost sender heartbeat: ID-"+tempKey+", time:"+currentTime.toString());
				}
			}
			//System.out.println("sender heartbeat: ID-"+tempKey+", timeLeft:"+tempValue);
		}
		
		for (Entry<Integer, Integer> entry: receiverHeartBeat.entrySet()) {
			Integer tempValue = entry.getValue();
			Integer tempKey = entry.getKey();
			if(tempValue>0){
				//---still connect
				tempValue--;
				entry.setValue(tempValue);
			} else {
				//---lose heart heartbeat
				if(receiverState.containsKey(tempKey)==true){
					//TODO: do not change the status, just write back to database
					//receiverState.put(tempKey, 7);		//update the status to disconnect
					OnlineMonitorSubject.getInstance().setChangedAccountStatus(tempKey, 7, 2);
					
					receiverHeartBeat.remove(tempKey,tempValue);
					receiverState.remove(tempKey);
					
					Date currentTime = new Date();
					System.out.println("lost receiver heartbeat: ID-"+tempKey+", time:"+currentTime.toString());
				}
			}
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		decreaseAll();
	}
}
