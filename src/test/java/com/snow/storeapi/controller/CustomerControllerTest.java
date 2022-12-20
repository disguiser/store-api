package com.snow.storeapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.snow.storeapi.TestConstant;
import com.snow.storeapi.entity.Customer;
import com.snow.storeapi.mapper.CustomerMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("测试客户记录")
public class CustomerControllerTest extends BaseControllerTest {
    @Autowired
    private CustomerMapper customerMapper;

    private final String BASE_URL = "/customer";

    @BeforeAll
    public void beforeAll() throws JsonProcessingException {
        super.beforeAll();
        jdbc.execute("truncate table customer;");
        var sql = """
                insert into `customer`(`id`, `debt`)
                values (${CUSTOMER_ID}, ${CUSTOMER_CREATE_DEBT});
                """
                .replace("${CUSTOMER_ID}", TestConstant.CUSTOMER_ID.toString())
                .replace("${CUSTOMER_CREATE_DEBT}", TestConstant.CUSTOMER_CREATE_DEBT.toString());
        jdbc.execute(sql);
    }

    @Order(1)
    @Test
    public void testCreate() throws Exception {
        var customer = Customer.builder()
                .name(TestConstant.CUSTOMER_CREATE_NAME)
                .mobile(TestConstant.CUSTOMER_CREATE_MOBILE)
                .address(TestConstant.CUSTOMER_CREATE_ADDRESS)
                .addressDetail(TestConstant.CUSTOMER_CREATE_ADDRESS_DETAIL)
                .debt(TestConstant.CUSTOMER_CREATE_DEBT)
                .build();
        var res = mockMvc.perform(
                MockMvcRequestBuilders.post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer))
                        .header(TestConstant.HEADER_STRING, TestConstant.TOKEN_PREFIX + token)
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        id = Integer.parseInt(res.getResponse().getContentAsString());
        var _customer = customerMapper.selectById(id);
        assertNotNull(_customer, "Customer should be exist.");
    }

    @Order(2)
    @Test
    public void testFindByPage() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.get(BASE_URL + "/page")
                                .accept(MediaType.APPLICATION_JSON)
                                .param("searchText", TestConstant.CUSTOMER_ID.toString())
                                .header(TestConstant.HEADER_STRING, TestConstant.TOKEN_PREFIX + token)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(id))
                .andDo(print());
    }
}
