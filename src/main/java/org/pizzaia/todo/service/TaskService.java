package org.pizzaia.todo.service;

import lombok.RequiredArgsConstructor;
import org.pizzaia.todo.model.Status;
import org.pizzaia.todo.model.Task;
import org.pizzaia.todo.repository.TaskRepository;
import org.pizzaia.todo.util.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    public Task findById(long id) {
        var task = taskRepository.findById(id);
        if(task.isPresent()) {
            return task.get();
        }
        throw new IllegalArgumentException("Tarefa não encontrada");
    }

    public List<Task> findByStatus(Status status) {
        return taskRepository.findAllByStatus(status);
    }

    public Task findByName(String name) {
        var task = taskRepository.findByName(name);
        if(task.isPresent()) {
            return task.get();
        }
        throw new IllegalArgumentException("Tarefa não encontrada");
    }

    public Task saveOrUpdate(Task task) {
        if(task.getName() == null || task.getName().equals("")) {
            throw new IllegalArgumentException("Preencha o nome da tarefa");
        }
        if(task.getDueDate() == null) {
            throw new IllegalArgumentException("Preencha a data da tarefa");
        }
        if(!DateUtils.isEqualOrFutureDate(task.getDueDate())) {
            throw new IllegalArgumentException("A data da tarefa não deve estar no passado");
        }
        return taskRepository.save(task);
    }

    public void delete(long id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
            return;
        }
        throw new IllegalArgumentException("A tarefa não existe");
    }

    @Transactional
    public void deleteByName(String name) {
        if (taskRepository.existsByName(name)) {
            taskRepository.deleteByName(name);
            return;
        }
        throw new IllegalArgumentException("A tarefa não existe");
    }
}
