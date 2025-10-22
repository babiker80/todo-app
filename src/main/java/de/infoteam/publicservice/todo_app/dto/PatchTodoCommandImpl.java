package de.infoteam.publicservice.todo_app.dto;

import de.infoteam.publicservice.todo_app.model.TodoStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class PatchTodoCommandImpl {
    @Schema(example = "DONE")
    private TodoStatus status;
}
