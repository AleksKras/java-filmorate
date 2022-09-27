package ru.yandex.practicum.filmorate.mapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

@Slf4j
@RequiredArgsConstructor
public class UserMapper implements RowMapper<User> {

    private final UserStorage userStorage;

    public User mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        HashSet<Long> friendSet= new HashSet<>(userStorage.getFriends(resultSet.getLong("id")));
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
