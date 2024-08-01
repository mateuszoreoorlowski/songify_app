package spring.songify_app.infrastructure.crud.album;

import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.songify_app.domain.crud.SongifyCrudFacade;
import spring.songify_app.domain.crud.dto.AlbumDto;
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
        return ResponseEntity.ok(new AlbumWithSongsResponseDto(albumDto.title(), albumDto.releaseDate(), albumDto.songsIds()));
    }
}
