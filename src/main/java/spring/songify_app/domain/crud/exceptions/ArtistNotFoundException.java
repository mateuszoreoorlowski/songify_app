package spring.songify_app.domain.crud.exceptions;

public class ArtistNotFoundException extends RuntimeException {
    public ArtistNotFoundException(final String message) {
        super(message);
    }
}
