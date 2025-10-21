package de.infoteam.publicservice.todo_app.entity;

import de.infoteam.publicservice.todo_app.model.TodoStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Todo {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private OffsetDateTime created;

    @Column(nullable = false)
    private OffsetDateTime lastModified;

    private LocalDate dueDate;

    @Lob
    private String details;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TodoStatus status;
}
