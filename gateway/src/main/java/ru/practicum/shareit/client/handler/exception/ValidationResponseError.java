package ru.practicum.shareit.client.handler.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
class ValidationResponseError implements BindingResponseError {
    private String object;
    private String field;
    private Object rejectedValue;
    private String message;

    ValidationResponseError(String object, String message) {
        this.object = object;
        this.message = message;
    }
}
