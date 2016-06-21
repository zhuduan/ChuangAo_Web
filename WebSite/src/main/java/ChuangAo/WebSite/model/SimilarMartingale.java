package ChuangAo.WebSite.model;


import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;

@Entity
@Table(name = "similarmartingale")
public class SimilarMartingale {

    @Column(name = "accountid", nullable = false)
    @Id
    private Integer accountid;
    
    @Column(name = "expiretime", nullable = false)
    private Timestamp expiretime;
    
    @Column(name = "netinputflag", nullable = false)
    private Short netinputflag;
    
    @Column(name = "inputparams", nullable = false)
    private String inputparams;
    
    

    public SimilarMartingale(Integer accountid, Timestamp expiretime, Short netinputflag, String inputparams) {
        this.accountid = accountid;
        this.expiretime = expiretime;
        this.netinputflag = netinputflag;
        this.inputparams = inputparams;
    }

    public SimilarMartingale() {

    }

    public Integer getAccountID() {
        return accountid;
    }

    public Timestamp getExpireTime() {
        return expiretime;
    }
    
    public Short getNetInputFlag() {
        return netinputflag;
    }
    
    public String getInputParams() {
        return inputparams;
    }
}
