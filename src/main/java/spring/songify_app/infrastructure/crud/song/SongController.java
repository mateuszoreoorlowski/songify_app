package spring.songify_app.infrastructure.crud.song;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.songify_app.domain.crud.SongifyCrudFacade;
import spring.songify_app.domain.crud.dto.*;
import spring.songify_app.infrastructure.crud.song.request.CreateSongRequestDto;
import spring.songify_app.infrastructure.crud.song.response.AllSongsResponseDto;
import spring.songify_app.infrastructure.crud.song.response.CreateSongResponseDto;
import spring.songify_app.infrastructure.crud.song.response.GetSongResponseDto;

import java.util.Set;

@RestController
@RequestMapping("/songs")
@AllArgsConstructor
class SongController {

    private final SongifyCrudFacade songifyCrudFacade;

    @PostMapping
    ResponseEntity<CreateSongResponseDto> postSong(@RequestBody @Valid CreateSongRequestDto request) {
        SongDto savedSong = songifyCrudFacade.addSong(request);
        CreateSongResponseDto body = new CreateSongResponseDto(savedSong.id(), savedSong.name(), savedSong.releaseDate(), savedSong.duration(), savedSong.language());
        return ResponseEntity.ok(body);
    }

    @GetMapping
    ResponseEntity<AllSongsResponseDto> getAllSongs() {
        Set<SongDto> allSongs = songifyCrudFacade.findAllSongs();
        AllSongsResponseDto response = new AllSongsResponseDto(allSongs);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SongUpdateDto> updateSong(@RequestBody SongUpdateDto songDto) {
        SongUpdateDto updatedSong = songifyCrudFacade.updateSong(
                new SongUpdateDto(
                        songDto.id(),
                        songDto.name(),
                        songDto.duration(),
                        songDto.releaseDate(),
                        songDto.language(),
                        songDto.artistId()));
        return ResponseEntity.ok(updatedSong);
    }

    @PutMapping("/{songId}/album/{albumId}")
    public ResponseEntity<SongAlbumDto> assignSongToAlbum(@PathVariable Long songId, @PathVariable Long albumId) {
        SongAlbumDto songAlbumDto = songifyCrudFacade.assignSongToAlbum(songId, albumId);
        return ResponseEntity.ok(songAlbumDto);
    }

    @PutMapping("/{albumId}/song/{songId}/artist/{artistId}")
    public ResponseEntity<SongArtistDto> assignSongToArtist(@PathVariable Long songId, @PathVariable Long albumId, @PathVariable Long artistId) {
        SongArtistDto songArtistDto = songifyCrudFacade.assignSongToArtist(new SongArtistDto(songId, albumId, artistId));
        return ResponseEntity.ok(songArtistDto);
    }

    @PutMapping("/{songId}/genres/{genreId}")
    ResponseEntity<String> assignGenreToSong(
            @PathVariable Long songId,
            @PathVariable Long genreId) {
        songifyCrudFacade.assignGenreToSong(genreId, songId);
        return ResponseEntity.ok("updated");
    }

    @DeleteMapping("/{songId}/album/{albumId}")
    public ResponseEntity<String> deleteSong(@PathVariable Long songId, @PathVariable Long albumId) {
        songifyCrudFacade.deleteSongFromAlbumById(songId, albumId);
        return ResponseEntity.ok("probably song deleted :)");
    }

    @GetMapping("/{id}")
    ResponseEntity<GetSongResponseDto> getSongById(@PathVariable Long id, @RequestHeader(required = false) String requestId) {
        SongDto song = songifyCrudFacade.findSongDtoById(id);
        GenreDto genre = songifyCrudFacade.findGenreBySongId(id);
        GetSongResponseDto response = new GetSongResponseDto(SongDto.builder()
                .id(song.id())
                .name(song.name())
                .duration(song.duration())
                .releaseDate(song.releaseDate())
                .language(song.language())
                .build(), genre);
        return ResponseEntity.ok(response);
    }
}
