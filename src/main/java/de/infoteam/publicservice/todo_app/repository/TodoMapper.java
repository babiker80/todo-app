package de.infoteam.publicservice.todo_app.repository;

import de.infoteam.publicservice.todo_app.dto.CreateTodoCommandImpl;
import de.infoteam.publicservice.todo_app.dto.TodoDto;
import de.infoteam.publicservice.todo_app.dto.UpdateTodoCommandImpl;
import de.infoteam.publicservice.todo_app.model.Todo;
import de.infoteam.publicservice.todo_app.model.TodoStatus;

import java.time.OffsetDateTime;
import java.util.Optional;

public class TodoMapper {

    public TodoDto toDto(Todo entity) {
        if (entity == null) return null;

        return TodoDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .created(entity.getCreated())
                .lastModified(entity.getLastModified())
                .dueDate(entity.getDueDate())
                .details(entity.getDetails())
                .status(entity.getStatus())
                .build();
    }

    public Todo toEntity(TodoDto dto) {
        if (dto == null) return null;
        return Todo.builder()
                .id(dto.getId())
                .name(dto.getName())
                .created(dto.getCreated())
                .lastModified(dto.getLastModified())
                .dueDate(dto.getDueDate())
                .details(dto.getDetails())
                .status(dto.getStatus())
                .build();
    }

    public Todo updateEntity(Todo todo, UpdateTodoCommandImpl command) {
        if (todo == null || command == null) return todo;

        Optional.ofNullable(command.getName()).ifPresent(todo::setName);
        Optional.ofNullable(command.getDetails()).ifPresent(todo::setDetails);
        Optional.ofNullable(command.getDueDate()).ifPresent(todo::setDueDate);
        Optional.ofNullable(command.getStatus()).ifPresent(todo::setStatus);

        todo.setLastModified(OffsetDateTime.now());
        return todo;
    }

    public Todo createEntity(CreateTodoCommandImpl command) {
        if (command == null) return null;

        OffsetDateTime now = OffsetDateTime.now();
        return Todo.builder()
                .name(command.getName())
                .dueDate(command.getDueDate())
                .details(command.getDetails())
                .status(TodoStatus.OPEN)
                .created(now)
                .lastModified(now)
                .build();
    }
}
