package ie.gmit.softwareeng.assignment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class Tests {

    FilmService filmService;
    int id;


    @Mock
    FilmRepository repository;

    @Mock
    OmdbClient omdbClient;

    @Mock
    Film film;

    @Before
    public void setup(){
        filmService = new FilmService(repository, omdbClient);
        id = 1;
        film = new Film(id, "Eds film :)", 5);
    }

    @Test(expected = FilmNotFoundException.class)
    public void testGetFilmInvalidID(){
        when(repository.getFilm(id)).thenReturn(null);
        filmService.getFilm(id);
    }

    @Test(expected = FilmNotFoundException.class)
    public void testRentFilmInvalidID(){
        when(repository.getFilm(id)).thenReturn(null);
        filmService.rentFilm(id);
    }

    @Test(expected = NotInStockException.class)
    public void testRentFilmNoStock(){
        Film film = new Film(2, "Eds movie 2 electric boogaloo", 0);
        when(repository.getFilm(id)).thenReturn(film);
//        when(film.getCopiesInStock()).thenReturn(0); // why doesn't this work ? :(
        filmService.rentFilm(id);
    }

    @Test
    public void testSuccessfulRent(){
        when(repository.getFilm(id)).thenReturn(film);
        int originalCopyCount = film.getCopiesInStock();
        int currentCopyCount = filmService.rentFilm(id).getCopiesInStock();
        assertEquals(originalCopyCount - 1, currentCopyCount );
    }

    @Test(expected = FilmNotFoundException.class)
    public void testReturnInvalidID(){
        when(repository.getFilm(id)).thenReturn(null);
        filmService.returnFilm(id, 1);
    }


}