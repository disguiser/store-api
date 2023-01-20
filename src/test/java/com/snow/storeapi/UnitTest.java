package com.snow.storeapi;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@DisplayName("测试")
@TestPropertySource("/application-test.properties")
//@SpringBootTest
// 旧版本
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Slf4j
public class UnitTest {
//    @BeforeAll
    public void setup() {
    }

    @BeforeEach
    void init() {
        log.info("before each");
    }

    @AfterEach
    void endEach() {
        log.info("after each");
    }

    @RepeatedTest(3)
    public void testCreate() {
        Assertions.assertEquals(1,1);
    }

    @ParameterizedTest
    @ValueSource(ints = {1,2,3})
    public void test5(int num) {
        log.info("ValueSource: " + num);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/output.csv")
    public void test8(String name, String token) {
        log.info(name + ", " + token);
    }

    @Disabled
    void neverExecute() {
        log.info("never execute");
    }
}
