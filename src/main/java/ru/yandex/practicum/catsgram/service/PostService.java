package ru.yandex.practicum.catsgram.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.IncorrectURLException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.Post;

import java.time.Instant;
import java.util.*;

// Указываем, что класс PostService - является бином и его
// нужно добавить в контекст приложения
@Service
public class PostService {
    private final Map<Long, Post> postRepository = new HashMap<>();
    UserService userService;

    public PostService(UserService userService) {
        this.userService = userService;
    }

    /**
     * метод должен возвращаться список всех постов, начиная с заданного порядкового номера
     * и в заданном количестве (либо оставшееся количество, если оно меньше заданного)
     *
     * @param size - размер списка возвращаемых постов (default = 10);
     * @param from - начальный порядковый номер, с которого возвращаются посты (default = 0);
     * @param sort - порядок сортировки (default = desc)
     */
    public Collection<Post> findAll(int size, int from, String sort) {
        List<Post> sortedPost = new ArrayList<>(postRepository.values().stream()
                .sorted(Comparator.comparing(Post::getPostDate))
                .toList());
        if (sortedPost.size() >= (size + from)) {
            if (SortOrder.from(sort) == SortOrder.DESCENDING) {    // по умолчанию сортировка будет по убыванию
                Collections.reverse(sortedPost);    // изменяем порядок списка
            }
            if (SortOrder.from(sort) == null) {
                throw new IncorrectURLException("Некорректный параметр sort=" + sort + " в URL запроса. Требуемые: \n" +
                        Arrays.toString(SortOrder.values()));
            }
            return sortedPost.subList(from, from + size);
        }
        throw new NotFoundException("Размер списка постов меньше " + (size + from) +
                ". Задайте в URL параметр {size} или {from} меньшего размера.");
    }


    public Optional<Post> findById(long postId) {
        Optional<Post> post = postRepository.values().stream()
                .filter(x -> x.getId() == postId)
                .findFirst();
        if (post.isEmpty()) {
            throw new ConditionsNotMetException("Пост с id=" + postId + " не существует");
        }
        return post;
    }

    public Post create(Post post) {
        if (post.getDescription() == null || post.getDescription().isBlank()) {
            throw new ConditionsNotMetException("Описание не может быть пустым");
        }
        userService.findById((int) post.getAuthorId());
        post.setId(getNextId());
        post.setPostDate(Instant.now());
        postRepository.put(post.getId(), post);
        return post;
    }

    public Post update(Post newPost) {
        if (newPost.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (postRepository.containsKey(newPost.getId())) {
            Post oldPost = postRepository.get(newPost.getId());
            if (newPost.getDescription() == null || newPost.getDescription().isBlank()) {
                throw new ConditionsNotMetException("Описание не может быть пустым");
            }
            oldPost.setDescription(newPost.getDescription());
            return oldPost;
        }
        throw new NotFoundException("Пост с id = " + newPost.getId() + " не найден");
    }

    private long getNextId() {
        long currentMaxId = postRepository.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}