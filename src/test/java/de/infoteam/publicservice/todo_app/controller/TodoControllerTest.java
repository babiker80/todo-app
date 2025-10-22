package de.infoteam.publicservice.todo_app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.infoteam.publicservice.todo_app.config.WebConfig;
import de.infoteam.publicservice.todo_app.dto.CreateTodoCommandImpl;
import de.infoteam.publicservice.todo_app.dto.PatchTodoCommandImpl;
import de.infoteam.publicservice.todo_app.dto.TodoDto;
import de.infoteam.publicservice.todo_app.dto.UpdateTodoCommandImpl;
import de.infoteam.publicservice.todo_app.service.TodoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TodoController.class)
//@Import(WebConfig.class)
class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean  // ✅ weiterhin erlaubt in Spring Boot 3.5.x
    private TodoService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    TodoControllerTest(TodoService service) {
        this.service = service;
    }

    @Test
    @DisplayName("POST /todo — should create todo and return 201")
    void shouldCreateTodo() throws Exception {
        CreateTodoCommandImpl cmd = new CreateTodoCommandImpl("Test", OffsetDateTime.now().toLocalDate(), "Details");
        TodoDto dto = TodoDto.builder().id(UUID.randomUUID()).name("Test").build();

        BDDMockito.given(service.create(any(CreateTodoCommandImpl.class))).willReturn(dto);

        mockMvc.perform(post("/todo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cmd)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test"));
    }

    @Test
    @DisplayName("GET /todo — should list todos")
    void shouldListTodos() throws Exception {
        TodoDto dto = TodoDto.builder().id(UUID.randomUUID()).name("Test").build();
        BDDMockito.given(service.findAll()).willReturn(List.of(dto));

        mockMvc.perform(get("/todo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test"));
    }

    @Test
    @DisplayName("GET /todo/{id} — should return single todo")
    void shouldGetById() throws Exception {
        UUID id = UUID.randomUUID();
        TodoDto dto = TodoDto.builder().id(id).name("Test").build();
        BDDMockito.given(service.findTodoById(eq(id))).willReturn(dto);

        mockMvc.perform(get("/todo/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()));
    }

    @Test
    @DisplayName("PUT /todo/{id} — should update todo")
    void shouldUpdateTodo() throws Exception {
        UUID id = UUID.randomUUID();
        UpdateTodoCommandImpl cmd = new UpdateTodoCommandImpl("Updated", null, OffsetDateTime.now().toLocalDate(), "Details");
        TodoDto dto = TodoDto.builder().id(id).name("Updated").build();
        BDDMockito.given(service.update(eq(id), any(UpdateTodoCommandImpl.class))).willReturn(dto);

        mockMvc.perform(put("/todo/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cmd)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated"));
    }

    @Test
    @DisplayName("PATCH /todo/{id} — should patch todo")
    void shouldPatchTodo() throws Exception {
        UUID id = UUID.randomUUID();
        PatchTodoCommandImpl cmd = new PatchTodoCommandImpl(null); // z. B. Status geändert
        TodoDto dto = TodoDto.builder().id(id).name("Patched").build();
        BDDMockito.given(service.patch(eq(id), any(PatchTodoCommandImpl.class))).willReturn(dto);

        mockMvc.perform(patch("/todo/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cmd)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Patched"));
    }

    @Test
    @DisplayName("DELETE /todo/{id} — should delete todo")
    void shouldDeleteTodo() throws Exception {
        UUID id = UUID.randomUUID();
        TodoDto dto = TodoDto.builder().id(id).name("Deleted").build();
        BDDMockito.given(service.delete(eq(id))).willReturn(dto);

        mockMvc.perform(delete("/todo/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Deleted"));
    }

    @Test
    void corsShouldAllowRequestsFromAngular() throws Exception {
        mockMvc.perform(options("/todo")
                        .header(HttpHeaders.ORIGIN, "http://localhost:4200")
                        .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "POST"))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:4200"))
                //.andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "GET,POST,PUT,PATCH,DELETE,OPTIONS"))
                .andExpect(header().exists(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS));
                //.andExpect(header().exists(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS));
    }
}