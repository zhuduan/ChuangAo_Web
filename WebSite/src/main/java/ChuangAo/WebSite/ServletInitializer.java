package ChuangAo.WebSite;

import org.springframework.boot.builder.SpringApplicationBuilder;  
import org.springframework.boot.context.web.SpringBootServletInitializer;

import ChuangAo.WebSite.util.commonTimer;


public class ServletInitializer extends SpringBootServletInitializer {  
	  
    @Override  
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {  
    	commonTimer.timerTasks();	//timer
    	return application.sources(Application.class);  
    }  
  
}  