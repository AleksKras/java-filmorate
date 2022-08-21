package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
abstract class Controller <T>{
    protected final Map<Integer, T> items = new HashMap();
    protected int id;

    protected List<T> findAll() {
        ArrayList<T> itemList = new ArrayList<>();
        for (T item : items.values()) {
            itemList.add(item);
        }
        log.info("Получен GET запрос");
        log.info("Ответ: " + items);
        return itemList;
    }
}
