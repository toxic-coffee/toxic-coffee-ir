/**
 * 
 */
package app.util;

import java.util.ArrayList;
import java.util.List;

import com.google.api.services.customsearch.model.Result;

/**
 * @author Isolachine
 *
 */
public class ResultListParser {
    class WeightedResults {
        int weight;
        Result result;
    }
    
    public List<Result> parseCSEList(List<Result> results) {
        List<Result> newList = new ArrayList<>();
        //TO-DO
        
        //weight each item
        
        // merge same id into a main page, if exists
        // otherwise merge to either one and set the url to main page url (use parse function in util)
        
        //sort by merged weight
        
        
        return newList;
    }
    
    
    
    
}
