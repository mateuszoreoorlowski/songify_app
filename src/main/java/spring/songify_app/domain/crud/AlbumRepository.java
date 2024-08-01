package spring.songify_app.domain.crud;

import org.springframework.data.domain.Pageable;

import org.springframework.data.repository.Repository;

import java.util.Optional;
import java.util.Set;

interface AlbumRepository extends Repository<Album, Long> {

   Album save(Album album);

   Set<Album> findAll();

   Optional<Album> findById(Long albumId);
}
