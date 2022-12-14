package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap();
    private long id;

    public Set<Film> getAll() {
        Set<Film> filmList = new HashSet<Film>();
        for (Film item : films.values()) {
            filmList.add(item);
        }
        return filmList;
    }

    public Film create(Film film) {
        validate(film);
        id++;
        film.setId(id);
        if (film.getLikes() == null) {
            film.setLikes(new HashSet<Long>());
        }
        films.put(id, film);
        return film;
    }

    public Film update(Film film) {
        validate(film);
        long filmId = film.getId();
        if (films.containsKey(filmId)) {
            if (film.getLikes() == null) {
                film.setLikes(new HashSet<Long>());
            }
            films.put(filmId, film);
        } else {
            throw new NotFoundException("Пользователь с ID=" + filmId + " не найден");
        }
        return film;
    }

    private void validate(Film film) {
        if (film.getDescription().length() > Film.DESCRIPTION_MAX_LENGTH) {
            throw new ValidationException("Длина поля description=" + film.getDescription().length() +
                    ", максимальная длина = " + Film.DESCRIPTION_MAX_LENGTH);
        }
        if (film.getReleaseDate().isBefore(Film.MIN_RELEASE_DATE)) {
            throw new ValidationException("Ошибка в поле releaseDate");
        }
    }

    public Film getFilmById(long id) {
        if (films.containsKey(id)) {
            return films.get(id);
        } else {
            throw new NotFoundException("Фильм с ID=" + id + "не найден");
        }
    }

}
