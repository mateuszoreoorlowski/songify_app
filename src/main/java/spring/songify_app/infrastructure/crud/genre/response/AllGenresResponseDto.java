package spring.songify_app.infrastructure.crud.genre.response;

import spring.songify_app.domain.crud.dto.GenreDto;

import java.util.Set;

public record AllGenresResponseDto(Set<GenreResponseDto> genres) {
}
