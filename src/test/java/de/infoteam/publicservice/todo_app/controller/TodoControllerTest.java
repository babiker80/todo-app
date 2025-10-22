package de.infoteam.publicservice.todo_app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.infoteam.publicservice.todo_app.dto.CreateTodoCommandImpl;
import de.infoteam.publicservice.todo_app.dto.TodoDto;
import de.infoteam.publicservice.todo_app.model.Todo;
import de.infoteam.publicservice.todo_app.model.TodoStatus;
import de.infoteam.publicservice.todo_app.service.TodoService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TodoController.class)
class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

//    @MockBean
//    private TodoService todoService;
    //@ReplaceBean
    private TodoService todoService = Mockito.mock(TodoService.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnListOfTodos() throws Exception {
        TodoDto todo = TodoDto.builder()
                .id(UUID.randomUUID())
                .name("Test")
                .status(TodoStatus.OPEN)
                .created(OffsetDateTime.now())
                .build();

        Mockito.when(todoService.findAll()).thenReturn(List.of(todo));

        mockMvc.perform(get("/todo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test"));
    }

    @Test
    void shouldCreateTodo() throws Exception {
        TodoDto todo = TodoDto.builder()
                .id(UUID.randomUUID())
                .name("New Todo")
                .status(TodoStatus.OPEN)
                .created(OffsetDateTime.now())
                .dueDate(LocalDate.now())
                .build();

        Mockito.when(todoService.create(any(CreateTodoCommandImpl.class))).thenReturn(todo);

        mockMvc.perform(post("/todo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(todo)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("New Todo"));
    }

    @Test
    void shouldReturn404WhenTodoNotFound() throws Exception {
        UUID id = UUID.randomUUID();
        Mockito.when(todoService.findTodoById(eq(id))).thenReturn(Optional.empty());

        mockMvc.perform(get("/todo/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldUpdateTodo() throws Exception {
        UUID id = UUID.randomUUID();
        Todo updated = new Todo();
        updated.setId(id);
        updated.setName("Updated Name");

        Mockito.when(todoService.update(eq(id), any(Todo.class)))
                .thenReturn(Optional.of(updated));

        mockMvc.perform(put("/todo/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Name"));
    }

    @Test
    void shouldDeleteTodo() throws Exception {
        UUID id = UUID.randomUUID();
        Todo deleted = new Todo();
        deleted.setId(id);
        deleted.setName("ToDelete");

        Mockito.when(todoService.delete(eq(id))).thenReturn(Optional.of(deleted));

        mockMvc.perform(delete("/todo/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("ToDelete"));
    }
}