package spring.songify_app.infrastructure.crud.song.response;

public record AssignGenreToSongDto(
        Long songId,
        Long genreId
) {
}
