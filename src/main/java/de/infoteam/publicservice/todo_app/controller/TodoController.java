package de.infoteam.publicservice.todo_app.controller;

import de.infoteam.publicservice.todo_app.entity.Todo;
import de.infoteam.publicservice.todo_app.service.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/todos")
@Tag(name = "ToDo API", description = "Verwaltung von ToDo-Aufgaben")
public class TodoController {
    private final TodoService service;

    public TodoController(TodoService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Liste aller ToDos abrufen")
    public List<Todo> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Ein bestimmtes ToDo abrufen")
    public ResponseEntity<Todo> getById(@PathVariable UUID id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Ein neues ToDo erstellen")
    public ResponseEntity<Todo> create(@RequestBody Todo todo) {
        return ResponseEntity.ok(service.create(todo));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Ein bestehendes ToDo aktualisieren")
    public ResponseEntity<Todo> update(@PathVariable UUID id, @RequestBody Todo todo) {
        return service.update(id, todo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Ein ToDo löschen")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
