package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.*;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage{
    private final Map<Integer, User> users = new HashMap();
    private int id;

    public User create(User user) {
        id++;
        user.setId(id);
        if (user.getFriends() == null) {
            user.setFriends(new HashSet<Integer>());
        }
        users.put(id, user);
        return user;
    }

    public User update(@Valid User user) {
        if (users.containsKey(user.getId())) {
            if (user.getFriends() == null) {user.setFriends(new HashSet<Integer>());}
            users.put(user.getId(), user);
        } else {
            throw new NotFoundException("Пользователь с ID=" + user.getId() + "не найден");
        }
        return user;
    }

    public User getUserById(int id) {
        if (users.containsKey(id)) {
            return users.get(id);
        } else {
            throw new NotFoundException("Пользователь с ID=" + id + "не найден");
        }
    }

    public List<User> getAll() {
        ArrayList<User> userList = new ArrayList<>();
        for (User user : users.values()) {
            userList.add(user);
        }
        return userList;
    }

}

