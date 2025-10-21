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

    public Todo create(Todo todo) {
        todo.setId(null);
        todo.setCreated(OffsetDateTime.now());
        todo.setLastModified(OffsetDateTime.now());
        if (todo.getStatus() == null) {
            todo.setStatus(TodoStatus.OPEN);
        }
        return repository.save(todo);
    }

    public Optional<Todo> update(UUID id, Todo updated) {
        return repository.findById(id).map(existing -> {
            existing.setName(updated.getName());
            existing.setDueDate(updated.getDueDate());
            existing.setDetails(updated.getDetails());
            existing.setStatus(updated.getStatus());
            existing.setLastModified(OffsetDateTime.now());
            return repository.save(existing);
        });
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }
}
