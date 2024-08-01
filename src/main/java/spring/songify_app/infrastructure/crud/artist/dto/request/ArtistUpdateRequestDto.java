package spring.songify_app.infrastructure.crud.artist.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ArtistUpdateRequestDto(
        @NotNull(message = "artistId must not be null")
        @NotEmpty(message = "artistId must not be empty")
        Long artistId,

        @NotNull(message = "newArtistName must not be null")
        @NotEmpty(message = "newArtistName must not be empty")
        String newArtistName
) {
}
