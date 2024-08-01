package spring.songify_app.domain.crud;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.songify_app.domain.crud.dto.ArtistDto;
import spring.songify_app.infrastructure.crud.artist.dto.request.ArtistRequestDto;

@Service
@AllArgsConstructor
@Transactional
public class SongifyCrudFacade {

    private final ArtistAdder artistAdder;

    public ArtistDto addArtist(ArtistRequestDto artist) {
        return artistAdder.addArtist(artist.name());
    }
}
