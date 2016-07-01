package ChuangAo.WebSite.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import ChuangAo.WebSite.Application;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)

@WebAppConfiguration
public class mailText {
	
	 @Autowired
	 private JavaMailSender mailSender;
	 
	 
	 @Test
	 public void sendSimpleEmail(){
		 SimpleMailMessage message = new SimpleMailMessage();
	      
	     message.setFrom("2570278383@qq.com");//发送者.
	     message.setTo("745316206@qq.com");//接收者.
	     message.setSubject("测试邮件（邮件主题）");//邮件主题.
	     message.setText("这是邮件内容");//邮件内容.
	      
	     mailSender.send(message);//发送邮件
	 }

}
