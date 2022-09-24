package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Component("mpaStorage")
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Mpa getMpaById(int id) {
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet("select * from \"ratings\" where \"id\" = ?", id);
        if (mpaRows.next()) {
            Mpa mpa = new Mpa (
                    mpaRows.getInt("id"),
                    mpaRows.getString("name"),
                    mpaRows.getString("description"));
            return mpa;
        } else {
            log.info("MPA с идентификатором {} не найден.", id);
            throw new NotFoundException("MPA с ID=" + id + "не найден");
        }
    }

    @Override
    public List<Mpa> getAll() {
        String sqlQuery = "select * from \"ratings\"";
        return jdbcTemplate.query(sqlQuery, this::mapRowToMpa);
    }

    private Mpa mapRowToMpa(ResultSet resultSet, int rowNum) throws SQLException {
        return new Mpa(resultSet.getInt("id"), resultSet.getString("name"),resultSet.getString("description"));
    }

}
