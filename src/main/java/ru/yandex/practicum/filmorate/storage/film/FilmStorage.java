package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Set;


public interface FilmStorage {
    Set<Film> getAll();

    Film create(Film film);

    Film update(Film film);

    Film getFilmById(long id);
}
