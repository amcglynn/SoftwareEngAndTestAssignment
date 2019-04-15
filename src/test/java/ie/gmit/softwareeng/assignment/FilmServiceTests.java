package ie.gmit.softwareeng.assignment;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class FilmServiceTests {

    private static final int ID_MOCK_FILM_1 = 1;
    private static final String TITLE_MOCK_FILM_1 = "film_1";
    private static final int NUM_COPIES_MOCK_FILM_1 = 5;
    private static final int TOTAL_SCORE_MOCK_FILM_1 = 25;
    private static final int NUM_REVIEWS_MOCK_FILM_1 = 5;
    private static final String METASCORE_MOCK_FILM_1 = "72";
    private static final String IMDB_RATING_MOCK_FILM_1 = "7.5";

    private static final int ID_MOCK_FILM_2 = 2;
    private static final String TITLE_MOCK_FILM_2 = "film_2";
    private static final int NUM_COPIES_MOCK_FILM_2 = 10;
    private static final int TOTAL_SCORE_MOCK_FILM_2 = 32;
    private static final int NUM_REVIEWS_MOCK_FILM_2 = 8;
    private static final String METASCORE_MOCK_FILM_2 = "80";
    private static final String IMDB_RATING_MOCK_FILM_2 = "8.2";

    private FilmService stub_service;

    @Mock
    private FilmRepository mock_repo;

    @Mock
    private OmdbClient mock_omdb;

    @Mock
    private Film mock_film_1;
    @Mock
    private Film mock_film_2;

    private Collection<Film> mock_films;

    @Before
    public void init() {
        stub_service = new FilmService(mock_repo, mock_omdb);

        Map<Integer, Film> films = new HashMap<Integer, Film>() {
            {
                put(ID_MOCK_FILM_1, mock_film_1);
                put(ID_MOCK_FILM_2, mock_film_2);
            }
        };
        mock_films = films.values();

        Mockito.when(mock_repo.getFilms()).thenReturn(mock_films);
        Mockito.when(mock_repo.getFilm(ID_MOCK_FILM_1)).thenReturn(mock_film_1);

        Mockito.when(mock_omdb.getFilmDetails(TITLE_MOCK_FILM_1)).thenReturn(new OmdbFilmDetails(METASCORE_MOCK_FILM_1, IMDB_RATING_MOCK_FILM_1));
        Mockito.when(mock_omdb.getFilmDetails(TITLE_MOCK_FILM_2)).thenReturn(new OmdbFilmDetails(METASCORE_MOCK_FILM_2, IMDB_RATING_MOCK_FILM_2));

        Mockito.when(mock_film_1.getId()).thenReturn(ID_MOCK_FILM_1);
        Mockito.when(mock_film_1.getTitle()).thenReturn(TITLE_MOCK_FILM_1);
        Mockito.when(mock_film_1.getCopiesInStock()).thenReturn(NUM_COPIES_MOCK_FILM_1);
        Mockito.when(mock_film_1.getTotalScore()).thenReturn(TOTAL_SCORE_MOCK_FILM_1);
        Mockito.when(mock_film_1.getNumReviews()).thenReturn(NUM_REVIEWS_MOCK_FILM_1);

        Mockito.when(mock_film_2.getId()).thenReturn(ID_MOCK_FILM_2);
        Mockito.when(mock_film_2.getTitle()).thenReturn(TITLE_MOCK_FILM_2);
        Mockito.when(mock_film_2.getCopiesInStock()).thenReturn(NUM_COPIES_MOCK_FILM_2);
        Mockito.when(mock_film_2.getTotalScore()).thenReturn(TOTAL_SCORE_MOCK_FILM_2);
        Mockito.when(mock_film_2.getNumReviews()).thenReturn(NUM_REVIEWS_MOCK_FILM_2);
    }


    @Test
    public void getAverageRatingTestWithReviews() {

        int expected = TOTAL_SCORE_MOCK_FILM_1 / NUM_REVIEWS_MOCK_FILM_1;
        int result = stub_service.getAverageRating(mock_film_1);

        Assert.assertEquals(expected, result);
    }
    @Test
    public void getAverageRatingTestNoReviews() {

        int numReviews = 0;
        Mockito.when(mock_film_1.getNumReviews()).thenReturn(numReviews);

        int expected = 0;
        int result = stub_service.getAverageRating(mock_film_1);

        Assert.assertEquals(expected, result);
    }
    @Test(expected = NullPointerException.class)
    public void getAverageRatingTestNullFilm() {

        stub_service.getAverageRating(null);
    }

    @Test
    public void getFilmCatalogTestSuccess() {

        StringBuilder sb = new StringBuilder();
        sb.append("\n-----------------------------------------------");
        for (Film mock_film : mock_films) {
            OmdbFilmDetails mock_details = mock_omdb.getFilmDetails(mock_film.getTitle());

            sb.append("\nFilm\nID: " + mock_film.getId() + "\nName: " + mock_film.getTitle() +
                    "\nCopies in stock: " + mock_film.getCopiesInStock() +
                    "\nRating: " + stub_service.getAverageRating(mock_film) + "\nBased on " + mock_film.getNumReviews() + " reviews" +
                    "\nIMDB rating: " + mock_details.getImdbRating() +
                    "\nMetacritic rating: " + mock_details.getMetascore());
            sb.append("\n-----------------------------------------------");
        }

        String response = stub_service.getFilmCatalog();

        Assert.assertEquals(sb.toString(), response);
    }
    @Test(expected = NullPointerException.class)
    public void getFilmCatalogTestNullException() {

        Mockito.when(mock_repo.getFilms()).thenReturn(null);

        stub_service.getFilmCatalog();
    }

    @Test
    public void getFilmTestSuccess() {

        Film result = stub_service.getFilm(ID_MOCK_FILM_1);

        Assert.assertEquals(mock_film_1, result);
    }
    @Test(expected = FilmNotFoundException.class)
    public void getFilmTestNotFoundException() {

        int id = 0;
        Mockito.when(mock_repo.getFilm(id)).thenReturn(null);

        stub_service.getFilm(id);
    }

    @Test
    public void rentFilmTestSuccess() {

        Film result = stub_service.rentFilm(ID_MOCK_FILM_1);

        mock_film_1.setCopiesInStock(NUM_COPIES_MOCK_FILM_1 - 1);

        Assert.assertEquals(mock_film_1, result);
    }
    @Test(expected = FilmNotFoundException.class)
    public void rentFilmTestFoundException() {

        int id = 0;
        Mockito.when(mock_repo.getFilm(id)).thenReturn(null);

        stub_service.rentFilm(id);
    }
    @Test(expected = NotInStockException.class)
    public void rentFilmTestStockException() {

        Mockito.when(mock_repo.getFilm(ID_MOCK_FILM_1)).thenReturn(mock_film_1);
        Mockito.when(mock_film_1.getCopiesInStock()).thenReturn(0);

        stub_service.rentFilm(ID_MOCK_FILM_1);
    }

    @Test
    public void returnFilmTestSuccess() {

        int rating = 5;
        Film result = stub_service.returnFilm(ID_MOCK_FILM_1, rating);

        mock_film_1.setCopiesInStock(NUM_COPIES_MOCK_FILM_1 + 1);
        mock_film_1.setNumReviews(NUM_REVIEWS_MOCK_FILM_1 + 1);
        mock_film_1.setTotalScore(TOTAL_SCORE_MOCK_FILM_1 + rating);

        Assert.assertEquals(mock_film_1, result);
    }
    @Test(expected = FilmNotFoundException.class)
    public void returnFilmTestException() {

        int id = 0;
        Mockito.when(mock_repo.getFilm(id)).thenReturn(null);

        stub_service.returnFilm(id, 0);
    }
}
