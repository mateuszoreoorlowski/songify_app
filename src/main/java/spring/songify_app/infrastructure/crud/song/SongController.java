package spring.songify_app.infrastructure.crud.song;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.songify_app.domain.crud.SongifyCrudFacade;
import spring.songify_app.domain.crud.dto.SongAlbumDto;
import spring.songify_app.domain.crud.dto.SongDto;
import spring.songify_app.domain.crud.dto.SongUpdateDto;
import spring.songify_app.infrastructure.crud.song.request.CreateSongRequestDto;
import spring.songify_app.infrastructure.crud.song.response.AllSongsResponseDto;
import spring.songify_app.infrastructure.crud.song.response.CreateSongResponseDto;

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
}
