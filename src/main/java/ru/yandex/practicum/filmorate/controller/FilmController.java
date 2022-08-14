package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController extends Controller<Film>{

    @GetMapping()
    public List<Film> getAll() {
        return findAll();
    }

    @PostMapping()
    public Film create(@Valid @RequestBody Film film) {
        log.info("Получен Post запрос к эндпоинту: /films");
        validate(film);
        id++;
        film.setId(id);
        items.put(id, film);
        return film;
    }

    @PutMapping()
    public Film update(@Valid @RequestBody Film film) {
        log.info("Получен Put запрос к эндпоинту: /films");
        log.info("Обновление фильма:" + film.getId());
        log.info("Данные фильма:" + film);
        validate(film);
        int filmId = film.getId();
        if (items.containsKey(filmId)) {
            items.put(filmId, film);
        } else {
            throw new ValidationException("Пользователь с ID=" + filmId + " не найден");
        }
        return film;
    }

    public static void validate(Film film) {
        if (film.getDescription().length() > Film.DESCRIPTION_MAX_LENGTH) {
            throw new ValidationException("Длина поля description=" + film.getDescription().length() +
                    ", максимальная длина = " + Film.DESCRIPTION_MAX_LENGTH);
        }
        if (film.getReleaseDate().isBefore(Film.MIN_RELEASE_DATE)) {
            throw new ValidationException("Ошибка в поле releaseDate");
        }
    }
}
