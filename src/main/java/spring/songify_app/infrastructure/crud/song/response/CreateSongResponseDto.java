package spring.songify_app.infrastructure.crud.song.response;

import spring.songify_app.domain.crud.dto.SongLanguageDto;

import java.time.Instant;

public record CreateSongResponseDto(Long id, String name, Instant releaseDate, Long duration, SongLanguageDto language) {
}
