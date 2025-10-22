package de.infoteam.publicservice.todo_app.repository;

import de.infoteam.publicservice.todo_app.dto.CreateTodoCommandImpl;
import de.infoteam.publicservice.todo_app.dto.TodoDto;
import de.infoteam.publicservice.todo_app.dto.UpdateTodoCommandImpl;
import de.infoteam.publicservice.todo_app.model.Todo;
import de.infoteam.publicservice.todo_app.model.TodoStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class TodoMapperTest {
    private TodoMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new TodoMapper();
    }

    @Test
    @DisplayName("toDto() should correctly map entity to DTO")
    void shouldMapEntityToDto() {
        // given
        UUID id = UUID.randomUUID();
        OffsetDateTime now = OffsetDateTime.now();
        Todo entity = Todo.builder()
                .id(id)
                .name("Test Todo")
                .details("Details")
                .created(now.minusDays(1))
                .lastModified(now)
                .dueDate(now.plusDays(2).toLocalDate())
                .status(TodoStatus.DONE)
                .build();

        // when
        TodoDto dto = mapper.toDto(entity);

        // then
        assertThat(dto)
                .usingRecursiveComparison()
                .isEqualTo(entity);
    }

    @Test
    @DisplayName("toEntity() should correctly map DTO to entity")
    void shouldMapDtoToEntity() {
        // given
        UUID id = UUID.randomUUID();
        OffsetDateTime now = OffsetDateTime.now();
        TodoDto dto = TodoDto.builder()
                .id(id)
                .name("Todo DTO")
                .details("Details from DTO")
                .created(now.minusDays(1))
                .lastModified(now)
                .dueDate(now.plusDays(3).toLocalDate())
                .status(TodoStatus.OPEN)
                .build();

        // when
        Todo entity = mapper.toEntity(dto);

        // then
        assertThat(entity)
                .usingRecursiveComparison()
                .isEqualTo(dto);
    }

    @Test
    @DisplayName("updateEntity() should update fields and refresh lastModified timestamp")
    void shouldUpdateEntityFromCommand() {
        // given
        Todo todo = Todo.builder()
                .id(UUID.randomUUID())
                .name("Old Name")
                .details("Old details")
                .dueDate(OffsetDateTime.now().plusDays(1).toLocalDate())
                .status(TodoStatus.OPEN)
                .lastModified(OffsetDateTime.now().minusDays(1))
                .build();

        UpdateTodoCommandImpl cmd = new UpdateTodoCommandImpl(
                "New Name",
                TodoStatus.DONE,
                OffsetDateTime.now().plusDays(5).toLocalDate(),
                "Updated details"
        );

        OffsetDateTime before = OffsetDateTime.now();

        // when
        Todo updated = mapper.updateEntity(todo, cmd);

        // then
        assertThat(updated.getName()).isEqualTo("New Name");
        assertThat(updated.getDetails()).isEqualTo("Updated details");
        assertThat(updated.getDueDate()).isEqualTo(cmd.getDueDate());
        assertThat(updated.getStatus()).isEqualTo(TodoStatus.DONE);
        assertThat(updated.getLastModified()).isAfterOrEqualTo(before);
    }

    @Test
    @DisplayName("createEntity() should create a new entity with OPEN status and timestamps set")
    void shouldCreateEntityFromCommand() {
        // given
        CreateTodoCommandImpl cmd = new CreateTodoCommandImpl(
                "New Todo",
                OffsetDateTime.now().plusDays(2).toLocalDate(),
                "Command details"
        );

        // when
        Todo entity = mapper.createEntity(cmd);

        // then
        assertThat(entity.getName()).isEqualTo("New Todo");
        assertThat(entity.getDetails()).isEqualTo("Command details");
        assertThat(entity.getDueDate()).isEqualTo(cmd.getDueDate());
        assertThat(entity.getStatus()).isEqualTo(TodoStatus.OPEN);
        assertThat(entity.getCreated()).isNotNull();
        assertThat(entity.getLastModified()).isNotNull();
        assertThat(entity.getLastModified()).isAfterOrEqualTo(entity.getCreated());
    }
}