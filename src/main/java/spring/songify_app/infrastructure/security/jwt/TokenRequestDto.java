package spring.songify_app.infrastructure.security.jwt;

public record TokenRequestDto(
        String username,
        String password
) {
}
