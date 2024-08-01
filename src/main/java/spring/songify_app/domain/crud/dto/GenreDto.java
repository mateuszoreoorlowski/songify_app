package spring.songify_app.domain.crud.dto;

import lombok.Builder;

@Builder
public record GenreDto(Long id, String name) {
}
