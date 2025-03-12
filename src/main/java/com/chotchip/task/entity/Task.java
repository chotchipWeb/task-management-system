package com.chotchip.task.entity;

import com.chotchip.task.entity.enums.Priority;
import com.chotchip.task.entity.enums.Status;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String details;
    @Enumerated(EnumType.STRING)
    private Status status;
    @Enumerated(EnumType.STRING)
    private Priority priority;
    @OneToMany(mappedBy = "task", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Comment> comment;
    @ManyToOne
    private User author;
    @ManyToOne
    private User executor;

    public void setComment(List<Comment> comments) {
        this.comment = comments;
        for (Comment comment : comments) {
            comment.setTask(this);
        }
    }

    public void setAuthor(User author) {
        this.author = author;
        author.setTasks(List.of(this));
    }

    public void setExecutor(User executor) {
        this.executor = executor;
        executor.setTasks(List.of(this));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
