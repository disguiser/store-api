package com.snow.storeapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.snow.storeapi.TestConstant;
import com.snow.storeapi.entity.Goods;
import com.snow.storeapi.mapper.GoodsMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GoodsControllerTest extends BaseControllerTest {

    @Autowired
    private GoodsMapper goodsMapper;

    private final String BASE_URL = "/goods";

//    @BeforeAll
    public void beforeAll() throws JsonProcessingException {
        super.beforeAll();
        jdbc.execute("truncate table goods;");
    }

    @Order(1)
//    @Test
    public void testCreate() throws Exception {
        var goods = Goods.builder()
                .preSku(TestConstant.GOODS_CREATE_PRE_SKU)
                .name(TestConstant.GOODS_CREATE_NAME)
                .sizeGroup(TestConstant.GOODS_CREATE_SIZE_GROUP)
                .imgUrl(TestConstant.GOODS_CREATE_IMG_URL)
                .salePrice(TestConstant.GOODS_CREATE_SALE_PRICE)
                .costPrice(TestConstant.GOODS_CREATE_COST_PRICE)
                .build();
        var res = mockMvc.perform(
                        MockMvcRequestBuilders.post(BASE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(goods))
                                .header(TestConstant.HEADER_STRING, TestConstant.TOKEN_PREFIX + token)
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        id = Integer.parseInt(res.getResponse().getContentAsString());
        var _goods = goodsMapper.selectById(id);
        assertNotNull(_goods, "Goods should be exist.");
    }

    @Order(2)
//    @Test
    public void testFindByPage() {

    }
}
