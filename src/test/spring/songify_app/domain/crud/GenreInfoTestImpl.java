package spring.songify_app.domain.crud;

import spring.songify_app.domain.crud.dto.AlbumInfo;

public class GenreInfoTestImpl implements AlbumInfo.SongInfo.GenreInfo {
    private final Genre genre;

    GenreInfoTestImpl(Genre genre) {
        this.genre = genre;
    }

    @Override
    public Long getId() {
        return genre.getId();
    }

    @Override
    public String getName() {
        return genre.getName();
    }
}
