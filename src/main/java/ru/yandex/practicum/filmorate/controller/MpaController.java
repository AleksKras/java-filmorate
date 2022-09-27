package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.mpa.MpaService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping(value = "/mpa", method = RequestMethod.GET)
public class MpaController {
    private final MpaService mpaService;

    @Autowired
    public MpaController(MpaService mpaService) {
        this.mpaService = mpaService;
    }

    @GetMapping
    public List<Mpa> getAll() {
        log.info("Получен Get запрос к эндпоинту: /mpa");
        return mpaService.findAll();
    }

    @GetMapping("/{id}")
    public Mpa getMpa(@PathVariable("id") Integer id) {
        return mpaService.getMpa(id);
    }

}
