package spring.songify_app.domain.crud.exceptions;

public class GenreWasNotDeletedException extends RuntimeException {
    public GenreWasNotDeletedException(String message) {
        super(message);
    }
}
