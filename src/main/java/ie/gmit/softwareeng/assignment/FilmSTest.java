package ie.gmit.softwareeng.assignment;

//Mary O' Brien - G00311277

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FilmSTest {
	@Mock
    FilmService filmService;
	
	@Mock
    FilmRepository filmRepository;
	
	@Mock
    OmdbClient omdbClient;
	
	@Mock
    Film film;
	
	@Before
    public void setup() {
        filmService = new FilmService(filmRepository, omdbClient);
        int id = 1;
        film = new Film(id, "Lord of The Rings: Fellowship of The Ring", 5);
    }

    @Test
    public void getFilmName() {
        FilmService service = new FilmService(filmRepository, omdbClient);
        Film film = new Film(1, "Lord of The Rings: The Two Towers", 5);
        int id = 1;
        when(filmRepository.getFilm(id)).thenReturn(film);
        assertEquals("Lord of The Rings: The Two Towers", film.getTitle());
        service.getFilm(1);
    }

    @Test
    public void testReview() {
        Film mockFilm = org.mockito.Mockito.mock(Film.class);
        when(mockFilm.getTotalScore()).thenReturn(6);

        when(mockFilm.getNumReviews()).thenReturn(3);
        assertEquals(2, filmService.getAverageRating(mockFilm));
    }

    public void rentFilm() {
        when(filmRepository.getFilm(1)).thenReturn(film);
        when(film.getCopiesInStock()).thenReturn(1);
        assertEquals(filmService.rentFilm(1), film);
    }

    public void returnFilm(){
        FilmService service = new FilmService(filmRepository, omdbClient);
        Film MockFilm = new Film(1, "Lord of The Rings: Fellowship of The Ring", 5);
        int id =1;
        when(filmRepository.getFilm(id)).thenReturn(film);
        service.returnFilm(1,5);
    }

    @Test(expected = NullPointerException.class)
    public void getFilmCatalogTestNullException() {

        Mockito.when(filmRepository.getFilms()).thenReturn(null);

        filmService.getFilmCatalog();
    }

    @Test
    public void getAverageRatingTestNoReviews() {

        int numReviews = 0;
        Mockito.when(film.getNumReviews()).thenReturn(numReviews);

        int expected = 0;
        int result = filmService.getAverageRating(film);

        Assert.assertEquals(expected, result);
    }

    @Test(expected = NullPointerException.class)
    public void getAverageRatingTestNullFilm() {

        filmService.getAverageRating(null);
    }
}
