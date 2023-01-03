package com.snow.storeapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.snow.storeapi.TestConstant;
import com.snow.storeapi.entity.Bill;
import com.snow.storeapi.enums.PaymentChannel;
import com.snow.storeapi.mapper.BillMapper;
import com.snow.storeapi.mapper.CustomerMapper;
import com.snow.storeapi.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author zhou
 * @date 2022/11/6 12:51
 */
@DisplayName("测试账单记录")
@TestPropertySource("/application-test.properties")
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // 只创建测试类的一个实例，并在测试之间重用它
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Slf4j
public class BillControllerTest {
    private MockMvc mockMvc;
    private String token;

    @Autowired
    private BillMapper billMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private JdbcTemplate jdbc;

    private Integer id;

    @BeforeAll
    public void beforeAll() throws JsonProcessingException {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        this.token = JwtUtils.getTestToken();
        jdbc.execute("truncate table customer;");
        jdbc.execute("truncate table bill;");
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
        var bill = Bill.builder()
                .amount(TestConstant.BILL_CREATE_AMOUNT)
                .paymentChannel(PaymentChannel.WECHAT)
                .date(LocalDate.now())
                .customerId(TestConstant.CUSTOMER_ID)
                .build();
            var res = mockMvc.perform(
                MockMvcRequestBuilders.post("/bill")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bill))
                        .header(TestConstant.HEADER_STRING, TestConstant.TOKEN_PREFIX + token)
        ).andExpect(status().isOk()).andReturn();
        id = Integer.parseInt(res.getResponse().getContentAsString());
        var _bill = billMapper.selectById(id);
        assertNotNull(_bill, "Bill should be exist.");
        var customer = customerMapper.selectById(TestConstant.CUSTOMER_ID);
        assertEquals(
                customer.getDebt(),
                TestConstant.CUSTOMER_CREATE_DEBT + TestConstant.BILL_CREATE_AMOUNT,
                "customer.debt should been changed"
        );
    }

    @Order(2)
    @Test
    public void testFindAll() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/bill/all")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("customerId", TestConstant.CUSTOMER_ID.toString())
                        .header(TestConstant.HEADER_STRING, TestConstant.TOKEN_PREFIX + token)
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(id))
                .andExpect(jsonPath("$[0].amount").value(TestConstant.BILL_CREATE_AMOUNT))
                .andExpect(jsonPath("$[0].paymentChannel").value(PaymentChannel.WECHAT))
                .andExpect(jsonPath("$[0].customerId").value(TestConstant.CUSTOMER_ID))
                .andExpect(jsonPath("$[0].date").value(LocalDate.now().toString()))
                .andDo(print());
    }

    @Order(3)
    @Test
    public void testUpdate() throws Exception {
        var oldCustomer = customerMapper.selectById(TestConstant.CUSTOMER_ID);
        var bill = Bill.builder()
                .id(id)
                .paymentChannel(PaymentChannel.WECHAT)
                .amount(TestConstant.BILL_UPDATE_AMOUNT)
                .customerId(TestConstant.CUSTOMER_ID)
                .build();
        mockMvc.perform(
                MockMvcRequestBuilders.patch("/bill")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bill))
                        .header(TestConstant.HEADER_STRING, TestConstant.TOKEN_PREFIX + token)
        ).andExpect(status().isOk());
        var newBill = billMapper.selectById(id);
        var newCustomer = customerMapper.selectById(TestConstant.CUSTOMER_ID);
        assertEquals(
                newBill.getAmount(),
                TestConstant.BILL_UPDATE_AMOUNT,
                "bill.amount should been changed"
        );
        assertEquals(
                oldCustomer.getDebt() - newCustomer.getDebt(),
                TestConstant.BILL_CREATE_AMOUNT - TestConstant.BILL_UPDATE_AMOUNT,
                "customer.debt should been changed"
        );
    }

    @Order(4)
    @Test
    public void testDelete() throws Exception {
        var oldCustomer = customerMapper.selectById(TestConstant.CUSTOMER_ID);
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/bill/" + id)
                        .header(TestConstant.HEADER_STRING, TestConstant.TOKEN_PREFIX + token)
        ).andExpect(status().isOk());
        var bill = billMapper.selectById(id);
        assertNull(bill, "Bill should not be exist.");
        var newCustomer = customerMapper.selectById(TestConstant.CUSTOMER_ID);
        assertEquals(
                oldCustomer.getDebt() - newCustomer.getDebt(),
                TestConstant.BILL_UPDATE_AMOUNT
        );
    }

    @AfterAll
    public void afterAll() {
        jdbc.execute("truncate table customer;");
        jdbc.execute("truncate table bill;");
    }
}
