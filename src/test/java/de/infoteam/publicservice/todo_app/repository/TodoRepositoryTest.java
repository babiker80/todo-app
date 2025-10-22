package de.infoteam.publicservice.todo_app.repository;

import de.infoteam.publicservice.todo_app.model.Todo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
class TodoRepositoryTest {
    @Autowired
    private TodoRepository todoRepository;

    @Test
    void shouldSaveAndFindTodo() {
        Todo todo = new Todo();
        todo.setName("RepoTest");
        todoRepository.save(todo);

        List<Todo> todos = todoRepository.findAll();
        assertThat(todos).extracting(Todo::getName).contains("RepoTest");
    }
}