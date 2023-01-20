package com.snow.storeapi;

import com.snow.storeapi.util.SMSUtil;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class StoreApiApplicationTests {

//	@Test
	public void contextLoads() {
		var sign = "周明帅的网站";
		var templateId = "851052";
		var phoneNumber="18758327028";
		var params = new String[]{"123456","10"};
//		SMSUtil.sendMessage(templateId, sign, phoneNumber, params);
	}
}
