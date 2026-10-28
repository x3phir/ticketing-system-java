package id.ac.upi.pppl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Katalog film contoh (data statis, tanpa database).
 */
public class MovieCatalog {

    private final List<Movie> movies;

    public MovieCatalog(List<Movie> movies) {
        if (movies == null) {
            throw new IllegalArgumentException("Daftar film tidak boleh null");
        }
        this.movies = List.copyOf(movies);
    }

    public static MovieCatalog sample() {
        List<Movie> samples = List.of(
                new Movie("M01", "Interstellar", 50000),
                new Movie("M02", "Pengabdi Setan 2", 45000),
                new Movie("M03", "Dilan 1990", 40000),
                new Movie("M04", "Laskar Pelangi", 35000)
        );
        return new MovieCatalog(samples);
    }

    public List<Movie> getMovies() {
        return Collections.unmodifiableList(movies);
    }

    public Optional<Movie> findById(String movieId) {
        if (movieId == null) {
            return Optional.empty();
        }
        return movies.stream()
                .filter(movie -> movie.getMovieId().equals(movieId))
                .findFirst();
    }
}
