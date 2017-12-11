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
import com.google.api.services.youtube.model.SearchListResponse;

import app.api.CustomSearchAPI;
import app.api.YoutubeAPI;
import app.util.ResultListParser;
import app.model.*;
import com.omertron.imdbapi.*;
import com.omertron.imdbapi.model.*;

/**
 * @author Isolachine
 *
 */
@Controller
@RestController
public class ResultController {
    @RequestMapping("youtube")
    @ResponseBody
    public SearchListResponse youtube(@RequestParam(value = "query", required = true, defaultValue = "") String query) {
        try {
            return new YoutubeAPI().search(query);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

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
    public Model results(@RequestParam(value = "query", required = true, defaultValue = "") String query, Model model) throws JsonParseException, JsonMappingException, ImdbException, IOException {
        // List<Result> results = new ArrayList<>();
        // try {
        // results = new CustomSearchAPI().cse(query);
        // model.addAttribute("results", results);
        // } catch (GeneralSecurityException e) {
        // model.addAttribute("results", results);
        // e.printStackTrace();
        // } catch (IOException e) {
        // model.addAttribute("results", results);
        // e.printStackTrace();
        // }

        ObjectMapper mapper = new ObjectMapper();
        List<Result> results = mapper.readValue(new File("ff.json"), new TypeReference<List<Result>>() {
        });

        // results = new ResultListParser().parseCSEList(results);
        List<Movie> movies = new ResultListParser().parseCSEList(results);

        model.addAttribute("query", query);
        model.addAttribute("count", movies.size());
        model.addAttribute("results", movies);
        return model;
    }

}
