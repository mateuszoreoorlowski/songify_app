package spring.songify_app.domain.crud;

import spring.songify_app.domain.crud.dto.AlbumInfo;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

class InMemoryAlbumRepository implements AlbumRepository {

    Map<Long, Album> db = new HashMap<>();
    AtomicInteger index = new AtomicInteger(0);


    @Override
    public Album save(Album album) {
        long index = this.index.getAndIncrement();
        db.put(index, album);
        album.setId(index);
        return album;
    }

    @Override
    public Set<Album> findAll() {
        return new HashSet<>(db.values());
    }

    @Override
    public Optional<Album> findById(Long albumId) {
        Album value = db.get(albumId);
        return Optional.ofNullable(value);
    }

    @Override
    public Optional<AlbumInfo> findAlbumByIdWithSongsAndArtists(Long id) {
        Album album = db.get(id);
        AlbumInfoTestImpl albumInfoTest = new AlbumInfoTestImpl(album);
        return Optional.of(albumInfoTest);
    }

    @Override
    public List<Album> findByArtist(Artist artist) {
        return db.values().stream()
                .filter(album -> album.getArtists().contains(artist))
                .collect(Collectors.toList());
    }

    @Override
    public Set<Album> findAllAlbumsByArtistId(Long id) {
        return db.values().stream()
                .filter(album -> album.getArtists().stream()
                        .anyMatch(artist -> artist.getId().equals(id)))
                .collect(Collectors.toSet());
    }

    @Override
    public int deleteByIdIn(Collection<Long> ids) {
        ids.forEach(
                id -> db.remove(id)
        );
        return 0;
    }

    @Override
    public int deleteById(Long albumId) {
        db.remove(albumId);
        return albumId.intValue();
    }
}
