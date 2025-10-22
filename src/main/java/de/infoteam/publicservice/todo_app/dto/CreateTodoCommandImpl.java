package de.infoteam.publicservice.todo_app.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class CreateTodoCommandImpl {
    @Schema(example = "Einkaufen gehen", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Schema(example = "2025-10-30")
    private LocalDate dueDate;

    @Schema(example = "Milch, Brot, Käse")
    private String details;
}
