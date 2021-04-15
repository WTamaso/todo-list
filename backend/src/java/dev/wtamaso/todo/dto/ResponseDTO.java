package dev.wtamaso.todo.dto;

import java.io.Serializable;

public class ResponseDTO implements Serializable {
    private int code;
    private Long id;
    private String message;
    private String reason;
    private Integer amount;
    private Object response;

    public ResponseDTO(int code, String message, String reason) {
        this.code = code;
        this.message = message;
        this.reason = reason;
    }

    public ResponseDTO(int code, Object response, Integer amount) {
        this.code = code;
        this.response = response;
        this.amount = amount;
    }

    public ResponseDTO(int code, Long id, String message) {
        this.code = code;
        this.id = id;
        this.message = message;
    }
}
