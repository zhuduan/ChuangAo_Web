package ChuangAo.WebSite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import ChuangAo.WebSite.util.*;

@SpringBootApplication
public class Application {
    public static void main(String[] args) throws Exception {
    	commonTimer.timerTasks();	//timer
        SpringApplication.run(Application.class, args);
    }
    
    
    
}
