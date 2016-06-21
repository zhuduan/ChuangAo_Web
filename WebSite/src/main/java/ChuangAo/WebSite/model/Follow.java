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
    
    @Column(name = "offline", nullable = false)
    private Short offline;
    
    @Column(name = "feedback", nullable = false)
    private Short feedback;
    
    @Column(name = "datastatistic", nullable = false)
    private Short datastatistic;
    
    

    public Follow(Integer accountid,Short accounttype,
    				String followaccount, Timestamp expiretime, 
    				Short offline, Short feedback, Short datastatistic) {
        this.accountid = accountid;
        this.accounttype = accounttype;
        this.followaccount = followaccount;
        this.expiretime = expiretime;
        this.offline = offline;
        this.feedback = feedback;
        this.datastatistic = datastatistic;
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
   
   public Short getOFFline(){
	   return offline;
   }
   
   public Short getFeedBack(){
	   return feedback;
   }
   
   public Short getDataStatistic(){
	   return datastatistic;
   }
}
