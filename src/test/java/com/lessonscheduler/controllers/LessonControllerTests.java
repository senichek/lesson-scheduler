package com.lessonscheduler.controllers;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lessonscheduler.models.Lesson;
import com.lessonscheduler.models.DTO.LessonCreationDTO;
import com.lessonscheduler.security.JwtGenerator;
import com.lessonscheduler.security.UserPrincipalDetailsService;

/* @SpringBootTest will bootstrap the full application 
context, which means we can @Autowire any bean that's 
picked up by component scanning into our test */
@SpringBootTest 
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // - It allows to use BeforeAll with non-static method
public class LessonControllerTests {

    @Autowired
    public UserPrincipalDetailsService userPrincipalDetailsService;

    @Autowired
    public JwtGenerator jwtGenerator;

    public String jwt;

    @BeforeAll
    public void getJwt() {
        // We need JWT for all the requests.
            UserDetails userDetails = userPrincipalDetailsService.loadUserByUsername("prof@gmail.com");

            UsernamePasswordAuthenticationToken authenticationToken = 
            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            jwt = "Bearer " + jwtGenerator.generateJWT(authenticationToken);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void createAndDeleteLesson() throws Exception {

        LessonCreationDTO lessonCreationDTO = new LessonCreationDTO();
        lessonCreationDTO.setFrom(LocalDateTime.of(2020, 1, 2, 15, 00));
        lessonCreationDTO.setTo(LocalDateTime.of(2020, 1, 2, 16, 00));


        MvcResult andReturn = mockMvc.perform(MockMvcRequestBuilders.post("/lesson/create")
            .content(objectMapper.writeValueAsString(lessonCreationDTO))
            .contentType("application/json")
            .header("Authorization", jwt)
            )
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn();
            
            String response = andReturn.getResponse().getContentAsString();
            Lesson lesson = objectMapper.readValue(response, Lesson.class);
            assertEquals(1, lesson.getId());
            assertEquals(LocalDateTime.of(2020, 1, 2, 15, 00), lesson.getDateTimeFrom());
            assertEquals(LocalDateTime.of(2020, 1, 2, 16, 00), lesson.getDateTimeTo());

            // Test deletion of the lesson
            MvcResult andRet = mockMvc.perform(MockMvcRequestBuilders.get("/lesson/delete/" + 1)
            .header("Authorization", jwt)
            )
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn();
            
            String deletionResponse = andRet.getResponse().getContentAsString();
            assertEquals("Lesson 1 has been deleted", deletionResponse);
    }

    @Test
    public void reserveAndGetAllUnreserved() throws Exception {
        // Create two lessons and reserve one of them
        LessonCreationDTO lessonOne = new LessonCreationDTO();
        lessonOne.setFrom(LocalDateTime.of(2020, 1, 2, 15, 00));
        lessonOne.setTo(LocalDateTime.of(2020, 1, 2, 16, 00));

        LessonCreationDTO lessonTwo = new LessonCreationDTO();
        lessonTwo.setFrom(LocalDateTime.of(2020, 1, 3, 10, 00));
        lessonTwo.setTo(LocalDateTime.of(2020, 1, 3, 11, 00));

        // Persist to DB
        mockMvc.perform(MockMvcRequestBuilders.post("/lesson/create")
            .content(objectMapper.writeValueAsString(lessonOne))
            .contentType("application/json")
            .header("Authorization", jwt)
            )
            .andExpect(status().isOk())
            .andDo(print());

        mockMvc.perform(MockMvcRequestBuilders.post("/lesson/create")
            .content(objectMapper.writeValueAsString(lessonTwo))
            .contentType("application/json")
            .header("Authorization", jwt)
            )
            .andExpect(status().isOk())
            .andDo(print());

            mockMvc.perform(MockMvcRequestBuilders.get("/lesson/reserve/3")
            .header("Authorization", jwt)
            )
            .andExpect(status().isOk())
            .andDo(print());

            // Get all lessons to make sure the first one is actually reserved
            MvcResult andReturn = mockMvc.perform(MockMvcRequestBuilders.get("/lesson/all")
            .header("Authorization", jwt)
            )
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn();

            String response = andReturn.getResponse().getContentAsString();
            // Convert response into the list of objects
            List<Lesson> lessons = objectMapper.readValue(response, new TypeReference<List<Lesson>>(){});

            assertEquals(true, lessons.get(1).isReserved());

            // Get all unreserved
            MvcResult andRet = mockMvc.perform(MockMvcRequestBuilders.get("/lesson/all/unreserved")
            .header("Authorization", jwt)
            )
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn();

            String responseTwo = andRet.getResponse().getContentAsString();
            // Convert response into the list of objects
            List<Lesson> lessonz = objectMapper.readValue(responseTwo, new TypeReference<List<Lesson>>(){});

            assertEquals(1, lessonz.size());
    }
}
