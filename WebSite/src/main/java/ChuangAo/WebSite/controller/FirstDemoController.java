package ChuangAo.WebSite.controller;

import ChuangAo.WebSite.model.Item;
import ChuangAo.WebSite.service.FirstDemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Hello world!
 *
 */
@RestController
public class FirstDemoController
{
	@Autowired
	private FirstDemoService firstDemoService;

	@RequestMapping("/")
	private Iterable<Item> home(){
		return firstDemoService.findByTitle("item");
	}
	
	@RequestMapping("/la")
	private String home(String la){
		la = "lala";
		return "hello " + la;
	}

}
