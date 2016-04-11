package ChuangAo.WebSite.service;

import ChuangAo.WebSite.util.followerStatusDaemon;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import org.json.JSONObject;

public class followOrderService {
	
	/***
	 * store the list info 
	 * ? -> but not think clearly, whether there is problem about the memory when the sender and list are too big
	 */
	protected static ConcurrentHashMap<Integer,ConcurrentHashMap<String,String>> sendOrders = new ConcurrentHashMap<Integer,ConcurrentHashMap<String,String>>();
	
	// 1：鉴权，   2：监听，  3：断开，  4：feedback:
	protected static ConcurrentHashMap<Integer,Integer> receiverState = new ConcurrentHashMap<Integer,Integer>();
	
	// 1: 鉴权，  2：连接，  3：变化，  4：断开
	protected static ConcurrentHashMap<Integer,Integer> senderState = new ConcurrentHashMap<Integer,Integer>();
	
	
	//--- inner variables
	private static int maxSleepSeconds = 5; 
	private static ConcurrentHashMap<Integer,Integer> senderCountDown = new ConcurrentHashMap<Integer,Integer>();
	
	/**
	 * @return whether the receiver should return the updated info
	 */
	static String getReceiverCheckResult(Integer senderID){
		//e.g. { "name": "7767180_1449565020_98225687", "priceNow": 1.50258, "lots": 0.21, "symbol": "GBPUSDm", "operationType": -11, "orderType": 0, "nowTicket": 98225896,"timelong": 1449565020,"accountID": 7767180} ;
		
		StringBuffer result = new StringBuffer();
		Integer dealingStats = 0;	// 0:初始值，  1：正常跟单处理，   2：正常单不处理，   -1：跟单号不存在，  -2:跟单号已断连   ，  -99：未知错误
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
					break;
				case 3:
					dealingStats = 1;
					result.append(jointInfoStr(senderID));
					break;
				case 4:
					dealingStats = -2;
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
	
	public static String jointInfoStr(Integer senderID){
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
			tempResult.append(tempValue.substring(0, tempValue.length()-1));
			tempResult.append(", ");	
			tempCount++;
		}
		return tempResult.toString();
	}
	
	
	public static String updateReceiverState(String receiverInfo){
		JSONObject json = new JSONObject(receiverInfo);
		int requestType = json.getInt("RequestType");
		int senderID = json.getInt("SenderID");
		int receiverID = json.getInt("AccountID");		
		String resultStr = "";
		switch(requestType){
			case 1:
				//鉴权 - TODO
				break;
			case 2:
				//listing
				resultStr = getReceiverCheckResult(senderID);
				break;
			case 3:
				//feedback - TODO
				break;
			default:
				//log error
				break;
		}
		return resultStr;
	}
	
	
	//todo function
	static boolean receiverGuard(){
		//TODO: 周期性检测receiver状态，如果长时间处于feedback，则feedback未响应
		//		类似的可能监听态未响应
		
		//TODO: 周期性检验sender状态，长时间
		//TODO: 对于断开的senderID进行删除
		return false;
	}
	
	
	
	
	
	/**
	 * 
	 * @return update the map successfully or not, 
	 * 		   1 for normal return,
	 * 		   2 for old orders and not dealing,
	 * 		   3 for normal close return,
	 * 		   4 for normal partly change or new return
	 */
	public static String updateSenderState(String senderInfo){
		JSONObject json = new JSONObject(senderInfo);
		Integer senderID = json.getInt("accountID");
		String orderName = json.getString("name");
		Integer operationType = json.getInt("operationType");
		json.put("result", 1);
		if(sendOrders.containsKey(senderID)==false){
			//TODO: 鉴权
			// senderStatus = 1;
			// if(althourity == false) { response false}
			// else senderOrdersNum.put(senderID, 0);
			// senderStatus = 2;
			
			//close operation
			if(operationType<0){
				//do nothing
				json.put("result", 2);
				return json.toString();
			}
			
			ConcurrentHashMap<String,String> orderDetails = new ConcurrentHashMap<String,String>();
			orderDetails.put(orderName, senderInfo);
			sendOrders.put(senderID, orderDetails);			
			
			// modify the sender status
			followerStatusDaemon fsDaemon = new followerStatusDaemon(senderID, maxSleepSeconds,operationType,orderName, senderCountDown, senderState,orderDetails);
			FutureTask<Integer> futureTask = new FutureTask<Integer>(fsDaemon);
			ExecutorService executor = Executors.newFixedThreadPool(1);
			executor.execute(futureTask);
		} else{
			ConcurrentHashMap<String,String> orderDetails = sendOrders.get(senderID);
			orderDetails.put(orderName, senderInfo);
			json.put("result", 4);
							
			// modify the sender status
			followerStatusDaemon fsDaemon = new followerStatusDaemon(senderID, maxSleepSeconds,operationType,orderName, senderCountDown, senderState,orderDetails);
			FutureTask<Integer> futureTask = new FutureTask<Integer>(fsDaemon);
			ExecutorService executor = Executors.newFixedThreadPool(1);
			executor.execute(futureTask);
			if((operationType == -1) || (operationType == -2)){
				json.put("result", 3);
			}	
		}
		return json.toString();
	}
}
