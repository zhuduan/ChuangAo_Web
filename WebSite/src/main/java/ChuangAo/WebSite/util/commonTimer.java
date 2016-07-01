package ChuangAo.WebSite.util;

import java.util.Timer;

import ChuangAo.WebSite.service.FollowOrderService;

public class commonTimer {
	
	public static void timerTasks(){
    	
    	//---follow order service : heartBeat task
    	HeartbeatStatusDaemon heartbeatStatusDaemon = new HeartbeatStatusDaemon(FollowOrderService.receiverState,
    																	FollowOrderService.senderState,
    																	FollowOrderService.senderHeartBeat,
    																	FollowOrderService.receiverHeartBeat,
    																	FollowOrderService.sendOrders,
    																	FollowOrderService.senderCountDown,
    																	FollowOrderService.accountDatas);
    	
    	OnlineObserversDaemon onlineObserversDaemon = OnlineObserversDaemon.getInstance();
    	
    	//--- star timer
    	Timer timer = new Timer();
    	timer.schedule(heartbeatStatusDaemon, 20000, 1000);	//interval 1000ms
    	timer.schedule(onlineObserversDaemon, 1000, 15000);	//interval 15,000ms
		
		
		//---others
    	
    }
	
	
}
