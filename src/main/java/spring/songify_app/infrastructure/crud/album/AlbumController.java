package spring.songify_app.infrastructure.crud.album;

import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.songify_app.domain.crud.SongifyCrudFacade;
import spring.songify_app.domain.crud.dto.AlbumDto;
import spring.songify_app.domain.crud.dto.AlbumInfo;
import spring.songify_app.domain.crud.dto.AlbumWithSongsDto;
import spring.songify_app.infrastructure.crud.album.request.AlbumWithSongsRequestDto;
import spring.songify_app.infrastructure.crud.album.response.AlbumWithSongsResponseDto;
import spring.songify_app.infrastructure.crud.album.response.AllAlbumsResponseDto;

import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("/albums")
class AlbumController {

    private final SongifyCrudFacade songifyCrudFacade;

    @GetMapping
    ResponseEntity<AllAlbumsResponseDto> getAllAlbums() {
        Set<AlbumDto> allAlbums = songifyCrudFacade.findAllAlbums();
        AllAlbumsResponseDto response = new AllAlbumsResponseDto(allAlbums);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    ResponseEntity<AlbumWithSongsResponseDto> addAlbumWithSongs(@RequestBody AlbumWithSongsRequestDto albumRequestDto) {
        AlbumWithSongsDto albumDto = songifyCrudFacade.addAlbumWithSong(albumRequestDto);
        return ResponseEntity.ok(new AlbumWithSongsResponseDto(albumDto.id(), albumDto.title(), albumDto.releaseDate(), albumDto.songsIds()));
    }

    @PutMapping("/{id}/name")
    public ResponseEntity<AlbumDto> updateAlbumTitle(@PathVariable Long id, @PathVariable String newName) {
        AlbumDto updatedAlbum = songifyCrudFacade.updateAlbumTitle(id, newName);
        return ResponseEntity.ok(updatedAlbum);
    }

    @PutMapping("/{albumId}/artists/{artistId}")
    public ResponseEntity<AlbumDto> addArtistToAlbum(@PathVariable Long albumId, @PathVariable Long artistId) {
        AlbumDto updatedAlbum = songifyCrudFacade.addNewArtistToAlbum(albumId, artistId);
        return ResponseEntity.ok(updatedAlbum);
    }

    @PutMapping("/{albumId}/songs/{songId}")
    public ResponseEntity<AlbumDto> addSongToAlbum(@PathVariable Long albumId, @PathVariable Long songId) {
        AlbumDto updatedAlbum = songifyCrudFacade.addSongToAlbum(albumId, songId);
        return ResponseEntity.ok(updatedAlbum);
    }

    @GetMapping("/{albumId}")
    ResponseEntity<AlbumInfo> getAlbumWithArtistsAndSongs(@PathVariable Long albumId) {
        AlbumInfo albumByIdWithArtistsAndSongs = songifyCrudFacade.findAlbumByIdWithArtistsAndSongs(albumId);
        return ResponseEntity.ok(albumByIdWithArtistsAndSongs);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAlbum(@PathVariable Long id) {
        songifyCrudFacade.deleteAlbumById(id);
        return ResponseEntity.ok("probably album deleted :)");
    }
}
