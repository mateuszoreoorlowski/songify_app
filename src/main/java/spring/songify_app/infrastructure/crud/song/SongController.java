package spring.songify_app.infrastructure.crud.song;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.songify_app.domain.crud.SongifyCrudFacade;
import spring.songify_app.domain.crud.dto.SongDto;
import spring.songify_app.infrastructure.crud.song.request.CreateSongRequestDto;
import spring.songify_app.infrastructure.crud.song.response.CreateSongResponseDto;

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
}
