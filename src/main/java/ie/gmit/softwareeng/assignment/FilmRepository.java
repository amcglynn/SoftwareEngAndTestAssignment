package ie.gmit.softwareeng.assignment;

import java.util.Collection;

public interface FilmRepository {

    Collection<Film> getFilms();
    Film getFilm(int id);
    void updateFilm(int filmId, Film film);
}
