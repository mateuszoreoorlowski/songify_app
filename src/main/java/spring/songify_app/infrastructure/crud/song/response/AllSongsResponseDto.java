package spring.songify_app.infrastructure.crud.song.response;

import spring.songify_app.domain.crud.dto.SongDto;

import java.util.Set;

public record AllSongsResponseDto(Set<SongDto> songs) {
}
