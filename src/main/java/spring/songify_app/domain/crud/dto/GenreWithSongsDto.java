package spring.songify_app.domain.crud.dto;

import java.util.List;

public record GenreWithSongsDto(
        Long id,
        String name,
        List<SongDto> songs
) {
}
