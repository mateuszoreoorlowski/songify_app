package spring.songify_app.domain.crud;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

class InMemoryArtistRepository implements ArtistRepository {

    Map<Long, Artist> db = new HashMap<>();
    AtomicInteger index = new AtomicInteger(0);


    @Override
    public Artist save(Artist artist) {
        long index = this.index.getAndIncrement();
        db.put(index, artist);
        artist.setId(index);
        return artist;
    }

    @Override
    public Set<Artist> findAll() {
        return new HashSet<>(db.values());
    }

    @Override
    public Optional<Artist> findById(Long id) {
        return Optional.ofNullable(db.get(id));
    }

    @Override
    public int deleteById(Long id) {
        db.remove(id);
        return id.intValue();
    }
}
