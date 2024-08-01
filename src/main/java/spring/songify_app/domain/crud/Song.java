package spring.songify_app.domain.crud;

import jakarta.persistence.*;
import lombok.*;
import spring.songify_app.domain.crud.util.BaseEntity;

import java.time.Instant;

@Builder
@Getter
@Entity
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(
        indexes =
                {@Index(
                        name = "idx_song_name",
                        columnList = "name"
                )}
)
class Song extends BaseEntity {

    @Id
    @GeneratedValue(generator = "song_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(
            name = "song_id_seq",
            sequenceName = "song_id_seq",
            allocationSize = 1
    )
    private Long id;

    @Column(nullable = false)
    private String name;

    private Instant releaseDate;

    private Long duration;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Genre genre;

    @ManyToOne
    @JoinColumn(name = "album_id")
    private Album album;

    @Enumerated(EnumType.STRING)
    private SongLanguage language;

    public Song(String name) {
        this.name = name;
    }

    Song(final String name, final Instant releaseDate, final Long duration, final SongLanguage language) {
        this.name = name;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.language = language;
    }
}
