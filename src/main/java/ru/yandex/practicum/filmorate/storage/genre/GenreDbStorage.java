package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage{

    @Autowired
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Genre getGenreById(int id) {
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("select * from \"genres\" where \"id\" = ?", id);
        if (genreRows.next()) {
            Genre genre = new Genre (
                    genreRows.getInt("id"),
            genreRows.getString("name"));
            return genre;
        } else {
            log.info("Genre с идентификатором {} не найден.", id);
            throw new NotFoundException("Genre с ID=" + id + "не найден");
        }
    }

    @Override
    public List<Genre> getAll() {
        String sqlQuery = "select * from \"genres\"";
        return jdbcTemplate.query(sqlQuery, this::mapRowToGenre);
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return new Genre(resultSet.getInt("id"), resultSet.getString("name"));
    }
}
