package de.infoteam.publicservice.todo_app.repository;

import de.infoteam.publicservice.todo_app.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TodoRepository extends JpaRepository<Todo, UUID> {
}
