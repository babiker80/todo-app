package de.infoteam.publicservice.todo_app.dto;

import de.infoteam.publicservice.todo_app.model.TodoStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class UpdateTodoCommandImpl {
    @Schema(example = "Wäsche waschen")
    private String name;

    @Schema(example = "DONE")
    private TodoStatus status;

    @Schema(example = "2025-10-22")
    private LocalDate dueDate;

    @Schema(example = "Nur helle Wäsche")
    private String details;
}
