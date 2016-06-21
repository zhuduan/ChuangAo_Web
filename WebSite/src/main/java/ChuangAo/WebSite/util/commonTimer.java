package ChuangAo.WebSite.util;

import java.util.Timer;

import ChuangAo.WebSite.service.FollowOrderService;

public class commonTimer {
	
	public static void timerTasks(){
    	
    	//---follow order service : heartBeat task
    	heartbeatStatusDaemon heartDaemon = new heartbeatStatusDaemon(FollowOrderService.receiverState,
    																	FollowOrderService.senderState,
    																	FollowOrderService.senderHeartBeat,
    																	FollowOrderService.receiverHeartBeat,
    																	FollowOrderService.sendOrders,
    																	FollowOrderService.senderCountDown);
    	Timer timer = new Timer();
    	timer.schedule(heartDaemon, 20000, 1000);	//interval 1000ms
		
		
		//---others
    	
    }
	
	
}
