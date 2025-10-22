package de.infoteam.publicservice.todo_app.service;

import de.infoteam.publicservice.todo_app.dto.CreateTodoCommandImpl;
import de.infoteam.publicservice.todo_app.dto.PatchTodoCommandImpl;
import de.infoteam.publicservice.todo_app.dto.TodoDto;
import de.infoteam.publicservice.todo_app.dto.UpdateTodoCommandImpl;
import de.infoteam.publicservice.todo_app.exceptions.TodoNotFoundException;
import de.infoteam.publicservice.todo_app.exceptions.TodoUpdateException;
import de.infoteam.publicservice.todo_app.model.Todo;
import de.infoteam.publicservice.todo_app.repository.TodoMapper;
import de.infoteam.publicservice.todo_app.repository.TodoRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TodoService {

    private final TodoRepository repository;
    private final TodoMapper mapper;

    public TodoDto create(CreateTodoCommandImpl command) {
        Todo todo = mapper.createEntity( command);
        Todo saved = null;
        try {
            saved = repository.save(todo);
        } catch (IllegalArgumentException es) {
            throw new TodoUpdateException("IllegalArgument while saving todo");
        }
        catch (OptimisticLockingFailureException ex) {
            throw new TodoUpdateException("OptimisticLockingFailure while saving todo");
        }
        catch (Exception  ex) {
            throw new TodoUpdateException("Error while saving todo");
        }

        return mapper.toDto(saved);
    }

    public List<TodoDto> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public TodoDto findTodoById(UUID id) {
        return mapper.toDto(repository.findById(id).orElseThrow(() -> new TodoNotFoundException(id)));
    }

    public TodoDto update(UUID todoId, UpdateTodoCommandImpl command) {
        Todo todo = repository.findById(todoId).orElseThrow(() -> new TodoNotFoundException(todoId));
        mapper.updateEntity(todo, command);

        Todo saved = null;
        try {
            saved = repository.save(todo);
        } catch (IllegalArgumentException es) {
            throw new TodoUpdateException("IllegalArgument while updating todo with id: ",todoId);
        }
        catch (OptimisticLockingFailureException ex) {
            throw new TodoUpdateException("OptimisticLockingFailure while updating todo with id: ",todoId);
        }
        catch (Exception  ex) {
            throw new TodoUpdateException("Error while updating todo with id: ",todoId);
        }

        return mapper.toDto(saved);
    }

    public TodoDto patch(UUID todoId, PatchTodoCommandImpl command) {
        Todo todo = repository.findById(todoId).orElseThrow(() -> new TodoNotFoundException(todoId));
        if (command.getStatus() == null) {
            throw new TodoUpdateException("Status is null", todoId);
        }

        todo.setStatus(command.getStatus());
        todo.setLastModified(OffsetDateTime.now());

        return mapper.toDto(repository.save(todo));
    }

    public TodoDto delete(UUID todoId) {
        Todo todo = repository.findById(todoId).orElseThrow(() -> new TodoNotFoundException(todoId));
        try {
            repository.deleteById(todoId);
        } catch (IllegalArgumentException es) {
            throw new TodoUpdateException("IllegalArgument while deleting todo with id: ",todoId);
        }
        catch (OptimisticLockingFailureException ex) {
            throw new TodoUpdateException("OptimisticLockingFailure while deleting todo with id: ",todoId);
        }
        catch (Exception  ex) {
            throw new TodoUpdateException("Error while deleting todo with id: ",todoId);
        }
        return mapper.toDto(todo);
    }

    /**
     * Zentrale Fehlerbehandlung für Save-Operationen (Create, Update, Patch)
     */
    private TodoDto executeSave(Todo todo, String actionDescription) {
        try {
            Todo saved = repository.save(todo);
            return mapper.toDto(saved);
        } catch (IllegalArgumentException e) {
            throw new TodoUpdateException("IllegalArgument while " + actionDescription);
        } catch (OptimisticLockingFailureException e) {
            throw new TodoUpdateException("OptimisticLockingFailure while " + actionDescription);
        } catch (Exception e) {
            throw new TodoUpdateException("Error while " + actionDescription);
        }
    }
}
