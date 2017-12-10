/**
 * 
 */
package app.controller;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.services.customsearch.model.Result;

import app.api.CustomSearchAPI;

/**
 * @author Isolachine
 *
 */
@Controller
@RestController
public class ResultController {
    @RequestMapping("cse")
    @ResponseBody
    public List<Result> cse(@RequestParam(value = "query", required = true, defaultValue = "") String query) {
        try {
            return new CustomSearchAPI().cse(query);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @RequestMapping("results")
    public Model results(@RequestParam(value = "query", required = true, defaultValue = "") String query, Model model) throws JsonParseException, JsonMappingException, IOException {
//        try {
//            List<Result> results = new CustomSearchAPI().cse(query);
//            model.addAttribute("results", results);
//        } catch (GeneralSecurityException e) {
//            model.addAttribute("results", new ArrayList<>());
//            e.printStackTrace();
//        } catch (IOException e) {
//            model.addAttribute("results", new ArrayList<>());
//            e.printStackTrace();
//        }
//        List<Integer> arr = new ArrayList<>();
//        arr.add(1);
//        arr.add(2);
//        arr.add(3);
//        arr.add(3);
        
        ObjectMapper mapper = new ObjectMapper();
        List<Result> results = mapper.readValue(new File("fg.json"), new TypeReference<List<Result>>(){});
        System.out.println(results.get(0).getTitle());
        
        model.addAttribute("query", query);

        model.addAttribute("count", results.size());
        model.addAttribute("results", results);
        return model;
    }
    
}
