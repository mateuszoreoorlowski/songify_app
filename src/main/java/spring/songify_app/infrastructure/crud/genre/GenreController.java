package spring.songify_app.infrastructure.crud.genre;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.songify_app.domain.crud.SongifyCrudFacade;
import spring.songify_app.domain.crud.dto.GenreDto;
import spring.songify_app.domain.crud.dto.GenreWithSongsDto;
import spring.songify_app.infrastructure.crud.genre.request.GenreRequestDto;
import spring.songify_app.infrastructure.crud.genre.response.AllGenresResponseDto;
import spring.songify_app.infrastructure.crud.genre.response.GenreResponseDto;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/genres")
class GenreController {

    private final SongifyCrudFacade songifyCrudFacade;

    @PostMapping
    ResponseEntity<GenreResponseDto> addGenre(@RequestBody GenreRequestDto genreRequestDto) {
        GenreDto genreDto = songifyCrudFacade.addGenre(genreRequestDto);
        return ResponseEntity.ok(new GenreResponseDto(genreDto.id(), genreDto.name()));
    }

    @GetMapping
    ResponseEntity<AllGenresResponseDto> getAllGenres() {
        Set<GenreDto> genreDtos = songifyCrudFacade.findAllGenres();
        AllGenresResponseDto genres = new AllGenresResponseDto(
                genreDtos.stream()
                        .map(genreDto -> new GenreResponseDto(
                                genreDto.id(),
                                genreDto.name()))
                        .collect(Collectors.toSet()));
        return ResponseEntity.ok(genres);
    }

    @PatchMapping
    ResponseEntity<GenreResponseDto> updateGenreNameById(@RequestBody GenreRequestDto genreRequestDto) {
        GenreDto genreDto = songifyCrudFacade.updateGenreNameById(genreRequestDto.id(), genreRequestDto.name());
        return ResponseEntity.ok(new GenreResponseDto(genreDto.id(), genreDto.name()));
    }

    @GetMapping("/{genreId}/songs")
    ResponseEntity<GenreWithSongsDto> getGenreWithSongs(@PathVariable Long genreId) {
        GenreWithSongsDto genreWithSongsDto = songifyCrudFacade.retrieveGenreWithSongs(genreId);
        return ResponseEntity.ok(genreWithSongsDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGenre(@PathVariable Long id) {
        songifyCrudFacade.deleteGenre(id);
        return ResponseEntity.ok("probably genre deleted :)");
    }

}
