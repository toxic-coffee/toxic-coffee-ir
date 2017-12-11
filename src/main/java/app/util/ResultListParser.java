/**
 * 
 */
package app.util;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.services.customsearch.model.Result;

import app.api.CustomSearchAPI;

/**
 * @author Isolachine
 *
 */
public class ResultListParser {
    class WeightedResults implements Comparable<Object> {
        Result result;
        int weight;

        WeightedResults(Result r, int w) {
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
     * @throws IOException
     * @throws GeneralSecurityException
     */
    public List<Result> parseCSEList(List<Result> results) throws GeneralSecurityException, IOException {
        Map<String, WeightedResults> map = new HashMap<String, WeightedResults>();
        for (int i = 0; i < results.size(); i++) {
            Result r = results.get(i);
            boolean isMain = false;
            String u = Util.parseUrl(r.getFormattedUrl().toLowerCase());
            if (r.getFormattedUrl().toLowerCase().equals(u))
                isMain = true;
            if (!u.equals("")) {
                if (isMain || !map.containsKey(u))
                    map.put(u, new WeightedResults(r, map.getOrDefault(u, new WeightedResults(null, 0)).weight + results.size() - i));
                else
                    map.put(u, new WeightedResults(map.get(u).result, map.getOrDefault(u, new WeightedResults(null, 0)).weight + results.size() - i));
            } else
                continue;
        }
        List<WeightedResults> tmpList = new ArrayList<WeightedResults>(map.values());
        Collections.sort(tmpList);
        List<Result> newList = new ArrayList<Result>();
        for (WeightedResults wr : tmpList) {
            String url = Util.parseUrl(wr.result.getFormattedUrl().toLowerCase());
            if (!wr.result.getFormattedUrl().equals(url)) {
                List<Result> mainPage;
                if (Const.DEV_ENVIRONMENT) {
                    ObjectMapper mapper = new ObjectMapper();
                    mainPage = mapper.readValue(new File(Const.TEST_JSON), new TypeReference<List<Result>>() {
                    });
                } else {
                    mainPage = new CustomSearchAPI().cse(url);
                    if (mainPage.size() > 0 && mainPage.get(0).getFormattedUrl().equals(url)) {
                        wr.result = mainPage.get(0);
                    }
                }
            }

            wr.result.setLink(Util.parseLink(wr.result.getLink().toLowerCase()));
            wr.result.setHtmlFormattedUrl(Util.parseUrl(wr.result.getHtmlFormattedUrl().toLowerCase()));
            wr.result.setFormattedUrl(Util.parseUrl(wr.result.getFormattedUrl().toLowerCase()));
            String year = Util.parseTitle(wr.result.getTitle())[1].equals("") ? "" : " (" + Util.parseTitle(wr.result.getTitle())[1] + ")";
            wr.result.setTitle(Util.parseTitle(wr.result.getTitle())[0] + year);

            newList.add(wr.result);
        }
        return newList;
    }

}
