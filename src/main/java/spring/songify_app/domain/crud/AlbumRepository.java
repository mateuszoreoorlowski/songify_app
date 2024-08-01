package spring.songify_app.domain.crud;

import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import spring.songify_app.domain.crud.dto.AlbumInfo;

import java.util.*;

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

    @Query("""
            select a from Album a 
            inner join a.artists artists 
            where artists = :artist
            """)
    List<Album> findByArtist(@Param("artist") Artist artist);

    @Query("""
            select a from Album a 
            inner join a.artists artists 
            where artists.id = :id
            """)
    Set<Album> findAllAlbumsByArtistId(@Param("id") Long id);

    @Modifying
    @Query("delete from Album a where a.id in :ids")
    int deleteByIdIn(Collection<Long> ids);

    @Modifying
    @Query("delete from Album a where a.id = :albumId")
    int deleteById(Long albumId);
}
