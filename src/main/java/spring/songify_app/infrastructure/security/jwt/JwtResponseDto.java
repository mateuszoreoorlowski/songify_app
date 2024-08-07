package spring.songify_app.infrastructure.security.jwt;

import lombok.Builder;

@Builder
public record JwtResponseDto(String token) {
}
