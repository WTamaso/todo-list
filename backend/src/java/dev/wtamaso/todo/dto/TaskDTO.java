package dev.wtamaso.todo.dto;

import dev.wtamaso.todo.constants.TaskStatus;
import dev.wtamaso.todo.entities.Task;

import java.io.Serializable;
import java.util.Date;

public class TaskDTO implements Serializable {
    private Long id;
    private TaskStatus status;
    private String title;
    private String description;
    private Date createdAt;
    private Date updatedAt;
    private Date completedAt;
    private Date deletedAt;

    public static TaskDTO getDto(Task entity) {
        TaskDTO dto = new TaskDTO();

        dto.setId(entity.getId());
        dto.setStatus(entity.getStatus());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        dto.setCompletedAt(entity.getCompletedAt());
        dto.setDeletedAt(entity.getDeletedAt());

        return dto;
    }

    public static void updateEntity(TaskDTO dto, Task entity) {
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setUpdatedAt(new Date());

        if(entity.getStatus() != dto.getStatus()) {
            entity.setStatus(dto.getStatus());
            switch (entity.getStatus()) {
                case LISTED:
                    entity.setCompletedAt(null);
                    entity.setDeletedAt(null);
                    break;
                case COMPLETED:
                    entity.setCompletedAt(new Date());
                    break;
            }
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Date completedAt) {
        this.completedAt = completedAt;
    }

    public Date getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }
}
