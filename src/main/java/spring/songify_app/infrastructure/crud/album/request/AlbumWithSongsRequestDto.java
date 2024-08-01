package spring.songify_app.infrastructure.crud.album.request;

import java.time.Instant;
import java.util.Set;

public record AlbumWithSongsRequestDto(String title, Instant releaseDate, Set<Long> songsIds) {
}
