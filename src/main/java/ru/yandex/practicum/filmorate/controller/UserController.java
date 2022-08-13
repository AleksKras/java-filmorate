package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class UserController {
    private final Map<Integer, User> users = new HashMap();
    private int id;

    @GetMapping("/users")
    public List<User> findAll() {
        ArrayList<User> usersList = new ArrayList<>();
        for (User user : users.values()) {
            usersList.add(user);
        }
        log.info("Получен GET запрос к эндпоинту: /users");
        log.info("Ответ: " + usersList);
        return usersList;
    }

    @PostMapping(value = "/users")
    public User create(@Valid @RequestBody User user) {
        log.info("Получен Post запрос к эндпоинту: /users");
        if (user.getName().isEmpty()) {
            log.info("Получен пустое имя, замена на значение из поля логин");
            user.setName(user.getLogin());
        }
        id++;
        user.setId(id);
        users.put(id, user);
        return user;
    }

    @PutMapping(value = "/users")
    public User update(@Valid @RequestBody User user) {
        log.info("Получен Put запрос к эндпоинту: /users");
        log.info("Обновление пользователя:" + user.getId());
        log.info("Данные пользователя:" + user);
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
        } else {
            throw new ValidationException("Пользователь с ID=" + user.getId() + "не найден");
        }
        return user;
    }
}
