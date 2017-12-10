/**
 * 
 */
package app.api;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;

public class YoutubeAPI {

    /** Application name. */
    private static final String APPLICATION_NAME = "toxic_coffee";

    /** Directory to store user credentials for this application. */
    private static final java.io.File DATA_STORE_DIR = new java.io.File(System.getProperty("user.home"), ".credentials/java-youtube-api-tests");

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    /** Global instance of the HTTP transport. */
    private static HttpTransport HTTP_TRANSPORT;

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Creates an authorized Credential object.
     * 
     * @return an authorized Credential object.
     * @throws IOException
     */
    private Credential authorize() throws IOException {
        InputStream in = YoutubeAPI.class.getResourceAsStream("/toxic-coffee.json");
        GoogleCredential credential = GoogleCredential.fromStream(in)
                .createScoped(Collections.singleton("https://www.googleapis.com/auth/youtube.force-ssl"));
        
        System.out.println("Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        return credential;
    }

    /**
     * Build and return an authorized API client service, such as a YouTube Data
     * API client service.
     * 
     * @return an authorized API client service
     * @throws IOException
     */
    private YouTube getYouTubeService() throws IOException {
        Credential credential = authorize();
        return new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME).build();
    }

    public SearchListResponse search(String query) throws IOException {

        YouTube youtube = getYouTubeService();

        try {
            HashMap<String, String> parameters = new HashMap<>();
            parameters.put("part", "snippet");
            parameters.put("maxResults", "25");
            parameters.put("q", query);
            parameters.put("type", "video");
            parameters.put("videoType", "movie");
            parameters.put("videoDuration", "long");
            
            

            YouTube.Search.List searchListByKeywordRequest = youtube.search().list(parameters.get("part").toString());
            if (parameters.containsKey("maxResults")) {
                searchListByKeywordRequest.setMaxResults(Long.parseLong(parameters.get("maxResults").toString()));
            }

            if (parameters.containsKey("q") && parameters.get("q") != "") {
                searchListByKeywordRequest.setQ(parameters.get("q").toString());
            }

            if (parameters.containsKey("type") && parameters.get("type") != "") {
                searchListByKeywordRequest.setType(parameters.get("type").toString());
            }
            
            if (parameters.containsKey("videoType") && parameters.get("videoType") != "") {
                searchListByKeywordRequest.setVideoType(parameters.get("videoType").toString());
            }

            if (parameters.containsKey("videoDuration") && parameters.get("videoDuration") != "") {
                searchListByKeywordRequest.setVideoDuration(parameters.get("videoDuration").toString());
            }

            SearchListResponse response = searchListByKeywordRequest.execute();
            return response;
            
        } catch (GoogleJsonResponseException e) {
            e.printStackTrace();
            System.err.println("There was a service error: " + e.getDetails().getCode() + " : " + e.getDetails().getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }
        
        return null;
    }
}