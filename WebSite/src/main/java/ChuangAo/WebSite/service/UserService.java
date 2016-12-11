package ChuangAo.WebSite.service;

import java.util.Date;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import ChuangAo.WebSite.model.User;
import ChuangAo.WebSite.repository.UserRepository;
import ChuangAo.WebSite.util.HashAlgorithm;
import ChuangAo.WebSite.util.MailMessageSender;
import ChuangAo.WebSite.util.includeTemplateUtil;
import ChuangAo.WebSite.util.userGroupUtil;

@Service
@Transactional
public class UserService {
	
	 @Autowired
	 private UserRepository userRepository;
	 
	 @Autowired
	 private TemplateEngine templateEngine;
	 
	 @Autowired
	 private JavaMailSender mailSender;
	 
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
	 
	 public void sendMailForNewPass(String mail){
		 User user = userRepository.findByemail(mail);
		 if(user==null){
			 //---mail address error
			 return;
		 }
		 Long timestamp = (new Date()).getTime();
		 String hash = HashAlgorithm.getInstance().getMd5Hash(mail, 
				 HashAlgorithm.getInstance().getSalt(user.getId(), user.getName(), timestamp));
		 String desUrl = includeTemplateUtil.getInstance().getHostHome() + "/password/mail/info" 
				 		+ "/" + user.getId().toString() + "/" + timestamp.toString() 
				 		+ "/" + hash;
		 Context context = new Context();
		 context.setVariable("desUrl", desUrl);
		 String emailContent = templateEngine.process("passEmailContent", context);
		 String subject = "创澳商务-密码找回服务";
		 MailMessageSender.getInstance().setMailSender(mailSender);
		 MailMessageSender.getInstance().sendHtmlEmail(mail, subject, emailContent);
	 }
	 
	 public Boolean validateHash(String hash,Integer ID,Long timestamp){ 
		 User user = userRepository.findByid(ID);
		 if(user==null){
			 return false;
		 }
		 Long currentTime = (new Date()).getTime();
		 //---default expire time is one day
		 if((currentTime-timestamp)>(24*3600*1000)){
			 return false;
		 }
		 String newHash = HashAlgorithm.getInstance().getMd5Hash(user.getEmail(), 
				 HashAlgorithm.getInstance().getSalt(user.getId(), user.getName(),timestamp));
		 if(newHash.equals(hash)){
			 return true;
		 } else{
			 return false;
		 }
	 }
	 
	 public void setNewPass(Integer id,String pass1,String pass2,ModelMap map){
		 if(!(pass1.equals(pass2))){
			 map.addAttribute("updateResult", -1);	
			 return;
		 }
		 User user = userRepository.findByid(id);
		 if(user==null){
			 map.addAttribute("updateResult", -1);
			 return;
		 }
		 int result = userRepository.updatePass(id,pass1);
		 if(result>0){
			 map.addAttribute("updateResult", 1);
		 } else {
			 map.addAttribute("updateResult", -1);
		 }
		 
	 }
	 
	 public void insertNewUser(String name,String authority,String email,int group,int noticetype){	
		 User user  = new User();		
		 userRepository.save(user);
	 }
	
}
