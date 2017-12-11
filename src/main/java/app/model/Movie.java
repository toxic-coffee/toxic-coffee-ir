package app.model;

public class Movie {
	public String id;
	public String title;
	public String url;
	public String youtubeUrl;
	public int year;
	public String snippet;
	public float rating;
	public int voting;
	public String posterUrl;
	public Movie () {
		id = "N/A";
		title = "N/A";
		year = 0;
		url = "N/A";
		youtubeUrl = "http://www.youtube.com/";
		snippet = "N/A";
		rating = 0;
		voting = 0;
		posterUrl = "N/A";
	}
}