package edu.asu.ser594.flickdeals.resources;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;

import edu.asu.ser594.flickdeals.service.FlickDealsDAO;
import edu.asu.ser594.flickdeals.service.impl.FlickDealsDAOImpl;

@Path("cast")
public class Cast {


	private FlickDealsDAO flickDealService;
	private Gson gson;

	public Cast() {
		this.flickDealService = new FlickDealsDAOImpl();
		gson = new Gson();
	}


	@GET
	@Path("/{castId}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getCastDetails(@PathParam("castId") String castId) {
		
		return gson.toJson(flickDealService.getCompleteCastDetail(castId));
	}
	
	
}
