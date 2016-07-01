package ChuangAo.WebSite.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import ChuangAo.WebSite.model.User;
import ChuangAo.WebSite.repository.UserRepository;
import ChuangAo.WebSite.util.userGroupUtil;

@Service
@Transactional
public class UserService {

	 @Autowired
	 private UserRepository userRepository;
	 
	 public User getUserByName(String name){
		 return userRepository.findByname(name);
	 }
	 
	 public User getUserById(Integer id){
		 return userRepository.findByid(id);
	 }
	 
	 public void getUserAccountInfoByID(Integer userID, ModelMap map){
		 User tempUser = getUserById(userID);
	     map.addAttribute("userName", tempUser.getName());
	     map.addAttribute("userID", tempUser.getId());
	     map.addAttribute("userType", userGroupUtil.getUserType(tempUser.getGroup()));    
	     map.addAttribute("eaJson", new JSONObject(tempUser.getAuthority()));
	     map.addAttribute("accountJson",new JSONObject(tempUser.getOwnAccounts()));
	 }
	 
	 public void getAllUserAccountInfo(ModelMap map,Pageable pageable ){
	     map.addAttribute("userPage",userRepository.findAll(pageable));
	 }
	 
	 
	
}
