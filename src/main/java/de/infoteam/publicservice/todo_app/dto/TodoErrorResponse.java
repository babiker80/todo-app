package de.infoteam.publicservice.todo_app.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
public class TodoErrorResponse {

    @Schema(example = "Requested Todo not found")
    private String details;

    @Schema(example = "/todo/046b6c7f-0b8a-43b9-b35d-6489e6daee91")
    private String uri;

    @Schema(example = "2025-10-21T12:00:00Z")
    private OffsetDateTime time;

    public static TodoErrorResponse notFound(UUID id) {
        return TodoErrorResponse.builder()
                .details("Requested Todo not found")
                .uri("/todo/" + id)
                .time(OffsetDateTime.now())
                .build();
    }
}
