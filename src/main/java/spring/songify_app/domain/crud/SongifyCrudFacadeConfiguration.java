package spring.songify_app.domain.crud;

class SongifyCrudFacadeConfiguration {

    public static SongifyCrudFacade createSongifyCrudFacade(
            final SongRepository songRepository,
            final AlbumRepository albumRepository,
            final ArtistRepository artistRepository,
            final GenreRepository genreRepository
    ) {

            // Initialize retrievers
            SongRetriever songRetriever = new SongRetriever(songRepository);
            AlbumRetriever albumRetriever = new AlbumRetriever(albumRepository);
            ArtistRetriever artistRetriever = new ArtistRetriever(artistRepository, albumRepository);
            GenreRetriever genreRetriever = new GenreRetriever(songRepository, genreRepository);

            // Initialize assigners
            ArtistAssigner artistAssigner = new ArtistAssigner(artistRetriever, albumRetriever);
            GenreAssigner genreAssigner = new GenreAssigner(songRetriever, genreRetriever);
            SongAssigner songAssigner = new SongAssigner(songRepository, albumRepository, songRetriever, albumRetriever, artistRetriever);

            // Initialize adders
            SongAdder songAdder = new SongAdder(songRepository, genreRepository, genreAssigner);
            AlbumAdder albumAdder = new AlbumAdder(albumRepository, songRetriever, albumRetriever, artistRetriever);
            ArtistAdder artistAdder = new ArtistAdder(artistRepository);
            GenreAdder genreAdder = new GenreAdder(genreRepository);

            // Initialize deleters
            SongDeleter songDeleter = new SongDeleter(songRepository, albumRepository);
            AlbumDeleter albumDeleter = new AlbumDeleter(songRepository, albumRepository);
            ArtistDeleter artistDeleter = new ArtistDeleter(artistRepository, artistRetriever, albumRetriever, albumDeleter, songDeleter);
            GenreDeleter genreDeleter = new GenreDeleter(genreRepository, songRepository);

            // Initialize updaters
            SongUpdater songUpdater = new SongUpdater(songRetriever, artistRetriever);
            AlbumUpdater albumUpdater = new AlbumUpdater(albumRetriever);
            ArtistUpdater artistUpdater = new ArtistUpdater(artistRetriever);
            GenreUpdater genreUpdater = new GenreUpdater(genreRetriever);

            return new SongifyCrudFacade(
                    artistAdder, genreAdder, songAdder, albumAdder,
                    artistRetriever, genreRetriever, songRetriever, albumRetriever,
                    artistUpdater, genreUpdater, albumUpdater, songUpdater,
                    songAssigner ,artistAssigner, genreAssigner,
                    artistDeleter, genreDeleter, albumDeleter, songDeleter
            );
    }
}