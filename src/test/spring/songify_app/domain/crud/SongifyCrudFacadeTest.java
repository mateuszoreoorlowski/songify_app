package spring.songify_app.domain.crud;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import spring.songify_app.domain.crud.dto.*;
import spring.songify_app.infrastructure.crud.album.request.AlbumWithSongsRequestDto;
import spring.songify_app.infrastructure.crud.album.response.AlbumWithSongsResponseDto;
import spring.songify_app.infrastructure.crud.artist.dto.request.ArtistRequestDto;
import spring.songify_app.infrastructure.crud.genre.request.GenreRequestDto;
import spring.songify_app.infrastructure.crud.song.request.CreateSongRequestDto;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SongifyCrudFacadeTest {

    SongifyCrudFacade songifyCrudFacade = SongifyCrudFacadeConfiguration.createSongifyCrudFacade(
            new InMemorySongRepository(),
            new InMemoryAlbumRepository(),
            new InMemoryArtistRepository(),
            new InMemoryGenreRepository()
    );

    // Wymaganie 1 - można dodać artystę (nazwa artysty)
    @Test
    @DisplayName("Should add artist 'Taco Hemingway' with id:0 When taco hemingway was sent")
    public void should_add_artist_taco_hemingway_with_id_0_when_taco_hemingway_was_sent() {
        // given
        ArtistRequestDto artist = ArtistRequestDto.builder()
                .name("Taco Hemingway")
                .build();

        Set<ArtistDto> allArtists = songifyCrudFacade.findAllArtists();
        assertTrue(allArtists.isEmpty());

        // when
        ArtistDto result = songifyCrudFacade.addArtist(artist);

        // then
        assertThat(result.id()).isEqualTo(0L);
        assertThat(result.name()).isEqualTo("Taco Hemingway");
        int size = songifyCrudFacade.findAllArtists().size();
        assertThat(size).isEqualTo(1);
    }

    // Wymaganie 2 - można dodać gatunek muzyczny (nazwa gatunku)
    @Test
    @DisplayName("Should add genre 'Rap' with id:0 When rap was sent")
    public void should_add_genre_rap_with_id_0_when_rap_was_sent() {
        // given
        GenreRequestDto genre = GenreRequestDto.builder()
                .id(1L)
                .name("Rap")
                .build();

        Set<GenreDto> allGenres = songifyCrudFacade.findAllGenres();
        assertThat(allGenres.isEmpty());

        // when
        GenreDto result = songifyCrudFacade.addGenre(genre);

        // then
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("Rap");
        assertThat(allGenres.stream().anyMatch(g -> g.name().equals("default"))).isTrue();
        int size = songifyCrudFacade.findAllGenres().size();
        assertThat(size).isEqualTo(2);
    }

    // Wymaganie 3 - można dodać album (tytuł, data wydania, ale musi być w nim przynajmniej jedna piosenka)
    @Test
    @DisplayName("Should add album 'Marmur' with song 'Wiatr'")
    public void should_add_album_marmur_with_song_wiatr() {

        // given
        CreateSongRequestDto song = CreateSongRequestDto.builder()
                .name("Wiatr")
                .language(SongLanguageDto.POLISH)
                .build();

        SongDto songDto = songifyCrudFacade.addSong(song);

        AlbumWithSongsRequestDto album = AlbumWithSongsRequestDto.builder()
                .title("Marmur")
                .releaseDate(Instant.now())
                .songsIds(Set.of(songDto.id()))
                .build();
        assertThat(songifyCrudFacade.findAllAlbums().isEmpty());

        // when
        AlbumWithSongsDto result = songifyCrudFacade.addAlbumWithSong(album);

        // then
        assertThat(!songifyCrudFacade.findAllAlbums().isEmpty());
        AlbumInfo albumWithSongs = songifyCrudFacade.findAlbumByIdWithArtistsAndSongs(result.id());
        Set<AlbumInfo.SongInfo> songs = albumWithSongs.getSongs();
        assertTrue(songs.stream().anyMatch(songInfo -> songInfo.getId().equals(songDto.id())));
    }

    // Wymaganie 4 - można dodać piosenkę (tytuł, czas trwania, data wydania, jezyk piosenki)
    @Test
    @DisplayName("Should add song 'Wiatr'")
    public void should_add_song_wiatr() {
        // given
        CreateSongRequestDto song = CreateSongRequestDto.builder()
                .name("Wiatr")
                .language(SongLanguageDto.POLISH)
                .build();

        assertThat(songifyCrudFacade.findAllSongs().isEmpty());

        // when
        SongDto result = songifyCrudFacade.addSong(song);

        // then
        Set<SongDto> allSongs = songifyCrudFacade.findAllSongs();
        assertThat(allSongs)
                .extracting(SongDto::id)
                .containsExactly(0L);
    }

    // Wymaganie 5 - mozna dodać artyste od razu z albumem i z piosenką (domyslne wartosci)
    @Test
    @DisplayName("Should add artist 'Taco Hemingway' with default album and song")
    public void should_add_artist_taco_hemingway_with_default_album_and_song() {
        // given
        ArtistRequestDto artist = ArtistRequestDto.builder()
                .name("Taco Hemingway")
                .build();
        Long artistId = songifyCrudFacade.addArtist(artist).id();

        CreateSongRequestDto song = CreateSongRequestDto.builder()
                .name("Wiatr")
                .language(SongLanguageDto.POLISH)
                .build();
        Long songId = songifyCrudFacade.addSong(song).id();

        AlbumWithSongsRequestDto album = AlbumWithSongsRequestDto.builder()
                .title("Marmur")
                .releaseDate(Instant.now())
                .songsIds(Set.of(songId))
                .build();
        Long albumId = songifyCrudFacade.addAlbumWithSong(album).id();

        assertThat(songifyCrudFacade.findAlbumByIdWithArtistsAndSongs(artistId).getArtists()).isEmpty();

        // when
        songifyCrudFacade.assignArtistToAlbum(artistId, albumId);
        songifyCrudFacade.addArtistWithDefaultAlbumAndSong(artist);

        // then
        AlbumInfo albumByArtistId = songifyCrudFacade.findAlbumByIdWithArtistsAndSongs(artistId);
        assertThat(albumByArtistId.getArtists()).isNotEmpty();
        assertThat(albumByArtistId.getArtists())
                .extracting(AlbumInfo.ArtistInfo::getId)
                .contains(artistId);
        assertThat(albumByArtistId.getSongs())
                .extracting(AlbumInfo.SongInfo::getId)
                .contains(songId);
    }

}
