package spring.songify_app.domain.crud;

import org.springframework.data.repository.Repository;

interface ArtistRepository extends Repository<Artist, Long> {

    Artist save(Artist artist);
}
