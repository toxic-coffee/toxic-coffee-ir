/**
 * 
 */
package app.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Isolachine
 *
 */
public class Util {
    /**
     * [parse IMDb entry title in google CSE SERP]
     * 
     * @param title
     *            [entry title in SERP, contains movie title, year, etc]
     * @return [String array, String[0] is movie title, String[1] is year]
     */
    public String[] parseTitle(String title) {
        int idxYear = indexOf(Pattern.compile("\\(\\d{4}\\)"), title);
        return new String[] { title.substring(0, idxYear), title.substring(idxYear + 1, idxYear + 5) };
    }

    /**
     * [parse IMDb url in google CSE SERP]
     * 
     * @param url
     *            [IMDb url in SERP]
     * @return [clean IMDb url]
     */
    public String parseUrl(String url) {
        int idxId = indexOf(Pattern.compile("\\/tt\\d{7}"), url);
        return url.substring(0, idxId + 10);
    }

    /**
     * [search for regular expression matching in given string, return the first index]
     * 
     * @param pattern
     *            [regular expression pattern]
     * @param s
     *            [target string]
     * @return [index of first occurance of pattern]
     */
    public int indexOf(Pattern pattern, String s) {
        Matcher matcher = pattern.matcher(s);
        return matcher.find() ? matcher.start() : -1;
    }
}
