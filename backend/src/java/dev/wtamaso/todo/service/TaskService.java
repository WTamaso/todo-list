package dev.wtamaso.todo.service;

import dev.wtamaso.todo.constants.TaskStatus;
import dev.wtamaso.todo.dto.TaskDTO;
import dev.wtamaso.todo.entities.Task;
import dev.wtamaso.todo.util.SessionFactoryUtil;
import org.hibernate.Session;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.util.*;

public class TaskService {
    public static Long create(TaskDTO dto) {
        try {
            Task task = new Task();
            TaskDTO.updateEntity(dto, task);
            task.setCreatedAt(new Date());

            Session s = SessionFactoryUtil.session();

            s.beginTransaction();
            s.save(task);
            s.getTransaction().commit();

            return task.getId();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create Task", e);
        }
    }

    public static Task get(Long id) {
        try {
            Session s = SessionFactoryUtil.session();

            CriteriaBuilder criteriaBuilder = s.getCriteriaBuilder();
            CriteriaQuery<Task> criteriaQuery = criteriaBuilder.createQuery(Task.class);
            Root<Task> root = criteriaQuery.from(Task.class);
            criteriaQuery.select(root);
            criteriaQuery.where(criteriaBuilder.equal(root.get("id"), id));
            Query query = s.createQuery(criteriaQuery).setMaxResults(1);

            return (Task) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get Task", e);
        }
    }

    public static List<Task> getList(TaskStatus status, String filter, Integer limit, Integer offset){
        try {
            Session s = SessionFactoryUtil.session();

            CriteriaBuilder criteriaBuilder = s.getCriteriaBuilder();
            CriteriaQuery<Task> criteriaQuery = criteriaBuilder.createQuery(Task.class);
            Root<Task> root = criteriaQuery.from(Task.class);
            criteriaQuery.select(root);

            List<Predicate> clauses = new ArrayList<>();

            if (status != null) {
                clauses.add(criteriaBuilder.equal(root.get("status"), status));
            }
            if (filter != null) {
                clauses.add(criteriaBuilder.or(
                        criteriaBuilder.like(root.get("title"), "%" + filter + "%"),
                        criteriaBuilder.like(root.get("description"), "%" + filter + "%")
                ));
            }


            if (clauses.size() > 0) {
                Predicate[] predArray = new Predicate[clauses.size()];
                clauses.toArray(predArray);
                criteriaQuery.where(predArray);
            }

            limit = limit != null ? limit : 20;
            offset = offset != null ? offset : 0;

            Query query = s.createQuery(criteriaQuery).setMaxResults(limit).setFirstResult(offset);

            return query.getResultList();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get Task list", e);
        }
    }

    public static Long update(Task task, TaskDTO dto) {
        try {
            Session s = SessionFactoryUtil.session();

            s.beginTransaction();
            TaskDTO.updateEntity(dto, task);
            s.update(task);
            s.getTransaction().commit();

            return task.getId();
        } catch (Exception e) {
            throw new RuntimeException("Failed to update Task", e);
        }
    }

    public static void delete(Task task) {
        try {
            Session s = SessionFactoryUtil.session();

            s.beginTransaction();
            task.setStatus(TaskStatus.DELETED);
            task.setDeletedAt(new Date());
            s.update(task);
            s.getTransaction().commit();

            return;
        } catch (Exception e) {
            throw new RuntimeException("Failed do delete Task", e);
        }
    }

    public static boolean validate(TaskDTO dto) {
        return dto.getTitle() != null && !dto.getTitle().trim().isEmpty() &&
                dto.getStatus() != null;
    }
}
