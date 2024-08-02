package spring.songify_app.domain.crud;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import spring.songify_app.domain.crud.dto.AlbumDto;
import spring.songify_app.domain.crud.dto.AlbumInfo;
import spring.songify_app.domain.crud.dto.ArtistDto;
import spring.songify_app.domain.crud.dto.ArtistWithAlbumsDto;
import spring.songify_app.domain.crud.exceptions.ArtistNotFoundException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
class ArtistRetriever {

    private final ArtistRepository artistRepository;
    private final AlbumRepository albumRepository;

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

    ArtistWithAlbumsDto retrieveArtistWithAlbums(Long artistId) {
        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() -> new ArtistNotFoundException(artistId.toString()));

        List<AlbumDto> albums = albumRepository.findByArtist(artist)
                .stream()
                .map(album -> new AlbumDto(
                        album.getId(),
                        album.getTitle()))
                .collect(Collectors.toList());

        return new ArtistWithAlbumsDto(artist.getId(), artist.getName(), albums);
    }

    public Artist findArtistById(Long artistId) {
        return artistRepository.findById(artistId)
                .orElseThrow(() -> new ArtistNotFoundException(artistId.toString()));
    }
}
