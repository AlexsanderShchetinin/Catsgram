package ru.yandex.practicum.catsgram.exception;

public class IncorrectURLException extends RuntimeException {
    public IncorrectURLException(String message) {
        super(message);
    }
}
