package ChuangAo.WebSite.service;

import java.util.Date;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ChuangAo.WebSite.model.SimilarMartingale;
import ChuangAo.WebSite.model.User;
import ChuangAo.WebSite.repository.SimilarMartingaleRepository;
import ChuangAo.WebSite.repository.UserRepository;

@Service
@Transactional
public class SimilarMartingaleService {

	
	 @Autowired
	 private SimilarMartingaleRepository similarMartingaleRepository;
	 
	 @Autowired
	 private UserRepository userRepository;
	 
	 
	 /***
	  * 
	  * @param accountID
	  * @param userInputFlag
	  * @return
	  *   authority: -1 for no account, -2 for expired, 1 for ok
	  *   netinputflag: -1 for user choose not use, -2 for force not use, 0 for default, 1 for user choose use, 2 for force use
	  *   inputParams: Json Object contains all params (not use this can be "{}"/null )
	  */
	 public String getAuthorityAndParams(String requestInfo){
		 //--- for only ea using, not check the input here
		 JSONObject request = new JSONObject(requestInfo);
		 Integer accountID = request.getInt("accountID");
		 
		 SimilarMartingale similarMartingale = similarMartingaleRepository.findByAccountID(accountID);
		 JSONObject json = new JSONObject();
		 if(similarMartingale==null){
			 json.put("authority", -2);
			 json.put("netinputflag", 0);
			 json.put("inputparams", "{}");
		 } else {
			 //--- check authority firstly
			 Date currentTime = new Date();
			 if(similarMartingale.getExpireTime().after(currentTime)==true){
				 json.put("authority", 1);
			 } else {
				 json.put("authority", -1);
			 }
			 
			 //---then check the params
			 json.put("inputparams", "{}");
			 JSONObject tempParams = new JSONObject(similarMartingale.getInputParams());
			 switch(similarMartingale.getNetInputFlag()){
			 	case -1:
			 		json.put("netinputflag", -1);
			 		break;
			 	case 0:
			 		json.put("netinputflag", 0);
			 		break;
			 	case 1:
			 		json.put("netinputflag", 1);
			 		json.put("inputparams",tempParams);
			 		break;
			 	case 2:
			 		json.put("netinputflag", 2);
			 		json.put("inputparams",tempParams);
			 		break;
			 	default:
			 		json.put("netinputflag", 0);
			 		break;
			 }
		 }
		 System.out.println(json.toString());
		 return json.toString();
	 }
	
	 
	 /***
	  * 
	  * @param params
	  * @param userName
	  * @param accountID
	  * @return
	  */
	 public String updateParams(String requestInfo){
		 
		 //--- should consider the input check, for simple not dealing at first
		 JSONObject request = new JSONObject(requestInfo);		 
		 JSONObject params = request.getJSONObject("inputParams");
		 String userName = request.getString("userName");
		 Integer accountID = request.getInt("accountID");
		 String pwd = request.getString("password");
		 
		 User user = userRepository.findByName(userName,pwd); 
		 JSONObject resultStr = new JSONObject();
		 //--- check user authority firstly
		 if(user==null){
			 //---not select successfully
			 resultStr.put("authority", -6);	
			 resultStr.put("paramsValidation", 0);
			 resultStr.put("paramsUpdate", 0);
			 return resultStr.toString();
		 }
		 resultStr.put("authority", 1);
		 JSONObject tempAuthority = new JSONObject(user.getAuthority());
		 if(user.getGroup()<30){
			 //---at least is a tansactioin user
			 resultStr.put("authority", -1);	
			 resultStr.put("paramsValidation", 0);
			 resultStr.put("paramsUpdate", 0);
			 return resultStr.toString();
		 }
		 
		 if(tempAuthority.has("similarmartingale")==false){
			 //--- do not have the modify params' authority
			 resultStr.put("authority", -2);
			 resultStr.put("paramsValidation", 0);
			 resultStr.put("paramsUpdate", 0);
			 return resultStr.toString();
		 } else {
			 if(tempAuthority.getInt("similarmartingale")<0){
				 //--- do not have the modify params' authority
				 resultStr.put("authority", -3);
				 resultStr.put("paramsValidation", 0);
				 resultStr.put("paramsUpdate", 0);
				 return resultStr.toString();
			 }
		 }
		 
		 //--- check params' consistency		 
		 resultStr.put("paramsValidation", 1);
		 //--- for simple, just validate the total num of params, can use more strict validation if needed
		 if(params.length()!= 27){
			 resultStr.put("paramsValidation", -1);
			 resultStr.put("paramsUpdate", 0);
			 return resultStr.toString();
		 }		 
		 
		 //--- update the db
		 resultStr.put("paramsUpdate", similarMartingaleRepository.updateParamsByAccountID(params.toString(), accountID));
		 
		 
		 //--- return result
		 return resultStr.toString();
	 }
	 
	 
	 /***
	  * 
	  * @param expireTime
	  * @param userName
	  * @param accountID
	  * @return
	  */
	 public String updateExpiretime(String requestInfo){
		//--- for only ea using, not check the input here
		 JSONObject request = new JSONObject(requestInfo);
		 Long expireTime = request.getLong("expireTime");
		 String userName = request.getString("userName");
		 Integer accountID = request.getInt("accountID");	
		 String pwd = request.getString("password");
		 
		 JSONObject resultStr = new JSONObject();
		 User user = userRepository.findByName(userName,pwd); 
		 
		 //--- check user authority firstly
		 if(user==null){
			 //---not select successfully
			 resultStr.put("authority", -6);	
			 resultStr.put("expiretimeValidation", 0);
			 resultStr.put("expiretimeUpdate", 0);
			 return resultStr.toString();
		 }
		 resultStr.put("authority", 1);
		 JSONObject tempAuthority = new JSONObject(user.getAuthority());
		 if(user.getGroup()<40){
			 //---at least is a admin user
			 resultStr.put("authority", -1);	
			 resultStr.put("expiretimeValidation", 0);
			 resultStr.put("expiretimeUpdate", 0);
			 return resultStr.toString();
		 }
		 if(tempAuthority.has("similarMatingale")==false){
			 //--- do not have the modify params' authority
			 resultStr.put("authority", -2);
			 resultStr.put("expiretimeValidation", 0);
			 resultStr.put("expiretimeUpdate", 0);
			 return resultStr.toString();
		 } else {
			 if(tempAuthority.getInt("similarMatingale")<0){
				 //--- do not have the modify params' authority
				 resultStr.put("authority", -3);
				 resultStr.put("expiretimeValidation", 0);
				 resultStr.put("expiretimeUpdate", 0);
				 return resultStr.toString();
			 }
		 }
		 
		 //--- check expiretime's validation	
		 //--- if needed
		 resultStr.put("expiretimeValidation", 1);
//		 Date currentTime = new Date();
//		 if(expireTime<=currentTime.getTime()){
//			 resultStr.put("expiretimeValidation", -1);
//			 resultStr.put("expiretimeUpdate", 0);
//			 return resultStr.toString();
//		 } 
		 
		 //--- update the db
		 resultStr.put("expiretimeUpdate", similarMartingaleRepository.updateExpiretimeByAccountID(expireTime, accountID));
		 
		 
		 //--- return result
		 return resultStr.toString();
	 }
	
}
