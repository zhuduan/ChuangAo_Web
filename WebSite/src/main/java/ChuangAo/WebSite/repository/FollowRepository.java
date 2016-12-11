package ChuangAo.WebSite.repository;

import ChuangAo.WebSite.model.Follow;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface FollowRepository extends JpaRepository<Follow, Integer> {
    @Query("SELECT f FROM Follow f WHERE f.accountid = :accountID")
    Follow findByAccountID(@Param("accountID") Integer accountID);
    
    @Query("SELECT f FROM Follow f WHERE f.accountid IN (:accountIDs)")
    List<Follow> findInAccountIDList(@Param("accountIDs") List<Integer> accountIDs);
    
    @SuppressWarnings("unchecked")
	Follow save(Follow follow);
   
}
