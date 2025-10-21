package de.infoteam.publicservice.todo_app.controller;

import de.infoteam.publicservice.todo_app.dto.*;
import de.infoteam.publicservice.todo_app.entity.Todo;
import de.infoteam.publicservice.todo_app.exceptions.TodoNotFoundException;
import de.infoteam.publicservice.todo_app.model.TodoStatus;
import de.infoteam.publicservice.todo_app.service.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/todo")
@Tag(name = "TodoController", description = "Operations for Todo resources")
public class TodoController {

    private final TodoService service;

    @Operation(summary = "create")
    @PostMapping
    public ResponseEntity<TodoDto> create(@RequestBody CreateTodoCommandImpl command) {
        Todo todo = Todo.builder()
                .name(command.getName())
                .dueDate(command.getDueDate())
                .details(command.getDetails())
                .status(TodoStatus.OPEN)
                .created(OffsetDateTime.now())
                .lastModified(OffsetDateTime.now())
                .build();
        Todo saved = service.save(todo);
        return ResponseEntity.status(HttpStatus.CREATED).body(TodoDto.fromEntity(saved));
    }

    @Operation(summary = "list")
    @GetMapping
    public ResponseEntity<List<TodoDto>> list() {
        List<TodoDto> todos = service.findAll()
                .stream()
                .map(TodoDto::fromEntity)
                .toList();
        return ResponseEntity.ok(todos);
    }

    @Operation(summary = "get by id")
    @GetMapping("/{todoId}")
    public ResponseEntity<TodoDto> getById(@PathVariable UUID todoId) {
        Todo todo = service.findById(todoId).orElseThrow(() -> new TodoNotFoundException(todoId));
        return ResponseEntity.ok(TodoDto.fromEntity(todo));
    }

    @Operation(summary = "update")
    @PutMapping("/{todoId}")
    public ResponseEntity<?> update(@PathVariable UUID todoId,
                                    @RequestBody UpdateTodoCommandImpl command) {
        Todo todo = service.findById(todoId).orElseThrow(() -> new TodoNotFoundException(todoId));
        todo.setName(command.getName());
        todo.setDetails(command.getDetails());
        todo.setDueDate(command.getDueDate());
        todo.setStatus(command.getStatus());
        todo.setLastModified(OffsetDateTime.now());
        Todo saved = service.save(todo);
        return ResponseEntity.ok(TodoDto.fromEntity(saved));
    }

    @Operation(summary = "patch")
    @PatchMapping("/{todoId}")
    public ResponseEntity<?> patch(@PathVariable UUID todoId,
                                   @RequestBody PatchTodoCommandImpl command) {
        Todo todo = service.findById(todoId).orElseThrow(() -> new TodoNotFoundException(todoId));
        if (command.getStatus() != null) {
            todo.setStatus(command.getStatus());
            todo.setLastModified(OffsetDateTime.now());
        }
        return ResponseEntity.ok(TodoDto.fromEntity(service.save(todo)));
    }

    @Operation(summary = "delete")
    @DeleteMapping("/{todoId}")
    public ResponseEntity<?> delete(@PathVariable UUID todoId) {
        Todo todo = service.findById(todoId).orElseThrow(() -> new TodoNotFoundException(todoId));
        service.delete(todoId);
        return ResponseEntity.ok(TodoDto.fromEntity(todo));
    }
}
