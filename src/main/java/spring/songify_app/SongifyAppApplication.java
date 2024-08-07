package spring.songify_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
public class SongifyAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(SongifyAppApplication.class, args);
    }

}
