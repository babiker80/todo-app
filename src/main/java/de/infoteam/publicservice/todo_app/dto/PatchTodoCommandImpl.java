package de.infoteam.publicservice.todo_app.dto;

import de.infoteam.publicservice.todo_app.model.TodoStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class PatchTodoCommandImpl {
    @Schema(example = "DONE")
    private TodoStatus status;
}
