package edu.asu.ser594.flickdeals.resources;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.servlet.ServletContext;

import com.google.gson.Gson;

import edu.asu.ser594.flickdeals.model.Person;
import edu.asu.ser594.flickdeals.model.Results;
import edu.asu.ser594.flickdeals.service.FlickDealsDAO;
import edu.asu.ser594.flickdeals.service.impl.FlickDealsDAOImpl;

@Path("movie")
public class Movie {

	private FlickDealsDAO flickDealService;
	private Gson gson;
	

	public Movie() {
		this.flickDealService = new FlickDealsDAOImpl();
		gson = new Gson();
	}

	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public String getAllMovies(@QueryParam("genre") String genre, @QueryParam("title") String title,
			@QueryParam("page") int page,@QueryParam("start") String startDate,@QueryParam("sortby") String sortby,@QueryParam("end") String endDate,@QueryParam("limit") int limit) {
		limit = limit == 0? 20:limit;
		page = page == 0? 1:page;
		System.out.println("genre : "+genre);
		
		sortby = sortby == null ? "DESC":sortby;
		String genres [] = genre==null? null:genre.split(",");
		int count = flickDealService.getTotalMovieResultCount(genres, title, startDate, endDate, page);
		int totalpages = count%limit==0?count/limit:(count/limit)+1;
		int recordsOnCurrentPage = totalpages == page?count%limit:limit;
		List<edu.asu.ser594.flickdeals.model.Movie> movies = flickDealService.getAllMovies( genres, title, startDate, endDate, page, limit, sortby); 
		return gson.toJson(new Results(count, totalpages, page, movies, recordsOnCurrentPage));
	}

	@GET
	@Path("/{movieId}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getMovieDetail(@PathParam("movieId") int movieId) {
		
		edu.asu.ser594.flickdeals.model.Movie movie = flickDealService.getMovieDetail( movieId);
		movie.setCasts(flickDealService.getAllCast( movieId));
		int count = flickDealService.getAllPromotionsCount(movie.getTitle(), "", "", "");
		movie.setPromotions(flickDealService.getAllPromotions(movie.getTitle(), "", "", "", 1, count,"DESC"));
		movie.setReviews(flickDealService.getMovieReviewsByMovieID(movieId));
		System.out.println(gson.toJson(movie.getPromotions()));
		return gson.toJson(new Results(1, 1, 1, movie, 1));
	}

	

}