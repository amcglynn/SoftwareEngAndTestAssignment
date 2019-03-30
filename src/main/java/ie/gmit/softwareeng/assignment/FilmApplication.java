package ie.gmit.softwareeng.assignment;

import java.util.Scanner;

/***
 * Command Line Application that demonstrates the functionality of the project
 */
public class FilmApplication {

    private Scanner scanner;
    private FilmService service;
    private FilmRepository filmRepository;

    public FilmApplication() {
        scanner = new Scanner(System.in);
        filmRepository = new InMemoryFilmRepository();
        service = new FilmService(filmRepository);

        while (true) {
            System.out.println("Would you like to do?");
            System.out.println("1. Display Catalog");
            System.out.println("2. Rent Film");
            System.out.println("3. Return Film");

            int selection = scanner.nextInt();

            try {
                if (selection == 1) {
                    displayCatalog();
                } else if (selection == 2) {
                    rentFilm();
                } else if (selection == 3) {
                    returnFilm();
                } else {
                    System.out.println("Invalid input");
                }
            } catch (FilmNotFoundException | NotInStockException f) {
                System.out.println(f.getMessage());
                System.out.println("Please try again\n");
            }
        }
    }

    public void displayCatalog() {
        System.out.println("Film Catalog: " + service.getFilmCatalog());
    }

    public void rentFilm() {
        System.out.print("Enter a film ID to rent: ");
        int id = scanner.nextInt();
        Film film = service.getFilm(id);

        System.out.println("Film details: " + film);
        System.out.print("Do you want to proceed and rent this film? (Y\\N)");
        String yesno = scanner.next();

        if (yesno.toUpperCase().charAt(0) == 'Y') {
            service.rentFilm(id);
            System.out.println("Film rented.");
        }
        else {
            System.out.println("OK, film not rented.");
        }
    }

    public void returnFilm() {
        System.out.print("Enter a film ID to return: ");
        int id = scanner.nextInt();

        System.out.print("Enter your rating: ");
        int rating = scanner.nextInt();

        service.returnFilm(id, rating);
    }

    public static void main(String[] args) {
        new FilmApplication();
    }
}
