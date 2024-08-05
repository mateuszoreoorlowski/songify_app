package spring.songify_app.domain.crud;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

class InMemorySongRepository implements SongRepository {

    Map<Long, Song> db = new HashMap<>();
    AtomicInteger index = new AtomicInteger(0);


    @Override
    public Song save(Song song) {
        long index = this.index.getAndIncrement();
        db.put(index, song);
        song.setId(index);
        return song;
    }

    @Override
    public Set<Song> findAll() {
        return new HashSet<>(db.values());
    }

    @Override
    public Optional<Song> findById(Long id) {
        Song value = db.get(id);
        return Optional.ofNullable(value);
    }

    @Override
    public List<Song> findByGenreName(String genreName) {
        return db.values().stream().filter(song -> song.getGenre() != null && song.getGenre().getName().equals(genreName)).collect(Collectors.toList());
    }

    @Override
    public int deleteByIdIn(Collection<Long> ids) {
        ids.forEach(
                id -> db.remove(id)
        );
        return 0;
    }

    @Override
    public boolean existsByGenreId(Long genreId) {
        return db.values().stream().anyMatch(song -> song.getGenre() != null && song.getGenre().getId().equals(genreId));
    }

    @Override
    public boolean existsByAlbumId(Long albumId) {
        return db.values().stream().anyMatch(song -> song.getAlbum() != null && song.getAlbum().getId().equals(albumId));
    }

    @Override
    public void deleteById(Long id) {
        db.remove(id);
    }

    @Override
    public List<Song> findByGenreId(Long genreId) {
        return db.values().stream()
                .filter(
                        song -> song.getGenre() != null && song.getGenre().getId().equals(genreId))
                .collect(Collectors.toList());
    }
}
