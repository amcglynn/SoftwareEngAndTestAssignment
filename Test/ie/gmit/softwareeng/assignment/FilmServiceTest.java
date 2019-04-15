package ie.gmit.softwareeng.assignment;


import org.junit.Test;

import org.mockito.Mock;
import org.mockito.Mockito;


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
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;



@RunWith(MockitoJUnitRunner.class)
public class FilmServiceTest {
    int id = 0;


    @Mock
    FilmService filmService;

    @Mock
    FilmRepository filmRepositoryMock;

    @Mock
    OmdbClient omdbClient;

    @Mock
    Film film;

    @Before
    public void setup() {
        filmService = new FilmService(filmRepositoryMock, omdbClient);
        id = 1;
        film = new Film(id, "Big Boobs XXX:)", 5);
    }


    @Test(expected = FilmNotFoundException.class)
    public void getFilmNull() {
        FilmService service = new FilmService(filmRepositoryMock, omdbClient);
        int id = 1;
        Mockito.when(filmRepositoryMock.getFilm(id)).thenReturn(null);
        service.getFilm(id);

    }

    @Test
    public void getFilmName() {
        FilmService service = new FilmService(filmRepositoryMock, omdbClient);
        Film film = new Film(1, "Batman Begins", 5);
        int id = 1;
        Mockito.when(filmRepositoryMock.getFilm(id)).thenReturn(film);
        assertEquals("Batman Begins", film.getTitle());
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
        when(filmRepositoryMock.getFilm(1)).thenReturn(film);
        when(film.getCopiesInStock()).thenReturn(1);
        assertEquals(filmService.rentFilm(1), film);
    }

    public void returnFilm(){
        FilmService service = new FilmService(filmRepositoryMock, omdbClient);
        Film MockFilm = new Film(1, "Batman Begins", 5);
        int id =1;
        Mockito.when(filmRepositoryMock.getFilm(id)).thenReturn(MockFilm);
        service.returnFilm(1,5);
    }


}
