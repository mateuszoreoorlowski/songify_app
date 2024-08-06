package spring.songify_app.infrastructure.crud.album.error;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import spring.songify_app.domain.crud.exceptions.AlbumNotFoundException;

@RestControllerAdvice
@Log4j2
public class AlbumErrorHandler {

    @ExceptionHandler(AlbumNotFoundException.class)
    public ResponseEntity<ErrorAlbumResponseDto> handleException(AlbumNotFoundException exception) {
        log.warn("AlbumNotFoundException while accessing album");
        ErrorAlbumResponseDto errorAlbumResponseDto = new ErrorAlbumResponseDto(exception.getMessage(), HttpStatus.NOT_FOUND);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorAlbumResponseDto);
    }
}
