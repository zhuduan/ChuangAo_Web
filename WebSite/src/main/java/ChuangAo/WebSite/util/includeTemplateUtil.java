package ChuangAo.WebSite.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.ui.ModelMap;

public class includeTemplateUtil {
	
	//---
	// inner class for singleton Mode
	private static class includeTemplateUtilHolder {  
        private static final includeTemplateUtil INSTANCE = new includeTemplateUtil();  
    }  
	
	public static final includeTemplateUtil getInstance() {  
        return includeTemplateUtilHolder.INSTANCE; 
    }  
	
	private static Logger logger = Logger.getLogger(includeTemplateUtil.class);
	private static final String configFile = "/application.properties";
	
	
	private String templateUrl = "/templates/common/";
	private String homeUrl = "http://localhost";
	
	//--- default construct
	private includeTemplateUtil(){
		Properties p = new Properties();
		InputStream inStream = this.getClass().getResourceAsStream(configFile);
		if(inStream == null){
			logger.error("根目录下找不到application.properties文件");
			return;
		}
		try {
			p.load(inStream);
			this.templateUrl = p.getProperty("thymeleaf.navigation.prefix") == null ?  templateUrl : p.getProperty("thymeleaf.navigation.prefix").trim();
			this.homeUrl = p.getProperty("thymeleaf.navigation.home")  == null ?  homeUrl : p.getProperty("thymeleaf.navigation.home").trim();
			inStream.close();
		} catch (IOException e) {
			logger.error("load application.properties error,class根目录下找不到application.properties文件");
			e.printStackTrace();
		}
		logger.info("application.properties success");
	}
	
	public void getNavigation(Integer naviType,ModelMap map){				
		String fileUrl = templateUrl;
		switch(naviType){
			case 1:
				//admin navigatioin
				fileUrl = fileUrl + "header_admin.html";
				break;
			case 2:
				//user navigation
				fileUrl = fileUrl + "header_trader.html";
				break;
			default:
				fileUrl = fileUrl + "header_trader.html";
				break;
		}		
		try {
			InputStream input = this.getClass().getResourceAsStream(fileUrl);
			Document doc = Jsoup.parse(input,"UTF-8","http://www.mychuangao.com/");
			setUserNameAndID(doc);
			map.addAttribute("headerContent", doc.select("header").toString());
			map.addAttribute("sideBarContent", doc.select("#admin-offcanvas").first().toString());
			map.addAttribute("footerContent", changeAttrAddress(doc.select("footer"),"script","src"));
			map.addAttribute("headContent", changeAttrAddress(doc.select("head"),"link","href"));			
		} catch (IOException e) {
			logger.error("error when using jsoup");
			logger.error(e.getMessage());
			map.addAttribute("headContent", "");
			map.addAttribute("headerContent", "");
			map.addAttribute("sideBarContent", "");
			map.addAttribute("footerContent", "");
		}
	}
	
	private String changeAttrAddress(Elements parentNode,String dealingNodeName,String attrName){
		Elements elements = parentNode.select(dealingNodeName);
		for(Element elment : elements){
			String orignalAddress = elment.attr(attrName);
			if(orignalAddress.isEmpty()){
				continue;
			}
			if(orignalAddress.contains("//cdn")){
				continue;
			}
			orignalAddress = homeUrl + delInnerPath(orignalAddress);
			elment.attr(attrName,orignalAddress);
		}
		return parentNode.toString();
	}
	
	private String delInnerPath(String address){
		String keyWord = "static";
		if(address.contains(keyWord)==false){
			return address;
		}
		int position = address.indexOf(keyWord) + keyWord.length() + 1;
		return address.substring(position);
	}
	
	
	private void setUserNameAndID(Document doc){
		Subject currentUser = SecurityUtils.getSubject(); 
		Session session = currentUser.getSession();
		logger.debug("userName: "+session.getAttribute("userName").toString());
		doc.select("#userName").first().html(session.getAttribute("userName").toString());
		doc.select("#userID").first().val(session.getAttribute("userID").toString());
	}
	
}
