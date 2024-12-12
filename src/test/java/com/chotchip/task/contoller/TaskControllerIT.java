package com.chotchip.task.contoller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
//@ActiveProfiles()
@Transactional
class TaskControllerIT {

    @Autowired
    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();


    @Test
    @Sql(value = {"/sql/createBeforeAll.sql", "/sql/task.sql"})
    void getTaskById_RequestUserIsAdmin_ResponseTaskResponseDTO() throws Exception {
        String token = takeTokenAdmin();
        //given
        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.get("/api/tasks/1")
                        .header(HttpHeaders.AUTHORIZATION, token);
        //when
        mockMvc.perform(requestBuilder)
                //then
                .andExpectAll(
                        status().isOk(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        content().json(
                                """
                                        {
                                            "id": 1,
                                            "title": "title",
                                            "details": "details",
                                            "status": "PENDING",
                                            "priority": "LOW",
                                            "comment": [
                                                {
                                                    "details": "details in title 1",
                                                    "author": {
                                                        "email": "admin.com"
                                                    }
                                                }
                                            ],
                                            "author": {
                                                "email": "admin.com"
                                            },
                                            "executor": {
                                                "email": "client.com"
                                            }
                                        }
                                                                                """
                        )
                );
    }

    @Test
    @Sql(value = {"/sql/createBeforeAll.sql", "/sql/task.sql"})
    @WithMockUser(username = "clientNotTask.com", authorities = "CLIENT")
    void getTaskById_RequestUserIsClientNotRights_ResponseUserNotRightsException() throws Exception {
        String token = takeTokenUserNotTask();
        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.get("/api/tasks/1")
                        .header(HttpHeaders.AUTHORIZATION, token);
        mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        content().json("""
                                    {
                                    "message": "User not rights to this task"
                                    }
                                """)
                );

    }


    @Test
    @Sql(value = {"/sql/createBeforeAll.sql", "/sql/user.sql"})
    void createTask_RequestUserIsAdmin_ResponseTaskResponseDTO() throws Exception {
        String token = takeTokenAdmin();

        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.post("/api/tasks/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "title": "title create",
                                "details": "details create",
                                "priority": "MIDDLE",
                                "comment": [
                                    {"details":"comment details"}
                                ],
                                "executor": "client.com"
                                }
                                """)
                        .header(HttpHeaders.AUTHORIZATION, token);

        mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isCreated(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        content().json("""
                                   {
                                            "id": 1,
                                            "title": "title create",
                                            "details": "details create",
                                            "status": "PENDING",
                                            "priority": "MIDDLE",
                                            "comment": [
                                                {
                                                    "details": "comment details",
                                                    "author": {
                                                        "email": "admin.com"
                                                    }
                                                }
                                            ],
                                            "author": {
                                                "email": "admin.com"
                                            },
                                            "executor": {
                                                "email": "client.com"
                                            }
                                        }
                                """)
                );
    }

    private String takeTokenAdmin() throws Exception {
        MockHttpServletRequestBuilder requestBuilderGetJWTToken =
                MockMvcRequestBuilders.post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "email": "admin.com",
                                "password": "123"
                                }
                                """);
        MvcResult mvcResult = mockMvc.perform(requestBuilderGetJWTToken).andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        Map<String,String> map = objectMapper.readValue(result, Map.class);
        return "Bearer " + map.get("token");
    }

    private String takeTokenUserNotTask() throws Exception {
        MockHttpServletRequestBuilder requestBuilderGetJWTToken =
                MockMvcRequestBuilders.post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "email": "clientNotTask.com",
                                "password": "123"
                                }
                                """);
        MvcResult mvcResult = mockMvc.perform(requestBuilderGetJWTToken).andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        Map<String,String> map = objectMapper.readValue(result, Map.class);
        return "Bearer " + map.get("token");
    }


}