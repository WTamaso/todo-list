package dev.wtamaso.todo.api;

import com.google.gson.Gson;
import dev.wtamaso.todo.constants.TaskStatus;
import dev.wtamaso.todo.dto.ResponseDTO;
import dev.wtamaso.todo.dto.TaskDTO;
import dev.wtamaso.todo.entities.Task;
import dev.wtamaso.todo.service.TaskService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "Task API", urlPatterns = {"/api/tasks/", "/api/tasks/*"})
public class TaskApi extends HttpServlet {

    private void successResponse(Long entityId, String message, Object response, Integer amount, int code, HttpServletResponse res) throws IOException {
        res.setStatus(code);
        res.setContentType("application/JSON");
        res.setCharacterEncoding("UTF-8");
        res.setHeader("Access-Control-Allow-Origin", "*");

        PrintWriter out = res.getWriter();

        ResponseDTO dto;
        if (entityId != null && message != null) {
            dto = new ResponseDTO(code, entityId, message);
        } else if (response != null) {
            dto = new ResponseDTO(code, response, amount);
        } else {
            dto = new ResponseDTO(HttpServletResponse.SC_NO_CONTENT, "Empty response", "There is no result for your request.");
        }

        Gson gson = new Gson();

        out.print(gson.toJson(dto));
        out.close();
    }

    private void errorResponse(String message, String reason, int code, HttpServletResponse res) throws IOException {
        res.setStatus(code);
        res.setContentType("application/JSON");
        res.setCharacterEncoding("UTF-8");
        res.setHeader("Access-Control-Allow-Origin", "*");

        PrintWriter out = res.getWriter();

        ResponseDTO dto = new ResponseDTO(code, message, reason);
        Gson gson = new Gson();

        out.print(gson.toJson(dto));
        out.close();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            req.setCharacterEncoding("UTF-8");
            if (req.getPathInfo() != null) {
                try {
                    Long id = Long.parseLong(req.getPathInfo().replaceFirst("/", ""));

                    Task result = TaskService.get(id);
                    if (result == null) {
                        errorResponse("Can't find task", "Task not found with the given parameters.", HttpServletResponse.SC_NOT_FOUND, res);
                    }

                    successResponse(null, null, TaskDTO.getDto(result), null, HttpServletResponse.SC_OK, res);
                } catch (Exception ex) {
                    errorResponse("Can't find Task", "Invalid parameters.", HttpServletResponse.SC_BAD_REQUEST, res);
                    return;
                }
            } else {
                try {
                    TaskStatus status = TaskStatus.fromValue(req.getParameter("status"));
                    Integer limit = req.getParameter("limit") != null ? Integer.parseInt(req.getParameter("limit")) : null;
                    Integer offset = req.getParameter("offset") != null ? Integer.parseInt(req.getParameter("offset")) : null;

                    List<Task> resultList = TaskService.getList(status, req.getParameter("filter"), limit, offset);
                    List<TaskDTO> dtoList = new ArrayList<>();

                    resultList.forEach(task -> dtoList.add(TaskDTO.getDto(task)));

                    successResponse(null, null, dtoList, dtoList.size(), HttpServletResponse.SC_OK, res);
                } catch (Exception ex) {
                    errorResponse("Can't find Task", "Invalid parameters.", HttpServletResponse.SC_BAD_REQUEST, res);
                    return;
                }
            }
        } catch (Exception e) {
            errorResponse("Failed to process request", e.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR, res);
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            req.setCharacterEncoding("UTF-8");
            if (req.getPathInfo() != null) {
                errorResponse("Not allowed", "Invalid parameter.", HttpServletResponse.SC_METHOD_NOT_ALLOWED, res);
                return;
            }

            Gson gson = new Gson();
            TaskDTO dto = gson.fromJson(req.getReader(), TaskDTO.class);

            if (dto == null) {
                errorResponse("Can't create Task", "No data submitted.", HttpServletResponse.SC_BAD_REQUEST, res);
                return;
            }
            if (!TaskService.validate(dto)) {
                errorResponse("Validation error", "Required fields not informed.", HttpServletResponse.SC_BAD_REQUEST, res);
                return;
            }

            Long newId = TaskService.create(dto);

            successResponse(newId, "Task created", null, null, HttpServletResponse.SC_CREATED, res);
        } catch (Exception e) {
            errorResponse("Failed to process request", e.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR, res);
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            req.setCharacterEncoding("UTF-8");
            if (req.getPathInfo() == null) {
                errorResponse("Not allowed", "Invalid parameter.", HttpServletResponse.SC_METHOD_NOT_ALLOWED, res);
                return;
            }
            Task task;

            try {
                Long id = Long.parseLong(req.getPathInfo().replaceFirst("/", ""));

                task = TaskService.get(id);
                if (task == null) {
                    errorResponse("Can't find Task", "Task not found with the given parameter.", HttpServletResponse.SC_NOT_FOUND, res);
                }
            } catch (Exception ex) {
                errorResponse("Can't find Task", "Invalid parameter.", HttpServletResponse.SC_BAD_REQUEST, res);
                return;
            }

            Gson gson = new Gson();
            TaskDTO dto = gson.fromJson(req.getReader(), TaskDTO.class);

            if (dto == null) {
                errorResponse("Can't update Task", "No data submitted.", HttpServletResponse.SC_BAD_REQUEST, res);
                return;
            }
            if (!TaskService.validate(dto)) {
                errorResponse("Validation error", "Required fields not informed.", HttpServletResponse.SC_BAD_REQUEST, res);
                return;
            }

            Long id = TaskService.update(task, dto);

            successResponse(id, "Task updated", null, null, HttpServletResponse.SC_OK, res);
        } catch (Exception e) {
            errorResponse("Failed to process request", e.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR, res);
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            req.setCharacterEncoding("UTF-8");
            if (req.getPathInfo() == null) {
                errorResponse("Not allowed", "Task not found with the given parameter.", HttpServletResponse.SC_NOT_FOUND, res);
                return;
            }

            Long id;
            Task task;
            try {
                id = Long.parseLong(req.getPathInfo().replaceFirst("/", ""));

                task = TaskService.get(id);
                if (task == null) {
                    errorResponse("Can't find Task", "Task not found with the given parameter.", HttpServletResponse.SC_NOT_FOUND, res);
                }
            } catch (Exception ex) {
                errorResponse("Can't find Task", "Invalid parameter.", HttpServletResponse.SC_BAD_REQUEST, res);
                return;
            }

            TaskService.delete(task);

            successResponse(id, "Task Deleted", null, null, HttpServletResponse.SC_OK, res);
        } catch (Exception e) {
            errorResponse("Failed to process request", e.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR, res);
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setHeader("Access-Control-Allow-Origin", "*");
        res.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
    }
}
