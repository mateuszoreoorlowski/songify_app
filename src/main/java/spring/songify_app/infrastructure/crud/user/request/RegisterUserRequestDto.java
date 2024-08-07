package spring.songify_app.infrastructure.crud.user.request;

public record RegisterUserRequestDto(
        String username,
        String password
) {
}
