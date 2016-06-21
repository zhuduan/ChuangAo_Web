package ChuangAo.WebSite.util;

public class userGroupUtil {
	
	public static String getUserType(Short groupID){
		if(groupID>0 && groupID<10){
			return "normal";
		} else if(groupID>10 && groupID<20){
			return "register";
		} else if(groupID>20 && groupID<30){
			return "developer";
		} else if(groupID>30 && groupID<40){
			return "trader";
		} else if(groupID>40 && groupID<50){
			return "admin";
		} else if(groupID>50 && groupID<60){
			return "sysAdmin";
		} else {
			return "unknown";
		}
	}
	
	
}
