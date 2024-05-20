package ru.yandex.practicum.catsgram.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.DuplicatedDataException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.exception.ParameterNotValidException;

import java.util.Map;

@RestControllerAdvice
public class ErrorHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    public Map<String, String> notFound(NotFoundException e){
        return Map.of("error: ", e.getMessage());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler
    public Map<String, String> duplicated(DuplicatedDataException e){
        return Map.of("error: ", e.getMessage());
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler
    public Map<String, String> notCondition(ConditionsNotMetException e){
        return Map.of("error: ", e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public Map<String, String> notValidateParameters(ParameterNotValidException e){
        return Map.of("error: ", "Некорректное значение параметра " + e.getParameter() + ": " + e.getReason());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public Map<String, String> anotherExceptions(Throwable e){
        return Map.of("error: ", "Произошла непредвиденная ошибка");
    }

}
