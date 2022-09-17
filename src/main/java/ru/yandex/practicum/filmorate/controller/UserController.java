package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<User> getAll() {
        log.info("Получен Get запрос к эндпоинту: /users");
        return userService.findAll();
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable("id") Integer id) {
        return userService.getUser(id);
    }

    @GetMapping("/users/{id}/friends")
    public List<User> getUserFriends(@PathVariable("id") Integer id) {
        return userService.getAllFriends(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> getUserCommonFriends(@PathVariable("id") Integer id,
                                           @PathVariable("otherId") Integer otherId) {
        return userService.getCommonFriend(id, otherId);
    }

    @PostMapping("/users")
    public User create(@Valid @RequestBody User user) {
        log.info("Получен Post запрос к эндпоинту: /users");
        return userService.create(user);
    }

    @PutMapping("/users")
    public User update(@Valid @RequestBody User user) {
        log.info("Получен Put запрос к эндпоинту: /users. Обновление пользователя:" + user.getId()
                + ". Данные пользователя:" + user);
        return userService.update(user);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") Integer id,
                          @PathVariable("friendId") Integer otherId) {
        userService.addFriend(id, otherId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable("id") Integer id,
                             @PathVariable("friendId") Integer otherId) {
        userService.deleteFriend(id, otherId);
    }

}
