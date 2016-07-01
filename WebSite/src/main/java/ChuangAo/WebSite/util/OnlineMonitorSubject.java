package ChuangAo.WebSite.util;

import java.util.Observable;


//---TODO：
//这里容易成为多线程时的性能瓶颈
public class OnlineMonitorSubject extends Observable {

	private Integer accountID;
	private Integer status;
	private Integer accountType;	// 1 for sender, 2 for receiver (0 for unknow)
	
	
	private static class OnlineMonitorSubjectHolder {  
        private static final OnlineMonitorSubject INSTANCE = new OnlineMonitorSubject();  
    }  
	
	public static final OnlineMonitorSubject getInstance() {  
        return OnlineMonitorSubjectHolder.INSTANCE; 
    }  
	
	private OnlineMonitorSubject(){
		
	}
	
	
	/***
	 * 
	 * @param ID : the account ID
	 * @param state : the state, value as followOrderSevice state maps
	 * @param accountType : 1 for sender, 2 for receiver, 0 for unknown
	 */
	public void setChangedAccountStatus(Integer ID, Integer state, Integer accountType){
		//--- to make thread safe, add a lock here
		synchronized(this){
			this.accountID = ID;
			this.status = state;
			this.accountType = accountType;
			accountStatusChanged();
		}		
	}
	
	
	private void accountStatusChanged(){
		setChanged();	
		notifyObservers();		//push method
	}
	
	
	public Integer getAccountID(){
		return this.accountID;
	}
	
	public Integer getStatus(){
		return this.status;
	}
	
	public Integer getAccountType(){
		return this.accountType;
	}
	
	
}
