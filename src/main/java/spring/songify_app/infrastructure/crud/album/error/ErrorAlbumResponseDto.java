package spring.songify_app.infrastructure.crud.album.error;

import org.springframework.http.HttpStatus;

public record ErrorAlbumResponseDto(
        String message,
        HttpStatus status
) {
}
