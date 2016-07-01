package ChuangAo.WebSite.model;


import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;

@Entity
@Table(name = "follow")
public class Follow {

    @Column(name = "accountid", nullable = false)
    @Id
    private Integer accountid;
    
    @Column(name = "accounttype", nullable = false)
    private Short accounttype;
    
    @Column(name = "followaccount", nullable = false)
    private String followaccount;
    
    @Column(name = "expiretime", nullable = false)
    private Timestamp expiretime;
    
    @Column(name = "as_sender", nullable = false)
    private Integer as_sender;
    
    @Column(name = "as_receiver", nullable = false)
    private Integer as_receiver;
    
    
    

    public Follow(Integer accountid,Short accounttype,
    				String followaccount, Timestamp expiretime, 
    				Integer as_sender, Integer as_receiver) {
        this.accountid = accountid;
        this.accounttype = accounttype;
        this.followaccount = followaccount;
        this.expiretime = expiretime;
        this.as_sender = as_sender;
        this.as_receiver = as_receiver;
    }

    public Follow() {

    }

   public Integer getAccountID(){
	   return accountid;
   }
   
   public Short getAccountType(){
	   return accounttype;
   }
   
   public String getFollowAccount(){
	   return followaccount;
   }
   
   public Timestamp getExpireTime(){
	   return expiretime;
   }
   
   public Integer getAsSender(){
	   return as_sender;
   }
   
   public Integer getAsReceiver(){
	   return as_receiver;
   }
   

   public void setAccountID(Integer ID){
	   this.accountid=ID;
   }
   
   public void setAccountType(Short type){
	   this.accounttype=type;
   }
   
   public void setFollowAccount(String fa){
	   this.followaccount=fa;
   }
   
   public void setExpireTime(Timestamp time){
	   this.expiretime=time;
   }
   
   public void setAsSender(Integer asSender){
	   this.as_sender=asSender;
   }
   
   public void setAsReceiver(Integer asReceiver){
	   this.as_receiver=asReceiver;
   }
   
}
