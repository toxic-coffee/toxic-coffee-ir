/**
 * 
 */
package app.api;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.customsearch.Customsearch;
import com.google.api.services.customsearch.CustomsearchRequestInitializer;
import com.google.api.services.customsearch.model.Result;
import com.google.api.services.customsearch.model.Search;

/**
 * @author Isolachine
 *
 */
public class CustomSearchAPI {
    private static final String GOOGLE_API_KEY = "AIzaSyAg_84WojpTo_p8T64rcLQoI-ARWYRvbAc";
    private static final String SEARCH_ENGINE_ID = "010650652691021911609:sbzherohkn4";

    public List<Result> cse(String query) throws GeneralSecurityException, IOException {
        Customsearch customsearch = new Customsearch.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), null)
                                                    .setApplicationName("toxic_coffee")
                                                    .setGoogleClientRequestInitializer(new CustomsearchRequestInitializer(GOOGLE_API_KEY))
                                                    .build();
        
        Customsearch.Cse.List list = customsearch.cse().list(query).setCx(SEARCH_ENGINE_ID);

        Search results = list.execute();
        List<Result> resultList = results.getItems();
        return resultList;
    }
}
