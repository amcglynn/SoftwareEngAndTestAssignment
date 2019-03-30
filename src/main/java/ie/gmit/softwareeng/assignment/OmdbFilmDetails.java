package ie.gmit.softwareeng.assignment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OmdbFilmDetails {

    private String imdbRating;
    private String metascore;

    public OmdbFilmDetails() {
    }

    public OmdbFilmDetails(String metascore, String imdbRating) {
        this.metascore = metascore;
        this.imdbRating = imdbRating;
    }

    public String getImdbRating() {
        return imdbRating;
    }

    public void setImdbRating(String imdbRating) {
        this.imdbRating = imdbRating;
    }

    public String getMetascore() {
        return metascore;
    }

    public void setMetascore(String metascore) {
        this.metascore = metascore;
    }
}
