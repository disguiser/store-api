package com.snow.storeapi;

import com.snow.storeapi.entity.Dict;
import com.snow.storeapi.entity.DictItem;
import com.snow.storeapi.mapper.DictMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

//@RunWith(SpringRunner.class)
@SpringBootTest
public class DictMapperTest {
    @Autowired
    private DictMapper dictMapper;

//    @Test
//    @Transactional
    public void testInsert() {
        var dictitem = new DictItem();
        dictitem.setItemCode("Boss");
        dictitem.setItemName("老板");
        var dictItems = new ArrayList<DictItem>();
        dictItems.add(dictitem);
        Dict dict = new Dict();
        dict.setDictName("roles");
//        dict.setData(dictItems);
        Integer id = dictMapper.insert(dict);
//        Assert.assertThat(id, equalTo(0));
        assertThat(id, greaterThan(0));
    }

//    @Test
    public void testUpdateById() {
        var dict = new Dict();
        dict.setId(1);
        dict.setMoreOption(false);
        dictMapper.updateById(dict);
    }

//    @Test
    public void testSelectAll() {
        List<Dict> dicts = dictMapper.selectAll("权", "dict_name", true);
//        System.out.println(dicts);
        assertThat(dicts.size(), greaterThan(0));
    }

//    @Test
    public void testDeleteById() {
        dictMapper.deleteById(2);
    }
}
