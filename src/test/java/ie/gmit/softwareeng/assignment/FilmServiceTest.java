package ie.gmit.softwareeng.assignment;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FilmServiceTest {

    private FilmService testObject;

    @Mock
    FilmRepository repository;
    @Mock
    OmdbClient omdbClient;
    @Mock
    OmdbFilmDetails filmDetails;
    @Mock
    Film film;


    @Before
    public void setUp() {
        testObject = new FilmService(repository, omdbClient);
    }

    @After
    public void tearDown() {
    }

    /********************************************************/
    // Constructor Tests

    @Test
    public void constructorTest() {
        // Test constructor
        assertNotNull(new FilmService(repository, omdbClient));
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorExceptionTest() {
        // Test constructor exception
        FilmService exceptionCase = new FilmService(null, null);
    }

    // Method Tests

    @Test
    public void getAverageRating() {
        // Test first path
        when(film.getNumReviews()).thenReturn(0);
        int result = testObject.getAverageRating(film);
        assertEquals(result, 0);

        // Test second path
        when(film.getNumReviews()).thenReturn(5);
        when(film.getTotalScore()).thenReturn(5);
        result = testObject.getAverageRating(film);
        assertEquals(result, 1);
    }

    @Test
    public void getFilmCatalog() {
        // Set up mock dependencies
        when(film.getTitle()).thenReturn("test title");
        when(omdbClient.getFilmDetails(film.getTitle())).thenReturn(filmDetails);
        Collection<Film> mockFilms = Arrays.asList(film);
        when(repository.getFilms()).thenReturn(mockFilms);
        when(film.getId()).thenReturn(1);
        when(film.getCopiesInStock()).thenReturn(1);
        when(film.getNumReviews()).thenReturn(1);
        when(film.getId()).thenReturn(1);
        when(film.getNumReviews()).thenReturn(1);
        when(film.getTotalScore()).thenReturn(1);
        when(filmDetails.getImdbRating()).thenReturn("test rating");
        when(filmDetails.getMetascore()).thenReturn("test rating");
        // Expected result string
        String expectedResult = "\n-----------------------------------------------"
                + "\nFilm\nID: " + 1 + "\nName: " + "test title" +
                "\nCopies in stock: " + 1 +
                "\nRating: " + 1 + "\nBased on " + 1 + " reviews" +
                "\nIMDB rating: " + "test rating" +
                "\nMetacritic rating: " + "test rating"
                + "\n-----------------------------------------------";

        assertEquals(testObject.getFilmCatalog(), expectedResult);
    }

    @Test
    public void getFilm() {
        when(repository.getFilm(1)).thenReturn(film);
        assertEquals(testObject.getFilm(1), film);
    }

    @Test(expected = FilmNotFoundException.class)
    public void getFilmWithFilmNotFoundException() {
        when(repository.getFilm(1)).thenReturn(null);
        testObject.getFilm(1);
    }

    public void rentFilm() {
        when(repository.getFilm(1)).thenReturn(film);
        when(film.getCopiesInStock()).thenReturn(1);
        assertEquals(testObject.rentFilm(1), film);
    }

    @Test(expected = FilmNotFoundException.class)
    public void rentFilmWithFilmNotFoundException() {
        // test FilmNotFoundException path
        when(repository.getFilm(1)).thenReturn(null);
        testObject.rentFilm(1);
    }

    @Test(expected = NotInStockException.class)
    public void rentFilmWithNotInStockException() {
        // test NotInStockException path
        when(repository.getFilm(1)).thenReturn(film);
        when(film.getCopiesInStock()).thenReturn(0);
        testObject.rentFilm(1);
    }

    @Test
    public void returnFilm() {
        when(repository.getFilm(1)).thenReturn(film);
        when(film.getCopiesInStock()).thenReturn(1);
        when(film.getCopiesInStock()).thenReturn(1);
        when(film.getNumReviews()).thenReturn(1);
        when(film.getTotalScore()).thenReturn(1);

        assertEquals(testObject.returnFilm(1, 1), film);
    }

    @Test(expected = FilmNotFoundException.class)
    public void returnFilmWithFilmNotFoundException() {
        when(repository.getFilm(1)).thenReturn(null);
        testObject.returnFilm(1, 1);
    }
}