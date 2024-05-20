package ru.yandex.practicum.catsgram.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.exception.ParameterNotValidException;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.service.PostService;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/posts")
public class PostController {

    PostService service;

    public PostController(PostService postService) {
        this.service = postService;
    }

    @GetMapping
    public Collection<Post> findAll(@RequestParam(defaultValue = "10") int size,
                                    @RequestParam(defaultValue = "0") int from,
                                    @RequestParam(defaultValue = "desc") String sort) {
        if (!sort.equals("desc") && !sort.equals("asc") &&
                !sort.equals("ascending") && !sort.equals("descending")) {
            throw new ParameterNotValidException(sort, "Некорректное значение сортировки. Допустимые: desc или asc");
        }
        if (size <= 0) {
            throw new ParameterNotValidException(String.valueOf(size),
                    "Некорректный размер выборки. Размер должен быть больше нуля");
        }
        if (from < 0) {
            throw new ParameterNotValidException(String.valueOf(from),
                    "Некорректное начало выборки. Начало не может быть меньше нуля");
        }
        return service.findAll(size, from, sort);
    }

    @GetMapping("/{postId}")
    public Optional<Post> findById(@PathVariable long postId) {
        return service.findById(postId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Post create(@RequestBody Post post) {
        return service.create(post);
    }

    @PutMapping
    public Post update(@RequestBody Post newPost) {
        return service.update(newPost);
    }

}