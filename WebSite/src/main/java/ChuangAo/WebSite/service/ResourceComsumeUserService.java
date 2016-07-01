package ChuangAo.WebSite.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import ChuangAo.WebSite.repository.ResourceComsumeUserRepository;


@Service
@Transactional
public class ResourceComsumeUserService {

	 @Autowired
	 private ResourceComsumeUserRepository resourceComsumeUserRepository;
	 
	
	 public void getAllResourceComsumeUser(ModelMap map,Pageable pageable ){
	     map.addAttribute("resourceComsumeUser",resourceComsumeUserRepository.findAll(pageable));
	 }
	 
	 
	
}
