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
    public static String[] parseTitle(String title) {
        int idxYear = indexOf(Pattern.compile("\\(\\d{4}\\)"), title);
        if (idxYear < 0) {
            int idxTypeYear = indexOf(Pattern.compile("\\([a-zA-Z\\s]+\\d{4}.*\\)"), title);
            if (idxTypeYear < 0) {
                int indexDash = indexOf(Pattern.compile("\\ \\-\\ "), title);
                if (indexDash < 0)
                    return new String[] { title, "" };
                else
                    return new String[] { title.substring(0, indexDash), "" };
            } else {
                idxYear = indexOf(Pattern.compile("\\d{4}"), title);
                return new String[] { title.substring(0, idxTypeYear), title.substring(idxYear, idxYear + 4) };
            }
        } else
            return new String[] { title.substring(0, idxYear), title.substring(idxYear + 1, idxYear + 5) };
    }

    /**
     * [parse IMDb url in google CSE SERP]
     * 
     * @param url
     *            [IMDb url in SERP]
     * @return [clean IMDb url]
     */
    public static String parseUrl(String url) {
        int idxId = indexOf(Pattern.compile("title\\/tt\\d{7}"), url);
        if (idxId < 0)
            return "";
        else
            return "www.imdb.com/" + url.substring(idxId, idxId + 16);
    }

    /**
     * [parse IMDb url in google CSE SERP]
     * 
     * @param url
     *            [IMDb url in SERP]
     * @return [clean IMDb url]
     */
    public static String parseLink(String link) {
        int idxId = indexOf(Pattern.compile("title\\/tt\\d{7}"), link);
        if (idxId < 0)
            return "";
        else
            return "http://www.imdb.com/" + link.substring(idxId, idxId + 16);
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
    public static int indexOf(Pattern pattern, String s) {
        Matcher matcher = pattern.matcher(s);
        return matcher.find() ? matcher.start() : -1;
    }
}
