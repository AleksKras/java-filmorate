package ru.yandex.practicum.filmorate.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private final UserStorage userStorage;

    public User create(@Valid User user) {
        if (user.getName().isEmpty()) {
            log.info("Получен пустое имя, замена на значение из поля логин");
            user.setName(user.getLogin());
        }
        return userStorage.create(user);
    }

    public User update(@Valid User user) {
        return userStorage.update(user);
    }

    public void addFriend(long userId, long friendId) {
        userStorage.addFriend(userId, friendId);
    }

    public void deleteFriend(long userId, long friendId) {
        userStorage.deleteFriend(userId, friendId);
    }

    public List<User> getAllFriends(long id) {
        ArrayList<User> userList = new ArrayList<>();
        User user = userStorage.getUserById(id);
        for (Long friendId : user.getFriends()) {
            userList.add(userStorage.getUserById(friendId));
        }
        return userList;
    }

    public List<User> findAll() {
        log.info("Получен запрос на список всех пользователей");
        return userStorage.getAll();
    }

    public User getUser(long id) {
        return userStorage.getUserById(id);
    }

    public List<User> getCommonFriend(long userId, long friendId) {
        ArrayList<User> userList = new ArrayList<>();
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        HashSet<Long> userFriends = user.getFriends();
        HashSet<Long> friendFriends = friend.getFriends();
        HashSet<Long> commonFriends;
        if (!(userFriends == null || friendFriends == null)) {
            commonFriends = (HashSet<Long>) friendFriends.clone();
            commonFriends.retainAll(userFriends);
            for (Long commonId : commonFriends) {
                userList.add(userStorage.getUserById(commonId));
            }
        }
        return userList;
    }

}
