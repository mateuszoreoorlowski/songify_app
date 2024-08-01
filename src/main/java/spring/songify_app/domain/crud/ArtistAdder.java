package spring.songify_app.domain.crud;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import spring.songify_app.domain.crud.dto.ArtistDto;
import spring.songify_app.infrastructure.crud.artist.dto.request.ArtistRequestDto;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
class ArtistAdder {

    private final ArtistRepository artistRepository;

    ArtistDto addArtist(final String name) {
        Artist artist = new Artist(name);
        Artist save = artistRepository.save(artist);
        return new ArtistDto(save.getId(), save.getName());
    }

    ArtistDto addArtistWithDefaultAlbumAndSong(final ArtistRequestDto dto) {
        String artistName = dto.name();
        Artist save = saveArtistWithDefaultAlbumAndSong(artistName);
        return new ArtistDto(save.getId(), save.getName());
    }

    private Artist saveArtistWithDefaultAlbumAndSong(String artistName) {
        Artist artist = new Artist(artistName);

        Album album = new Album();
        album.setTitle("default-album:" + UUID.randomUUID());
        album.setReleaseDate(LocalDateTime.now().toInstant(ZoneOffset.UTC));

        Song song = new Song("default-song-name: " + UUID.randomUUID());

        album.addSongToAlbum(Set.of(song));
        artist.addAlbum(album);

        return artistRepository.save(artist);
    }
}
