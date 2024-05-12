package ru.yandex.practicum.catsgram.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.DuplicatedDataException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.User;

import java.time.Instant;
import java.util.*;

@Service
public class UserService {
    private final Map<Long, User> userRepository = new HashMap<>();

    public Collection<User> findAll() {
        return userRepository.values();
    }

    public User create(User user) {
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
        userRepository.put(user.getId(), user);
        return user;
    }

    public User update(User newUser) {
        // проверяем необходимые условия
        if (newUser.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        checkEmail(newUser.getEmail());
        if (userRepository.containsKey(newUser.getId())) {
            User oldUser = userRepository.get(newUser.getId());
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
        long currentMaxId = userRepository.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    private void checkEmail(String email) {
        List<String> emails = userRepository.values()
                .stream()
                .map(User::getEmail)
                .filter(checkEmail -> checkEmail.equals(email))
                .toList();
        if (!emails.isEmpty()) {
            throw new DuplicatedDataException("Этот имейл уже используется");
        }
    }

    public User findById(long userId) {
        Optional<User> userOpt = userRepository.values().stream()
                .filter(user -> user.getId() == userId)
                .findFirst();
        if (userOpt.isEmpty()) {
            throw new ConditionsNotMetException("Пользователь с id=" + userId + " не существует");
        }
        return userOpt.get();
    }

}

