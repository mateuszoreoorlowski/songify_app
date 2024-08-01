package spring.songify_app.domain.crud;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import spring.songify_app.domain.crud.dto.ArtistDto;
import spring.songify_app.domain.crud.exceptions.ArtistNotFoundException;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
class ArtistRetriever {

    private final ArtistRepository artistRepository;

    Set<ArtistDto> findAllArtists() {
        return artistRepository.findAll()
                .stream()
                .map(artist -> new ArtistDto(
                        artist.getId(),
                        artist.getName()))
                .collect(Collectors.toSet());
    }

    Artist findById(Long id) {
        return artistRepository.findById(id)
                .orElseThrow(() -> new ArtistNotFoundException("Artist with id " + id + " not found"));
    }

}
