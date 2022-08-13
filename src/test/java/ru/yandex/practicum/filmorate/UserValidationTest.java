package ru.yandex.practicum.filmorate;

import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Set;

import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import static org.junit.jupiter.api.Assertions.*;


public class UserValidationTest {
    @Test
    void ShouldCreateUser() {
        User user = new User(0, "test@yandex.ru", "TestLogin", "TestName", LocalDate.of(1990, 1, 1));
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }

    @Test
    void ShouldCreateUserWithWrongEmail() {
        User user = new User(0, "test", "TestLogin", "TestName", LocalDate.of(1990, 1, 1));
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    void ShouldCreateUserWithWrongLogin() {
        User user = new User(0, "test@yandex.rr", "", "TestName", LocalDate.of(1990, 1, 1));
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    void ShouldCreateUserWithWrongBirthday() {
        User user = new User(0, "test@yandex.rr", "", "TestName", LocalDate.of(2990, 1, 1));
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

}
