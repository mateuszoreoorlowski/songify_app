package spring.songify_app.domain.crud;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import spring.songify_app.domain.crud.dto.ArtistDto;

@Service
@AllArgsConstructor
class ArtistAdder {

    private final ArtistRepository artistRepository;

    ArtistDto addArtist(final String name) {
        Artist artist = new Artist(name);
        Artist save = artistRepository.save(artist);
        return new ArtistDto(save.getId(), save.getName());
    }
}
