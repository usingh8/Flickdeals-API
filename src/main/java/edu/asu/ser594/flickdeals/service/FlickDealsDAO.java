package edu.asu.ser594.flickdeals.service;

import java.util.List;

import edu.asu.ser594.flickdeals.model.Movie;
import edu.asu.ser594.flickdeals.model.MovieReview;
import edu.asu.ser594.flickdeals.model.Person;
import edu.asu.ser594.flickdeals.model.Promotion;

public interface FlickDealsDAO {


	public List<Movie> getAllMovies( String[] genre, String title, String startDate,String endDate, int page, int limit, String sortby);
	public int getTotalMovieResultCount( String[] genre, String title, String startDate,String endDate, int page);
	public Movie getMovieDetail(int movieId);
	public List<Person> getAllCast( int movieId);
	public Person getCompleteCastDetail( String castId);
	public List<Person> searchCastByName(String castName, int page, int limit, String sortBy);
	public int getAllPromotionsCount( String movieName, String location, String startDate, String endDate);
	public List<Promotion> getAllPromotions( String movieName, String location, String startDate, String endDate,int page ,int limit, String sortby);
	public int getResultCountOfSearchCastByName(String query);
	public List<MovieReview> getMovieReviewsByMovieID(int movieId);
}
