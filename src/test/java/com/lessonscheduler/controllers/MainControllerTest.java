package com.lessonscheduler.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lessonscheduler.models.DTO.loginDTO;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.assertEquals;

/* @SpringBootTest will bootstrap the full application 
context, which means we can @Autowire any bean that's 
picked up by component scanning into our test */
@SpringBootTest 
@AutoConfigureMockMvc
public class MainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void loginShouldReturnJWT() throws Exception {

        loginDTO loginDTO = new loginDTO();
        loginDTO.setEmail("prof@gmail.com");
        loginDTO.setPassword("pass111");

        MvcResult andReturn = mockMvc.perform(MockMvcRequestBuilders.post("/login")
            .content(objectMapper.writeValueAsString(loginDTO))
            .contentType("application/json")
            )
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn();
            //.andExpect(content().string(containsString("Bearer")));

            // Check if there is a jwt token in the response.
            // JWT consists of 3 parts divided by "."
            // I split JWT to check if there are 3 parts in it.
            // If yes  - all is good.
            String response = andReturn.getResponse().getContentAsString();
            String substringContainingToken = response.substring(13, response.length() - 2);
            String[] split = substringContainingToken.split("\\.");
            assertEquals(3, split.length);
    }
}
