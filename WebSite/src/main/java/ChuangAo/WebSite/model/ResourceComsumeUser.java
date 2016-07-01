package ChuangAo.WebSite.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;

@Entity
@Table(name = "resource_comsume_user")
public class ResourceComsumeUser {

    @Column(name = "userid", nullable = false)
    @Id
    private Integer userid;
    
    @Column(name = "mail_used_times", nullable = false)
    private Long mail_used_times;
    
    @Column(name = "text_used_times", nullable = false)
    private Long text_used_times;
    
    @Column(name = "onlinemonitor_time", nullable = false)
    private Long onlinemonitor_time;
    
    

    public ResourceComsumeUser(Integer userid,Long mail_used_times,Long text_used_times,Long onlinemonitor_time) {
        this.userid = userid;
        this.mail_used_times = mail_used_times;
        this.text_used_times = text_used_times;
        this.onlinemonitor_time = onlinemonitor_time;       
    }

    public ResourceComsumeUser() {

    }

    public Integer getUserID() {
        return userid;
    }

    public Long getMailUsedTimes() {
        return mail_used_times;
    }
    
    public Long getTextUsedTimes() {
        return text_used_times;
    }
    
    public Long getOnlineMonitorTime() {
        return onlinemonitor_time;
    }
    
    
    public void setUserID(Integer userID){
    	this.userid = userID;
    }
    
    public void setMailUsedTimes(Long mail_used_times){
    	this.mail_used_times = mail_used_times;
    }
    
    public void setTextUsedTimes(Long text_used_times){
    	this.text_used_times = text_used_times;
    }
    
    public void setOnlineMonitorTime(Long onlinemonitor_time){
    	this.onlinemonitor_time = onlinemonitor_time;
    }

}
