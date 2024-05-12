package ru.yandex.practicum.catsgram.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
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