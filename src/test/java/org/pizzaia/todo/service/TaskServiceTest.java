package org.pizzaia.todo.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pizzaia.todo.model.Status;
import org.pizzaia.todo.model.Task;
import org.pizzaia.todo.repository.TaskRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private final Task utilTask = new Task();

    @BeforeEach
    public void setupMock() {
        utilTask.setId(1);
        utilTask.setName("Testes");
        utilTask.setDueDate(LocalDate.now());
    }

    @Test
    void shouldNotSaveTaskWithoutName() {
        Task task = new Task();
        task.setDueDate(LocalDate.now());
        try {
            taskService.saveOrUpdate(task);
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("Preencha o nome da tarefa");
        }
    }

    @Test
    void shouldNotSaveTaskWithoutDate() {
        Task task = new Task();
        task.setName("Testes");
        try {
            taskService.saveOrUpdate(task);
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("Preencha a data da tarefa");
        }
    }

    @Test
    void shouldNotSaveTaskWithPastDate() {
        Task task = new Task();
        task.setName("Testes");
        task.setDueDate(LocalDate.of(2015, 1, 1));
        try {
            taskService.saveOrUpdate(task);
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("A data da tarefa não deve estar no passado");
        }
    }

    @Test
    void shouldSaveTaskWithSuccess() throws IllegalArgumentException {
        Task task = new Task();
        task.setName("Testes");
        task.setDueDate(LocalDate.now());
        taskService.saveOrUpdate(task);
        Mockito.verify(taskRepository).save(task);
    }

    @Test
    public void shouldReturnAllTasks() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task());
        Mockito.when(taskRepository.findAll()).thenReturn(tasks);
        List<Task> expected = taskService.findAll();
        assertThat(expected).isEqualTo(tasks);
        Mockito.verify(taskRepository).findAll();
    }

    @Test
    void shouldReturnTaskById() throws IllegalArgumentException {
        Mockito.when(taskRepository.findById(1L)).thenReturn(Optional.of(utilTask));
        Task expectedTask = taskService.findById(1);
        assertThat(expectedTask).isEqualTo(utilTask);
        Mockito.verify(taskRepository).findById(1L);
    }

    @Test
    void shouldNotReturnTaskById() {
        try {
            taskService.findById(77);
        } catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Tarefa não encontrada");
        }
    }

    @Test
    public void shouldReturnAllTasksByStatus() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task());
        Mockito.when(taskRepository.findAllByStatus(Status.TODO)).thenReturn(tasks);
        List<Task> expected = taskService.findByStatus(Status.TODO);
        assertThat(expected).isEqualTo(tasks);
        Mockito.verify(taskRepository).findAllByStatus(Status.TODO);
    }

    @Test
    void shouldReturnTaskByName() throws IllegalArgumentException {
        Mockito.when(taskRepository.findByName("Testes")).thenReturn(Optional.of(utilTask));
        Task expectedTask = taskService.findByName("Testes");
        assertThat(expectedTask).isEqualTo(utilTask);
        Mockito.verify(taskRepository).findByName("Testes");
    }

    @Test
    void shouldNotReturnTaskByName() {
        try {
            taskService.findByName("Error");
        } catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Tarefa não encontrada");
        }
    }

    /*@Test
    void shoudlDeleteATaskById() throws IllegalArgumentException {
        Mockito.when(taskRepository.findById(utilTask.getId())).thenReturn(Optional.of(utilTask));
        taskService.delete(utilTask.getId());
        Mockito.verify(taskRepository).deleteById(utilTask.getId());
    }*/

    @Test
    void shouldReturnAnErrorWhenDeleteNonExistTaskById() {
        try {
            taskService.delete(utilTask.getId());
        } catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("A tarefa não existe");
        }
    }

    /*@Test
    void shoudlDeleteATaskByName() throws IllegalArgumentException {
        Mockito.when(taskRepository.findByName(utilTask.getName())).thenReturn(Optional.of(utilTask));
        taskService.deleteByName(utilTask.getName());
        Mockito.verify(taskRepository).deleteByName(utilTask.getName());
    }*/

    @Test
    void shouldReturnAnErrorWhenDeleteNonExistTaskByName() {
        try {
            taskService.deleteByName(utilTask.getName());
        } catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("A tarefa não existe");
        }
    }
}
