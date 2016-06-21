package ChuangAo.WebSite.repository;

import ChuangAo.WebSite.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface UserRepository extends JpaRepository<User, Integer> {
    
	@Query("SELECT u FROM User u WHERE u.name = :name and pass= PASSWORD(:pass)")
    User findByName(@Param("name") String name,
    				@Param("pass") String pass);
	
	@Query("SELECT u FROM User u WHERE u.id = :id and pass= PASSWORD(:pass)")
    User findById(@Param("id") Integer id,
				  @Param("pass") String pass);
	
	User findByid(Integer id);
	
	User findByname(String name);
}
