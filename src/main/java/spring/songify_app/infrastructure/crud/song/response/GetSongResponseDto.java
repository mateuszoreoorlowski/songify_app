package spring.songify_app.infrastructure.crud.song.response;

import spring.songify_app.domain.crud.dto.GenreDto;
import spring.songify_app.domain.crud.dto.SongDto;

public record GetSongResponseDto(
        SongDto song,
        GenreDto genre
) {
}
