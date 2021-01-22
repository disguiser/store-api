package com.snow.storeapi;

import com.snow.storeapi.util.SMSUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StoreApiApplicationTests {

	@Test
	public void contextLoads() {
		String sign = "周明帅的网站";
		Integer templateId = 851052;
		String[] phoneNumbers={"18868804337","18758327028"};
		String[] params ={"123456","10"};

	}

}
