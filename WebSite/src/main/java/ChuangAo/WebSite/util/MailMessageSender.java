package ChuangAo.WebSite.util;


import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

public class MailMessageSender {
	
	private JavaMailSender mailSender;
	private static final Logger logger = LoggerFactory.getLogger(MailMessageSender.class);
	
	private static class MailMessageSenderHolder {  
        private static final MailMessageSender INSTANCE = new MailMessageSender();  
    }  
	
	public static final MailMessageSender getInstance() {  
        return MailMessageSenderHolder.INSTANCE; 
    }  
	
	private MailMessageSender(){
		
	}
	
	public void setMailSender(JavaMailSender mailSender){
		this.mailSender = mailSender;
	}
	
	public void sendSimpleEmail(String desAddress, String subject, String content){
		 SimpleMailMessage message = new SimpleMailMessage();
	      
	     message.setFrom("2570278383@qq.com");//发送者.
	     message.setTo(desAddress);//接收者.
	     message.setSubject(subject);//邮件主题.
	     message.setText(content);//邮件内容.
	     
	     try{
	    	 mailSender.send(message);//发送邮件
	     }
	     catch(Exception e){
	    	 logger.error("send mail fail: "+e.getMessage());
	    	 e.printStackTrace();
	     }
	}
	
	public void sendHtmlEmail(String desAddress, String subject, String content){
		MimeMessage msg = mailSender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(msg, true);
			helper.setFrom("2570278383@qq.com");//发送者.
			
			helper.setTo(desAddress);
			helper.setSubject(subject);//邮件主题.
			helper.setText(content,true);//邮件内容.
			
			mailSender.send(msg);
		} catch (Exception e) {
			logger.error("send rich text mail fail: "+e.getMessage());
			e.printStackTrace();
		}
	}
	
}
