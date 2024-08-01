package spring.songify_app.domain.crud;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.songify_app.domain.crud.dto.*;
import spring.songify_app.infrastructure.crud.album.request.AlbumWithSongsRequestDto;
import spring.songify_app.infrastructure.crud.song.request.CreateSongRequestDto;
import spring.songify_app.infrastructure.crud.artist.dto.request.ArtistRequestDto;
import spring.songify_app.infrastructure.crud.genre.request.GenreRequestDto;

import java.util.Set;

@Service
@AllArgsConstructor
@Transactional
public class SongifyCrudFacade {

    private final ArtistAdder artistAdder;
    private final GenreAdder genreAdder;
    private final SongAdder songAdder;
    private final AlbumAdder albumAdder;
    private final ArtistRetriever artistRetriever;
    private final GenreRetriever genreRetriever;
    private final SongRetriever songRetriever;
    private final AlbumRetriever albumRetriever;
    private final ArtistUpdater artistUpdater;
    private final GenreUpdater genreUpdater;
    private final AlbumUpdater albumUpdater;
    private final SongUpdater songUpdater;
    private final SongAssigner songAssigner;
    private final ArtistAssigner artistAssigner;
    private final GenreAssigner genreAssigner;

    public ArtistDto addArtist(ArtistRequestDto artist) {
        return artistAdder.addArtist(artist.name());
    }

    public Set<ArtistDto> findAllArtists() {
        return artistRetriever.findAllArtists();
    }

    public GenreDto addGenre(GenreRequestDto dto) {
        return genreAdder.addGenre(dto.name());
    }

    public Set<GenreDto> findAllGenres() {
        return genreRetriever.findAllGenres();
    }

    public SongDto addSong(CreateSongRequestDto songDto) {
        return songAdder.addSong(songDto);
    }

    public Set<SongDto> findAllSongs() {
        return songRetriever.findAll();
    }

    public AlbumWithSongsDto addAlbumWithSong(AlbumWithSongsRequestDto albumWithSongsRequestDto) {
        return albumAdder.addAlbumWithSongs(albumWithSongsRequestDto.title(), albumWithSongsRequestDto.releaseDate(), albumWithSongsRequestDto.songsIds());
    }

    public Set<AlbumDto> findAllAlbums() {
        return albumRetriever.findAllAlbums();
    }

    public ArtistDto addArtistWithDefaultAlbumAndSong(ArtistRequestDto dto){
        return artistAdder.addArtistWithDefaultAlbumAndSong(dto);
    }

    public ArtistDto updateArtistNameById(Long artistId, String name){
        return artistUpdater.updateArtistNameById(artistId, name);
    }

    public GenreDto updateGenreNameById(Long genreId, String name) {
        return genreUpdater.updateGenreNameById(genreId, name);
    }

    public AlbumDto updateAlbumTitle(final Long albumId, final String title) {
        return albumUpdater.updateAlbumTitle(albumId, title);
    }

    public AlbumDto addNewArtistToAlbum(Long albumId, Long artistId){
        return albumAdder.addArtistToAlbum(albumId, artistId);
    }

    public AlbumDto addSongToAlbum(Long albumId, Long songId){
        return albumAdder.addSongToAlbum(albumId, songId);
    }

    public SongUpdateDto updateSong(SongUpdateDto dto){
        return songUpdater.updateSong(dto);
    }

    public SongAlbumDto assignSongToAlbum(Long songId, Long albumId){
        return songAssigner.assignSongToAlbum(songId, albumId);
    }

    public SongArtistDto assignSongToArtist(SongArtistDto dto){
        return songAssigner.assignSongToArtist(dto);
    }

    public ArtistAlbumDto assignArtistToAlbum(Long artistId, Long albumId){
        return artistAssigner.assignArtistToAlbum(artistId, albumId);
    }

    public void assignGenreToSong(Long genreId, Long songId) {
        genreAssigner.assignGenreToSong(genreId, songId);
    }

    public AlbumInfo findAlbumByIdWithArtistsAndSongs(Long id){
        return albumRetriever.findAlbumByIdWithArtistsAndSongs(id);
    }

    public GenreWithSongsDto retrieveGenreWithSongs(Long genreId) {
        return genreRetriever.retrieveGenreWithSongs(genreId);
    }

    public ArtistWithAlbumsDto retrieveArtistWithAlbums(Long artistId) {
        return artistRetriever.retrieveArtistWithAlbums(artistId);
    }
}