package ru.yandex.practicum.filmorate.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

@RequiredArgsConstructor
public class FilmMapper implements RowMapper<Film> {
    private final FilmStorage filmStorage;
    private final MpaStorage mpaStorage;

    public Film mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        long filmId = resultSet.getLong("id");
        HashSet<Genre> genresSet = new HashSet<>(filmStorage.getListGenres(filmId));
        HashSet<Long> likesSet = new HashSet<>(filmStorage.getLikes(filmId));
        return Film.builder().id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .duration(resultSet.getInt("duration"))
                .genres(genresSet)
                .likes(likesSet)
                .mpa(mpaStorage.getMpaById(resultSet.getInt("rating_id")))
                .build();
    }
}
