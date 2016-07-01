package ChuangAo.WebSite.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import ChuangAo.WebSite.model.ResourceComsumeUser;

@Transactional
public interface ResourceComsumeUserRepository extends JpaRepository<ResourceComsumeUser, Integer> {    
	
	ResourceComsumeUser findByUserid(Integer Id);
	
	Page<ResourceComsumeUser> findAll(Pageable pageable);	
	
	
}
