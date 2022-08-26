package org.pizzaia.todo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pizzaia.todo.model.Status;
import org.pizzaia.todo.model.Task;
import org.pizzaia.todo.model.TaskDTO;
import org.pizzaia.todo.repository.TaskRepository;
import org.pizzaia.todo.service.TaskService;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class TaskControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    private JacksonTester<Task> json;
    private JacksonTester<List<Task>> listJson;
    private JacksonTester<TaskDTO> jsonDTO;

    private Task utilTask;
    private final TaskDTO utilDtoTask = new TaskDTO();

    private final List<Task> utilListTasks = new ArrayList<>();

    @BeforeEach
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper().registerModule(new JavaTimeModule()));

        mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();
        utilDtoTask.setId(1);
        utilDtoTask.setName("Testes");
        utilDtoTask.setDueDate(LocalDate.now());
        utilTask = new Task(utilDtoTask);
        utilListTasks.add(utilTask);
    }

    @Test
    public void shouldReturnAllTasks() throws Exception {
        Mockito.when(taskService.findAll()).thenReturn(utilListTasks);

        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.get("/todo").accept(MediaType.APPLICATION_JSON)
        )
                .andReturn()
                .getResponse();

        List<Task> expected = new ArrayList<>();
        expected.add(utilTask);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString())
                .isEqualTo(
                        listJson.write(expected).getJson()
                );
    }

    @Test
    public void shouldReturnAllTasksByStatus() throws Exception {
        Mockito.when(taskService.findByStatus(Status.TODO)).thenReturn(utilListTasks);

        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.get("/todo/status/TODO").accept(MediaType.APPLICATION_JSON)
                )
                .andReturn()
                .getResponse();

        List<Task> expected = new ArrayList<>();
        expected.add(utilTask);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString())
                .isEqualTo(
                        listJson.write(expected).getJson()
                );
    }

    @Test
    public void shouldReturnTaskById() throws Exception {
        Mockito.when(taskService.findById(1)).thenReturn(utilTask);

        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.get("/todo/1").accept(MediaType.APPLICATION_JSON)
                )
                .andReturn()
                .getResponse();

        Task expected = new Task(utilDtoTask);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.FOUND.value());
        assertThat(response.getContentAsString())
                .isEqualTo(
                        json.write(expected).getJson()
                );
    }

    @Test
    public void shouldReturnTaskWasNotFoundById() throws Exception {
        Mockito.when(taskService.findById(77)).thenThrow(new IllegalArgumentException());

        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.get("/todo/77").accept(MediaType.APPLICATION_JSON)
                )
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(response.getContentAsString()).isEmpty();
    }

    @Test
    public void shouldReturnTaskByName() throws Exception {
        Mockito.when(taskService.findByName("Testes")).thenReturn(utilTask);

        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.get("/todo/name/Testes").accept(MediaType.APPLICATION_JSON)
                )
                .andReturn()
                .getResponse();

        Task expected = new Task(utilDtoTask);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.FOUND.value());
        assertThat(response.getContentAsString())
                .isEqualTo(
                        json.write(expected).getJson()
                );
    }

    @Test
    public void shouldReturnTaskWasNotFoundByName() throws Exception {
        Mockito.when(taskService.findByName("Error")).thenThrow(new IllegalArgumentException());

        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.get("/todo/name/Error").accept(MediaType.APPLICATION_JSON)
                )
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(response.getContentAsString()).isEmpty();
    }

    @Test
    public void shouldSaveTask() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.post("/todo")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        jsonDTO.write(new TaskDTO(2, "Teste", "descricao", LocalDate.now(), Status.TODO)).getJson()
                                )
                )
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());

        ArgumentCaptor<Task> argCaptor = ArgumentCaptor.forClass(Task.class);
        Mockito.verify(taskService).saveOrUpdate(argCaptor.capture());
        Task task = argCaptor.getValue();

        assertThat(task.getId()).isEqualTo(2);
        assertThat(task.getName()).isEqualTo("Teste");
        assertThat(task.getDueDate()).isEqualTo(LocalDate.now());
    }

    @Test
    public void shouldNotSaveTaskWithoutName() throws Exception {
        TaskDTO taskWithoutName = new TaskDTO(2, null, "descricao", LocalDate.now(), Status.TODO);
        Mockito.when(taskService.saveOrUpdate(new Task(taskWithoutName))).thenThrow(new IllegalArgumentException());

        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.post("/todo")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        jsonDTO.write(taskWithoutName).getJson()
                                )
                )
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void shouldNotSaveTaskWithoutDueDate() throws Exception {
        TaskDTO taskWithoutName = new TaskDTO(2, "Teste", "descricao", null, Status.TODO);
        Mockito.when(taskService.saveOrUpdate(new Task(taskWithoutName))).thenThrow(new IllegalArgumentException());

        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.post("/todo")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        jsonDTO.write(taskWithoutName).getJson()
                                )
                )
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void shouldNotSaveTaskWithPastDate() throws Exception {
        TaskDTO taskWithoutName = new TaskDTO(2, "Teste", "descricao", LocalDate.of(2000, 1, 1), Status.TODO);
        Mockito.when(taskService.saveOrUpdate(new Task(taskWithoutName))).thenThrow(new IllegalArgumentException());

        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.post("/todo")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        jsonDTO.write(taskWithoutName).getJson()
                                )
                )
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void shouldDeleteTask() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.delete("/todo/1").accept(MediaType.APPLICATION_JSON)
                )
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    /*@Test
    public void shouldNotDeleteNonExistentTask() throws Exception {
        Mockito.when(taskRepository.existsById(77L)).thenThrow(new IllegalStateException());
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.delete("/todo/77").accept(MediaType.APPLICATION_JSON)
                )
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }*/

    @Test
    public void shouldDeleteTaskByName() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.delete("/todo/name/Testes").accept(MediaType.APPLICATION_JSON)
                )
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    /*@Test
    public void shouldNotDeleteNonExistentTaskByName() throws Exception {
        Mockito.when(taskRepository.existsByName("Error")).thenReturn(false);
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.delete("/todo/name/Error").accept(MediaType.APPLICATION_JSON)
                )
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }*/
}