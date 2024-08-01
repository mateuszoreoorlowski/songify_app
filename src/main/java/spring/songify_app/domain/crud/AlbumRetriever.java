package spring.songify_app.domain.crud;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.songify_app.domain.crud.dto.AlbumDto;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
class AlbumRetriever {

    private final AlbumRepository albumRepository;

    Set<AlbumDto> findAllAlbums() {
        return albumRepository.findAll().stream()
                .map(album ->
                        new AlbumDto(album.getId(),
                        album.getTitle()))
                .collect(Collectors.toSet());
    }
}
