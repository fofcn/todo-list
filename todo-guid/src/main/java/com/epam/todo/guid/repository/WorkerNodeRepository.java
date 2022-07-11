package com.epam.todo.guid.repository;

import com.epam.todo.guid.model.WorkerNode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkerNodeRepository extends JpaRepository<WorkerNode, Long> {
}
