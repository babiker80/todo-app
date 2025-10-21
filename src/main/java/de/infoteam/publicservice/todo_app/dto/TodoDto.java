package de.infoteam.publicservice.todo_app.dto;

import de.infoteam.publicservice.todo_app.entity.Todo;
import de.infoteam.publicservice.todo_app.model.TodoStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
public class TodoDto {
    private UUID id;
    private String name;
    private OffsetDateTime created;
    private OffsetDateTime lastModified;
    private LocalDate dueDate;
    private String details;
    private TodoStatus status;

    public static TodoDto fromEntity(Todo todo) {
        return TodoDto.builder()
                .id(todo.getId())
                .name(todo.getName())
                .created(todo.getCreated())
                .lastModified(todo.getLastModified())
                .dueDate(todo.getDueDate())
                .details(todo.getDetails())
                .status(todo.getStatus())
                .build();
    }
}
