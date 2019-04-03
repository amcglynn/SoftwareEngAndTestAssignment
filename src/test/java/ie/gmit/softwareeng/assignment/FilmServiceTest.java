package ie.gmit.softwareeng.assignment;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.Mockito;

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
    private Film film1;

    @Mock
    private Film film2;

    @Mock
    private OmdbFilmDetails omdbFilm1;

    @Mock
    private OmdbFilmDetails omdbFilm2;


    private FilmService service;

    private Map<Integer, Film> filmMap = new HashMap<>();
    private String TITLE_FILM1 = "Happy Feet";
    private int ID_FILM1 = 1;
    private String METASCORE_FILM1 = "77";
    private String IMDB_RATING_FILM1 = "6.5";

    private String TITLE_FILM2 = "The Godfather";
    private int ID_FILM2 = 2;
    private String METASCORE_FILM2 = "100";
    private String IMDB_RATING_FILM2 = "9.2";

    private int currentScore = 85;
    private int currentViews = 5;
    private int initialCopy = 10;
    private int testFilmID = 3;

    @Before
    public void setUp() throws Exception {
        film1 = Mockito.mock(Film.class);
        when(film1.getCopiesInStock()).thenReturn(0);
        when(film1.getId()).thenReturn(ID_FILM1);
        when(film1.getNumReviews()).thenReturn(0);
        when(film1.getTotalScore()).thenReturn(0);
        when(film1.getTitle()).thenReturn(TITLE_FILM1);

        film2 = Mockito.mock(Film.class);
        when(film2.getCopiesInStock()).thenReturn(initialCopy);
        when(film2.getId()).thenReturn(ID_FILM2);
        when(film2.getNumReviews()).thenReturn(currentViews);
        when(film2.getTotalScore()).thenReturn(currentScore);
        when(film2.getTitle()).thenReturn(TITLE_FILM2);

        filmMap.put(ID_FILM1, film1);
        filmMap.put(ID_FILM2, film2);

        filmRepository = Mockito.mock(FilmRepository.class);
        when(filmRepository.getFilm(ID_FILM1)).thenReturn(film1);
        when(filmRepository.getFilm(ID_FILM2)).thenReturn(film2);
        when(filmRepository.getFilms()).thenReturn(filmMap.values());

        omdbClient = Mockito.mock(OmdbClient.class);

        omdbFilm1 = Mockito.mock(OmdbFilmDetails.class);
        when(omdbClient.getFilmDetails(TITLE_FILM1)).thenReturn(omdbFilm1);
        when(omdbFilm1.getImdbRating()).thenReturn(IMDB_RATING_FILM1);
        when(omdbFilm1.getMetascore()).thenReturn(METASCORE_FILM1);

        omdbFilm2 = Mockito.mock(OmdbFilmDetails.class);
        when(omdbClient.getFilmDetails(TITLE_FILM2)).thenReturn(omdbFilm2);
        when(omdbFilm2.getImdbRating()).thenReturn(IMDB_RATING_FILM2);
        when(omdbFilm2.getMetascore()).thenReturn(METASCORE_FILM2);

        service = new FilmService(filmRepository, omdbClient);
    }


    @After
    public void tearDown() throws Exception {
    }

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void testFilmServiceNullRepo() {
        exception.expect(IllegalArgumentException.class);
        FilmService filmService = new FilmService(null, omdbClient);
    }

    @Test
    public void testFilmServiceNullClient() {
        exception.expect(IllegalArgumentException.class);
        FilmService filmService = new FilmService(filmRepository, null);
    }

    @Test
    public void testFilmService() {
        assertNotNull(new FilmService(filmRepository, omdbClient));
    }

    @Test
    public void testGetAverageRatingZero() {
        assertEquals(service.getAverageRating(film1), 0);

    }

    @Test
    public void testGetAverageRating() {
        assertNotEquals(service.getAverageRating(film2), 0);

    }

    @Test
    public void testGetFilmCatalog() {
        assertNotNull(service.getFilmCatalog());
    }

    @Test
    public void testGetFilm() {
        assertEquals(service.getFilm(film1.getId()), film1);

    }

    @Test
    public void testGetFilmInCorrectID() {
        assertNotEquals(service.getFilm(film1.getId()), film2);
    }

    @Test
    public void testGetFilmNull() {
        exception.expect(FilmNotFoundException.class);
        service.getFilm(testFilmID);
    }

    @Test
    public void testRentFilmNull() {
        exception.expect(FilmNotFoundException.class);
        service.rentFilm(testFilmID);
    }

    @Test
    public void testRentFilmStockZero() {
        exception.expect(NotInStockException.class);
        service.rentFilm(film1.getId());
    }

    @Test
    public void testRentFilm() {
        Film rent = service.rentFilm(film2.getId());
        assertEquals(rent, film2);
        //assertEquals("Copies reduced", rent.getCopiesInStock(), initialCopy-1);
    }

    @Test
    public void testReturnFilmNull() {
        exception.expect(FilmNotFoundException.class);
        service.returnFilm(testFilmID, 10);
    }

    @Test
    public void testReturnFilm() {
        int rating = 50;
        Film returned = service.returnFilm(film2.getId(), rating);
        assertEquals(returned, film2);
        /*assertEquals("Copies", returned.getCopiesInStock(), initialCopy + 1);
        assertEquals("Reviews", returned.getNumReviews(), currentViews + 1);
        assertEquals("Score", returned.getTotalScore(), currentScore + rating);*/
    }


}