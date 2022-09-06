package org.pizzaia.todo.controller;

import org.pizzaia.todo.model.Status;
import org.pizzaia.todo.model.Task;
import org.pizzaia.todo.model.TaskDTO;
import org.pizzaia.todo.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/todo")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping
    public ResponseEntity<List<Task>> getTasks(@RequestParam Map<String, String> params) {
        List<Task> tasks = new ArrayList<>();
        try {
            if(params.get("id") != null) {
                tasks.add(taskService.findById(Integer.parseInt(params.get("id"))));
            } else if(params.get("name") != null) {
                tasks.add(taskService.findByName(params.get("name")));
            } else if(params.get("status") != null) {
                Status status = Status.valueOf(params.get("status").toUpperCase());
                tasks.addAll(taskService.findByStatus(status));
            } else {
                tasks.addAll(taskService.findAll());
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }

        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<? super Task> saveOrUpdateTask(@RequestBody TaskDTO taskDTO) {
        Task task = new Task(taskDTO);
        try {
            var savedTask = taskService.saveOrUpdate(task);
            return new ResponseEntity<>(savedTask, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping
    public ResponseEntity<String> deleteTasks(@RequestParam Map<String, String> params) {
        try {
            if(params.get("id") != null) {
                taskService.delete(Integer.parseInt(params.get("id")));
            } else if(params.get("name") != null) {
                taskService.deleteByName(params.get("name"));
            } else {
                throw new IllegalArgumentException("Argumentos inválidos");
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }

        return new ResponseEntity<>("Tarefa excluida", HttpStatus.OK);
    }
}
