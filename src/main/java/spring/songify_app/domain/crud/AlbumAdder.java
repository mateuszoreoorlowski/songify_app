package spring.songify_app.domain.crud;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.songify_app.domain.crud.dto.AlbumDto;
import spring.songify_app.domain.crud.dto.AlbumWithSongsDto;

import java.time.Instant;
import java.util.HashSet;
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

    AlbumWithSongsDto addAlbumWithSongs(String title, Instant releaseDate, Set<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            ids = new HashSet<>();
        }

        Set<Song> songs = ids.stream()
                .map(songRetriever::findSongById)
                .collect(Collectors.toSet());

        System.out.println("Znalezione piosenki: " + songs); // Logowanie znalezionych piosenek

        Album album = new Album();
        album.setTitle(title);
        album.setReleaseDate(releaseDate);
        album.addSongToAlbum(songs);

        Album savedAlbum = albumRepository.save(album);
        System.out.println("Zapisany album: " + savedAlbum); // Logowanie zapisanego albumu

        Set<Long> songsIds = savedAlbum.getSongs().stream().map(Song::getId).collect(Collectors.toSet());
        System.out.println("ID piosenek w zapisanym albumie: " + songsIds); // Logowanie ID piosenek w albumie

        return new AlbumWithSongsDto(savedAlbum.getId(), savedAlbum.getTitle(), savedAlbum.getReleaseDate(), songsIds);
    }


    public AlbumDto addArtistToAlbum(Long albumId, Long artistId){
        Album album = albumRetriever.findById(albumId);
        Artist artist = artistRetriever.findById(artistId);

        album.getArtists().add(artist);
        artist.getAlbums().add(album);

        albumRepository.save(album);
        return new AlbumDto(album.getId(), album.getTitle());
    }

    public AlbumWithSongsDto addSongToAlbum(Long albumId, Long songId){
        Album album = albumRetriever.findById(albumId);
        Song song = songRetriever.findSongById(songId);

        album.getSongs().add(song);

        albumRepository.save(album);
        return new AlbumWithSongsDto(album.getId(), album.getTitle(), album.getReleaseDate(), album.getSongsIds());
    }
}
