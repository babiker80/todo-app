package de.infoteam.publicservice.todo_app.service;

import de.infoteam.publicservice.todo_app.model.Todo;
import de.infoteam.publicservice.todo_app.repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TodoServiceTest {
    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoService todoService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateTodo() {
        Todo todo = new Todo();
        todo.setName("Test");
        todo.setDueDate(LocalDate.now());

        when(todoRepository.save(any(Todo.class))).thenReturn(todo);

        Todo created = todoService.create(todo);

        assertThat(created.getName()).isEqualTo("Test");
        verify(todoRepository, times(1)).save(any(Todo.class));
    }

    @Test
    void shouldReturnAllTodos() {
        when(todoRepository.findAll()).thenReturn(List.of(new Todo(), new Todo()));
        List<Todo> todos = todoService.findAll();
        assertThat(todos).hasSize(2);
    }

    @Test
    void shouldUpdateExistingTodo() {
        UUID id = UUID.randomUUID();
        Todo existing = new Todo();
        existing.setId(id);
        existing.setName("Old");

        when(todoRepository.findById(id)).thenReturn(Optional.of(existing));
        when(todoRepository.save(any(Todo.class))).thenReturn(existing);

        Todo update = new Todo();
        update.setName("New");

        Optional<Todo> result = todoService.update(id, update);

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("New");
    }
}