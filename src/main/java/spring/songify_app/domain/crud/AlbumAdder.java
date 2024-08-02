package spring.songify_app.domain.crud;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.songify_app.domain.crud.dto.AlbumDto;
import spring.songify_app.domain.crud.dto.AlbumWithSongsDto;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
class AlbumAdder {

    private final AlbumRepository albumRepository;
    private final SongRetriever songRetriever;
    private final AlbumRetriever albumRetriever;
    private final ArtistRetriever artistRetriever;

    AlbumWithSongsDto addAlbumWithSongs(String title, Instant releaseDate , Set<Long> ids) {
        Set<Song> songs = ids.stream()
                .map(songRetriever::findSongById)
                .collect(Collectors.toSet());

        Album album = new Album();
        album.setTitle(title);
        album.setReleaseDate(releaseDate);
        album.addSongToAlbum(songs);

        Album savedAlbum = albumRepository.save(album);
        Set<Long> songsIds = savedAlbum.getSongs().stream().map(Song::getId).collect(Collectors.toSet());

        return new AlbumWithSongsDto(savedAlbum.getId() ,savedAlbum.getTitle(), savedAlbum.getReleaseDate(), songsIds);
    }

    public AlbumDto addArtistToAlbum(Long albumId, Long artistId){
        Album album = albumRetriever.findById(albumId);
        Artist artist = artistRetriever.findById(artistId);

        album.getArtists().add(artist);
        artist.getAlbums().add(album);

        albumRepository.save(album);
        return new AlbumDto(album.getId(), album.getTitle());
    }

    public AlbumDto addSongToAlbum(Long albumId, Long songId){
        Album album = albumRetriever.findById(albumId);
        Song song = songRetriever.findSongById(songId);

        album.getSongs().add(song);

        albumRepository.save(album);
        return new AlbumDto(album.getId(), album.getTitle());
    }
}
