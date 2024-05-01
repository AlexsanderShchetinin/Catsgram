package ru.yandex.practicum.catsgram.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@EqualsAndHashCode(of = {"id"})
@Getter
@Setter
public class Post {

    Long id;    // уникальный идентификатор сообщения,
    long authorId;    // пользователь, который создал сообщение,
    String description;    // текстовое описание сообщения,
    Instant postDate;    // дата и время создания сообщения.
}
