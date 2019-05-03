package ie.gmit.softwareeng.assignment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collection;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FilmServiceTest {

    private final int FILM_ID = 1;
    private final int copiesInStock = 10;
    private final int numOfReviews = 6;
    private final int totalScore = 54;
    private final int filmRating =3;
    private String filmTitle = "Avengers: Endgame";
    private String imdbScore = "50";
    private String metacriticScore = "75";

    private FilmService service;

    @Mock
    FilmRepository filmRepositoryMock;

    @Mock
    OmdbClient omdbClientMock;

    @Mock
    Film filmMock, film2Mock;

    @Mock
    OmdbFilmDetails film1DetailsMock, film2DetailsMock;

    @Before
    public void setup(){
        service = new FilmService(filmRepositoryMock, omdbClientMock);
        when(filmMock.getId()).thenReturn(FILM_ID);
        when(filmMock.getNumReviews()).thenReturn(numOfReviews);
        when(filmMock.getTotalScore()).thenReturn(totalScore);
        when(filmMock.getCopiesInStock()).thenReturn(copiesInStock);
        when(filmMock.getNumReviews()).thenReturn(numOfReviews);
        when(filmMock.getTotalScore()).thenReturn(totalScore);
        when(filmRepositoryMock.getFilm(FILM_ID)).thenReturn(filmMock);
    }



    @Test(expected = IllegalArgumentException.class)
    public void NullArgumentsTest(){
        new FilmService(null,null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void NullRepositoryTest(){
        new FilmService(null, omdbClientMock);
    }

    @Test(expected = IllegalArgumentException.class)
    public void NullOmdbClient(){
        new FilmService(filmRepositoryMock,null);
    }
    @Test
    public void notNull(){
        new FilmService(filmRepositoryMock, omdbClientMock);
    }

    @Test
    public void testAverageRatingNegNumReviews() {
        when(filmMock.getNumReviews()).thenReturn(-1);
        int retVal = service.getAverageRating(filmMock);
        assertEquals(retVal,0);
    }


    @Test
    public void AverageRatingTest(){
        //check if number of ratings is calculated correctly
        int averageRating = service.getAverageRating(filmMock);
        assertEquals(totalScore / numOfReviews,averageRating);
    }

    @Test
    public void testGetFilm(){
        Film retFilm = service.getFilm(FILM_ID);
        assertEquals(filmMock,retFilm);
    }

    @Test(expected = FilmNotFoundException.class)
    public void testGetNullFilm(){
        when(filmRepositoryMock.getFilm(FILM_ID)).thenReturn(null);
        service.getFilm(FILM_ID);
    }

    @Test
    public void testRentFilm(){
        ArgumentCaptor<Integer> copiesInStockCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> filmIdCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Film> filmArgumentCaptor = ArgumentCaptor.forClass(Film.class);

        Film rentedFilm = service.rentFilm(FILM_ID);

        verify(filmMock).setCopiesInStock(copiesInStockCaptor.capture());
        assertEquals(copiesInStock - 1,(int)copiesInStockCaptor.getValue());

        verify(filmRepositoryMock).updateFilm(filmIdCaptor.capture(),filmArgumentCaptor.capture());
        assertEquals(filmMock,filmArgumentCaptor.getValue());
        assertEquals(FILM_ID,(int)filmIdCaptor.getValue());

        assertEquals(filmMock,rentedFilm);
    }

    @Test(expected = NotInStockException.class)
    public void testRentNotInStock(){
        when(filmMock.getCopiesInStock()).thenReturn(-1);
        service.rentFilm(FILM_ID);
    }

    @Test(expected = FilmNotFoundException.class)
    public void testReturnFilmNotInStock(){
        final int FILM_ID_THIS_TEST = 2;
        when(filmRepositoryMock.getFilm(FILM_ID_THIS_TEST)).thenReturn(null);
        service.returnFilm(FILM_ID_THIS_TEST, filmRating);
    }
    @Test
    public void testReturnFilm(){
        ArgumentCaptor<Integer> repoUpdateFilmFilmId = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Film> repoUpdateFilmFilm = ArgumentCaptor.forClass(Film.class);
        ArgumentCaptor<Integer> filmSetCopiesInStock = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> fimSetNumReviews = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> filmSetTotalScore = ArgumentCaptor.forClass(Integer.class);

        Film returnedFilm = service.returnFilm(FILM_ID, filmRating);

        verify(filmMock).setCopiesInStock(filmSetCopiesInStock.capture());
        verify(filmMock).setNumReviews(fimSetNumReviews.capture());
        verify(filmMock).setTotalScore(filmSetTotalScore.capture());
        verify(filmRepositoryMock).updateFilm(repoUpdateFilmFilmId.capture(),repoUpdateFilmFilm.capture());


        assertEquals(Integer.valueOf(copiesInStock +1),filmSetCopiesInStock.getValue());
        assertEquals(Integer.valueOf(numOfReviews + 1),fimSetNumReviews.getValue());
        assertEquals(Integer.valueOf(totalScore + filmRating),filmSetTotalScore.getValue());

        assertEquals(filmMock,repoUpdateFilmFilm.getValue());
        assertEquals(Integer.valueOf(FILM_ID),repoUpdateFilmFilmId.getValue());
        assertEquals(filmMock,returnedFilm);
    }
    @Test
    public void testFilmCatalogOutput(){
        final int film2Id = 2;
        final int film2CopiesInStock = 10;
        final int film2NumOfReviews = 8;
        final int film2TotalScore = 74;
        final String film2Title = "Detective Pikachu";
        final String film2Imdbscore = "93";
        final String film2MetacriticScore = "89";

        Collection<Film> filmCollection = new ArrayList<>();
        filmCollection.add(filmMock);
        filmCollection.add(film2Mock);

        when(filmRepositoryMock.getFilms()).thenReturn(filmCollection);

        when(filmMock.getTitle()).thenReturn(filmTitle);


        when(film2Mock.getId()).thenReturn(film2Id);
        when(film2Mock.getTitle()).thenReturn(film2Title);
        when(film2Mock.getCopiesInStock()).thenReturn(film2CopiesInStock);
        when(film2Mock.getTotalScore()).thenReturn(film2TotalScore);
        when(film2Mock.getNumReviews()).thenReturn(film2NumOfReviews);

        when(omdbClientMock.getFilmDetails(filmTitle)).thenReturn(film1DetailsMock);
        when(omdbClientMock.getFilmDetails(film2Title)).thenReturn(film2DetailsMock);

        when(film1DetailsMock.getImdbRating()).thenReturn(imdbScore);
        when(film1DetailsMock.getMetascore()).thenReturn(metacriticScore);

        when(film2DetailsMock.getImdbRating()).thenReturn(film2Imdbscore);
        when(film2DetailsMock.getMetascore()).thenReturn(film2MetacriticScore);

        String retString = service.getFilmCatalog();

        System.out.println(retString);

        String expectedString =
                "\n-----------------------------------------------" +
                        "\nFilm\nID: " + FILM_ID + "\nName: " + filmTitle +
                        "\nCopies in stock: " + copiesInStock +
                        "\nRating: " + (totalScore / numOfReviews) + "\nBased on " + numOfReviews + " reviews" +
                        "\nIMDB rating: " + imdbScore +
                        "\nMetacritic rating: " + metacriticScore +
                        "\n-----------------------------------------------" +
                        "\nFilm\nID: " + film2Id + "\nName: " + film2Title +
                        "\nCopies in stock: " + film2CopiesInStock +
                        "\nRating: " + (film2TotalScore / film2NumOfReviews) + "\nBased on " + film2NumOfReviews + " reviews" +
                        "\nIMDB rating: " + film2Imdbscore +
                        "\nMetacritic rating: " + film2MetacriticScore +
                        "\n-----------------------------------------------";
        assertEquals(expectedString,retString);

    }
}
