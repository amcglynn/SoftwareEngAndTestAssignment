package ie.gmit.softwareeng.assignment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class FilmServiceTest {

    private FilmService MockitoTests;
    int id;

    @Mock
    FilmRepository filmRepo;

    @Mock
    OmdbClient omdbClient;

    @Mock
    Film film, film2;

    @Mock
    OmdbFilmDetails filmDetails;

    @Before
    public void init(){
        MockitoTests = new FilmService(filmRepo, omdbClient);
        film = new Film(id, "Toy Story", 100);
    }


    @Test(expected = FilmNotFoundException.class)
    public void invalidFilm(){
        when(filmRepo.getFilm(id)).thenReturn(null);
        MockitoTests.getFilm(id);
    }


    @Test(expected = NotInStockException.class)
    public void testRentFilmNoStock(){
        Film film2 = new Film(2, "Tiger Woods GOAT", 0);
        when(filmRepo.getFilm(id)).thenReturn(film2);
        MockitoTests.rentFilm(id);
    }


    @Test(expected = FilmNotFoundException.class)
    public void testRentFilmInvalidFilm(){
        when(filmRepo.getFilm(id)).thenReturn(null);
        MockitoTests.rentFilm(id);
    }


    @Test
    public void testReview(){
        Film mockFilm = org.mockito.Mockito.mock(Film.class);
        when(mockFilm.getTotalScore()).thenReturn(80);
        when(mockFilm.getNumReviews()).thenReturn(20);
        //assertEquals(5, MockitoTests.getAverageRating(mockFilm));
        assertEquals(4, MockitoTests.getAverageRating(mockFilm));

    }
    @Test
    public void successfulRent(){
        when(filmRepo.getFilm(id)).thenReturn(film);
        int inStock = film.getCopiesInStock();
        int rentCount = MockitoTests.rentFilm(id).getCopiesInStock();
        assertEquals(inStock - 1, rentCount );
    }


    @Test(expected = FilmNotFoundException.class)
    public void invalidReturn(){
        when(filmRepo.getFilm(id)).thenReturn(null);
        MockitoTests.returnFilm(id, 1);
    }

    @Test
    public void testFilmCatalog() {
        when(film2.getTitle()).thenReturn("Shrek");
        when(omdbClient.getFilmDetails(film2.getTitle())).thenReturn(filmDetails);
        Collection<Film> mockFilms;
        mockFilms = Arrays.asList(film2);
        when(filmRepo.getFilms()).thenReturn(mockFilms);
        when(film2.getId()).thenReturn(1);
        when(film2.getCopiesInStock()).thenReturn(1);
        when(film2.getNumReviews()).thenReturn(1);
        when(film2.getId()).thenReturn(1);
        when(film2.getNumReviews()).thenReturn(1);
        when(film2.getTotalScore()).thenReturn(1);
        when(filmDetails.getImdbRating()).thenReturn("7.8");
        when(filmDetails.getMetascore()).thenReturn("100");
        String expectedResult = "\n-----------------------------------------------"
                + "\nFilm\nID: " + 1 + "\nName: " + "Shrek" +
                "\nCopies in stock: " + 1 +
                "\nRating: " + 1 + "\nBased on " + 1 + " reviews" +
                "\nIMDB rating: " + "7.8" +
                "\nMetacritic rating: " + "100"
                + "\n-----------------------------------------------";

        assertEquals(MockitoTests.getFilmCatalog(), expectedResult);
    }
}