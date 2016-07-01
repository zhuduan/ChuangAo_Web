package ChuangAo.WebSite.util;

import java.util.Date;

import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;

public class TextMessageSender {
	private String url = "http://gw.api.taobao.com/router/rest";
	private String appkey = "23396351";
	private String secret = "3d0b65d3f2095469ed88bfe0494e95a3";
	
	public boolean sendNoticeText(String userName,Integer AccountID,String reason,String phone){
		TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
		AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
		req.setExtend( "" );
		req.setSmsType( "normal" );
		req.setSmsFreeSignName( "创澳商务" );
		req.setSmsParamString( "{name:'"+userName+"',account:'"+AccountID
						+"',time:'"+(new Date()).toString()+"',reason:' "+reason+" '}" );
		req.setRecNum(phone);
		req.setSmsTemplateCode( "SMS_10991415" );
		AlibabaAliqinFcSmsNumSendResponse rsp;
		try {
			rsp = client.execute(req);
			System.out.println(rsp.getBody());
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return true;
	}
	
}
