package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController extends Controller<User> {

    @GetMapping()
    public List<User> getAll() {
        return findAll();
    }

    @PostMapping()
    public User create(@Valid @RequestBody User user) {
        log.info("Получен Post запрос к эндпоинту: /users");
        if (user.getName().isEmpty()) {
            log.info("Получен пустое имя, замена на значение из поля логин");
            user.setName(user.getLogin());
        }
        id++;
        user.setId(id);
        items.put(id, user);
        return user;
    }

    @PutMapping()
    public User update(@Valid @RequestBody User user) {
        log.info("Получен Put запрос к эндпоинту: /users. Обновление пользователя:" + user.getId()
                + ". Данные пользователя:" + user);
        if (items.containsKey(user.getId())) {
            items.put(user.getId(), user);
        } else {
            throw new ValidationException("Пользователь с ID=" + user.getId() + "не найден");
        }
        return user;
    }
}
