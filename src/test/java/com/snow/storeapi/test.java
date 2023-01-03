package com.snow.storeapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.snow.storeapi.controller.CustomerController;

import java.time.LocalDate;
import java.util.Arrays;

public class test {
    public static void main(String[] args) throws JsonProcessingException {
        var json = "{\"orderTime\":\"2022-11-23T20:05:00.172089900\"}";
        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.registerModule(new JSR310Module());
//        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
//        System.out.println(objectMapper.writeValueAsString(map));
        JsonNode jsonNode = objectMapper.readTree(json);
        System.out.println(jsonNode.isArray());
        System.out.println(Arrays.asList(TestConstant.CUSTOMER_CREATE_ADDRESS));
//        Link link = WebMvcLinkBuilder.linkTo(methodOn(CustomerController.class)
//                .getAllEmployees())
//                .withRel("employees");
    }
}
