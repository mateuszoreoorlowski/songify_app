package spring.songify_app.domain.crud;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import spring.songify_app.domain.crud.dto.*;
import spring.songify_app.domain.crud.exceptions.AlbumNotFoundException;
import spring.songify_app.domain.crud.exceptions.GenreWasNotDeletedException;
import spring.songify_app.domain.crud.exceptions.SongNotFoundException;
import spring.songify_app.infrastructure.crud.album.request.AlbumWithSongsRequestDto;
import spring.songify_app.infrastructure.crud.artist.dto.request.ArtistRequestDto;
import spring.songify_app.infrastructure.crud.genre.request.GenreRequestDto;
import spring.songify_app.infrastructure.crud.song.request.CreateSongRequestDto;

import java.time.Instant;
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

    // Wymaganie 11 - można edytować nazwę gatunku muzycznego
    @Test
    @DisplayName("Should update genre 'Rap' name to 'Hip-Hop' by id")
    public void should_update_genre_rap_name_to_hip_hop_by_id() {
        // given
        GenreRequestDto genre = GenreRequestDto.builder()
                .name("Rap")
                .build();
        Long genreId = songifyCrudFacade.addGenre(genre).id();

        assertThat(songifyCrudFacade.findGenreById(genreId).getName()).isEqualTo("Rap");

        // when
        GenreDto result = songifyCrudFacade.updateGenreNameById(genreId, "Hip-Hop");

        // then
        assertThat(result.name()).isEqualTo("Hip-Hop");
    }

    // Wymaganie 12 - można edytować album (dodawać piosenki, arytstów, zmieniac nazwe albumu)
    // Przykład 1 - Kiedy dodajemy piosenkę do albumu
    @Test
    @DisplayName("Should add song 'Wujek Dobra Rada' to album 'Pocztówka z WWA, lato 2K19' by id")
    public void should_add_song_wujek_dobra_rada_to_album_pocztowka_z_wwa_lato_2k19_by_id() {
        // given
        CreateSongRequestDto song = CreateSongRequestDto.builder()
                .name("Wujek Dobra Rada")
                .language(SongLanguageDto.POLISH)
                .build();
        Long songId = songifyCrudFacade.addSong(song).id();

        AlbumWithSongsRequestDto album = AlbumWithSongsRequestDto.builder()
                .title("Pocztówka z WWA, lato 2K19")
                .build();
        Long albumId = songifyCrudFacade.addAlbumWithSong(album).id();
        assertThat(songifyCrudFacade.findSongsByAlbumId(albumId).isEmpty());

        // when
        songifyCrudFacade.addSongToAlbum(albumId, songId);

        // then
        assertThat(songifyCrudFacade.findSongsByAlbumId(albumId).size()).isEqualTo(1);
        assertThat(songifyCrudFacade.findSongsByAlbumId(albumId).stream().findFirst().get().getName()).isEqualTo("Wujek Dobra Rada");
    }

    // Przykład 2 - Kiedy dodajemy artystę do albumu
    @Test
    @DisplayName("Should add artist 'Pezet' to album 'Pocztówka z WWA, lato 2K19' by id")
    public void should_add_artist_pezet_to_album_pocztowka_z_wwa_lato_2k19_by_id() {
        // given
        ArtistRequestDto artist = ArtistRequestDto.builder()
                .name("Pezet")
                .build();
        Long artistId = songifyCrudFacade.addArtist(artist).id();

        CreateSongRequestDto song = CreateSongRequestDto.builder()
                .name("Wujek Dobra Rada")
                .language(SongLanguageDto.POLISH)
                .build();
        Long songId = songifyCrudFacade.addSong(song).id();

        AlbumWithSongsRequestDto album = AlbumWithSongsRequestDto.builder()
                .title("Pocztówka z WWA, lato 2K19")
                .build();
        Long albumId = songifyCrudFacade.addAlbumWithSong(album).id();
        assertThat(songifyCrudFacade.findArtistsByAlbumId(albumId).isEmpty());

        // when
        songifyCrudFacade.addArtistToAlbum(albumId, artistId);

        // then
        assertThat(songifyCrudFacade.findArtistsByAlbumId(albumId).size()).isEqualTo(1);
        assertThat(songifyCrudFacade.findArtistsByAlbumId(albumId).stream().findFirst().get().getName()).isEqualTo("Pezet");
    }

    // Przykład 3 - Kiedy zmieniamy nazwę albumu
    @Test
    @DisplayName("Should update album 'Pocztówka z WWA, lato 2K19' title to '1-800 Oświecenie' by id")
    public void should_update_album_pocztowka_z_wwa_lato_2k19_title_to_1_800_oswiecenie_by_id() {
        // given
        CreateSongRequestDto song = CreateSongRequestDto.builder()
                .name("Pakiet Platinium")
                .language(SongLanguageDto.POLISH)
                .build();
        Long songId = songifyCrudFacade.addSong(song).id();

        AlbumWithSongsRequestDto album = AlbumWithSongsRequestDto.builder()
                .title("Pocztówka z WWA, lato 2K19")
                .build();
        Long albumId = songifyCrudFacade.addAlbumWithSong(album).id();
        assertThat(songifyCrudFacade.findAlbumById(albumId).getTitle()).isEqualTo("Pocztówka z WWA, lato 2K19");

        // when
        AlbumDto result = songifyCrudFacade.updateAlbumTitle(albumId, "1-800 Oświecenie");

        // then
        assertThat(result.title()).isEqualTo("1-800 Oświecenie");
    }

    // Wymaganie 13 - można edytować piosenkę (czas trwania, artystę, nazwę piosenki)
    @Test
    @DisplayName("Should update song 'Imię' to 'Nametag' by id")
    public void should_update_song_imie_to_nametag_by_id() {
        // given
        CreateSongRequestDto song = CreateSongRequestDto.builder()
                .name("Imię")
                .language(SongLanguageDto.POLISH)
                .build();
        Long songId = songifyCrudFacade.addSong(song).id();

        ArtistRequestDto artist = ArtistRequestDto.builder()
                .name("Taco Hemingway")
                .build();
        Long artistId = songifyCrudFacade.addArtist(artist).id();
        assertThat(songifyCrudFacade.findSongDtoById(songId).name()).isEqualTo("Imię");

        // when
        SongUpdateDto result = songifyCrudFacade.updateSong(new SongUpdateDto(songId, "Nametag", 180L, Instant.now(), SongLanguageDto.POLISH, artistId));

        // then
        assertThat(result.name()).isEqualTo("Nametag");
    }

    // Wymaganie 14 - można przypisać piosenki tylko do albumów
    @Test
    @DisplayName("Should assign song 'Nametag' to album '1-800 Oświecenie' by id")
    public void should_assign_song_nametag_to_album_1_800_oswiecenie_by_id() {
        // given
        CreateSongRequestDto song = CreateSongRequestDto.builder()
                .name("Nametag")
                .language(SongLanguageDto.POLISH)
                .build();
        Long songId = songifyCrudFacade.addSong(song).id();

        AlbumWithSongsRequestDto album = AlbumWithSongsRequestDto.builder()
                .title("1-800 Oświecenie")
                .build();
        Long albumId = songifyCrudFacade.addAlbumWithSong(album).id();
        assertThat(songifyCrudFacade.findSongsByAlbumId(albumId)).isEmpty();
        assertThat(songifyCrudFacade.findAlbumById(albumId).getTitle()).isEqualTo("1-800 Oświecenie");

        // when
        songifyCrudFacade.assignSongToAlbum(songId, albumId);

        // then
        assertThat(songifyCrudFacade.findSongsByAlbumId(albumId).size()).isEqualTo(1);
        assertThat(songifyCrudFacade.findSongsByAlbumId(albumId).stream().findFirst().get().getName()).isEqualTo("Nametag");
    }

    // Wymaganie 15 - można przypisać piosenki do artysty (poprzez album)
    @Test
    @DisplayName("Should assign song 'Nametag' to artist 'Taco Hemingway' by id via album '1-800 Oświecenie'")
    public void should_assign_song_nametag_to_artist_taco_hemingway_by_id_via_album_1_800_oswiecenie() {
        // given
        CreateSongRequestDto song = CreateSongRequestDto.builder()
                .name("Nametag")
                .language(SongLanguageDto.POLISH)
                .build();
        Long songId = songifyCrudFacade.addSong(song).id();

        AlbumWithSongsRequestDto album = AlbumWithSongsRequestDto.builder()
                .title("1-800 Oświecenie")
                .build();
        Long albumId = songifyCrudFacade.addAlbumWithSong(album).id();

        ArtistRequestDto artist = ArtistRequestDto.builder()
                .name("Taco Hemingway")
                .build();
        Long artistId = songifyCrudFacade.addArtist(artist).id();
        assertThat(songifyCrudFacade.findArtistsByAlbumId(albumId).isEmpty());

        // when
        songifyCrudFacade.assignSongToArtist(new SongArtistDto(songId, albumId, artistId));

        // then
        assertThat(songifyCrudFacade.findArtistsByAlbumId(albumId).size()).isEqualTo(1);
        assertThat(songifyCrudFacade.findArtistsByAlbumId(albumId).stream().findFirst().get().getName()).isEqualTo("Taco Hemingway");
    }

    // Wymaganie 16 - można przypisać artystów do albumów (album może mieć więcej artystów, artysta może mieć kilka albumów)
    @Test
    @DisplayName("Should assign artist 'Taco Hemingway' to album '1-800 Oświecenie' and 'Cafe Belga' by id")
    public void should_assign_artist_taco_hemingway_to_album_1_800_oswiecenie_and_cafe_belga_by_id() {
        // given
        ArtistRequestDto artist = ArtistRequestDto.builder()
                .name("Taco Hemingway")
                .build();
        Long artistId = songifyCrudFacade.addArtist(artist).id();

        CreateSongRequestDto song1 = CreateSongRequestDto.builder()
                .name("Nametag")
                .language(SongLanguageDto.POLISH)
                .build();
        songifyCrudFacade.addSong(song1);

        CreateSongRequestDto song2 = CreateSongRequestDto.builder()
                .name("ZTM")
                .language(SongLanguageDto.POLISH)
                .build();
        songifyCrudFacade.addSong(song2);

        AlbumWithSongsRequestDto album1 = AlbumWithSongsRequestDto.builder()
                .title("1-800 Oświecenie")
                .build();
        Long albumId1 = songifyCrudFacade.addAlbumWithSong(album1).id();
        assertThat(songifyCrudFacade.findArtistsByAlbumId(albumId1).isEmpty()).isTrue();

        AlbumWithSongsRequestDto album2 = AlbumWithSongsRequestDto.builder()
                .title("Cafe Belga")
                .build();
        Long albumId2 = songifyCrudFacade.addAlbumWithSong(album2).id();
        assertThat(songifyCrudFacade.findArtistsByAlbumId(albumId2).isEmpty()).isTrue();

        // when
        songifyCrudFacade.addArtistToAlbum(artistId, albumId1);
        songifyCrudFacade.addArtistToAlbum(artistId, albumId2);

        // then
        assertThat(songifyCrudFacade.findArtistsByAlbumId(albumId1).size()).isEqualTo(1);
        assertThat(songifyCrudFacade.findArtistsByAlbumId(albumId1).stream().findFirst().get().getName()).isEqualTo("Taco Hemingway");
        assertThat(songifyCrudFacade.findArtistsByAlbumId(albumId2).size()).isEqualTo(1);
        assertThat(songifyCrudFacade.findArtistsByAlbumId(albumId2).stream().findFirst().get().getName()).isEqualTo("Taco Hemingway");
    }

    // Wymaganie 17 - można przypisać tylko jeden gatunek muzyczny do piosenki
    @Test
    @DisplayName("Should assign genre 'Rap' to song 'Nametag' by id")
    public void should_assign_genre_rap_to_song_nametag_by_id() {
        // given
        GenreRequestDto genre = GenreRequestDto.builder()
                .name("Rap")
                .build();
        Long genreId = songifyCrudFacade.addGenre(genre).id();

        CreateSongRequestDto song = CreateSongRequestDto.builder()
                .name("Nametag")
                .language(SongLanguageDto.POLISH)
                .build();
        Long songId = songifyCrudFacade.addSong(song).id();

        assertThat(songifyCrudFacade.findSongDtoById(songId).name()).isEqualTo("Nametag");
        assertThat(songifyCrudFacade.findGenreById(genreId).getName()).isEqualTo("Rap");

        // when
        songifyCrudFacade.assignGenreToSong(genreId, songId);

        // then
        assertThat(songifyCrudFacade.findGenresBySongId(songId).getName()).isEqualTo("Rap");
    }

    // Wymaganie 18 - gdy nie ma przypisanego gatunku muzycznego do piosenki, to wyświetlamy "default"
    @Test
    @DisplayName("Should return 'default' When there is no genre assigned to song 'Nametag'")
    public void should_return_default_when_there_is_no_genre_assigned_to_song_nametag() {
        // given
        CreateSongRequestDto song = CreateSongRequestDto.builder()
                .name("Nametag")
                .language(SongLanguageDto.POLISH)
                .build();
        Long songId = songifyCrudFacade.addSong(song).id();
        assertThat(songifyCrudFacade.findSongDtoById(songId).name()).isEqualTo("Nametag");
        assertThat(songifyCrudFacade.findAllSongs().size()).isEqualTo(1);

        // when
        String genreNameForSong = songifyCrudFacade.getGenreNameForSong(songId);

        // then
        assertThat(genreNameForSong).isEqualTo("default");
    }

    // Wymaganie 19 - można wyświetlać wszystkie piosenki
    @Test
    @DisplayName("Should return all songs")
    public void should_return_all_songs() {
        // given
        CreateSongRequestDto song1 = CreateSongRequestDto.builder()
                .name("Nametag")
                .language(SongLanguageDto.POLISH)
                .build();
        Long songId1 = songifyCrudFacade.addSong(song1).id();

        CreateSongRequestDto song2 = CreateSongRequestDto.builder()
                .name("ZTM")
                .language(SongLanguageDto.POLISH)
                .build();
        Long songId2 = songifyCrudFacade.addSong(song2).id();

        // when
        Set<SongDto> allSongs = songifyCrudFacade.findAllSongs();

        // then
        assertThat(allSongs.size()).isEqualTo(2);
        assertThat(allSongs)
                .extracting(SongDto::id)
                .containsExactlyInAnyOrder(songId1, songId2);
    }

    // Wymaganie 20 - można wyświetlać wszystkie gatunki
    @Test
    @DisplayName("Should return all genres")
    public void should_return_all_genres() {
        // given
        GenreDto genre1 = GenreDto.builder()
                .name("Rap")
                .build();
        Long genreId1 = songifyCrudFacade.addGenre(new GenreRequestDto(1L, genre1.name())).id();

        GenreDto genre2 = GenreDto.builder()
                .name("Pop")
                .build();
        Long genreId2 = songifyCrudFacade.addGenre(new GenreRequestDto(2L, genre1.name())).id();

        // when
        Set<GenreDto> allGenres = songifyCrudFacade.findAllGenres();

        // then
        assertThat(allGenres.size()).isEqualTo(3);
        assertThat(allGenres)
                .extracting(GenreDto::id)
                .containsExactlyInAnyOrder(0L, genreId1, genreId2);
    }

    // Wymaganie 21 - można wyświetlać wszystkich artystów
    @Test
    @DisplayName("Should return all artists")
    public void should_return_all_artists() {
        // given
        ArtistRequestDto artist1 = ArtistRequestDto.builder()
                .name("Taco Hemingway")
                .build();
        Long artistId1 = songifyCrudFacade.addArtist(artist1).id();

        ArtistRequestDto artist2 = ArtistRequestDto.builder()
                .name("Dawid Podsiadło")
                .build();
        Long artistId2 = songifyCrudFacade.addArtist(artist2).id();

        // when
        Set<ArtistDto> allArtists = songifyCrudFacade.findAllArtists();

        // then
        assertThat(allArtists.size()).isEqualTo(2);
        assertThat(allArtists)
                .extracting(ArtistDto::id)
                .containsExactlyInAnyOrder(artistId1, artistId2);
    }

    // Wymaganie 22 - można wyświetlać wszystkie albumy
    @Test
    @DisplayName("Should return all albums")
    public void should_return_all_albums() {
        // given
        CreateSongRequestDto song1 = CreateSongRequestDto.builder()
                .name("Nametag")
                .language(SongLanguageDto.POLISH)
                .build();
        Long songId1 = songifyCrudFacade.addSong(song1).id();

        CreateSongRequestDto song2 = CreateSongRequestDto.builder()
                .name("ZTM")
                .language(SongLanguageDto.POLISH)
                .build();
        Long songId2 = songifyCrudFacade.addSong(song2).id();

        AlbumWithSongsRequestDto album1 = AlbumWithSongsRequestDto.builder()
                .title("1-800 Oświecenie")
                .build();
        Long albumId1 = songifyCrudFacade.addAlbumWithSong(album1).id();

        AlbumWithSongsRequestDto album2 = AlbumWithSongsRequestDto.builder()
                .title("Cafe Belga")
                .build();
        Long albumId2 = songifyCrudFacade.addAlbumWithSong(album2).id();

        // when
        Set<AlbumDto> allAlbums = songifyCrudFacade.findAllAlbums();

        // then
        assertThat(allAlbums.size()).isEqualTo(2);
        assertThat(allAlbums)
                .extracting(AlbumDto::id)
                .containsExactlyInAnyOrder(albumId1, albumId2);
    }

    // Wymaganie 23 - można wyświetlać konkretne albumy z artystami oraz piosenkami w albumie
    @Test
    @DisplayName("Should return album '1-800 Oświecenie' with artist 'Taco Hemingway' and song 'Nametag' and 'Pakiet Platinium' by id")
    public void should_return_album_1_800_oswiecenie_with_artist_taco_hemingway_and_song_nametag_and_pakiet_platinium_by_id() {
        // given
        ArtistRequestDto artist = ArtistRequestDto.builder()
                .name("Taco Hemingway")
                .build();
        Long artistId = songifyCrudFacade.addArtist(artist).id();

        CreateSongRequestDto song1 = CreateSongRequestDto.builder()
                .name("Nametag")
                .language(SongLanguageDto.POLISH)
                .build();
        Long songId1 = songifyCrudFacade.addSong(song1).id();

        CreateSongRequestDto song2 = CreateSongRequestDto.builder()
                .name("Pakiet Platinium")
                .language(SongLanguageDto.POLISH)
                .build();
        Long songId2 = songifyCrudFacade.addSong(song2).id();

        AlbumWithSongsRequestDto album = AlbumWithSongsRequestDto.builder()
                .title("1-800 Oświecenie")
                .build();
        Long albumId = songifyCrudFacade.addAlbumWithSong(album).id();

        songifyCrudFacade.addSongToAlbum(albumId, songId1);
        songifyCrudFacade.addSongToAlbum(albumId, songId2);
        songifyCrudFacade.addArtistToAlbum(artistId, albumId);

        assertThat(songifyCrudFacade.findAlbumsByArtistId(artistId).size()).isEqualTo(1);
        assertThat(songifyCrudFacade.findSongsByAlbumId(albumId).size()).isEqualTo(2);
        assertThat(songifyCrudFacade.findArtistsByAlbumId(albumId).stream().findFirst().get().getName()).isEqualTo("Taco Hemingway");

        // when
        AlbumInfo albumInfo = songifyCrudFacade.findAlbumByIdWithArtistsAndSongs(albumId);

        // then
        assertThat(albumInfo).isNotNull();
        assertThat(albumInfo.getTitle()).isEqualTo("1-800 Oświecenie");
        assertThat(albumInfo.getArtists()).hasSize(1);
        assertThat(albumInfo.getArtists().stream().anyMatch(artistDto -> artistDto.getName().equals("Taco Hemingway"))).isTrue();
        assertThat(albumInfo.getSongs()).hasSize(2);
        assertThat(albumInfo.getSongs().stream().anyMatch(song -> song.getName().equals("Nametag"))).isTrue();
        assertThat(albumInfo.getSongs().stream().anyMatch(song -> song.getName().equals("Pakiet Platinium"))).isTrue();
    }

    // Wymaganie 24 - można wyświetlać konkretne gatunki muzyczne wraz z piosenkami
    @Test
    @DisplayName("Should return genre 'Rap' with song 'Nametag' and 'Pakiet Platinium' by id")
    public void should_return_genre_rap_with_song_nametag_and_pakiet_platinium_by_id() {
        // given
        GenreRequestDto genre = GenreRequestDto.builder()
                .name("Rap")
                .build();
        Long genreId = songifyCrudFacade.addGenre(genre).id();

        CreateSongRequestDto song1 = CreateSongRequestDto.builder()
                .name("Nametag")
                .language(SongLanguageDto.POLISH)
                .build();
        Long songId1 = songifyCrudFacade.addSong(song1).id();

        CreateSongRequestDto song2 = CreateSongRequestDto.builder()
                .name("Pakiet Platinium")
                .language(SongLanguageDto.POLISH)
                .build();
        Long songId2 = songifyCrudFacade.addSong(song2).id();

        songifyCrudFacade.assignGenreToSong(genreId, songId1);
        songifyCrudFacade.assignGenreToSong(genreId, songId2);

        assertThat(songifyCrudFacade.findSongsByGenreId(genreId).size()).isEqualTo(2);

        // when
        GenreWithSongsDto genreInfo = songifyCrudFacade.getGenreWithSongs(genreId);

        // then
        assertThat(genreInfo).isNotNull();
        assertThat(genreInfo.name()).isEqualTo("Rap");
        assertThat(genreInfo.songs()).hasSize(2);
        assertThat(genreInfo.songs().stream().anyMatch(song -> song.name().equals("Nametag"))).isTrue();
        assertThat(genreInfo.songs().stream().anyMatch(song -> song.name().equals("Pakiet Platinium"))).isTrue();
    }

    // Wymaganie 25 - można wyświetlać konkretnych artystów wraz z ich albumami
    @Test
    @DisplayName("Should return artist 'Taco Hemingway' with album '1-800 Oświecenie' and 'Cafe Belga' by id")
    public void should_return_artist_taco_hemingway_with_album_1_800_oswiecenie_and_cafe_belga_by_id() {
        // given
        ArtistRequestDto artist = ArtistRequestDto.builder()
                .name("Taco Hemingway")
                .build();
        Long artistId = songifyCrudFacade.addArtist(artist).id();

        CreateSongRequestDto song1 = CreateSongRequestDto.builder()
                .name("Nametag")
                .language(SongLanguageDto.POLISH)
                .build();
        Long songId1 = songifyCrudFacade.addSong(song1).id();

        CreateSongRequestDto song2 = CreateSongRequestDto.builder()
                .name("ZTM")
                .language(SongLanguageDto.POLISH)
                .build();
        Long songId2 = songifyCrudFacade.addSong(song2).id();

        AlbumWithSongsRequestDto album1 = AlbumWithSongsRequestDto.builder()
                .title("1-800 Oświecenie")
                .build();
        Long albumId1 = songifyCrudFacade.addAlbumWithSong(album1).id();

        AlbumWithSongsRequestDto album2 = AlbumWithSongsRequestDto.builder()
                .title("Cafe Belga")
                .build();
        Long albumId2 = songifyCrudFacade.addAlbumWithSong(album2).id();

        songifyCrudFacade.addArtistToAlbum(artistId, albumId1);
        songifyCrudFacade.addArtistToAlbum(artistId, albumId2);

        assertThat(songifyCrudFacade.findAlbumsByArtistId(artistId).size()).isEqualTo(2);

        // when
        ArtistWithAlbumsDto artistInfo = songifyCrudFacade.retrieveArtistWithAlbums(artistId);

        // then
        assertThat(artistInfo).isNotNull();
        assertThat(artistInfo.name()).isEqualTo("Taco Hemingway");
        assertThat(artistInfo.albums()).hasSize(2);
        assertThat(artistInfo.albums().stream().anyMatch(album -> album.title().equals("1-800 Oświecenie"))).isTrue();
        assertThat(artistInfo.albums().stream().anyMatch(album -> album.title().equals("Cafe Belga"))).isTrue();
    }
}
