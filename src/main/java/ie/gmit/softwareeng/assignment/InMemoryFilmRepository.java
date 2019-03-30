package ie.gmit.softwareeng.assignment;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class InMemoryFilmRepository implements FilmRepository {
    private Map<Integer, Film> filmMap = new HashMap<>();

    public InMemoryFilmRepository() {
        filmMap.put(1, new Film(1, "Batman Begins", 5));
        filmMap.put(2, new Film(2, "Titanic", 20));
        filmMap.put(3, new Film(3, "Terminator", 30));
        filmMap.put(4, new Film(4, "The French Connection", 40));
        filmMap.put(5, new Film(5, "Star Wars", 50));
    }

    @Override
    public Collection<Film> getFilms() {
        return filmMap.values();
    }

    @Override
    public Film getFilm(int id) {
        return filmMap.get(id);
    }

    @Override
    public void updateFilm(int filmId, Film film) {
        filmMap.put(filmId, film);
    }
}
