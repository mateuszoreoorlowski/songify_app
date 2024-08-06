package spring.songify_app.infrastructure.crud.genre.response;

import java.util.Set;

public record AllGenresResponseDto(Set<GenreResponseDto> genres) {
}
