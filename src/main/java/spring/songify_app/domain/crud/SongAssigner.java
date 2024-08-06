package spring.songify_app.domain.crud;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import spring.songify_app.domain.crud.dto.SongAlbumDto;
import spring.songify_app.domain.crud.dto.SongArtistDto;
import spring.songify_app.domain.crud.dto.SongDto;
import spring.songify_app.domain.crud.dto.SongLanguageDto;

@Service
@AllArgsConstructor
class SongAssigner {

    private final SongRepository songRepository;
    private final AlbumRepository albumRepository;
    private final SongRetriever songRetriever;
    private final AlbumRetriever albumRetriever;
    private final ArtistRetriever artistRetriever;

    public SongAlbumDto assignSongToAlbum(Long songId, Long albumId) {
        Song song = songRetriever.findSongById(songId);
        Album album = albumRetriever.findById(albumId);

        album.getSongs().add(song);
        song.setAlbum(album);
        songRepository.save(song);
        albumRepository.save(album);

        SongDto songDto = SongDto.builder()
                .id(song.getId())
                .name(song.getName())
                .duration(song.getDuration())
                .releaseDate(song.getReleaseDate())
                .language(SongLanguageDto.valueOf(song.getLanguage().name()))
                .build();

        return SongAlbumDto.builder()
                .song(songDto)
                .albumId(album.getId())
                .build();
    }

    public SongArtistDto assignSongToArtist(SongArtistDto dto) {
        Song song = songRetriever.findSongById(dto.songId());
        Album album = albumRetriever.findById(dto.albumId());
        Artist artist = artistRetriever.findById(dto.artistId());

        // Przypisanie piosenki do albumu, jeśli nie jest już przypisana
        if (song.getAlbum() == null || !song.getAlbum().getId().equals(album.getId())) {
            song.setAlbum(album);
            songRepository.save(song);
        }

        // Przypisanie artysty do albumu, jeśli nie jest już przypisany
        if (!album.getArtists().contains(artist)) {
            album.addArtist(artist);
            albumRepository.save(album);
        }

        // Aktualizacja relacji pomiędzy albumem i artystą
        album.getArtists().add(artist);
        artist.getAlbums().add(album);

        return new SongArtistDto(song.getId(), album.getId(), artist.getId());
    }
}
