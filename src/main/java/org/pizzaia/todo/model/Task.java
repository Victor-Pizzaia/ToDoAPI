package org.pizzaia.todo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "Tasks")
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id", nullable = false, unique = true)
    private long id;

    @Column(name = "task_name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "due_date", nullable = false)
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dueDate;

    @Column(name = "status", nullable = false)
    private Status status;

    public Task(TaskDTO task) {
        this.id = task.getId();
        this.name = task.getName();
        this.description = task.getDescription();
        this.dueDate = task.getDueDate();
        this.status = task.getStatus() != null ? task.getStatus() : Status.TODO;
    }
}
