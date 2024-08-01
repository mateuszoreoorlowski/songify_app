package spring.songify_app.domain.crud;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@AllArgsConstructor
@Service
class AlbumDeleter {

    private final SongRepository songRepository;
    private final AlbumRepository albumRepository;

    void deleteAllAlbumsByIds(final Set<Long> albumIdsToDelete) {
        albumRepository.deleteByIdIn(albumIdsToDelete);
    }

    void deleteAlbumById(Long albumId) {
        // Sprawdź, czy istnieją piosenki powiązane z tym albumem
        if (songRepository.existsByAlbumId(albumId)) {
            throw new IllegalArgumentException("Nie można usunąć albumu, istnieją piosenki powiązane z nim.");
        }
        albumRepository.deleteById(albumId);
    }
}
