package com.dimastik.webtask.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/create-user-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/create-user-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class WebControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebController webController;


    @Test
    void contextLoadsTest() {
        assertThat(webController).isNotNull();
    }

    @Test
    void HomePageTest() throws Exception {
        this.mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Добро пожаловать в менеджер задач.")));
    }

    @Test
    @WithUserDetails("Admin")
    void authenticatedHomePageTest() throws Exception {
        this.mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(SecurityMockMvcResultMatchers.authenticated())
                .andExpect(xpath("//*[@id='hello_user']").string(containsString("Admin")));
    }

    @Test
    @WithUserDetails("User")
    void authenticatedHomePageTest2() throws Exception {
        this.mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(SecurityMockMvcResultMatchers.authenticated())
                .andExpect(xpath("//*[@id='hello_user']").string(containsString("User")));
    }

    @Test
    void unauthenticatedHomePageTest() throws Exception {
        this.mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(SecurityMockMvcResultMatchers.unauthenticated())
                .andExpect(content().string(containsString("Для начала работы войдите или авторизируйтесь!")));
    }

    @Test
    void loginPageTest() throws Exception {
        this.mockMvc.perform(get("/login"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(SecurityMockMvcResultMatchers.unauthenticated())
                .andExpect(content().string(containsString("Авторизация")));
    }
}






































