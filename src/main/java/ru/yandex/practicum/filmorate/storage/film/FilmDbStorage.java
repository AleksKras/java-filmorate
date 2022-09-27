package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.Valid;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    @Autowired
    private final JdbcTemplate jdbcTemplate;
    @Autowired
    private final GenreStorage genreStorage;
    @Autowired
    private final MpaStorage mpaStorage;

    @Autowired
    private final UserStorage userStorage;

    public Film create(Film film) {
        String sqlQuery = "insert into \"films\"(\"name\", \"description\", \"release_date\", \"duration\", \"rating_id\") " +
                "values (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        long filmId = keyHolder.getKey().longValue();
        film.setId(filmId);
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                createGenreLink(filmId, genre.getId());
            }
        }
        Film updatedFilm = getFilmById(filmId);
        return updatedFilm;
    }


    private void removeGenreLink(long filmId) {
        String sqlQuery = "delete from \"film_genres\" where \"film_id\" = ?";
        jdbcTemplate.update(sqlQuery, filmId);
    }
    private void createGenreLink(long filmId, int genreId) {
        Genre genre = genreStorage.getGenreById(genreId);
        List<Integer> genresId = getGenresId(filmId);
        if (genresId.contains(genre)) {
            log.info("у фильма с ID={} уже есть жанр с ID={}", filmId, genreId);
        } else {
            String sqlQuery = "insert into \"film_genres\"(\"film_id\", \"genre_id\") values (?, ?)";
            jdbcTemplate.update(sqlQuery,
                    filmId,
                    genreId);
        }
        genresId = getGenresId(filmId);
    }

    public Film update(@Valid Film film) {
        // выполняем запрос к базе данных.
        long id = film.getId();
        if (getFilmById(id) != null) {
            String sqlQuery = "update \"films\" set " +
                    "\"name\" = ?, \"description\" = ?, \"release_date\" = ?, \"duration\" = ?, \"rating_id\" = ? " +
                    "where \"id\" = ?";
            jdbcTemplate.update(sqlQuery
                    , film.getName()
                    , film.getDescription()
                    , film.getReleaseDate()
                    , film.getDuration()
                    , film.getMpa().getId()
                    , id);
            removeGenreLink(film.getId());
            if (film.getGenres() != null) {
                for (Genre genre : film.getGenres()) {
                    createGenreLink(film.getId(), genre.getId());
                }
            }
        } else {
            log.info("Фильм с идентификатором {} не найден.", id);
            throw new NotFoundException("Фильм с ID=" + id + "не найден");
        }
        film = getFilmById(id);
        return film;
    }

    public Film getFilmById(long id) {
        String sqlQuery = "select * from \"films\" where \"id\" = ?";
        List<Film> films = jdbcTemplate.query(sqlQuery, new FilmMapper(this, mpaStorage), id);
        if (!films.isEmpty()) {
            Film film = films.get(0);
            log.info("Найден фильм: {} {}", film.getId(), film.getName());
            return film;
        } else {
            log.info("Фильм с идентификатором {} не найден.", id);
            throw new NotFoundException("Фильм с ID=" + id + "не найден");
        }
    }

    public List<Film> getAll() {
        String sqlQuery = "select * from \"films\" order by \"id\"";
        return jdbcTemplate.query(sqlQuery, new FilmMapper(this, mpaStorage));
    }

    private List<Integer> getGenresId(long id) {
        String sqlQuery = "select * from \"film_genres\" where \"film_id\" = ? order by \"genre_id\"";
        return jdbcTemplate.query(sqlQuery, this::mapRowToGenresId, id);
    }

    public List<Genre> getListGenres(long filmId) {
        ArrayList<Genre> genres = new ArrayList<>();
        List<Integer> genresId = getGenresId(filmId);
        for (Integer genreId : genresId) {
            genres.add(genreStorage.getGenreById(genreId));
        }
        return genres;
    }

    public List<Long> getLikes(long id) {
        String sqlQuery = "select * from \"films_likes\" where \"film_id\" = ?";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUserId, id);
    }


    private Integer mapRowToGenresId(ResultSet resultSet, int rowNum) throws SQLException {
        return resultSet.getInt("genre_id");
    }

    private Long mapRowToUserId(ResultSet resultSet, int rowNum) throws SQLException {
        return resultSet.getLong("user_id");
    }

    public void addLike(long filmId, long userId) {
        Film film = getFilmById(filmId);
        User user = userStorage.getUserById(userId);
        if (film.getLikes().contains(userId)) {
            log.info("У фильма с ID={} уже есть лайк от пользователя с ID={}", filmId, userId);
        } else {
            String sqlQuery = "insert into \"films_likes\"(\"film_id\", \"user_id\") " +
                    "values (?, ?)";
            jdbcTemplate.update(sqlQuery,
                    filmId,
                    userId);
        }
    }

    public void deleteLike(long filmId, long userId) {
        Film film = getFilmById(filmId);
        User user = userStorage.getUserById(userId);
        String sqlQuery = "delete from \"films_likes\" where \"film_id\" = ? and \"user_id\" = ?";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }
}