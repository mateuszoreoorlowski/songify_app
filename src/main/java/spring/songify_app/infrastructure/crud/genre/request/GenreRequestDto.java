package spring.songify_app.infrastructure.crud.genre.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record GenreRequestDto(
        @NotNull(message = "id must not be null")
        @NotEmpty(message = "id must not be empty")
        Long id,

        @NotNull(message = "name must not be null")
        @NotEmpty(message = "name must not be empty")
        String name
) {
}
