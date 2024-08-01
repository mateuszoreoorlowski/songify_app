package spring.songify_app.domain.crud;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.songify_app.domain.crud.dto.ArtistDto;
import spring.songify_app.domain.crud.dto.GenreDto;
import spring.songify_app.domain.crud.dto.SongDto;
import spring.songify_app.infrastructure.crud.song.request.CreateSongRequestDto;
import spring.songify_app.infrastructure.crud.artist.dto.request.ArtistRequestDto;
import spring.songify_app.infrastructure.crud.genre.request.GenreRequestDto;

import java.util.Set;

@Service
@AllArgsConstructor
@Transactional
public class SongifyCrudFacade {

    private final ArtistAdder artistAdder;
    private final GenreAdder genreAdder;
    private final SongAdder songAdder;
    private final ArtistRetriever artistRetriever;
    private final GenreRetriever genreRetriever;
    private final SongRetriever songRetriever;

    public ArtistDto addArtist(ArtistRequestDto artist) {
        return artistAdder.addArtist(artist.name());
    }

    public Set<ArtistDto> findAllArtists(Pageable pageable) {
        return artistRetriever.findAllArtists(pageable);
    }

    public GenreDto addGenre(GenreRequestDto dto) {
        return genreAdder.addGenre(dto.name());
    }

    public Set<GenreDto> findAllGenres(Pageable pageable) {
        return genreRetriever.findAllGenres(pageable);
    }

    public SongDto addSong(CreateSongRequestDto songDto) {
        return songAdder.addSong(songDto);
    }

    public Set<SongDto> findAllSongs(Pageable pageable) {
        return songRetriever.findAll(pageable);
    }


}
