package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class FilmController {
    private final Map<Integer, Film> films = new HashMap();
    private int id;

    @GetMapping("/films")
    public List<Film> findAll() {
        log.info("Получен GET запрос к эндпоинту: /films");
        ArrayList<Film> filmsList = new ArrayList<>();
        for (Film film : films.values()) {
            filmsList.add(film);
        }
        log.info("Ответ: " + filmsList);
        return filmsList;
    }

    @PostMapping(value = "/films")
    public Film create(@Valid @RequestBody Film film) {
        log.info("Получен Post запрос к эндпоинту: /films");
        film.validate();
        id++;
        film.setId(id);
        films.put(id, film);
        return film;
    }

    @PutMapping(value = "/films")
    public Film update(@Valid @RequestBody Film film) {
        log.info("Получен Put запрос к эндпоинту: /films");
        log.info("Обновление фильма:" + film.getId());
        log.info("Данные фильма:" + film);
        film.validate();
        int filmId = film.getId();
        if (films.containsKey(filmId)) {
            films.put(filmId, film);
        } else {
            throw new ValidationException("Пользователь с ID=" + filmId + "не найден");
        }
        return film;
    }
}
