package de.infoteam.publicservice.todo_app.service;

import de.infoteam.publicservice.todo_app.dto.CreateTodoCommandImpl;
import de.infoteam.publicservice.todo_app.dto.PatchTodoCommandImpl;
import de.infoteam.publicservice.todo_app.dto.TodoDto;
import de.infoteam.publicservice.todo_app.dto.UpdateTodoCommandImpl;
import de.infoteam.publicservice.todo_app.exceptions.TodoNotFoundException;
import de.infoteam.publicservice.todo_app.exceptions.TodoUpdateException;
import de.infoteam.publicservice.todo_app.model.Todo;
import de.infoteam.publicservice.todo_app.model.TodoStatus;
import de.infoteam.publicservice.todo_app.repository.TodoMapper;
import de.infoteam.publicservice.todo_app.repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.OptimisticLockingFailureException;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.*;

class TodoServiceTest {

    @Mock
    private TodoRepository repository;

    @Mock
    private TodoMapper mapper;

    @InjectMocks
    private TodoService service;

    private UUID todoId;
    private Todo todo;
    private TodoDto todoDto;

    @BeforeEach
    void setup() {
        todoId = UUID.randomUUID();
        todo = Todo.builder()
                .id(todoId)
                .name("Test Todo")
                .status(TodoStatus.OPEN)
                .created(OffsetDateTime.now())
                .lastModified(OffsetDateTime.now())
                .build();

        todoDto = TodoDto.builder()
                .id(todoId)
                .name("Test Todo")
                .status(TodoStatus.OPEN)
                .build();
    }

    // === CREATE ===
    @Test
    void shouldCreateTodoSuccessfully() {
        CreateTodoCommandImpl command = CreateTodoCommandImpl.builder()
                .name("New Todo")
                .dueDate(null)
                .details(null)
                .build();

        given(mapper.createEntity(command)).willReturn(todo);
        given(repository.save(todo)).willReturn(todo);
        given(mapper.toDto(todo)).willReturn(todoDto);

        TodoDto result = service.create(command);

        assertThat(result).isEqualTo(todoDto);
        verify(repository).save(todo);
    }

    @Test
    void shouldThrowUpdateExceptionWhenSaveFails() {
        CreateTodoCommandImpl command = CreateTodoCommandImpl.builder()
                .name("Fail Todo")
                .dueDate(null)
                .details(null)
                .build();
        given(mapper.createEntity(command)).willReturn(todo);
        given(repository.save(todo)).willThrow(new OptimisticLockingFailureException("DB error"));

        assertThatThrownBy(() -> service.create(command))
                .isInstanceOf(TodoUpdateException.class)
                .hasMessageContaining("while creating todo");
    }

    // === FIND ===
    @Test
    void shouldFindAllTodos() {
        given(repository.findAll()).willReturn(List.of(todo));
        given(mapper.toDto(todo)).willReturn(todoDto);

        List<TodoDto> result = service.findAll();

        assertThat(result).hasSize(1).contains(todoDto);
    }

    @Test
    void shouldFindTodoById() {
        given(repository.findById(todoId)).willReturn(Optional.of(todo));
        given(mapper.toDto(todo)).willReturn(todoDto);

        TodoDto result = service.findTodoById(todoId);

        assertThat(result).isEqualTo(todoDto);
    }

    @Test
    void shouldThrowNotFoundWhenTodoDoesNotExist() {
        given(repository.findById(todoId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> service.findTodoById(todoId))
                .isInstanceOf(TodoNotFoundException.class);
    }

    // === UPDATE ===
    @Test
    void shouldUpdateTodoSuccessfully() {
        UpdateTodoCommandImpl command = new UpdateTodoCommandImpl("Updated", TodoStatus.DONE, null, null);
        given(repository.findById(todoId)).willReturn(Optional.of(todo));
        willDoNothing().given(mapper).updateEntity(todo, command);
        given(repository.save(todo)).willReturn(todo);
        given(mapper.toDto(todo)).willReturn(todoDto);

        TodoDto result = service.update(todoId, command);

        assertThat(result).isEqualTo(todoDto);
        verify(repository).save(todo);
    }

    // === PATCH ===
    @Test
    void shouldPatchTodoStatusSuccessfully() {
        PatchTodoCommandImpl command = new PatchTodoCommandImpl(TodoStatus.DONE);
        given(repository.findById(todoId)).willReturn(Optional.of(todo));
        given(repository.save(todo)).willReturn(todo);
        given(mapper.toDto(todo)).willReturn(todoDto);

        TodoDto result = service.patch(todoId, command);

        assertThat(result).isEqualTo(todoDto);
        assertThat(todo.getStatus()).isEqualTo(TodoStatus.DONE);
    }

    @Test
    void shouldThrowUpdateExceptionWhenStatusIsNull() {
        PatchTodoCommandImpl command = PatchTodoCommandImpl.builder()
                .status(null)
                .build();
        given(repository.findById(todoId)).willReturn(Optional.of(todo));

        assertThatThrownBy(() -> service.patch(todoId, command))
                .isInstanceOf(TodoUpdateException.class)
                .hasMessageContaining("Status is null");
    }

    // === DELETE ===
    @Test
    void shouldDeleteTodoSuccessfully() {
        given(repository.findById(todoId)).willReturn(Optional.of(todo));
        willDoNothing().given(repository).deleteById(todoId);
        given(mapper.toDto(todo)).willReturn(todoDto);

        TodoDto result = service.delete(todoId);

        assertThat(result).isEqualTo(todoDto);
        verify(repository).deleteById(todoId);
    }

    @Test
    void shouldThrowUpdateExceptionOnDeleteError() {
        given(repository.findById(todoId)).willReturn(Optional.of(todo));
        willThrow(new IllegalArgumentException("bad")).given(repository).deleteById(todoId);

        assertThatThrownBy(() -> service.delete(todoId))
                .isInstanceOf(TodoUpdateException.class)
                .hasMessageContaining("deleting todo");
    }
}