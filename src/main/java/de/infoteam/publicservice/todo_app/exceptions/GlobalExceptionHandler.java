package de.infoteam.publicservice.todo_app.exceptions;

import de.infoteam.publicservice.todo_app.dto.TodoErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TodoNotFoundException.class)
    public ResponseEntity<TodoErrorResponse> handleNotFound(TodoNotFoundException ex) {
        TodoErrorResponse err = TodoErrorResponse.builder()
                .details("Requested Todo not found")
                .uri("/todo/" + ex.getId())
                .time(OffsetDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(err);
    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<TodoErrorResponse> handleGenericException(Exception ex, HttpServletRequest request) {
        TodoErrorResponse err = TodoErrorResponse.builder()
                .details("An unexpected error occurred")
                .uri(request.getRequestURI())
                .time(OffsetDateTime.now())
                .build();
        return ResponseEntity.internalServerError().body(err);
    }
}
