package ChuangAo.WebSite.controller;

import ChuangAo.WebSite.model.Item;
import ChuangAo.WebSite.service.FirstDemoService;
import ChuangAo.WebSite.service.followOrderService;

import java.util.Date;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Hello world!
 *
 */
@RestController
public class FirstDemoController
{
	
	Date dt = new Date();
	
//	@Autowired
//	private FirstDemoService firstDemoService;

//	@RequestMapping("/")
//	private Iterable<Item> home(){
//		return firstDemoService.findByTitle("item");
//	}
	
	@RequestMapping("/la")
	private String home(String la){
		la = "lala";
		return "hello " + la;
	}
	
	

	@RequestMapping("/commonService/follow/sender")
	private String followSender(HttpServletRequest request, HttpServletResponse  response){
		Map<String, String[]> map = request.getParameterMap();
		String[] keys = {"{ \"result\" : -1 }"};
		map.keySet().toArray(keys);		
		String outRes = followOrderService.updateSenderState(keys[0]);
		System.out.println("~~~sender: "+outRes);
		return outRes;
	}
	
	@RequestMapping("/commonService/follow/reveiver")
	private String followReceiver(HttpServletRequest request, HttpServletResponse  response){
		Map<String, String[]> map = request.getParameterMap();
		String[] keys = {"{ \"result\" : -1 }"};
		map.keySet().toArray(keys);
		//System.out.println("~~~keys: "+keys[0]);
		String result = followOrderService.updateReceiverState(keys[0]);
		System.out.println("***receiver: "+result);
		return result;
	}
	
	
	

}
