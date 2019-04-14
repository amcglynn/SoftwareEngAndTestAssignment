package ie.gmit.softwareeng.assignment;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.management.MonitorInfo;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FilmServiceTest {

    @Mock
    FilmRepository repositoryMock;

    @Mock
    FilmApplication applicationMock;


    @Mock
    OmdbClient omdbClient;


    @Mock
    private OmdbFilmDetails omdbFilm;

    @Mock
    Film film1;

    @Mock
    Film id,  title, copiesInStock;




    @Test (expected = FilmNotFoundException.class)
    public void getFilmNull() {
        FilmService service = new FilmService(repositoryMock, omdbClient);
        int id =1;
        Mockito.when(repositoryMock.getFilm(id)).thenReturn(null);
        service.getFilm(id);
    }

    @Test
    public void getFilmName() {
        FilmService service = new FilmService(repositoryMock, omdbClient);
        Film film = new Film(1, "Batman Begins", 5);
        int id = 1;
        Mockito.when(repositoryMock.getFilm(id)).thenReturn(film);
        assertEquals("Batman Begins", film.getTitle());
        service.getFilm(1);
    }

    @Test
    public void rentFilmInStock(){
        FilmService service = new FilmService(repositoryMock, omdbClient);
        Film MockFilm = new Film(1, "Batman Begins", 5);
        Mockito.when(repositoryMock.getFilm(1)).thenReturn(MockFilm);
        assertEquals(5, MockFilm.getCopiesInStock());
        service.rentFilm(1);
    }

    @Test (expected = NotInStockException.class)
    public void rentFilmNotInStock(){
        FilmService service = new FilmService(repositoryMock, omdbClient);
        Mockito.when(repositoryMock.getFilm(-1)).thenReturn(film1);
        Mockito.when(film1.getCopiesInStock()).thenReturn(-1);
        service.rentFilm(-1);
    }


    @Test (expected = FilmNotFoundException.class)
    public void rentFilmNull() {
        FilmService service = new FilmService(repositoryMock, omdbClient);
        int id =1;
        Mockito.when(repositoryMock.getFilm(id)).thenReturn(null);
        service.rentFilm(id);
    }

    @Test (expected = FilmNotFoundException.class)
    public void returnFilmNull(){
        FilmService service = new FilmService(repositoryMock, omdbClient);
        int id =1;
        Mockito.when(repositoryMock.getFilm(id)).thenReturn(null);
        service.returnFilm(id, 5);
    }

    @Test
    public void returnFilmPass(){
        FilmService service = new FilmService(repositoryMock, omdbClient);
        Film MockFilm = new Film(1, "Batman Begins", 5);
        int id =1;
        Mockito.when(repositoryMock.getFilm(id)).thenReturn(MockFilm);
        service.returnFilm(1,5);
    }

    @Test
    public void getFileRating(){
        FilmService service = new FilmService(repositoryMock, omdbClient);
        //Film MockFilm = new Film(1, "Batman Begins", 5);
        omdbFilm = Mockito.mock(OmdbFilmDetails.class);
        String name = "Batman Begins";
        String IMDB_RATING = "4.4";


        when(omdbClient.getFilmDetails(name)).thenReturn(omdbFilm);
        when(omdbFilm.getImdbRating()).thenReturn(IMDB_RATING);

        Mockito.when(omdbFilm.getImdbRating()).thenReturn(IMDB_RATING);
        assertEquals("4.4", omdbFilm.getImdbRating());
        //service.rentFilm(1);
    }


}