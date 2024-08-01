package spring.songify_app.infrastructure.crud.artist.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record ArtistRequestDto(
        @NotNull(message = "name must not be null")
        @NotEmpty(message = "name must not be empty")
        String name
) {
}
