package ChuangAo.WebSite.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ChuangAo.WebSite.model.User;
import ChuangAo.WebSite.repository.UserRepository;

@Service
@Transactional
public class UserService {

	 @Autowired
	 private UserRepository userRepository;
	 
	 public User getUserByName(String name){
		 return userRepository.findByname(name);
	 }
	
}
