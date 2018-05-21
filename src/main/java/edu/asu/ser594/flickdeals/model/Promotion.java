package edu.asu.ser594.flickdeals.model;

public class Promotion {

	private String movieId;
	private String movieName;
	private String location;
	private String source;
	private String details;
	private String date;
	
	public Promotion(String movieName, String location, String details, String source, String date)
	{
		this.location = location;
		this.source = source;
		this.date = date;
		this.movieName = movieName;
		this.details = details;
	}
	public String getMovieId() {
		return movieId;
	}
	public void setMovieId(String movieId) {
		this.movieId = movieId;
	}
	public String getMovieName() {
		return movieName;
	}
	public void setMovieName(String movieName) {
		this.movieName = movieName;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
}
