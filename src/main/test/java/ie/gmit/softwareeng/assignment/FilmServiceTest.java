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

public class FilmServiceTest {

    FilmService filmService;
    int id;

    @Mock
    FilmRepository repository;

    @Mock
    OmdbClient omdbClient;

    @Mock
    Film film;

    @Test (expected = FilmNotFoundException.class)
    public void getFilmTest() {
        FilmService service = new FilmService(repository, omdbClient);
        int id = 1;
        Mockito.when(repository.getFilm(id)).thenReturn(null);
        service.getFilm(id);
    }

    @Test
    public void getFilmNameTest() {
        FilmService = new FilmService(repository, omdbClient);
        Film film = new film(5, "Star Wars", 50);
        int id = 5;
        Mockito.when(repository.getFilm(5)).thenReturn(film);
        assertEquals(50, film.getCopiesInStock());
        service.rentFilm(5);
    }

    @Test (expected = FilmNotFoundException.class)
    public void rentFilmTest() {
        FilmService service = new FilmService(repository, omdbClient);
        int id = 1;
        Mockito.when(repositoryMock.getFilm(id)).thenReturn(null);
        service.rentFilm(id);
    }

    @Test (expected = FilmNotFoundException.class)
    public void returnFilmTest() {
        FilmService service = new FilmService(repository, omdbClient);
        int id = 1;
        Mockito.when(repository.getFilm(id)).thenReturn(null);
        service.returnFilm(id, 50);
    }

    @Test
    public void returnFilmName() {
        FilmService service = new FilmService(repository, omdbClient);
        Film film = new film(5, "Star Wars", 50);
        int id = 5;
        Mockito.when(repository.getFilm(id)).thenReturn(film);
        service.returnFilm(5, 50);
    }
}
