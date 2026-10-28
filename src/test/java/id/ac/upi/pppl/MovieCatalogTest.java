package id.ac.upi.pppl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

class MovieCatalogTest {

    @Test
    void movieCatalog_sample_returnsFourMovies() {
        MovieCatalog catalog = MovieCatalog.sample();
        assertEquals(4, catalog.getMovies().size());
    }

    @Test
    void movieCatalog_findById_existingId_returnsMovie() {
        MovieCatalog catalog = MovieCatalog.sample();
        Optional<Movie> found = catalog.findById("M01");
        assertTrue(found.isPresent());
        assertEquals("Interstellar", found.get().getTitle());
    }

    @Test
    void movieCatalog_findById_unknownId_returnsEmpty() {
        MovieCatalog catalog = MovieCatalog.sample();
        assertFalse(catalog.findById("M99").isPresent());
    }

    @Test
    void movieCatalog_findById_nullId_returnsEmpty() {
        MovieCatalog catalog = MovieCatalog.sample();
        assertFalse(catalog.findById(null).isPresent());
    }

    @Test
    void movieCatalog_constructorNullList_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new MovieCatalog(null));
    }

    @Test
    void movieCatalog_getMovies_returnsSameSizeAsGivenList() {
        MovieCatalog catalog = new MovieCatalog(List.of(new Movie("M01", "Interstellar", 50000)));
        assertEquals(1, catalog.getMovies().size());
    }
}
