package triaina.test.webview.api;

import android.os.Bundle;
import triaina.commons.json.JSONConverter;
import triaina.webview.entity.device.NetHttpSendParams;
import junit.framework.TestCase;

public class NetHttpSendParamsTest extends TestCase {
	
	public void testConvertToObject() throws Exception {
		NetHttpSendParams param = JSONConverter.toObject("{\"url\": \"http://mixi.jp/service/rpc.json\", \"method\": \"POST\"," +
			"\"headers\": {\"Content-Type\": \"multipart/form-data\"},"+
			"\"body\":{"+
			"\"request\": '{\"jsonrpc\":\"2.0\",\"method\":\"home.getCommunity\",\"params\":{},\"id\":0}'," +
			 "\"photo1\":{\"type\":\"File\",\"value\":\"test\"}}}", NetHttpSendParams.class);
		
		Bundle headers = param.getHeaders();
		assertEquals("multipart/form-data", headers.get("Content-Type"));
		
		Bundle body = param.getBody();
		Bundle photo1 = body.getBundle("photo1");
		assertEquals("File", photo1.get("type"));
		assertEquals("test", photo1.get("value"));
	}
}
