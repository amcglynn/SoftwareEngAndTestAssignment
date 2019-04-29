package ie.gmit.softwareeng.assignment;

import org.junit.*;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class FilmServiceTest {

    @Mock
    private FilmRepository filmRepository;

    @Mock
    private OmdbClient omdbClient;

    @Mock
    private OmdbFilmDetails omdbFilmDetails;


    private Map<Integer, Film> filmMap = new HashMap<>();
    private Film movie1;
    private FilmService service;
    private Film movie2;

    @Before
    public void init() throws Exception {
        //filmMap.put(1, new Film(1, "Free Solo", 5));
        //filmMap.put(2, new Film(2, "The Dawn Wall", 5));

        movie1 = Mockito.mock(Film.class);
        when(movie1.getId()).thenReturn(1);
        when(movie1.getTitle()).thenReturn("Free Solo");
        when(movie1.getCopiesInStock()).thenReturn(5);
        when(movie1.getTotalScore()).thenReturn(297);
        when(movie1.getNumReviews()).thenReturn(3);

        movie2 = Mockito.mock(Film.class);
        when(movie2.getId()).thenReturn(2);
        when(movie2.getTitle()).thenReturn("The Dawn Wall");
        when(movie2.getCopiesInStock()).thenReturn(0);
        when(movie2.getTotalScore()).thenReturn(0);
        when(movie2.getNumReviews()).thenReturn(0);

        filmMap.put(1,movie1);
        filmMap.put(2,movie2);
        filmRepository = Mockito.mock(FilmRepository.class);
        when(filmRepository.getFilm(1)).thenReturn(movie1);
        when(filmRepository.getFilm(2)).thenReturn(movie2);
        when(filmRepository.getFilms()).thenReturn(filmMap.values());

        omdbFilmDetails = Mockito.mock(OmdbFilmDetails.class);
        when(omdbFilmDetails.getImdbRating()).thenReturn(String.valueOf(10));
        when(omdbFilmDetails.getMetascore()).thenReturn(String.valueOf(10));
        omdbClient = Mockito.mock(OmdbClient.class);
        when(omdbClient.getFilmDetails(movie1.getTitle())).thenReturn(omdbFilmDetails);
        when(omdbClient.getFilmDetails(movie2.getTitle())).thenReturn(omdbFilmDetails);
        when(omdbClient.getFilmDetails(movie1.getTitle()).getMetascore()).thenReturn(String.valueOf(10));
        when(omdbClient.getFilmDetails(movie2.getTitle()).getMetascore()).thenReturn(String.valueOf(10));
        when(omdbClient.getFilmDetails(movie1.getTitle()).getImdbRating()).thenReturn(String.valueOf(10));
        when(omdbClient.getFilmDetails(movie2.getTitle()).getImdbRating()).thenReturn(String.valueOf(10));

        service = new FilmService(filmRepository, omdbClient);
        when(service.getFilmCatalog()).thenReturn("ABC");
        when(service.getAverageRating(movie1)).thenReturn(10);
        when(service.getAverageRating(movie2)).thenReturn(9);
    }
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    /////////// getAverageRating() Tests //////////////
    @Test
    public void getAverageRating() {
        assertEquals(service.getAverageRating(movie1),29);
    }
    @Test
    public void getAverageRatingWrong() {
        assertNotEquals(service.getAverageRating(movie1),50);
    }
    @Test
    public void getAverageRatingZero() {
        assertEquals(service.getAverageRating(movie2),0);
    }

    /////////// getRightFilmCatalog() Tests //////////////
    @Test
    public void getRightFilmCatalog() {
        String exResult ="\n-----------------------------------------------" +
                "\nFilm" +
                "\nID: 1" +
                "\nName: Free Solo" +
                "\nCopies in stock: 5" +
                "\nRating: 29" +
                "\nBased on 10 reviews" +
                "\nIMDB rating: 10" +
                "\nMetacritic rating: ABC" +
                "\n-----------------------------------------------" +
                "\nFilm" +
                "\nID: 2" +
                "\nName: The Dawn Wall" +
                "\nCopies in stock: 0" +
                "\nRating: 0" +
                "\nBased on 9 reviews" +
                "\nIMDB rating: 10" +
                "\nMetacritic rating: ABC" +
                "\n-----------------------------------------------";
        assertEquals(service.getFilmCatalog(),exResult);
    }


    /////////// getWrongFilmCatalog() Tests //////////////
    @Test
    public void getWrongFilmCatalog() {
        String exResult ="ABC";
        assertNotEquals(service.getFilmCatalog(),exResult);
    }
    /////////// getFilm() Tests //////////////
    @Test
    public void getCorrectMovie1() {
        assertEquals(service.getFilm(1),movie1);
    }
    @Test
    public void getCorrectMovie2() {
        assertEquals(service.getFilm(2),movie2);
    }
    @Test
    public void getWrongMovie1() {
        assertNotEquals(service.getFilm(1),movie2);
    }
    @Test
    public void getWrongMovie2() {
        assertNotEquals(service.getFilm(2),movie1);
    }
    @Test
    public void getMovieNotFound() {
        exception.expect(FilmNotFoundException.class);
        service.getFilm(3);
    }
    /////////// rentFilm() Tests //////////////
    @Test
    public void rentCorrectMovie() {
        assertEquals(service.rentFilm(1),movie1);
    }
    @Test
    public void rentWrongMovie2() {
        assertNotEquals(service.rentFilm(1),movie2);
    }
    @Test
    public void rentOutOfStock() {
        exception.expect(NotInStockException.class);
        service.rentFilm(2);
    }
    @Test
    public void rentMovieNotFound() {
        exception.expect(FilmNotFoundException.class);
        service.rentFilm(3);
    }
    /////////// returnFilm() Tests //////////////
    @Test
    public void returnMovieNotFound() {
        exception.expect(FilmNotFoundException.class);
        service.rentFilm(3);
    }
    @Test
    public void returnCorrectMovie() {
        assertEquals(service.returnFilm(1,10),movie1);
    }
    @Test
    public void returnWrongMovie() {

        assertNotEquals(service.returnFilm(1,10),movie2);
    }
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }
}