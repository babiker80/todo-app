package de.infoteam.publicservice.todo_app.exceptions;

import java.util.UUID;

public class TodoUpdateException extends RuntimeException {
    private final UUID id;

    public TodoUpdateException(String message
    ) {
        super(message);
        this.id = null;
    }

    public TodoUpdateException(String message, UUID id) {
        super(message + id);
        this.id = id;
    }

    public UUID getId() {
        return id;
    }
}
