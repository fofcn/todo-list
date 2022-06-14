package com.epam.todo.task.infrastructure.repository;

import com.epam.todo.task.infrastructure.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {

}
