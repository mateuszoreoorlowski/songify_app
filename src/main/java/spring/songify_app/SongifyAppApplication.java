package spring.songify_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import spring.songify_app.infrastructure.security.jwt.JwtConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(value = JwtConfigurationProperties.class)
public class SongifyAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(SongifyAppApplication.class, args);
    }

}
