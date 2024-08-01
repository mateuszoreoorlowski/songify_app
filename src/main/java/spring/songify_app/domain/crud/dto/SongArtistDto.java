package spring.songify_app.domain.crud.dto;

public record SongArtistDto(
        Long songId,
        Long albumId,
        Long artistId
) {
}
