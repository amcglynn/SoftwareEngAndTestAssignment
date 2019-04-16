package ie.gmit.softwareeng.assignment;
// Task is to build out unit tests for FilmService.java.
// Unit tests must use the JUnit framework V4.12.
// FilmService contains dependencies object(s).
// To make the tests reliable, use test mocks to mock out behaviour on the dependencies
// A comprehensive test suite for the FilmService class must be developed.
// This must ensure all code paths are tested.
// No changes must be made to the code in the src/main/java directory
/*Karl Nolan G00328753*/

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TestFilmService {
    // Film 1 & 2 variables
    private int    film1_id        = 0;
    private String film1_title     = "Hobo with a Shotgun";
    private int    film1_stock     = 5;
    private int    film1_avgRating = 50;
    private int    film1_NoReviews = 10;
    private String film1_ImdbRating= "5/10";
    private String film1_MetaRating= "5/10";

    private int    film2_id        = 1;
    private String film2_title     = "Don't Be A Menace to South Central while drinking your Juice In The Hood";
    private int    film2_stock     = 0;
    private int    film2_avgRating = 0;
    private int    film2_NoReviews = 0;
    private String film2_ImdbRating= "8/10";
    private String film2_MetaRating= "8/10";
    //instances
    @Mock private Film film1, film2;			             // Contains information about a film
    @Mock private FilmRepository filmRepo;             // An interface for film repositories
    @Mock private OmdbClient omdbClient;               // A web client that makes a request to omdbapi.com to get metacritic and imdb ratings for a movie
    @Mock private OmdbFilmDetails omdbFilm1, omdbFilm2;// A class that contains the metacritic and imdb ratings from omdbapi.com
    private FilmService filmService;
    private Map<Integer,Film> filmHashmap = new HashMap<>();

    @Before
    public void SetUp(){
        film1 = Mockito.mock(Film.class);
        when(film1.getId()).thenReturn(film1_id);
        when(film1.getTitle()).thenReturn(film1_title);
        when(film1.getCopiesInStock()).thenReturn(film1_stock);
        when(film1.getTotalScore()).thenReturn(film1_avgRating);
        when(film1.getNumReviews()).thenReturn(film1_NoReviews);
        film2 = Mockito.mock(Film.class);
        when(film2.getId()).thenReturn(film2_id);
        when(film2.getTitle()).thenReturn(film2_title);
        when(film2.getCopiesInStock()).thenReturn(film2_stock);
        //when(film2.getTotalScore()).thenReturn(film2_avgRating);
        when(film2.getNumReviews()).thenReturn(film2_NoReviews);
        //  filmRepo & omdbClient > FilmService methodzInitNshit
        filmHashmap.put(film1_id, film1);
        filmHashmap.put(film2_id, film2);
        filmRepo = Mockito.mock(FilmRepository.class);
        when(filmRepo.getFilm(film1_id)).thenReturn(film1);
        when(filmRepo.getFilm(film2_id)).thenReturn(film2);
        when(filmRepo.getFilms()).thenReturn(filmHashmap.values());
        omdbClient = Mockito.mock(OmdbClient.class);
        omdbFilm1 = Mockito.mock(OmdbFilmDetails.class);
        when(omdbClient.getFilmDetails(film1_title)).thenReturn(omdbFilm1);
        when(omdbFilm1.getImdbRating()).thenReturn(film1_ImdbRating);
        when(omdbFilm1.getMetascore()).thenReturn(film1_MetaRating);
        omdbFilm2 = Mockito.mock(OmdbFilmDetails.class);
        when(omdbClient.getFilmDetails(film2_title)).thenReturn(omdbFilm2);
        when(omdbFilm2.getImdbRating()).thenReturn(film2_ImdbRating);
        when(omdbFilm2.getMetascore()).thenReturn(film2_MetaRating);
        filmService = new FilmService(filmRepo, omdbClient);
    }

    // Test FilmService
    // params
    @Test(expected = IllegalArgumentException.class)
    public void test_nullParams(){
        new FilmService(null,null);
    }
    @Test(expected = IllegalArgumentException.class)
    public void test_nullRepo(){
        new FilmService(null,omdbClient);
    }
    @Test(expected = IllegalArgumentException.class)
    public void test_nullClient(){
        new FilmService(filmRepo, null);
    }
    @Test
    public void test_filmService() {
        assertNotNull(new FilmService(filmRepo, omdbClient));
    }

    //  getAverageRating(Film film) Tests
    @Test
    public void test_zAvgRating() {
        assertEquals(filmService.getAverageRating(film2), 0);
    }
    // test getAverageRating
    @Test
    public void test_getAvgRating(){
        assertEquals(filmService.getAverageRating(film2), 0);
    }

    // Test      String getFilmCatalog()
    // Get a string representation of the entire film catalog that is stored in the repo
    @Test
    public void test_getFilmCatalog() {
        System.out.println(filmService.getFilmCatalog());
        String expectedString =
                "\n-----------------------------------------------"+
                "\nFilm\nID: " + 0 + "\nName: " + "Hobo with a Shotgun" +
                "\nCopies in stock: " + 5 +
                "\nRating: " + 5 + "\nBased on " + 10 + " reviews" +
                "\nIMDB rating: " + "5/10" +
                "\nMetacritic rating: " + "5/10"+
                "\n-----------------------------------------------" +
                "\nFilm\nID: " + 1 + "\nName: " + "Don't Be A Menace to South Central while drinking your Juice In The Hood" +
                "\nCopies in stock: " + 0 +
                "\nRating: " + 0 + "\nBased on " + 0 + " reviews" +
                "\nIMDB rating: " + "8/10" +
                "\nMetacritic rating: " + "8/10"+
                "\n-----------------------------------------------";
        assertEquals(filmService.getFilmCatalog(), expectedString);
    }

    //test   getFilm(film_id)
    @Test
    public void test_getFilm() {
        assertEquals(filmService.getFilm(1), film2 );
    }
    @Test(expected = FilmNotFoundException.class)
    public void test_getFilm_notInRepoEx() {
        filmService.getFilm(2);
    }

    // test rentFilm(int filmId)-gets film by id -throws exception if not found
    @Test                       //-gets stock-throws exception if not found
    public void test_rentFilm() {//return a Film on success
        assertEquals(filmService.rentFilm(0), film1);
    }
    // test film not found exception
    @Test(expected = FilmNotFoundException.class)
    public void test_filmNotFoundEx() {
        filmService.rentFilm(4);
    }
    // test noStockException
    @Test(expected = NotInStockException.class)
    public void test_notInStockExc() {
        filmService.rentFilm(1);
    }
    @Test
    public void test_returnFilm(){//find film with id
        assertEquals(filmService.returnFilm(0, 1), film1);
    }
    @Test(expected = FilmNotFoundException.class)
    public void test_rFilm_notFoundException() {
        filmService.returnFilm(2,0);
    }
}
