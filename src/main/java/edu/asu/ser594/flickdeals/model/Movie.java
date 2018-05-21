package edu.asu.ser594.flickdeals.model;

import java.util.List;

public class Movie {
	
	private String title;
	private String overview;
	private int movieId;
	private int voteCount;
	private double averageVote;
	private boolean hasVideo;
	private String posterPath;
	private double popularity;
	private String language;
	private List<String> genres;
	private String backdrop_path;
	private boolean isAdultMovie;
	private String releaseDate;
	private List<Person> casts;
	private List<Promotion> promotions;
	private List<MovieReview> reviews;
	public Movie()
	{}
	public Movie(String title, String overview, int movieId, double averageVote, String posterPath,
			double popularity, String backdrop_path, String releaseDate) {
		super();
		this.title = title;
		this.overview = overview;
		this.movieId = movieId;
		this.averageVote = averageVote;
		this.posterPath = posterPath;
		this.popularity = popularity;
		this.backdrop_path = backdrop_path;
		this.releaseDate = releaseDate;
	}
	public Movie(int movieId,String title, String releaseDate, float popularity) {
		// TODO Auto-generated constructor stub
		this.popularity = popularity;
		this.movieId = movieId;
		this.title = title;
		this.releaseDate = releaseDate;
	}
	public List<MovieReview> getReviews() {
		return reviews;
	}
	public void setReviews(List<MovieReview> reviews) {
		this.reviews = reviews;
	}
	public List<Promotion> getPromotions() {
		return promotions;
	}
	public void setPromotions(List<Promotion> promotions) {
		this.promotions = promotions;
	}
	public List<Person> getCasts() {
		return casts;
	}
	public void setCasts(List<Person> casts) {
		this.casts = casts;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getOverview() {
		return overview;
	}
	public void setOverview(String overview) {
		this.overview = overview;
	}
	public int getMovieId() {
		return movieId;
	}
	public void setMovieId(int movieId) {
		this.movieId = movieId;
	}
	public int getVoteCount() {
		return voteCount;
	}
	public void setVoteCount(int voteCount) {
		this.voteCount = voteCount;
	}
	public double getAverageVote() {
		return averageVote;
	}
	public void setAverageVote(double averageVote) {
		this.averageVote = averageVote;
	}
	public boolean isHasVideo() {
		return hasVideo;
	}
	public void setHasVideo(boolean hasVideo) {
		this.hasVideo = hasVideo;
	}
	public String getPosterPath() {
		return posterPath;
	}
	public void setPosterPath(String posterPath) {
		this.posterPath = posterPath;
	}
	public double getPopularity() {
		return popularity;
	}
	public void setPopularity(double popularity) {
		this.popularity = popularity;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public List<String> getGenres() {
		return genres;
	}
	public void setGenres(List<String> genres) {
		this.genres = genres;
	}
	public String getBackdrop_path() {
		return backdrop_path;
	}
	public void setBackdrop_path(String backdrop_path) {
		this.backdrop_path = backdrop_path;
	}
	public boolean isAdultMovie() {
		return isAdultMovie;
	}
	public void setAdultMovie(boolean isAdultMovie) {
		this.isAdultMovie = isAdultMovie;
	}
	public String getReleaseDate() {
		return releaseDate;
	}
	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}
	

}
