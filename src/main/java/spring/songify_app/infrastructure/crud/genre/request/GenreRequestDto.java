package spring.songify_app.infrastructure.crud.genre.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record GenreRequestDto(
        @NotNull(message = "name must not be null")
        @NotEmpty(message = "name must not be empty")
        String name
) {
}
