package com.snow.storeapi;

import com.snow.storeapi.service.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

//@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomerServiceTest {
    @Autowired
    private ICustomerService customerService;

//    @Test
    public void testSelect() {
        var customer = customerService.getById(1);
        System.out.println(customer);
    }
}
