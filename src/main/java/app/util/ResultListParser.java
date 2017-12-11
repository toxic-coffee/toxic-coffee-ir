/**
 * 
 */
package app.util;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

import com.google.api.services.customsearch.model.Result;

import app.model.*;
import com.omertron.imdbapi.*;
import com.omertron.imdbapi.model.*;

/**
 * @author Isolachine
 *
 */
public class ResultListParser {
    class WeightedResults implements Comparable<Object> {
        Object result;
        int weight;
        WeightedResults(Object r, int w) {
            this.result = r;
            this.weight = w;
        }
        public int compareTo(Object rhs) {
            WeightedResults other = (WeightedResults) rhs;
            int diff = weight - other.weight;
            if (diff == 0) return 0;
            else if (diff < 0) return 1;
            else return -1;
        }
    }

    /**
     * [parse the results list and return sorted list]
     * @param  results       [results list from CSE]
     * @return               [sorted results list, based on original position and times of appearance]
     * @throws ImdbException [ImdbApi exceptions]
     */
    public List<Movie> parseCSEList(List<Result> results) throws ImdbException{
        Map<String, WeightedResults> map = new HashMap<String, WeightedResults>();
        ImdbApi imdb = new ImdbApi();
        for (int i = 0; i < results.size(); i++) {
            Result r = results.get(i);
            String id = Util.urlToId(r.getFormattedUrl().toLowerCase());
            if (!id.equals("")) {
                if (!map.containsKey(id)) {
                    Movie m = new Movie();
                    m.id = id;
                    m.url = "www.imdb.com/title/" + id;
                    m.snippet = r.getSnippet();
                    List<ImdbImage> photos = imdb.getTitlePhotos(id);
                    if (photos.size() > 0)
                        m.posterUrl = photos.get(photos.size()-1).getImage().getUrl();
                    else
                        m.posterUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/5/52/Coffee_font_awesome.svg/512px-Coffee_font_awesome.svg.png";
                    ImdbMovieDetails details = imdb.getFullDetails(id);
                    m.title = details.getTitle();
                    m.year = details.getYear();
                    m.rating = details.getRating();
                    m.voting = (int)details.getNumVotes();
                    map.put(id, new WeightedResults(m, results.size() - i));
                }
                else
                    map.put(id, new WeightedResults(map.get(id).result, map.get(id).weight + results.size() - i));
            }
            else
                continue;
        }
        List<WeightedResults> tmpList = new ArrayList<WeightedResults>(map.values());
        Collections.sort(tmpList);
        List<Movie> newList = new ArrayList<Movie>();
        for (WeightedResults wr : tmpList) {
            newList.add((Movie)wr.result);
        }
        return newList;
    }

}
