package de.infoteam.publicservice.todo_app.exceptions;

import java.util.UUID;

public class TodoNotFoundException extends RuntimeException {
    private final UUID id;

    public TodoNotFoundException(UUID id) {
        super("Todo not found: " + id);
        this.id = id;
    }
    public TodoNotFoundException(String message, UUID id) {
        super(message +": " + id);
        this.id = id;
    }

    public UUID getId() {
        return id;
    }
}
