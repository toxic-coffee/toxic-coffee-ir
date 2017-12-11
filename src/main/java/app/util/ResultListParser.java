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
            if (diff == 0)
                return 0;
            else if (diff < 0)
                return 1;
            else
                return -1;
        }
    }

    /**
     * [parse the results list and return sorted list]
     * 
     * @param results
     *            [results list from CSE]
     * @return [sorted results list, based on original position and times of appearance]
     */
    public List<Movie> parseCSEList(List<Result> results) throws ImdbException{
        Map<String, WeightedResults> map = new HashMap<String, WeightedResults>();
        // List<Result> newList = new ArrayList<Result>();
        // for (int i = 0; i < results.size(); i++) {
        //     Result r = results.get(i);
        //     boolean isMain = false;
        //     String u = Util.parseUrl(r.getFormattedUrl().toLowerCase());
        //     if (r.getFormattedUrl().toLowerCase().equals(u))
        //         isMain = true;
        //     if (!u.equals("")) {
        //         if (isMain || !map.containsKey(u))
        //             map.put(u, new WeightedResults(r, map.getOrDefault(u, new WeightedResults(null, 0)).weight + results.size() - i));
        //         else
        //             map.put(u, new WeightedResults(map.get(u).result, map.getOrDefault(u, new WeightedResults(null, 0)).weight + results.size() - i));
        //     } else
        //         continue;
        // }
        // List<WeightedResults> tmpList = new ArrayList<WeightedResults>(map.values());
        // Collections.sort(tmpList);
        // for (WeightedResults wr : tmpList) {
        //     Result r = (Result) wr.result;
        //     r.setLink(Util.parseLink(r.getLink().toLowerCase()));
        //     r.setHtmlFormattedUrl(Util.parseUrl(r.getHtmlFormattedUrl().toLowerCase()));
        //     r.setFormattedUrl(Util.parseUrl(r.getFormattedUrl().toLowerCase()));
        //     String year = Util.parseTitle(r.getTitle())[1].equals("") ? "" : " (" + Util.parseTitle(r.getTitle())[1] + ")";
        //     r.setTitle(Util.parseTitle(r.getTitle())[0] + year);
        //     newList.add(r);
        // }

        ImdbApi imdb = new ImdbApi();
        for (int i = 0; i < results.size(); i++) {
            Result r = results.get(i);
            String id = Util.urlToId(r.getFormattedUrl().toLowerCase());
            if (!id.equals("")) {
                if (!map.containsKey(id)) {
                    ImdbMovieDetails details = imdb.getFullDetails(id);
                    Movie m = new Movie();
                    m.id = id;
                    m.title = details.getTitle();
                    m.year = details.getYear();
                    m.url = "www.imdb.com/title/" + id;
                    m.snippet = r.getSnippet();
                    m.posterUrl = "https://cdn.eventcinemas.com.au/cdn/resources/movies/6/images/largeposter.jpg";
                    // m.snippet = details.getBestPlot().getOutline().equals("") ? details.getBestPlot().getSummary() : details.getBestPlot().getOutline();
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
