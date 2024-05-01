package ru.yandex.practicum.catsgram.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@EqualsAndHashCode(of = {"email"})
@Getter
@Setter
public class User {

    Long id;    // уникальный идентификатор пользователя
    String username;    // имя пользователя
    String email;    // электронная почта пользователя
    String password;    // пароль пользователя
    Instant registrationDate;    // дата и время регистрации

}
