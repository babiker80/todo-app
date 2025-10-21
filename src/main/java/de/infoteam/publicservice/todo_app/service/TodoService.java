package de.infoteam.publicservice.todo_app.service;

import de.infoteam.publicservice.todo_app.entity.Todo;
import de.infoteam.publicservice.todo_app.model.TodoStatus;
import de.infoteam.publicservice.todo_app.repository.TodoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TodoService {

    private final TodoRepository repository;

    public List<Todo> findAll() {
        return repository.findAll();
    }

    public Optional<Todo> findById(UUID id) {
        return repository.findById(id);
    }

    public Todo save(Todo todo) {
        return repository.save(todo);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }
}
