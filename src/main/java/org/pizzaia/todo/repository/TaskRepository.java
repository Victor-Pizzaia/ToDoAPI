package org.pizzaia.todo.repository;

import org.pizzaia.todo.model.Status;
import org.pizzaia.todo.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByStatus(Status status);
    Optional<Task> findByName(String name);
    boolean existsByName(String name);
    void deleteByName(String name);
}
