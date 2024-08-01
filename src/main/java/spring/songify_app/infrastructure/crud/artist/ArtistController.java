package spring.songify_app.infrastructure.crud.artist;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.songify_app.domain.crud.SongifyCrudFacade;
import spring.songify_app.domain.crud.dto.ArtistDto;
import spring.songify_app.infrastructure.crud.artist.dto.request.ArtistRequestDto;
import spring.songify_app.infrastructure.crud.artist.dto.response.ArtistResponseDto;

@RestController
@AllArgsConstructor
@RequestMapping("/artists")
class ArtistController {

    private final SongifyCrudFacade songifyCrudFacade;

    @PostMapping
    ResponseEntity<ArtistResponseDto> addArtist(@RequestBody ArtistRequestDto artistRequestDto) {
        ArtistDto artistDto = songifyCrudFacade.addArtist(artistRequestDto);
        return ResponseEntity.ok(new ArtistResponseDto(artistDto.name()));
    }
}
