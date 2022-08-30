package ru.practicum.shareit.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    public <T> ResourceNotFoundException(String sourceName, String fieldName, T valueName) {
        super(String.format("%s with {%s=%s} not found", sourceName, fieldName, valueName));
    }
}
