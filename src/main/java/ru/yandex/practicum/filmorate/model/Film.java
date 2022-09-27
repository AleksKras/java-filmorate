package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;

@Data
@AllArgsConstructor
@Builder
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
    @NotNull
    private Mpa mpa;
    private HashSet<Genre> genres;
    private HashSet<Long> likes;
}
