package ru.yandex.practicum.filmorate.service.genre;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GenreService {

    @Autowired
    private final GenreStorage genreStorage;

    public List<Genre> findAll() {
        log.info("Получен запрос на список всех жанров");
        return genreStorage.getAll();
    }

    public Genre getGenre(int id) {
        return genreStorage.getGenreById(id);
    }

}
