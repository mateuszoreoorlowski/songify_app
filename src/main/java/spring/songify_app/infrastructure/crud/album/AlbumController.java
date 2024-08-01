package spring.songify_app.infrastructure.crud.album;

import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.songify_app.domain.crud.SongifyCrudFacade;
import spring.songify_app.domain.crud.dto.AlbumDto;
import spring.songify_app.infrastructure.crud.album.response.AllAlbumsResponseDto;

import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("/albums")
class AlbumController {

    private final SongifyCrudFacade songifyCrudFacade;

    @GetMapping
    ResponseEntity<AllAlbumsResponseDto> getAllAlbums(@PageableDefault(page = 0, size = 10) Pageable pageable) {
        Set<AlbumDto> allAlbums = songifyCrudFacade.findAllAlbums(pageable);
        AllAlbumsResponseDto response = new AllAlbumsResponseDto(allAlbums);
        return ResponseEntity.ok(response);
    }
}
