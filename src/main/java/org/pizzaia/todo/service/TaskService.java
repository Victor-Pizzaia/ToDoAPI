package org.pizzaia.todo.service;

import lombok.RequiredArgsConstructor;
import org.pizzaia.todo.exception.ValidationException;
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

    public Task findById(long id) throws Exception {
        var task = taskRepository.findById(id);
        if(task.isPresent()) {
            return task.get();
        }
        throw new Exception("Tarefa não encontrada");
    }

    public List<Task> findByStatus(Status status) {
        return taskRepository.findAllByStatus(status);
    }

    public Task findByName(String name) throws Exception {
        var task = taskRepository.findByName(name);
        if(task.isPresent()) {
            return task.get();
        }
        throw new Exception("Tarefa não encontrada");
    }

    public Task saveOrUpdate(Task task) throws ValidationException {
        if(task.getName() == null || task.getName().equals("")) {
            throw new ValidationException("Preencha o nome da tarefa");
        }
        if(task.getDueDate() == null) {
            throw new ValidationException("Preencha a data da tarefa");
        }
        if(!DateUtils.isEqualOrFutureDate(task.getDueDate())) {
            throw new ValidationException("A data da tarefa não deve estar no passado");
        }
        return taskRepository.save(task);
    }

    public void delete(long id) throws Exception {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
            return;
        }
        throw new Exception("A tarefa não existe");
    }

    @Transactional
    public void deleteByName(String name) throws Exception {
        if (taskRepository.existsByName(name)) {
            taskRepository.deleteByName(name);
            return;
        }
        throw new Exception("A tarefa não existe");
    }
}
