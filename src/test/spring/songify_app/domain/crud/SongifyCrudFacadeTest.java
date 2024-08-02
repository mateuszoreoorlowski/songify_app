package spring.songify_app.domain.crud;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import spring.songify_app.domain.crud.dto.*;
import spring.songify_app.domain.crud.exceptions.AlbumNotFoundException;
import spring.songify_app.domain.crud.exceptions.GenreWasNotDeletedException;
import spring.songify_app.domain.crud.exceptions.SongNotFoundException;
import spring.songify_app.infrastructure.crud.album.request.AlbumWithSongsRequestDto;
import spring.songify_app.infrastructure.crud.album.response.AlbumWithSongsResponseDto;
import spring.songify_app.infrastructure.crud.artist.dto.request.ArtistRequestDto;
import spring.songify_app.infrastructure.crud.genre.request.GenreRequestDto;
import spring.songify_app.infrastructure.crud.song.request.CreateSongRequestDto;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
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

    // Wymaganie 6 - można usunąć artystę (usuwamy wtedy jego piosenki oraz albumy, ale jesli było więcej niż jeden artysta w albumie, to usuwamy tylko artystę z albumu i samego artystę)
    // Przykład 1 - Kiedy artysta nie ma żadnego albumu
    @Test
    @DisplayName("Should delete artist 'Taco Hemingway' by id When he has no albums")
    public void should_delete_artist_taco_hemingway_by_id_when_he_has_no_albums() {
        // given
        ArtistRequestDto artist = ArtistRequestDto.builder()
                .name("Taco Hemingway")
                .build();
        Long artistId = songifyCrudFacade.addArtist(artist).id();
        assertThat(songifyCrudFacade.findAllArtists().isEmpty());

        // when
        songifyCrudFacade.deleteArtistByIdWithAlbumsAndSongs(artistId);

        // then
        assertThat(songifyCrudFacade.findAllArtists().isEmpty());
    }

    // Przykład 2 - Kiedy artysta ma jeden album, ale nie ma innych artystów w albumie
    @Test
    @DisplayName("Should delete artist 'Taco Hemingway' by id When he has one album and he was only artists in album")
    public void should_delete_artist_taco_hemingway_by_id_when_he_has_one_album_and_he_was_only_artists_in_album() {
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
                .songsIds(Set.of(songId))
                .build();
        Long albumId = songifyCrudFacade.addAlbumWithSong(album).id();

        songifyCrudFacade.addArtistToAlbum(artistId, albumId);
        assertThat(songifyCrudFacade.findAlbumsByArtistId(artistId).isEmpty());
        assertThat(songifyCrudFacade.countArtistsByAlbumId(albumId)).isEqualTo(1);

        // when
        songifyCrudFacade.deleteArtistByIdWithAlbumsAndSongs(artistId);

        // then
        assertThat(songifyCrudFacade.findAllArtists()).isEmpty();
        Throwable throwable = catchThrowable(() -> songifyCrudFacade.findSongDtoById(songId));
        assertThat(throwable).isInstanceOf(SongNotFoundException.class);
        assertThat(throwable.getMessage()).isEqualTo("Song with id: " + songId + " not found");
        Throwable throwable2 = catchThrowable(() -> songifyCrudFacade.findAlbumDtoById(albumId));
        assertThat(throwable2).isInstanceOf(AlbumNotFoundException.class);
        assertThat(throwable2.getMessage()).isEqualTo("Album with id: " + albumId + " not found");
    }

    // Przykład 3 - Kiedy artysta ma jeden album, ale ma innych artystów w albumie
    @Test
    @DisplayName("Should delete artist 'Taco Hemingway' by id When he has one album and he was not only artists in album")
    public void should_delete_artist_taco_hemingway_by_id_when_he_has_one_album_and_he_was_not_only_artists_in_album() {
        // given
        ArtistRequestDto artist1 = ArtistRequestDto.builder()
                .name("Taco Hemingway")
                .build();
        Long artistId1 = songifyCrudFacade.addArtist(artist1).id();

        ArtistRequestDto artist2 = ArtistRequestDto.builder()
                .name("Dawid Podsiadło")
                .build();
        Long artistId2 = songifyCrudFacade.addArtist(artist2).id();

        CreateSongRequestDto song = CreateSongRequestDto.builder()
                .name("W piątki leżę w wannie")
                .language(SongLanguageDto.POLISH)
                .build();
        Long songId = songifyCrudFacade.addSong(song).id();

        AlbumWithSongsRequestDto album = AlbumWithSongsRequestDto.builder()
                .title("Pocztówka z WWA, lato 2K19")
                .songsIds(Set.of(songId))
                .build();
        Long albumId = songifyCrudFacade.addAlbumWithSong(album).id();

        songifyCrudFacade.addArtistToAlbum(artistId1, albumId);
        songifyCrudFacade.addArtistToAlbum(artistId2, albumId);

        assertThat(songifyCrudFacade.findAlbumsByArtistId(artistId1)).isNotEmpty();
        assertThat(songifyCrudFacade.countArtistsByAlbumId(albumId)).isEqualTo(2);

        // when
        songifyCrudFacade.deleteArtistByIdWithAlbumsAndSongs(artistId1);

        // then
        assertThat(songifyCrudFacade.findAllArtists().isEmpty()).isFalse();
        assertThat(songifyCrudFacade.countArtistsByAlbumId(albumId)).isEqualTo(1);
        assertThat(songifyCrudFacade.findAlbumsByArtistId(artistId1).isEmpty()).isTrue();
        assertThat(songifyCrudFacade.findAlbumsByArtistId(artistId2).isEmpty()).isFalse();
    }

    // Wymaganie 7 - można usunąć gatunek muzyczny (ale nie może istnieć piosenka z takim gatunkiem)
    // Przykład 1 - Kiedy nie ma piosenek z gatunkiem 'Rap'
    @Test
    @DisplayName("Should delete genre 'Rap' by id When there is no song with this genre")
    public void should_delete_genre_rap_by_id_when_there_is_no_song_with_this_genre() {
        // given
        GenreRequestDto genre = GenreRequestDto.builder()
                .name("Rap")
                .build();

        assertThat(songifyCrudFacade.findAllGenres()).size().isEqualTo(1);
        Long genreId = songifyCrudFacade.addGenre(genre).id();
        assertThat(songifyCrudFacade.findAllGenres()).size().isEqualTo(2);
        assertThat(!songifyCrudFacade.findAllGenres().isEmpty());

        // when
        songifyCrudFacade.deleteGenre(genreId);

        // then
        assertThat(!songifyCrudFacade.findAllGenres().isEmpty()).isTrue();
        assertThat(songifyCrudFacade.findAllGenres().size()).isEqualTo(1);
    }

    // Przykład 2 - Kiedy istnieją piosenki z gatunkiem 'Rap'
    @Test
    @DisplayName("Should throw exception When there is song with genre 'Rap'")
    public void should_throw_exception_when_there_is_song_with_genre_rap() {
        // given
        GenreRequestDto genre = GenreRequestDto.builder()
                .name("Rap")
                .build();
        Long genreId = songifyCrudFacade.addGenre(genre).id();

        CreateSongRequestDto song = CreateSongRequestDto.builder()
                .name("Tamagotchi")
                .language(SongLanguageDto.POLISH)
                .build();
        Long songId = songifyCrudFacade.addSong(song).id();

        songifyCrudFacade.assignGenreToSong(genreId, songId);

        // when
        Throwable throwable = catchThrowable(() -> songifyCrudFacade.deleteGenre(genreId));

        // then
        assertThat(songifyCrudFacade.findAllGenres().size()).isEqualTo(2);
        assertThat(throwable).isInstanceOf(GenreWasNotDeletedException.class);
        assertThat(throwable.getMessage()).isEqualTo("genre not deleted, there are songs related to it");
    }

    // Wymaganie 8 - można usunąć album (ale dopiero wtedy kiedy nie ma już żadnej piosenki przypisanej do albumu)
    @Test
    @DisplayName("Should delete album 'Marmur' by id When there is no song in album")
    public void should_delete_album_marmur_by_id_when_there_is_no_song_in_album() {
        // given
        CreateSongRequestDto song = CreateSongRequestDto.builder()
                .name("Tamagotchi")
                .language(SongLanguageDto.POLISH)
                .build();
        Long songId = songifyCrudFacade.addSong(song).id();

        AlbumWithSongsRequestDto album = AlbumWithSongsRequestDto.builder()
                .title("Pocztówka z WWA, lato 2K19")
                .build();
        Long albumId = songifyCrudFacade.addAlbumWithSong(album).id();

        assertThat(songifyCrudFacade.findAllAlbums().size()).isEqualTo(1);
        assertThat(songifyCrudFacade.findAllSongs().size()).isEqualTo(1);

        // when
        songifyCrudFacade.deleteSongFromAlbumById(songId, albumId);
        if (songifyCrudFacade.findSongsByAlbumId(albumId).isEmpty()) {
            songifyCrudFacade.deleteAlbumById(albumId);
        }

        // then
        assertThat(songifyCrudFacade.findSongsByAlbumId(albumId).isEmpty()).isTrue();
        assertThat(songifyCrudFacade.findAllSongs().size()).isEqualTo(0);
        Throwable throwable = catchThrowable(() -> songifyCrudFacade.findAlbumById(albumId));
        assertThat(throwable).isInstanceOf(AlbumNotFoundException.class);
        assertThat(throwable.getMessage()).isEqualTo("Album with id: " + albumId + " not found");
    }

    // Wymaganie 9 - można usunąć piosenkę, ale nie usuwamy albumu i artystów gdy była tylko 1 piosenka w albumie
    @Test
    @DisplayName("Should delete song 'Tamagotchi' by id When there is only one song in album and not delete album or artists")
    public void should_delete_song_tamagotchi_by_id_when_there_is_only_one_song_in_album_and_not_delete_album_or_artists() {
        // given
        CreateSongRequestDto song = CreateSongRequestDto.builder()
                .name("Tamagotchi")
                .language(SongLanguageDto.POLISH)
                .build();
        Long songId = songifyCrudFacade.addSong(song).id();

        AlbumWithSongsRequestDto album = AlbumWithSongsRequestDto.builder()
                .title("Pocztówka z WWA, lato 2K19")
                .build();
        Long albumId = songifyCrudFacade.addAlbumWithSong(album).id();

        ArtistRequestDto artist = ArtistRequestDto.builder()
                .name("Taco Hemingway")
                .build();
        Long artistId = songifyCrudFacade.addArtist(artist).id();
        songifyCrudFacade.addArtistToAlbum(artistId, albumId);

        assertThat(songifyCrudFacade.findAllAlbums().size()).isEqualTo(1);
        assertThat(songifyCrudFacade.findAllSongs().size()).isEqualTo(1);
        assertThat(songifyCrudFacade.findArtistsByAlbumId(albumId).size()).isEqualTo(1);

        // when
        songifyCrudFacade.deleteSongFromAlbumById(songId, albumId);

        // then
        assertThat(songifyCrudFacade.findSongsByAlbumId(albumId).isEmpty()).isTrue();
        assertThat(songifyCrudFacade.findAllSongs().isEmpty()).isTrue();
        assertThat(songifyCrudFacade.findAlbumById(albumId)).isNotNull();
        assertThat(songifyCrudFacade.findArtistsByAlbumId(albumId).size()).isEqualTo(1);
    }

    // Wymaganie 10 - można edytować nazwę artysty
    @Test
    @DisplayName("Should update artist 'Taco Hemingway' name to 'Kękę' by id")
    public void should_update_artist_taco_hemingway_name_to_keke_by_id() {
        // given
        ArtistRequestDto artist = ArtistRequestDto.builder()
                .name("Taco Hemingway")
                .build();
        Long artistId = songifyCrudFacade.addArtist(artist).id();

        assertThat(songifyCrudFacade.findArtistById(artistId).getName()).isEqualTo("Taco Hemingway");

        // when
        ArtistDto result = songifyCrudFacade.updateArtistNameById(artistId, "Kękę");

        // then
        assertThat(result.name()).isEqualTo("Kękę");
    }

}
