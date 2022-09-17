package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    public Film create(@Valid Film film) {
        return filmStorage.create(film);
    }

    public Film update(@Valid Film film) {
        return filmStorage.update(film);
    }

    public Set<Film> findAll() {
        log.info("Получен запрос на список всех фильмов");
        return filmStorage.getAll();
    }

    public Film getFilm(int id) {
        return filmStorage.getFilmById(id);
    }

    public void addLike(int filmId, long userId) {
        Film film = filmStorage.getFilmById(filmId);
        User user = userService.getUser(userId);
        HashSet<Long> likes = film.getLikes();
        likes.add(userId);
    }

    public void deleteLike(int filmId, int userId) {
        Film film = filmStorage.getFilmById(filmId);
        User user = userService.getUser(userId);
        HashSet<Long> likes = film.getLikes();
        likes.remove(userId);
    }

    public Set<Film> getPopularFilms(int count) {
        Set<Film> films = filmStorage.getAll();
        return films.stream().sorted((film0, film1) -> {
            Integer likeFilm0Size = film0.getLikes().size();
            Integer likeFilm1Size = film1.getLikes().size();
            int comp = likeFilm1Size.compareTo(likeFilm0Size);
            return comp;
        }).limit(count).collect(Collectors.toSet());
    }
}
