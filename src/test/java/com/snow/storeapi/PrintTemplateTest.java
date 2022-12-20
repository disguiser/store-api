package com.snow.storeapi;

import com.snow.storeapi.entity.PrintTemplate;
import com.snow.storeapi.service.IPrintTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//@RunWith(SpringRunner.class)
@SpringBootTest
public class PrintTemplateTest {
    @Autowired
    private IPrintTemplateService printTemplateService;

//    @Test
    public void testInsert() {
        var printTemplate = new PrintTemplate();
        printTemplate.setName("test");
        var list = new ArrayList<Map>();
        var map = new HashMap<String, Object>();
        var map2 = new HashMap<String, Object>();
        map2.put("c", 3);
        map.put("a", 1);
        map.put("b", map2);
        list.add(map);
//        printTemplate.setData(list);
//        printTemplate.setData(JSON.parseArray(JSON.toJSONString(list)));
        printTemplateService.save(printTemplate);
        System.out.println(printTemplate);
    }

//    @Test
    public void testSelect() {
        var printTemplate = printTemplateService.getById(4);
        System.out.println(printTemplate);
        System.out.println(printTemplate.getData().toString());
    }
}
