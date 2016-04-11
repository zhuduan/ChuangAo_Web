package ChuangAo.WebSite.util;

import java.util.concurrent.ConcurrentHashMap;

import java.util.concurrent.ScheduledThreadPoolExecutor;  
import java.util.concurrent.TimeUnit;  

public class followerStatusUtil {
	
	public followerStatusUtil(){};
	
	
	/***
	 * 
	 * @param senderID
	 * @param maxSleepSeconds
	 * @param senderCountDown
	 * @param senderState
	 * @return 
	 * 		1 for normal return,
	 * 		-1 for changed by other,
	 * 		2 for guard by other thread,
	 * 		0 for default dealing,
	 * 		3 for changed by other, and guard by other
	 * 		
	 */
	public static int guardSenderStatus(int senderID, 
								int maxSleepSeconds, 
								ConcurrentHashMap<Integer,Integer> senderCountDown,
								ConcurrentHashMap<Integer,Integer> senderState){
		if(senderCountDown.containsKey(senderID)==false){
			senderState.put(senderID, 3);
			senderCountDown.put(senderID, maxSleepSeconds);
			int tempCount = maxSleepSeconds;
			for(int i=1;i<=maxSleepSeconds;i++){
				try {
					//sleep 1 second per time
					Thread.sleep(1000);					
					if(senderCountDown.get(senderID)>tempCount){
						//changed by other thread
						return 3;	//changed by other, and guard by other
					}
					tempCount = maxSleepSeconds-i;
					senderCountDown.put(senderID, tempCount);
					//System.out.println("Countdown "+senderID+": "+tempCount);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(senderCountDown.get(senderID)<=0){
				senderState.put(senderID, 2);
				senderCountDown.remove(senderID);
				return 1;	//1 for normal return
			} else {
				//should be changed by other thread 
				return -1;	//-1 for changed by other
			}
		} else {
			if(senderCountDown.get(senderID)>=maxSleepSeconds){
				return 2;	//2 for guard by other thread;
			} else {
				senderState.put(senderID, 3);
				senderCountDown.put(senderID, maxSleepSeconds);
				int tempCount = maxSleepSeconds;
				for(int i=1;i<=maxSleepSeconds;i++){
					try {
						//sleep 1 second per time
						Thread.sleep(1000);						
						if(senderCountDown.get(senderID)>tempCount){
							//changed by other thread
							return 3;	//changed by other, and guard by other
						}
						tempCount = maxSleepSeconds-i;
						senderCountDown.put(senderID, tempCount);
						//System.out.println("Countdown "+senderID+": "+tempCount);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(senderCountDown.get(senderID)<=0){
					senderState.put(senderID, 2);
					senderCountDown.remove(senderID);
					return 1;	//1 for normal return
				} else {
					//should be changed by other thread 
					return -1;	//-1 for changed by other
				}
			}
		}
		//return 0;	//0 for default dealing
	}
    
}
