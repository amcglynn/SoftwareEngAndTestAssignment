package ie.gmit.softwareeng.assignment;


/***
 * Class that contains information about a film
 */
public class Film {
    private int id;
    private String title;
    private int copiesInStock;
    private int totalScore = 0;
    private int numReviews = 0;

    public Film(int id, String title, int copiesInStock) {
        this.id = id;
        this.title = title;
        this.copiesInStock = copiesInStock;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getCopiesInStock() {
        return copiesInStock;
    }

    public void setCopiesInStock(int copiesInStock) {
        this.copiesInStock = copiesInStock;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public int getNumReviews() {
        return numReviews;
    }

    public void setNumReviews(int numReviews) {
        this.numReviews = numReviews;
    }

    @Override
    public String toString() {
        return "Film " +
                "[title=" + title +
                ", id=" + id +
                ", copiesInStock=" + copiesInStock +
                ", numReviews=" + numReviews +
                ", totalScore=" + totalScore + "]";
    }
}
