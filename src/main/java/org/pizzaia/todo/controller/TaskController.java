package org.pizzaia.todo.controller;

import org.pizzaia.todo.exception.ValidationException;
import org.pizzaia.todo.model.Status;
import org.pizzaia.todo.model.Task;
import org.pizzaia.todo.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/todo")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        return new ResponseEntity<>(taskService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Task>> getTasksByStatus(@PathVariable Status status) {
        return new ResponseEntity<>(taskService.findByStatus(status), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<? super Task> getTaskById(@PathVariable long id) {
        try {
            var task = taskService.findById(id);
            return new ResponseEntity<>(task, HttpStatus.FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<? super Task> getTaskByName(@PathVariable String name) {
        try {
            var task = taskService.findByName(name);
            return new ResponseEntity<>(task, HttpStatus.FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<? super Task> saveOrUpdateTask(@RequestBody Task task) {
        try {
            var savedTask = taskService.saveOrUpdate(task);
            return new ResponseEntity<>(savedTask, HttpStatus.CREATED);
        } catch (ValidationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable long id) {
        try {
            taskService.delete(id);
            return new ResponseEntity<>("Tarefa excluida", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/name/{name}")
    public ResponseEntity<String> deleteTaskByName(@PathVariable String name) {
        try {
            taskService.deleteByName(name);
            return new ResponseEntity<>("Tarefa excluida", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
