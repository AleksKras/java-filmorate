package ru.yandex.practicum.filmorate.service.mpa;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MpaService {

    @Autowired
    private final MpaStorage mpaStorage;

    public List<Mpa> findAll() {
        log.info("Получен запрос на список всех жанров");
        return mpaStorage.getAll();
    }

    public Mpa getMpa(int id) {
        return mpaStorage.getMpaById(id);
    }

}
