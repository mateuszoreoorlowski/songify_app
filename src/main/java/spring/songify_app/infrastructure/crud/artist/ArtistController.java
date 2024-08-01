package spring.songify_app.infrastructure.crud.artist;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.songify_app.domain.crud.SongifyCrudFacade;
import spring.songify_app.domain.crud.dto.ArtistAlbumDto;
import spring.songify_app.domain.crud.dto.ArtistDto;
import spring.songify_app.infrastructure.crud.artist.dto.request.ArtistRequestDto;
import spring.songify_app.infrastructure.crud.artist.dto.request.ArtistUpdateRequestDto;
import spring.songify_app.infrastructure.crud.artist.dto.response.AllArtistsResponseDto;
import spring.songify_app.infrastructure.crud.artist.dto.response.ArtistResponseDto;

import java.util.Set;
import java.util.stream.Collectors;

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

    @GetMapping
    ResponseEntity<AllArtistsResponseDto> getAllArtists() {
        Set<ArtistDto> artistDtos = songifyCrudFacade.findAllArtists();
        AllArtistsResponseDto artists = new AllArtistsResponseDto(
                artistDtos.stream()
                        .map(artistDto -> new ArtistResponseDto(artistDto.name()))
                        .collect(Collectors.toSet()));
        return ResponseEntity.ok(artists);
    }

    @PostMapping("/album/song")
    ResponseEntity<ArtistResponseDto> addArtistWithDefaultAlbumAndSong(@RequestBody ArtistRequestDto artistRequestDto) {
        ArtistDto artistDto = songifyCrudFacade.addArtistWithDefaultAlbumAndSong(artistRequestDto);
        return ResponseEntity.ok(new ArtistResponseDto(artistDto.name()));
    }

    @PatchMapping("/{artistId}")
    ResponseEntity<ArtistResponseDto> updateArtistNameById(@RequestBody ArtistUpdateRequestDto dto) {
        ArtistDto artistDto = songifyCrudFacade.updateArtistNameById(dto.artistId(), dto.newArtistName());
        return ResponseEntity.ok(new ArtistResponseDto(artistDto.name()));
    }

    @PutMapping("/{artistId}/albums/{albumId}")
    ResponseEntity<ArtistAlbumDto> addArtistToAlbum(@PathVariable Long albumId, @PathVariable Long artistId) {
        songifyCrudFacade.assignArtistToAlbum(artistId, albumId);
        return ResponseEntity.ok(new ArtistAlbumDto(artistId, albumId));
    }
}
