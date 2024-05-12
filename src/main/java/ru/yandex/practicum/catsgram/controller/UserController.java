package ru.yandex.practicum.catsgram.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.model.User;
import ru.yandex.practicum.catsgram.service.UserService;

import java.util.Collection;

@RestController
@RequestMapping("/users")
public class UserController {

    UserService service;

    public UserController(UserService userService) {
        this.service = userService;
    }

    @GetMapping
    public Collection<User> findAll() {
        return service.findAll();
    }

    @GetMapping("/{userId}")
    public User findById(@PathVariable int userId) {
        return service.findById(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody User user) {
        return service.create(user);
    }

    @PutMapping
    public User update(@RequestBody User newUser) {
        return service.update(newUser);
    }

}
