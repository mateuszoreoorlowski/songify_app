package spring.songify_app.domain.crud;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

class InMemoryGenreRepository implements GenreRepository {

    Map<Long, Genre> db = new HashMap<>();
    AtomicInteger index = new AtomicInteger(0);

    public InMemoryGenreRepository() {
        save(new Genre("default"));
    }

    @Override
    public Genre save(Genre genre) {
        long index = this.index.getAndIncrement();
        db.put(index, genre);
        genre.setId(index);
        return genre;
    }

    @Override
    public Set<Genre> findAll() {
        return new HashSet<>(db.values());
    }

    @Override
    public boolean existsById(Long id) {
        return db.containsKey(id);
    }

    @Override
    public Optional<Genre> findById(Long genreId) {
        return Optional.ofNullable(db.get(genreId));
    }

    @Override
    public int deleteById(Long genreId) {
        db.remove(genreId);
        return genreId.intValue();
    }
}
