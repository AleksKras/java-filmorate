package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User create(User user);

    User update(User user);

    User getUserById(long id);

    List<User> getAll();

    void addFriend(long userId, long friendId);

    void deleteFriend(long userId, long friendId);

     List<Long> getFriends(long id);
}
