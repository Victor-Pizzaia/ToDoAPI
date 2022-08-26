package org.pizzaia.todo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class IndexControllerTest {
    private MockMvc mockMvc;
    private JacksonTester<String> json;

    @InjectMocks
    private IndexController indexController;

    @BeforeEach
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper().registerModule(new JavaTimeModule()));

        mockMvc = MockMvcBuilders.standaloneSetup(indexController).build();
    }

    @Test
    private void shouldBeReturnAHomeMesssage() throws Exception {
        Mockito.when(indexController.index()).thenReturn("Sejá bem vindo ao app de cadastro de tarefas!");
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andReturn()
                .getResponse();
        assertThat(response.getContentAsString()).isEqualTo(json.write("Sejá bem vindo ao app de cadastro de tarefas!").getJson());
    }
}
