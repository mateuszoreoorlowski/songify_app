package feature;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import spring.songify_app.SongifyAppApplication;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = SongifyAppApplication.class)
@Testcontainers
@AutoConfigureMockMvc
@ActiveProfiles("integration")
class HappyPathIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:14-alpine");

    @Autowired
    public MockMvc mockMvc;

    @DynamicPropertySource
    public static void propertyOverride(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
    }

    @Test
    public void happy_path_integration_test() throws Exception {

//        1. when I go to /song then I can see no songs
        mockMvc.perform(get("/songs")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.songs", empty()));
//        2. when I post to /song with Song "Mrugnąłem tylko raz" then Song "Mrugnąłem tylko raz" is returned with id 1
        mockMvc.perform(post("/songs")
                        .content("""
                                        {
                                          "name": "Mrugnąłem tylko raz",
                                          "releaseDate": "2024-08-05T18:51:20.107Z",
                                          "duration": 161,
                                          "language": "POLISH"
                                        }
                                """.trim())
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Mrugnąłem tylko raz")))
                .andExpect(jsonPath("$.releaseDate", is("2024-08-05T18:51:20.107Z")))
                .andExpect(jsonPath("$.duration", is(161)))
                .andExpect(jsonPath("$.language", is("POLISH")));
//        3. when I post to /song with Song "Tylko ciemność" then Song "Tylko ciemność" is returned with id 2
        mockMvc.perform(post("/songs")
                        .content("""
                                {
                                  "name": "Tylko ciemność",
                                  "releaseDate": "2024-07-25T13:55:21.850Z",
                                  "duration": 192,
                                  "language": "POLISH"
                                }
                                """.trim())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.name", is("Tylko ciemność")))
                .andExpect(jsonPath("$.releaseDate", is("2024-07-25T13:55:21.850Z")))
                .andExpect(jsonPath("$.duration", is(192)))
                .andExpect(jsonPath("$.language", is("POLISH")));
//        4. when I go to /genre then I can see only default genre with id 1
        mockMvc.perform(get("/genres")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.genres[0].id", is(1)))
                .andExpect(jsonPath("$.genres[0].name", is("default")));
//        5. when I post to /genre with Genre "Rap" then Genre "Rap" is returned with id 2
        mockMvc.perform(post("/genres")
                        .content("""
                        {
                          "name": "Rap"
                        }
                        """.trim())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.name", is("Rap")));
//        6. when I go to /song/1 then I can see default genre with id 1 and name default
//        7. when I put to /song/1/genre/1 then Genre with id 2 ("Rap") is added to Song with id 1 ("Mrugnąłem tylko raz")
//        8. when I go to /song/1 then I can see "Rap" genre
//        9. when I go to /albums then I can see no albums
//        10. when I post to /albums with Album "04:01" and Song with id 1 then Album "04:01" is returned with id 1
//        11. when I go to /albums/1 then I can not see any albums because there is no artist in system
//        12. when I post to /artists with Artist "Kękę" then Artist "Kękę" is returned with id 1
//        13. when I put to /artists/1/albums/2 then Artist with id 1 ("Kękę") is added to Album with id 1 ("04:01")
//        14. when I go to /albums/1 then I can see album with single song with id 1 and single artist with id 1
//        15. when I put to /albums/1/songs/2 then Song with id 2 ("Tylko ciemność") is added to Album with id 1 ("04:01")
//        16. when I go to /albums/1 then I can see album with 2 songs (id1 and id2)


    }
}
