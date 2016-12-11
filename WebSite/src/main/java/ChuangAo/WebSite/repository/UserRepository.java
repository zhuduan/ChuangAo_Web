package ChuangAo.WebSite.repository;

import ChuangAo.WebSite.model.User;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface UserRepository extends JpaRepository<User, Integer> {
    
	@Query("SELECT u FROM User u WHERE u.name = :name and pass= PASSWORD(:pass)")
    public User findByName(@Param("name") String name,
    				@Param("pass") String pass);
	
	@Query("SELECT u FROM User u WHERE u.id = :id and pass= PASSWORD(:pass)")
    public User findById(@Param("id") Integer id,
				  @Param("pass") String pass);
	
	public User findByid(Integer id);
	
	public User findByname(String name);
	
	public User findByemail(String email);
	
	public Page<User> findAll(Pageable pageable);	
	
	@Modifying 
	@Transactional
	@Query("UPDATE User u SET pass=PASSWORD(:pass) WHERE u.id = :id")
    public int updatePass(@Param("id") Integer id,
				  @Param("pass") String pass);	
	
	@SuppressWarnings("unchecked")
	public User save(User user);
}
