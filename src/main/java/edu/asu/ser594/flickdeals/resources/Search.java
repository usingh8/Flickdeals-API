package edu.asu.ser594.flickdeals.resources;

import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;

import edu.asu.ser594.flickdeals.model.Person;
import edu.asu.ser594.flickdeals.model.Results;
import edu.asu.ser594.flickdeals.service.FlickDealsDAO;
import edu.asu.ser594.flickdeals.service.impl.FlickDealsDAOImpl;
@Path("search")
public class Search {


	private FlickDealsDAO flickDealService;
	private Gson gson;
	@Context
	private ServletContext context;

	
	public Search() {
		this.flickDealService = new FlickDealsDAOImpl();
		gson = new Gson();
	}
	
	
	@GET
	@Path("/movie")
	@Produces(MediaType.APPLICATION_JSON)
	public String seachMovie( @QueryParam("start") String startDate, @QueryParam("end") String endDate,
			@QueryParam("genre") String genre, @QueryParam("query") String query,@QueryParam("page") int page,
			@QueryParam("limit") int limit,@QueryParam("location") String location, @QueryParam("sortby") String sortby) {
		
		limit = limit == 0? 20:limit;
		page = page == 0? 1:page;
		sortby = sortby ==null ? "DESC": sortby;
		String genres [] = genre==null? null:genre.split(",");
		int count = flickDealService.getTotalMovieResultCount(genres, query, startDate, endDate, page);
		int totalpages = count%limit==0?count/limit:(count/limit)+1;
		List<edu.asu.ser594.flickdeals.model.Movie> movies = flickDealService.getAllMovies( genres, query, startDate, endDate, page, limit, sortby);
		int recordsOnCurrentPage = movies.size();
		return gson.toJson(new Results(count, totalpages, page, movies, recordsOnCurrentPage));
		
	}
	
	@GET
	@Path("/promotion")
	@Produces(MediaType.APPLICATION_JSON)
	public String seachPromotion( @QueryParam("start") String startDate, @QueryParam("end") String endDate,
			@QueryParam("genre") String genres, @QueryParam("query") String query,@QueryParam("page") int page,
			@QueryParam("limit") int limit,@QueryParam("location") String location, @QueryParam("sortby") String sortby) {
		
		limit = limit == 0? 20:limit;
		page = page == 0? 1:page;
		sortby = sortby ==null ? "DESC": sortby;
		int totalResults = flickDealService.getAllPromotionsCount( query, location, startDate, endDate);
		int totalpages = totalResults%limit==0?totalResults/limit:(totalResults/limit)+1;
		List<edu.asu.ser594.flickdeals.model.Promotion> promotions =flickDealService.getAllPromotions( query,location, startDate, endDate, page, limit, sortby);
		int recordsOnCurrentPage = promotions.size();
		return gson.toJson(new Results(totalpages, page, promotions, totalResults, recordsOnCurrentPage));
		
	}
	
	@GET
	@Path("/cast")
	@Produces(MediaType.APPLICATION_JSON)
	public String seachCast( @QueryParam("start") String startDate, @QueryParam("end") String endDate,
			@QueryParam("genre") String genres, @QueryParam("query") String query,@QueryParam("page") int page,
			@QueryParam("limit") int limit,@QueryParam("location") String location, @QueryParam("sortby") String sortby) {
		limit = limit == 0? 20:limit;
		page = page == 0? 1:page;
		sortby = sortby ==null ? "DESC": sortby;
		int totalResults = flickDealService.getResultCountOfSearchCastByName(query);
		List<Person> people = flickDealService.searchCastByName(query, page, limit, sortby);
		int totalpages = totalResults%limit==0?totalResults/limit:(totalResults/limit)+1;
		int recordsOnCurrentPage = people.size();
		return gson.toJson(new Results(totalResults, people, page, totalpages, recordsOnCurrentPage));
	}
	
	@GET
	@Path("/review")
	@Produces(MediaType.APPLICATION_JSON)
	public String seachReview( @QueryParam("start") String startDate, @QueryParam("end") String endDate,
			@QueryParam("genre") String genre, @QueryParam("query") String query,@QueryParam("page") int page, @QueryParam("title") String title,
			@QueryParam("limit") int limit,@QueryParam("location") String location, @QueryParam("sortby") String sortby) {
		limit = limit == 0? 20:limit;
		page = page == 0? 1:page;
		String genres [] = genre==null? null:genre.split(",");
		sortby = sortby ==null ? "DESC": sortby;
		int count = flickDealService.getTotalMovieResultCount(genres, query, startDate, endDate, page);
		int totalpages = count%limit==0?count/limit:(count/limit)+1;
		
		List<edu.asu.ser594.flickdeals.model.Movie> movies = flickDealService.getAllMovies( genres, query, startDate, endDate, page, limit, sortby);
		int recordsOnCurrentPage = movies.size();
		return gson.toJson(new Results(count, totalpages, page, movies, recordsOnCurrentPage));
		
	}
	

}
