package edu.asu.ser594.flickdeals.resources;

import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;

import edu.asu.ser594.flickdeals.model.Results;
import edu.asu.ser594.flickdeals.service.FlickDealsDAO;
import edu.asu.ser594.flickdeals.service.impl.FlickDealsDAOImpl;
@Path("promotion")
public class Promotion {
	
	private FlickDealsDAO flickDealService;
	private Gson gson;


	public Promotion() {
		this.flickDealService = new FlickDealsDAOImpl();
		gson = new Gson();
	}
	
	
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public String getPromotions(@QueryParam("location") String location, @QueryParam("movie") String movieName, 
			@QueryParam("start") String startDate,@QueryParam("end") String endDate ,@QueryParam("page") int page,@QueryParam("limit") int limit, @QueryParam("sortby") String sortby ) {
		sortby = sortby == null ? "DESC":sortby;
		limit = limit == 0? 20:limit;
		page = page == 0? 1:page;
		int totalResults = flickDealService.getAllPromotionsCount( movieName, location, startDate, endDate);
		List<edu.asu.ser594.flickdeals.model.Promotion> promotions =flickDealService.getAllPromotions(movieName, location, startDate, endDate, page, limit, sortby);
		int recordsOnCurrentPage = promotions.size();
		return gson.toJson(new Results(totalResults%limit==0?totalResults/limit:(totalResults/limit)+1, page, promotions, totalResults,recordsOnCurrentPage));
	}

}
