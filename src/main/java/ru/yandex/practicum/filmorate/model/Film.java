package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.HashSet;

@Data
@AllArgsConstructor
public class Film {
    public final static int DESCRIPTION_MAX_LENGTH = 200;
    public final static LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    private long id;
    @NotBlank
    private String name;
    private String description;
    private LocalDate releaseDate;
    @Positive
    private int duration;
    private HashSet<Long> likes;
}
