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

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/create-user-before.sql", "/create-task-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/create-user-after.sql", "/create-task-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void unauthenticatedManagerPageTest() throws Exception {
        this.mockMvc.perform(get("/manager"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(SecurityMockMvcResultMatchers.unauthenticated())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @WithUserDetails("Admin")
    void authenticatedManagerPageTest() throws Exception {
        this.mockMvc.perform(get("/manager"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(authenticated())
                .andExpect(content().string(containsString("Менеджер задач")));
    }

    @Test
    @WithUserDetails("User")
    void contentManagerTest() throws Exception {
        this.mockMvc.perform(get("/manager"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(authenticated())
                .andExpect(xpath("//*[@id='title']").string(containsString("Title 1")))
                .andExpect(xpath("//*[@id='description']").string(containsString("description 1")));
    }

    @Test
    @WithUserDetails("User")
    void taskListManagerTest() throws Exception {
        this.mockMvc.perform(get("/manager"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(xpath("//*[@id='task']").nodeCount(3));
    }

    @Test
    @WithUserDetails("Admin")
    void taskListManagerTest2() throws Exception {
        this.mockMvc.perform(get("/manager"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(xpath("//*[@id='task']").nodeCount(2));
    }

    @Test
    @WithUserDetails("User")
    void taskEditorPageTest() throws Exception {
        this.mockMvc.perform(get("/task/taskEditor"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Редактор задачи")));
    }

    @Test
    @WithUserDetails("User")
    void addTaskTest() throws Exception {
        this.mockMvc.perform(post("/task/taskEditor").param("title", "title")
                        .param("description", "descriptionTEXT").with(csrf()))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("descriptionTEXT")));

        this.mockMvc.perform(get("/manager"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(xpath("//*[@id='task']").nodeCount(4));
    }

    @Test
    @WithUserDetails("User")
    void addBadTaskTest2() throws Exception {
        this.mockMvc.perform(post("/task/taskEditor")
                        .param("title", "")
                        .param("description", "")
                        .with(csrf()))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Введите название!")))
                .andExpect(content().string(containsString("Введите описание!")));
    }

    @Test
    @WithUserDetails("User")
    void changeTaskTest() throws Exception {
        this.mockMvc.perform(post("/task/taskEditor")
                        .param("id", "7")
                        .param("status", "ACTIVE")
                        .param("title", "new title test")
                        .param("description", "new description text")
                        .with(csrf()))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk());

        this.mockMvc.perform(get("/task/7"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(authenticated())
                .andExpect(content().string(containsString("new title test")))
                .andExpect(content().string(containsString("new description text")));
    }

}




















