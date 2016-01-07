package ChuangAo.WebSite.controller;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

/**
 * Hello world!
 *
 */
@RestController
public class Example 
{
	@RequestMapping("/")
	private String home(){
		return "hello world";
	}
	
	@RequestMapping("/la")
	private String home(String la){
		la = "lala";
		return "hello " + la;
	}

}
