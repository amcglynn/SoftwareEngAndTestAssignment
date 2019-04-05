package ie.gmit.softwareeng.assignment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class Tests {
    private static final int FILM_ID = 1;
    private static final int COPIES_IN_STOCK = 10;
    private static final int NUM_OF_REVIEWS = 3;
    private static final int TOTAL_SCORE = 5;
    private static final int FILM_RATING =3;
    private static final String FILM_TITLE = "FILM 1 TITLE";
    private static final String IMDB_SCORE = "5/7";
    private static final String METACRITIC_SCORE = "9/10";

    private static final int FILM_ID2 = 2;
    private static final int COPIES_IN_STOCK2 = 10;
    private static final int NUM_OF_REVIEWS2 = 8;
    private static final int TOTAL_SCORE2 = 33;
    private static final int FILM_RATING2 = 5;
    private static final String FILM_TITLE2 = "FILM 2 TITLE";
    private static final String IMDB_SCORE2 = "5/7";
    private static final String METACRITIC_SCORE2 = "8/10";

    @Mock
    FilmRepository filmRepository;

    @Mock
    OmdbClient omdbClient;

    @Mock
    Film film, film2;

    @Before
    public void setup(){
        when(film.getId()).thenReturn(FILM_ID);
        when(film.getNumReviews()).thenReturn(NUM_OF_REVIEWS);
        when(film.getTotalScore()).thenReturn(TOTAL_SCORE);
        when(film.getCopiesInStock()).thenReturn(COPIES_IN_STOCK);

        when(filmRepository.getFilm(FILM_ID)).thenReturn(film);
    }


    @Test(expected = IllegalArgumentException.class)
    public void testNullArguments(){
        new FilmService(null,null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullRepo(){
        new FilmService(null,omdbClient);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullClient(){
        new FilmService(filmRepository, null);
    }

    @Test
    public void notNull(){
        try {
            new FilmService(filmRepository, omdbClient);
        } catch (Exception e) {
            //fail the test if exception is thrown
            fail();
        }
    }

    @Test
    public void testAverageRatingNegNumReviews() {
        when(film.getNumReviews()).thenReturn(-1);
        int retVal = new FilmService(filmRepository,omdbClient).getAverageRating(film);

        assertEquals(retVal,0);
    }

    @Test
    public void testAverageCalculation(){
        int retVal = new FilmService(filmRepository,omdbClient).getAverageRating(film);
        assertEquals(TOTAL_SCORE/NUM_OF_REVIEWS,retVal);
    }

    @Test
    public void testGetFilm(){
        Film retFilm = new FilmService(filmRepository,omdbClient).getFilm(FILM_ID);

        assertEquals(film,retFilm);
    }

    @Test(expected = FilmNotFoundException.class)
    public void testGetNullFilm(){
        when(filmRepository.getFilm(FILM_ID)).thenReturn(null);
        new FilmService(filmRepository,omdbClient).getFilm(FILM_ID);
    }

    @Test
    public void testRentFilm(){
        ArgumentCaptor<Integer> copiesCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> filmIdCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Film> filmArgumentCaptor = ArgumentCaptor.forClass(Film.class);

        new FilmService(filmRepository,omdbClient).rentFilm(FILM_ID);

        verify(film).setCopiesInStock(copiesCaptor.capture());
        assertEquals(Integer.valueOf(COPIES_IN_STOCK-1),copiesCaptor.getValue());

        verify(filmRepository).updateFilm(filmIdCaptor.capture(),filmArgumentCaptor.capture());
        assertEquals(film,filmArgumentCaptor.getValue());
        assertEquals(Integer.valueOf(FILM_ID),filmIdCaptor.getValue());
    }

    @Test(expected = NotInStockException.class)
    public void testRentNotInStock(){
        when(film.getCopiesInStock()).thenReturn(-1);
        new FilmService(filmRepository, omdbClient).rentFilm(FILM_ID);
    }

    @Test(expected = FilmNotFoundException.class)
    public void testReturnFilmNotInStock(){
        final int FILM_ID_THIS_TEST = 2;
        when(filmRepository.getFilm(FILM_ID_THIS_TEST)).thenReturn(null);
        new FilmService(filmRepository,omdbClient).returnFilm(FILM_ID_THIS_TEST,FILM_RATING);
    }
    @Test
    public void testReturnFilm(){
        ArgumentCaptor<Integer> repoUpdateFilmFilmId = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Film> repoUpdateFilmFilm = ArgumentCaptor.forClass(Film.class);
        ArgumentCaptor<Integer> filmSetCopiesInStock = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> fimSetNumReviews = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> filmSetTotalScore = ArgumentCaptor.forClass(Integer.class);

        Film retFilm = new FilmService(filmRepository,omdbClient).returnFilm(FILM_ID,FILM_RATING);

        verify(film).setCopiesInStock(filmSetCopiesInStock.capture());
        verify(film).setNumReviews(fimSetNumReviews.capture());
        verify(film).setTotalScore(filmSetTotalScore.capture());
        verify(filmRepository).updateFilm(repoUpdateFilmFilmId.capture(),repoUpdateFilmFilm.capture());


        assertEquals(Integer.valueOf(COPIES_IN_STOCK+1),filmSetCopiesInStock.getValue());
        assertEquals(Integer.valueOf(NUM_OF_REVIEWS + 1),fimSetNumReviews.getValue());
        assertEquals(Integer.valueOf(TOTAL_SCORE + FILM_RATING),filmSetTotalScore.getValue());

        assertEquals(film,repoUpdateFilmFilm.getValue());
        assertEquals(Integer.valueOf(FILM_ID),repoUpdateFilmFilmId.getValue());
        assertEquals(film,retFilm);
    }
    @Test
    public void testFilmCatalog(){
        Map<Integer,Film> filmMap = new HashMap<Integer, Film>(){
            {
                put(1,film);
                put(2,film2);
            }
        };

        Collection<Film> filmCollection = filmMap.values();

        when(filmRepository.getFilms()).thenReturn(filmCollection);

        when(film.getTitle()).thenReturn(FILM_TITLE);
        when(film2.getId()).thenReturn(FILM_ID2);

        when(film2.getTitle()).thenReturn(FILM_TITLE2);
        when(film2.getCopiesInStock()).thenReturn(COPIES_IN_STOCK2);
        when(film2.getTotalScore()).thenReturn(TOTAL_SCORE2);
        when(film2.getNumReviews()).thenReturn(NUM_OF_REVIEWS2);

        OmdbFilmDetails film1Details = Mockito.mock(OmdbFilmDetails.class);
        OmdbFilmDetails film2Details = Mockito.mock(OmdbFilmDetails.class);

        when(omdbClient.getFilmDetails(FILM_TITLE)).thenReturn(film1Details);
        when(omdbClient.getFilmDetails(FILM_TITLE2)).thenReturn(film2Details);

        when(film1Details.getImdbRating()).thenReturn(IMDB_SCORE);
        when(film1Details.getMetascore()).thenReturn(METACRITIC_SCORE);

        when(film2Details.getImdbRating()).thenReturn(IMDB_SCORE2);
        when(film2Details.getMetascore()).thenReturn(METACRITIC_SCORE2);

        String retString = new FilmService(filmRepository,omdbClient).getFilmCatalog();

        String expectedString =
                "\n-----------------------------------------------" +
                "\nFilm\nID: " + FILM_ID + "\nName: " + FILM_TITLE +
                "\nCopies in stock: " + COPIES_IN_STOCK +
                "\nRating: " + (TOTAL_SCORE / NUM_OF_REVIEWS) + "\nBased on " + NUM_OF_REVIEWS + " reviews" +
                "\nIMDB rating: " + IMDB_SCORE +
                "\nMetacritic rating: " + METACRITIC_SCORE +
                "\n-----------------------------------------------" +
                "\nFilm\nID: " + FILM_ID2 + "\nName: " + FILM_TITLE2 +
                "\nCopies in stock: " + COPIES_IN_STOCK2 +
                "\nRating: " + (TOTAL_SCORE2 / NUM_OF_REVIEWS2) + "\nBased on " + NUM_OF_REVIEWS2 + " reviews" +
                "\nIMDB rating: " + IMDB_SCORE2 +
                "\nMetacritic rating: " + METACRITIC_SCORE2 +
                "\n-----------------------------------------------";
        assertEquals(expectedString,retString);

    }
}
