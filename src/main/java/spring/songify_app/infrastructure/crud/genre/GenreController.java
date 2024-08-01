package spring.songify_app.infrastructure.crud.genre;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.songify_app.domain.crud.SongifyCrudFacade;
import spring.songify_app.domain.crud.dto.GenreDto;
import spring.songify_app.infrastructure.crud.genre.request.GenreRequestDto;
import spring.songify_app.infrastructure.crud.genre.response.GenreResponseDto;

@RestController
@AllArgsConstructor
@RequestMapping("/genres")
class GenreController {

    private final SongifyCrudFacade songifyCrudFacade;

    @PostMapping
    ResponseEntity<GenreResponseDto> addGenre(@RequestBody GenreRequestDto genreRequestDto) {
        GenreDto genreDto = songifyCrudFacade.addGenre(genreRequestDto);
        return ResponseEntity.ok(new GenreResponseDto(genreDto.name()));
    }

}
