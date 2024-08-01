package spring.songify_app.domain.crud;

import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import spring.songify_app.domain.crud.dto.AlbumInfo;

import java.util.Optional;
import java.util.Set;

interface AlbumRepository extends Repository<Album, Long> {

   Album save(Album album);

   Set<Album> findAll();

   Optional<Album> findById(Long albumId);

   @Query("""
            select a from Album a
            join fetch a.songs songs
            join fetch a.artists artists
            where a.id = :id""")
   Optional<AlbumInfo> findAlbumByIdWithSongsAndArtists(Long id);
}
