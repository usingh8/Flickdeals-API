package edu.asu.ser594.flickdeals.model;

public class MovieReview {
	
	private int movieId;
	private String source;
	private String author;
	private String reviewContent;
	
	public MovieReview(int movieId, String source, String author,
			String reviewContent, String reviewDate) {
		
		this.movieId = movieId;
		this.source = source;
		this.author = author;
		this.reviewContent = reviewContent;
		this.reviewDate = reviewDate;
	}
	public int getMovieId() {
		return movieId;
	}
	public void setMovieId(int movieId) {
		this.movieId = movieId;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getReviewContent() {
		return reviewContent;
	}
	public void setReviewContent(String reviewContent) {
		this.reviewContent = reviewContent;
	}
	public String getReviewDate() {
		return reviewDate;
	}
	public void setReviewDate(String reviewDate) {
		this.reviewDate = reviewDate;
	}
	private String reviewDate;

}
