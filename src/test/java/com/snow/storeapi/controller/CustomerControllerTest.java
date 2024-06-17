package com.snow.storeapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.snow.storeapi.TestConstant;
import com.snow.storeapi.entity.Customer;
import com.snow.storeapi.mapper.CustomerMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.*;
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
    }

    @Order(1)
    @Test
    @RepeatedTest(2)
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
                                .param("searchText", TestConstant.CUSTOMER_CREATE_NAME)
                                .param("limit", "2")
                                .header(TestConstant.HEADER_STRING, TestConstant.TOKEN_PREFIX + token)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.items", hasSize(2)))
                .andExpect(jsonPath("$.total").value("3"))
                .andExpect(jsonPath("$.items[0].id").value(id))
                .andExpect(jsonPath("$.items[0].name").value(TestConstant.CUSTOMER_CREATE_NAME))
                .andExpect(jsonPath("$.items[0].mobile").value(TestConstant.CUSTOMER_CREATE_MOBILE))
                .andExpect(jsonPath("$.items[0].addressDetail").value(TestConstant.CUSTOMER_CREATE_ADDRESS_DETAIL))
                .andExpect(jsonPath("$.items[0].address[0]").value(TestConstant.CUSTOMER_CREATE_ADDRESS[0]))
                .andExpect(jsonPath("$.items[0].address[1]").value(TestConstant.CUSTOMER_CREATE_ADDRESS[1]))
                .andExpect(jsonPath("$.items[0].address[2]").value(TestConstant.CUSTOMER_CREATE_ADDRESS[2]))
                .andExpect(jsonPath("$.items[0].address[3]").value(TestConstant.CUSTOMER_CREATE_ADDRESS[3]))
                .andExpect(jsonPath("$.items[0].debt").value(TestConstant.CUSTOMER_CREATE_DEBT.toString()));
        mockMvc.perform(
                        MockMvcRequestBuilders.get(BASE_URL + "/page")
                                .accept(MediaType.APPLICATION_JSON)
                                .param("searchText", TestConstant.CUSTOMER_CREATE_MOBILE)
                                .param("limit", "2")
                                .header(TestConstant.HEADER_STRING, TestConstant.TOKEN_PREFIX + token)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.items", hasSize(2)))
                .andExpect(jsonPath("$.total").value("3"))
                .andExpect(jsonPath("$.items[0].id").value(id))
                .andExpect(jsonPath("$.items[0].name").value(TestConstant.CUSTOMER_CREATE_NAME))
                .andExpect(jsonPath("$.items[0].mobile").value(TestConstant.CUSTOMER_CREATE_MOBILE))
                .andExpect(jsonPath("$.items[0].addressDetail").value(TestConstant.CUSTOMER_CREATE_ADDRESS_DETAIL))
                .andExpect(jsonPath("$.items[0].debt").value(TestConstant.CUSTOMER_CREATE_DEBT.toString()))
                .andDo(print());
    }

    @Order(3)
    @Test
    public void testUpdate() throws Exception {
        var updateName = "王庆云改";
        var updateMobile = "15163927337";
        var updateAddressDetail = "双城路19号";
        var updateDebt = 1001;
        var updateCustomerDTO = Customer.builder()
                .name(updateName)
                .mobile(updateMobile)
                .addressDetail(updateAddressDetail)
                .debt(updateDebt)
                .build();
        mockMvc.perform(
                        MockMvcRequestBuilders.patch(BASE_URL + "/" + TestConstant.CUSTOMER_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateCustomerDTO))
                                .header(TestConstant.HEADER_STRING, TestConstant.TOKEN_PREFIX + token)
                ).andExpect(status().isOk());
        var newCustomer = customerMapper.selectById(TestConstant.CUSTOMER_ID);
        assertEquals(newCustomer.getName(), updateName, "customer.name should been changed");
        assertEquals(newCustomer.getMobile(), updateMobile, "customer.mobile should been changed");
        assertEquals(newCustomer.getAddressDetail(), updateAddressDetail, "customer.addressDetail should been changed");
    }

    @Order(4)
    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete(BASE_URL + "/" + id)
                        .header(TestConstant.HEADER_STRING, TestConstant.TOKEN_PREFIX + token)
        ).andExpect(status().isOk());
        var bill = customerMapper.selectById(id);
        assertNull(bill, "Customer should not be exist.");
    }

    @AfterAll
    public void afterAll() {
//        jdbc.execute("truncate table customer;");
    }

}
