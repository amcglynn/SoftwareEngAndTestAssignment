package ie.gmit.softwareeng.assignment;

import java.util.Collection;

public class FilmService {

    private final FilmRepository repository;
    private final OmdbClient omdbClient;

    /***
     * Constructor for the FilmService.
     * @param repository FilmRepository, must not be null
     */
    public FilmService(FilmRepository repository, OmdbClient omdbClient) {
        if (repository == null || omdbClient == null) {
            throw new IllegalArgumentException("Repository cannot be null");
        }
        this.repository = repository;
        this.omdbClient = omdbClient;
    }

    /***
     * Get the average rating of a film
     * @param film the film object which the average is being calculated on
     * @return the average rating of the film
     */
    public int getAverageRating(Film film) {

        if (film.getNumReviews() <= 0) {
            return 0;
        }
        else {
            return film.getTotalScore() / film.getNumReviews();
        }
    }

    /***
     * Get a string representation of the entire film catalog that is stored in the repository
     * @return
     */
    public String getFilmCatalog() {
        Collection<Film> films = repository.getFilms();

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("\n-----------------------------------------------");
        for (Film film: films) {
            OmdbFilmDetails filmDetails = omdbClient.getFilmDetails(film.getTitle());

            stringBuilder.append("\nFilm\nID: " + film.getId() + "\nName: " + film.getTitle() +
                    "\nCopies in stock: " + film.getCopiesInStock() +
                    "\nRating: " + getAverageRating(film) + "\nBased on " + film.getNumReviews() + " reviews" +
                    "\nIMDB rating: " + filmDetails.getImdbRating() +
                    "\nMetacritic rating: " + filmDetails.getMetascore());
            stringBuilder.append("\n-----------------------------------------------");
        }

        return stringBuilder.toString();
    }

    /***
     * Get a film based off of the filmId. Throws FilmNotFoundException if the film is not found in the repository
     * @param filmId ID of the film being looked up
     * @return Film object if found
     */
    public Film getFilm(int filmId) {
        Film film = repository.getFilm(filmId);

        if (film == null) {
            throw new FilmNotFoundException(filmId);
        }

        return film;
    }

    /***
     * Rent a film. This checks the stock level of the film and throws a NotInStockException if there are no copies left in stock.
     * When the film is rented, the number of copies is decremented
     * Throws FilmNotFoundException if the film was not in the repository
     * @param filmId ID of the film being rented
     * @return the film object that was successfully rented
     */
    public Film rentFilm(int filmId) {
        Film film = repository.getFilm(filmId);
        if (film == null) {
            throw new FilmNotFoundException(filmId);
        }

        int copiesInStock = film.getCopiesInStock();
        if (copiesInStock <= 0) {
            throw new NotInStockException(filmId);
        }

        copiesInStock--;

        film.setCopiesInStock(copiesInStock);
        repository.updateFilm(filmId, film);

        return film;
    }

    /***
     * Return a rented film and submit a rating.
     * Throws FilmNotFoundException if the film was not in the repository
     * @param filmId ID of the film being returned
     * @param rating the rating given for the film
     * @return the film object that was successfully returned
     */
    public Film returnFilm(int filmId, int rating) {
        Film film = repository.getFilm(filmId);
        if (film == null) {
            throw new FilmNotFoundException(filmId);
        }

        film.setCopiesInStock(film.getCopiesInStock() + 1);
        film.setNumReviews(film.getNumReviews() + 1);
        film.setTotalScore(film.getTotalScore() + rating);

        repository.updateFilm(filmId, film);

        return film;
    }
}
