/*package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class FilmValidationTest {
    @Test
    void shouldCreateFilm() {
        Film film = new Film(0, "Test Film", "Test description", LocalDate.of(1990, 1, 1), 100);
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty());
        try {
            FilmController.validate(film);
        } catch (ValidationException e) {
            assertTrue(false, e.getMessage());
        }
    }

    @Test
    @DisplayName("Тест валидации неправильного Name")
    void shouldCreateFilmWithWrongName() {
        Film film = new Film(0, "", "Test description", LocalDate.of(1990, 1, 1), 100);
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
        try {
            FilmController.validate(film);
        } catch (ValidationException e) {
            assertTrue(false, e.getMessage());
        }
    }

    @Test
    @DisplayName("Тест валидации неправльного Duration")
    void shouldCreateFilmWithWrongDuration() {
        Film film = new Film(0, "Test Name", "Test description", LocalDate.of(1990, 1, 1), -100);
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
        try {
            FilmController.validate(film);
        } catch (ValidationException e) {
            assertTrue(false, e.getMessage());
        }
    }

    @Test
    @DisplayName("Тест валидации неправльного ReleaseDate")
    void shouldCreateFilmWithWrongReleaseDate() {
        Film film = new Film(0, "Test Name", "Test description", LocalDate.of(990, 1, 1), 100);
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty());
        try {
            FilmController.validate(film);
        } catch (ValidationException e) {
            assertTrue(true, e.getMessage());
        }
    }

    @Test
    @DisplayName("Тест валидации неправльного Description")
    void shouldCreateFilmWithWrongDescription() {
        StringBuilder description = new StringBuilder(Film.DESCRIPTION_MAX_LENGTH + 10);
        for (int i = 0; i < Film.DESCRIPTION_MAX_LENGTH + 10; i++) {
            description.append("a");
        }
        Film film = new Film(0, "Test Name", description.toString(), LocalDate.of(1990, 1, 1), 100);
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty());
        try {
            FilmController.validate(film);
        } catch (ValidationException e) {
            assertTrue(true, e.getMessage());
        }
    }
}
*/