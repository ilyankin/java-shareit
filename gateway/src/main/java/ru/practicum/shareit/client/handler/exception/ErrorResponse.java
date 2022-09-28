package ru.practicum.shareit.client.handler.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
class ErrorResponse {
    private final LocalDateTime timestamp = LocalDateTime.now();
    @JsonIgnore
    private final HttpStatus httpStatus;
    private final int status;
    private final String error;

    private String message = "Unexpected internal error";
    private List<BindingResponseError> bindingErrors;

    public ErrorResponse(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
        this.status = httpStatus.value();
        this.error = httpStatus.getReasonPhrase();
    }

    private void addSubResponseError(BindingResponseError bindingResponseError) {
        if (bindingErrors == null) {
            bindingErrors = new ArrayList<>();
        }
        bindingErrors.add(bindingResponseError);
    }

    private void addValidationError(String object, String field, Object rejectedValue, String message) {
        addSubResponseError(new ValidationResponseError(object, field, rejectedValue, message));
    }

    private void addValidationError(String object, String message) {
        addSubResponseError(new ValidationResponseError(object, message));
    }

    private void addValidationError(FieldError fieldError) {
        this.addValidationError(fieldError.getObjectName(), fieldError.getField(), fieldError.getRejectedValue(),
                fieldError.getDefaultMessage());
    }

    public void addValidationErrors(List<FieldError> fieldErrors) {
        fieldErrors.forEach(this::addValidationError);
    }

    private void addValidationError(ObjectError objectError) {
        addValidationError(
                objectError.getObjectName(),
                objectError.getDefaultMessage());
    }

    public void addValidationError(List<ObjectError> globalErrors) {
        globalErrors.forEach(this::addValidationError);
    }

}

