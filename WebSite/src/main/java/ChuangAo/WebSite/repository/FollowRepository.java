package ChuangAo.WebSite.repository;

import ChuangAo.WebSite.model.Follow;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface FollowRepository extends JpaRepository<Follow, Integer> {
    @Query("SELECT f FROM Follow f WHERE f.accountid = :accountID")
    Follow findByAccountID(@Param("accountID") Integer accountID);
    
    
    @Modifying
    @Query("UPDATE Follow f set f.offline = :offline where f.accountid = :accountid")
    int updateOFFlineByAccountID(@Param("offline") Short offline,
    						     @Param("accountid") Integer accountid);
    
    
    @Modifying
    @Query("UPDATE Follow f set f.feedback = :feedback where f.accountid = :accountid")
    int updateFeedBackByAccountID(@Param("feedback") Short feedback,
    							  @Param("accountid") Integer accountid);
   
    
    @Modifying
    @Query("UPDATE Follow f set f.datastatistic = :datastatistic where f.accountid = :accountid")
    int updateDataStatisticByAccountID(@Param("datastatistic") Short datastatistic,
    							       @Param("accountid") Integer accountid);
   
}
