package ie.gmit.softwareeng.assignment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
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
    public void testGetFilmInvalidFilm(){
        when(repository.getFilm(id)).thenReturn(null);
        filmService.getFilm(id);
    }

    @Test(expected = FilmNotFoundException.class)
    public void testRentFilmInvalidFilm(){
        when(repository.getFilm(id)).thenReturn(null);
        filmService.rentFilm(id);
    }

    @Test(expected = NotInStockException.class)
    public void testRentFilmNoStock(){
        Film film2 = new Film(2, "Eds film 2 electric boogaloo", 0);
        when(repository.getFilm(id)).thenReturn(film2);
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
    public void testReturnInvalidFilm(){
        when(repository.getFilm(id)).thenReturn(null);
        filmService.returnFilm(id, 1);
    }

    @Test
    public void testReview(){
        Film mockFilm = org.mockito.Mockito.mock(Film.class);
        when(mockFilm.getTotalScore()).thenReturn(6);

        when(mockFilm.getNumReviews()).thenReturn(3);
        assertEquals(2, filmService.getAverageRating(mockFilm));
    }

    @Test
    public void testCatalog(){
        OmdbFilmDetails filmDetails = org.mockito.Mockito.mock(OmdbFilmDetails.class);

        Map<Integer, Film> entrySet = new HashMap<>();
        entrySet.put(1, new Film(1, "Eds film", 5));
        when(repository.getFilms()).thenReturn(entrySet.values());

        when(omdbClient.getFilmDetails("Eds film")).thenReturn(filmDetails);
        when(filmDetails.getImdbRating()).thenReturn("10/10");
        when(filmDetails.getMetascore()).thenReturn("99");

        System.out.println(filmService.getFilmCatalog());
    }
}
