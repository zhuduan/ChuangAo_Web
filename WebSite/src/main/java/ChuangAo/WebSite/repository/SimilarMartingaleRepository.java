package ChuangAo.WebSite.repository;

import ChuangAo.WebSite.model.SimilarMartingale;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface SimilarMartingaleRepository extends JpaRepository<SimilarMartingale, Integer> {
    @Query("SELECT s FROM SimilarMartingale s WHERE s.accountid = :accountID")
    SimilarMartingale findByAccountID(@Param("accountID") Integer accountID);
    
    
    @Modifying
    @Query("UPDATE SimilarMartingale s set s.inputparams = :inputparams where s.accountid = :accountid")
    int updateParamsByAccountID(@Param("inputparams") String inputparams,
    						    @Param("accountid") Integer accountid);
    
    
    @Modifying
    @Query("UPDATE SimilarMartingale s set s.expiretime = :expiretime where s.accountid = :accountid")
    int updateExpiretimeByAccountID(@Param("expiretime") Long expiretime,
    								@Param("accountid") Integer accountid);
    
    
//    @Modifying
//    @Query("INSERT INTO SimilarMartingale(accountid,expiretime,netinputflag,inputparams) VALUES(:accountid,:expiretime,:netinputflag,:inputparams)")
//    int insertNew(@Param("expiretime") Long expiretime,
//    			  @Param("accountid") Integer accountid,
//    			  @Param("inputparams") String inputparams,
//    			  @Param("netinputflag") Integer netinputflag);
}
