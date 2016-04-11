package ChuangAo.WebSite;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

/**
 * Hello world!
 *
 */
@RestController
@EnableAutoConfiguration
public class Example 
{
	@RequestMapping("/")
	String home(){
		return "hello world";
	}
	
	@RequestMapping("/la")
	String home(String la){
		la = "lala";
		return "hello " + la;
	}
	
	
	public static void main(String[] args) throws Exception {
        SpringApplication.run(Example.class, args);
    }
}
