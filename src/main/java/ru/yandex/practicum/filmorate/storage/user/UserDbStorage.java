package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.sql.PreparedStatement;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.*;


@Component("userStorage")
@Slf4j
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public User create(User user) {
        String sqlQuery = "insert into \"users\"(\"email\", \"login\", \"name\", \"birthday\") " +
                "values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));

            return stmt;
        }, keyHolder);
        user.setId(keyHolder.getKey().longValue());
        return user;
    }

    public User update(@Valid User user) {
        // выполняем запрос к базе данных.
        long id = user.getId();
        if (getUserById(id) != null) {
            String sqlQuery = "update \"users\" set " +
                    "\"email\" = ?, \"login\" = ?, \"name\" = ? , \"birthday\" = ? " +
                    "where \"id\" = ?";
            jdbcTemplate.update(sqlQuery
                    , user.getEmail()
                    , user.getLogin()
                    , user.getName()
                    , user.getBirthday()
                    , user.getId());
        } else {
            log.info("Пользователь с идентификатором {} не найден.", id);
            throw new NotFoundException("Пользователь с ID=" + id + "не найден");
        }
        return user;
    }

    public User getUserById(long id) {
        // выполняем запрос к базе данных.
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from \"users\" where \"id\" = ?", id);

        // обрабатываем результат выполнения запроса
        if (userRows.next()) {
            HashSet<Long> friendSet= new HashSet<>(getFriends(userRows.getLong("id")));
            User user = new User(
                    userRows.getLong("id"),
                    userRows.getString("email"),
                    userRows.getString("login"),
                    userRows.getString("name"),
                    userRows.getDate("birthday").toLocalDate(),
                    friendSet);
            log.info("Найден пользователь: {} {}", user.getId(), user.getName());
            return user;
        } else {
            log.info("Пользователь с идентификатором {} не найден.", id);
            throw new NotFoundException("Пользователь с ID=" + id + "не найден");
        }
    }

    public List<User> getAll() {
        String sqlQuery = "select * from \"users\"";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser);
    }

    public void addFriend(long userId, long friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        if (user.getFriends().contains(friendId)) {
            log.info("У пользователя с ID={} уже есть в друзьях пользователь с ID={}", userId, friendId);
        } else {
            String sqlQuery = "insert into \"users_relation\"(\"user_id\", \"friend_id\") " +
                    "values (?, ?)";
            jdbcTemplate.update(sqlQuery,
                    userId,
                    friendId);
        }
    }

    public void deleteFriend(long userId, long friendId) {
        User user = getUserById(userId);
        String sqlQuery = "delete from \"users_relation\" where \"user_id\" = ? and \"friend_id\" = ?";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    private List<Long> getFriends(long id) {
        String sqlQuery = "select * from \"users_relation\" where \"user_id\" = ?";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFriendId, id);
    }
    private Long mapRowToFriendId(ResultSet resultSet, int rowNum) throws SQLException {
        return resultSet.getLong("friend_id");
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        HashSet<Long> friendSet= new HashSet<>(getFriends(resultSet.getLong("id")));
        return User.builder()
                .id(resultSet.getLong("id"))
                .email(resultSet.getString("email"))
                .login(resultSet.getString("login"))
                .name(resultSet.getString("name"))
                .birthday(resultSet.getDate("birthday").toLocalDate())
                .friends(friendSet)
                .build();
    }

}

