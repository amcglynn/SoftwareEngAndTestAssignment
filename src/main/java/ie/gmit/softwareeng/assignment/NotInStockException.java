package ie.gmit.softwareeng.assignment;

public class NotInStockException extends RuntimeException {
    public NotInStockException(int filmId) {
        super("Film not in stock: " + filmId);
    }
}
