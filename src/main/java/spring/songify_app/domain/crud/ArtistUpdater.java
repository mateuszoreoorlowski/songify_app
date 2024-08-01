package spring.songify_app.domain.crud;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import spring.songify_app.domain.crud.dto.ArtistDto;

@AllArgsConstructor
@Service
class ArtistUpdater {

    private final ArtistRetriever artistRetriever;

    ArtistDto updateArtistNameById(final Long artistId, final String name) {
        Artist artist = artistRetriever.findById(artistId);
        artist.setName(name);
        return new ArtistDto(artist.getId(), artist.getName());
    }


}
