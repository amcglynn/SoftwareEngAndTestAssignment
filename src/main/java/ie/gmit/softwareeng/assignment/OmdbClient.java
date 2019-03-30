package ie.gmit.softwareeng.assignment;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;

public class OmdbClient {
    private String apiKey;

    public OmdbClient(String apiKey) {
        this.apiKey = apiKey;
    }

    public OmdbFilmDetails getFilmDetails(String filmName) {
        String responseString = "";
        OmdbFilmDetails filmDetails = null;

        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet request = new HttpGet("http://www.omdbapi.com/?t=" + URLEncoder.encode(filmName, "UTF-8") + "&apikey=" + apiKey);
            request.addHeader("accept", "application/json");
            HttpResponse response = httpClient.execute(request);

            BufferedReader br = new BufferedReader(
                    new InputStreamReader((response.getEntity().getContent())));

            String output;

            while ((output = br.readLine()) != null) {
                responseString += output;
            }

            httpClient.getConnectionManager().shutdown();

            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
            filmDetails = mapper.readValue(responseString, OmdbFilmDetails.class);
        } catch (Exception e) {
            System.err.println("Could not read film details from omdbapi.com for " + filmName);
            filmDetails = new OmdbFilmDetails("Unknown", "Unknown");
        }

        return filmDetails;
    }
}
