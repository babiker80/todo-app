package de.infoteam.publicservice.todo_app.controller;

import de.infoteam.publicservice.todo_app.dto.*;
import de.infoteam.publicservice.todo_app.service.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(command));
    }

    @Operation(summary = "list")
    @GetMapping
    public ResponseEntity<List<TodoDto>> list() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "get by id")
    @GetMapping("/{todoId}")
    public ResponseEntity<TodoDto> getById(@PathVariable UUID todoId) {
        return ResponseEntity.ok(service.findTodoById(todoId));
    }

    @Operation(summary = "update")
    @PutMapping("/{todoId}")
    public ResponseEntity<TodoDto> update(@PathVariable UUID todoId,
                                    @RequestBody UpdateTodoCommandImpl command) {
        return ResponseEntity.ok(service.update(todoId, command));
    }

    @Operation(summary = "patch")
    @PatchMapping("/{todoId}")
    public ResponseEntity<TodoDto> patch(@PathVariable UUID todoId,
                                   @RequestBody PatchTodoCommandImpl command) {
        return ResponseEntity.ok(service.patch(todoId, command));
    }

    @Operation(summary = "delete")
    @DeleteMapping("/{todoId}")
    public ResponseEntity<TodoDto> delete(@PathVariable UUID todoId) {
        return ResponseEntity.ok(service.delete(todoId));
    }
}
