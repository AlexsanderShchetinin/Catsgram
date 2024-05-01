package ru.yandex.practicum.catsgram.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.DuplicatedDataException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.User;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        // проверяем выполнение необходимых условий
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ConditionsNotMetException("Имейл должен быть указан");
        }
        if (user.getUsername() == null || user.getUsername().isBlank()) {
            throw new ConditionsNotMetException("Имя пользователя должно быть указано");
        }
        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new ConditionsNotMetException("Пароль должен быть указан");
        }
        checkEmail(user.getEmail());
        // формируем дополнительные данные
        user.setId(getNextId());
        user.setRegistrationDate(Instant.now());
        // сохраняем нового пользователя в памяти приложения
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody User newUser) {
        // проверяем необходимые условия
        if (newUser.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        checkEmail(newUser.getEmail());
        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());
            // если пользователь найден и все условия соблюдены, обновляем его содержимое
            if (!(newUser.getEmail() == null || newUser.getEmail().isBlank())) {
                oldUser.setEmail(newUser.getEmail());
            }
            if (!(newUser.getUsername() == null || newUser.getUsername().isBlank())) {
                oldUser.setUsername(newUser.getUsername());
            }
            if (!(newUser.getPassword() == null || newUser.getPassword().isBlank())) {
                oldUser.setPassword(newUser.getPassword());
            }
            return oldUser;
        }
        throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    private void checkEmail(String email) {
        List<String> emails = users.values()
                .stream()
                .map(User::getEmail)
                .filter(checkEmail -> checkEmail.equals(email))
                .toList();
        if (!emails.isEmpty()) {
            throw new DuplicatedDataException("Этот имейл уже используется");
        }
    }


}
