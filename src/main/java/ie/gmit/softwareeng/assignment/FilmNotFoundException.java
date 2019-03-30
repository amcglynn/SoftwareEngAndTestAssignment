package ie.gmit.softwareeng.assignment;

public class FilmNotFoundException extends RuntimeException {
    public FilmNotFoundException(int filmId) {
        super("Film not found: " + filmId);
    }
}
