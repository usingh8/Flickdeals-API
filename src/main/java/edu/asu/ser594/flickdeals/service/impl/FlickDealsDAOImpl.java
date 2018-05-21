package edu.asu.ser594.flickdeals.service.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.RDFNode;


import com.google.gson.Gson;
import edu.asu.ser594.flickdeals.model.Movie;
import edu.asu.ser594.flickdeals.model.MovieReview;
import edu.asu.ser594.flickdeals.model.Person;
import edu.asu.ser594.flickdeals.model.Promotion;
import edu.asu.ser594.flickdeals.service.FlickDealsDAO;

public class FlickDealsDAOImpl implements FlickDealsDAO {

	private String defaultNameSpace = "http://org.semweb/flickdeals/movie#";
	private String service ="http://localhost:3030/Flickdeals-API/query";
	
	private String serviceURI =null;//"http://localhost:3030/Flickdeals-API/query";
	
	public String getServiceurl()
	{
		if(serviceURI!=null)
			return serviceURI;
		
		Properties prop = new Properties();
		InputStream propFile = FlickDealsDAOImpl.class.getResourceAsStream("system.properties");
		
		try {
			prop.load(propFile);
		} catch (IOException e) {

			e.printStackTrace();
		}
		
		String url = prop.getProperty("url");
		String port = prop.getProperty("port");
		String dataset = prop.getProperty("dataset");
		
	    serviceURI = "http://"+url+":"+port+"/"+dataset+"/query" ;
	    
	    System.out.println(serviceURI);
		return serviceURI;
	}
	
	@Override	
	public int getTotalMovieResultCount(String[] genres, String title, String startDate,String endDate, int page)
	{
		int count = 0;
		genres = genres == null?new String[0]:genres;
		title = title == null?"":title;	
		String q = "PREFIX foaf: <http://xmlns.com/foaf/0.1/>" 
				+ "PREFIX pr: <https://archive.org/details/flickdeals#> " 
				+ "select  (count(*) as ?count)   where {"
				+ " ?movie pr:has_movie_title ?title." + "?movie pr:has_movie_id ?movieId."
				+ "?movie pr:has_video ?hasVideo." + "?movie pr:has_average_vote ?avgVote."
				+ "?movie pr:has_vote_count ?voteCount." + "?movie pr:has_genrename ?genre."
				+ "?movie pr:has_overview ?overview." + "?movie pr:has_poster_path ?posterPath."
				+ "?movie pr:has_backdrop_path ?backdropPath." + "?movie pr:has_language ?language."
				+ "?movie pr:has_popularity ?popularity." + "?movie pr:has_adult_status ?adult."
				+ "?movie pr:has_release_date ?releaseDate.";
		for(String genre:genres)
			q	+=	" FILTER (contains(lcase(str(?genre)) ,lcase('" + genre+ "')))";
			
		q	+= "FILTER (regex(lcase(?title) ,lcase('" + title + "')))";
		if(startDate!=null)
			q+= "FILTER ((?releaseDate >='"+startDate+"'))";
		if(endDate!=null)
			q+= "FILTER ((?releaseDate <='"+endDate+"'))";
	    q+= " }Order By DESC (?releaseDate)";
		
	    QueryExecution qexec = QueryExecutionFactory.sparqlService(getServiceurl(), q);
		
		try {
			ResultSet response = qexec.execSelect();
            
			while (response.hasNext()) {
				QuerySolution soln = response.nextSolution();

				String name = soln.get("?count").toString();
				//System.out.println(soln.get("?name").toString());
				int index = name.indexOf("^");
				System.out.println(name.substring(0,index));
				count  = Integer.parseInt(name.substring(0, index));

			}
		
		} finally {
			qexec.close();
		}

		
		return count;
	}

	@Override
	public List<Movie> getAllMovies( String[] genres, String title, String startDate,String endDate, int page, int limit, String sortby) {
		
		int offset = (page-1)*limit;
		genres = genres == null?new String[0]:genres;
		title = title == null?"":title;	
		String q = "PREFIX foaf: <http://xmlns.com/foaf/0.1/>" + "PREFIX fd: <http://www.w3.org/2001/vcard-rdf/3.0#> "
				+ "PREFIX pr: <https://archive.org/details/flickdeals#> " + "select * where {"
				+ " ?movie pr:has_movie_title ?title." + "?movie pr:has_movie_id ?movieId."
				+ "?movie pr:has_video ?hasVideo." + "?movie pr:has_average_vote ?avgVote."
				+ "?movie pr:has_vote_count ?voteCount." + "?movie pr:has_genrename ?genre."
				+ "?movie pr:has_overview ?overview." + "?movie pr:has_poster_path ?posterPath."
				+ "?movie pr:has_backdrop_path ?backdropPath." + "?movie pr:has_language ?language."
				+ "?movie pr:has_popularity ?popularity." + "?movie pr:has_adult_status ?adult."
				+ "?movie pr:has_release_date ?releaseDate.";
		for(String genre:genres)
			q	+=	" FILTER (contains(lcase(str(?genre)) ,lcase('" + genre+ "')))";
			
		q	+= "FILTER (regex(lcase(?title) ,lcase('" + title + "')))";
		if(startDate!=null)
			q+= "FILTER ((?releaseDate >='"+startDate+"'))";
		if(endDate!=null)
			q+= "FILTER ((?releaseDate <='"+endDate+"'))";
	    q+= " }Order By "+sortby+"(?releaseDate) LIMIT "+limit+" OFFSET " + offset;
	    
	   
	    
		return runMovieQuery(q);
	}
  
	private List<Movie>  runMovieQuery(String q)
	    {
	    	List<Movie> movies = new ArrayList<Movie>();
	    	QueryExecution qexec = QueryExecutionFactory.sparqlService(getServiceurl(), q);
			try {
				ResultSet response = qexec.execSelect();

				while (response.hasNext()) {
					QuerySolution soln = response.nextSolution();

					RDFNode name = soln.get("?title");

					Movie movie = new Movie(name.toString(), soln.get("?overview").toString(),
							Integer.parseInt(soln.get("?movieId").toString()), Double.parseDouble(soln.get("?avgVote").toString()),
							soln.get("?posterPath").toString(), Double.parseDouble(soln.get("?popularity").toString()),
							soln.get("?backdropPath").toString(), soln.get("?releaseDate").toString());
					String[] genres = soln.get("?genre").toString().split(",");
					movie.setGenres(Arrays.asList(genres));
					movie.setAdultMovie(Boolean.parseBoolean(soln.get("?posterPath").toString()));
					movie.setLanguage(soln.get("?language").toString());
					movie.setVoteCount((int) Double.parseDouble(soln.get("?voteCount").toString()));
					movie.setHasVideo(Boolean.parseBoolean(soln.get("?hasVideo").toString()));
					movies.add(movie);

				}
			} finally {
				qexec.close();
			}
			return movies;
	    }
	@Override
	public List<MovieReview> getMovieReviewsByMovieID(int movieId)
	{
		List<MovieReview> reviews = new ArrayList<MovieReview>();
		
		String q = "prefix mr: <http://www.semanticweb.org/krish/ontologies/2016/9/flickdeals#>"
				   +"SELECT * WHERE {"
			       +"?review mr:has_review_content ?content." 
			       +"?review mr:has_movie_id '"+movieId+"'."
			       +"?review mr:has_review_source ?source."
			       +"?review mr:has_author ?author."
			       +"?review mr:has_review_date ?date."
			       +"} ";
		QueryExecution qexec = QueryExecutionFactory.sparqlService(getServiceurl(), q);
		try {
			ResultSet response = qexec.execSelect();
			
			while (response.hasNext()) {
				QuerySolution soln = response.nextSolution();

				RDFNode content = soln.get("?content");
				RDFNode author = soln.get("?author");
				RDFNode source = soln.get("?source");
				RDFNode date = soln.get("?date");
				
				MovieReview review = new MovieReview(movieId, source.toString(), author.toString(),content.toString() 
						, date.toString());
				reviews.add(review);
			}
		
		
		} finally {
			qexec.close();
		}

		return reviews;
	}
	@Override
	public Movie getMovieDetail( int movieId) {
		Movie movie = null;
		
		String q = "PREFIX foaf: <http://xmlns.com/foaf/0.1/>" + "PREFIX fd: <http://www.w3.org/2001/vcard-rdf/3.0#> "
				+ "PREFIX pr: <https://archive.org/details/flickdeals#> " + "select * where {"
				+ " ?movie pr:has_movie_title ?title." + "?movie pr:has_movie_id ?movieId."
				+ "?movie pr:has_video ?hasVideo." + "?movie pr:has_average_vote ?avgVote."
				+ "?movie pr:has_vote_count ?voteCount." + "?movie pr:has_genrename ?genre."
				+ "?movie pr:has_overview ?overview." + "?movie pr:has_poster_path ?posterPath."
				+ "?movie pr:has_backdrop_path ?backdropPath." + "?movie pr:has_language ?language."
				+ "?movie pr:has_popularity ?popularity." + "?movie pr:has_adult_status ?adult."
				+ "?movie pr:has_release_date ?releaseDate."
				+ "?movie pr:has_cast_name ?cast."
				+ " FILTER (regex(?movieId ,'" + movieId + "'))"
				+"}";
		QueryExecution qexec = QueryExecutionFactory.sparqlService(getServiceurl(), q);
		try {
			ResultSet response = qexec.execSelect();
			
			while (response.hasNext()) {
				QuerySolution soln = response.nextSolution();

				RDFNode name = soln.get("?title");

				movie = new Movie(name.toString(), soln.get("?overview").toString(),
						Integer.parseInt(soln.get("?movieId").toString()), Double.parseDouble(soln.get("?avgVote").toString()),
						soln.get("?posterPath").toString(), Double.parseDouble(soln.get("?popularity").toString()),
						soln.get("?backdropPath").toString(), soln.get("?releaseDate").toString());
				String[] genres = soln.get("?genre").toString().split(",");
				movie.setGenres(Arrays.asList(genres));
				movie.setAdultMovie(Boolean.parseBoolean(soln.get("?posterPath").toString()));
				movie.setLanguage(soln.get("?language").toString());
				movie.setVoteCount((int) Double.parseDouble(soln.get("?voteCount").toString()));
				movie.setHasVideo(Boolean.parseBoolean(soln.get("?hasVideo").toString()));
			}
			
			
			
		} finally {
			qexec.close();
		}


		return movie;
	}

	@Override
	public List<Person> getAllCast( int movieId) {
		List<Person> persons = new ArrayList<Person>();
		String q =
				"PREFIX cst: <http://www.semanticweb.org/krish/ontologies/2016/9/flickdeals_people#> "
				+"PREFIX pr: <http://www.semanticweb.org/krish/ontologies/2016/9/flickdeals_promotion#>" 
				+"PREFIX mv: <https://archive.org/details/flickdeals#>"
				+"prefix mr: <http://www.semanticweb.org/krish/ontologies/2016/9/flickdeals#>"
				+ "select * where {"
				+"?movie mv:has_movie_id '"+movieId+"'."
			
				+ "?movie mv:has_cast_name ?cast."
				
				+"}";
		
		QueryExecution qexec = QueryExecutionFactory.sparqlService(getServiceurl(), q);
		
		try {
			ResultSet response = qexec.execSelect();
            //System.out.println(response.);
			
			while (response.hasNext()) {
				QuerySolution soln = response.nextSolution();

				String castString =soln.get("?cast").toString();
				if(castString.contains("null"))
					continue;
				String[] casts = castString.split(",");
				
				if(casts.length>=3)
					{
					Person person = new Person(casts[0], casts[1], casts[2], Float.parseFloat(casts[3].trim()));
					persons.add(person);
					}
			}
		
		} finally {
			qexec.close();
		}
		Collections.sort(persons, new Comparator<Person>() {

			@Override
			public int compare(Person o1, Person o2) {
				// TODO Auto-generated method stub
				return (int) (o2.getPopularity() -  o1.getPopularity());
			}
		});
		
		return persons;
	}

	@Override
	public int getResultCountOfSearchCastByName(String castName)
	{
		int count = 0;
		String q =
				"PREFIX cst: <http://www.semanticweb.org/krish/ontologies/2016/9/flickdeals_people#> "
				+"PREFIX pr: <http://www.semanticweb.org/krish/ontologies/2016/9/flickdeals_promotion#>" 
				+"PREFIX mv: <https://archive.org/details/flickdeals#>"
				+"prefix mr: <http://www.semanticweb.org/krish/ontologies/2016/9/flickdeals#>" 
				+ "select  (count(*) as ?count) where {"
				+"?cast cst:has_cast_id ?castId."
				+ "?cast cst:has_castimdbid ?imdbId."
				+ "?cast cst:has_castadultstatus ?adult."
				+ "?cast cst:has_popularity ?popularity."
				+ "?cast cst:has_cast_birthday ?birthday."
				+ "?cast cst:has_cast_name ?name."
				+ "?cast cst:has_castgender ?gender"
				+ " FILTER (regex(lcase(?name) ,lcase('"+castName+"'))) "
				+"} Group By ?dname Order By DESC(?birthday)";
		
		
		QueryExecution qexec = QueryExecutionFactory.sparqlService(getServiceurl(), q);
		
		try {
			ResultSet response = qexec.execSelect();
			while (response.hasNext()) {
				QuerySolution soln = response.nextSolution();
				
			
				String name = soln.get("?count").toString();
				//System.out.println(soln.get("?name").toString());
				int index = name.indexOf("^");
				System.out.println(name.substring(0,index));
				count  = Integer.parseInt(name.substring(0, index));
				
			
			}
		
		} finally {
			qexec.close();
		}

		
		return count;
	}
	@Override
	public List<Person> searchCastByName(String castName, int page, int limit, String sortBy) {
		// TODO Auto-generated method stub
		int	offset = (page-1)*limit;
		List<Person> persons = new ArrayList<Person>();
		String q = "PREFIX cst: <http://www.semanticweb.org/krish/ontologies/2016/9/flickdeals_people#> "
				+"PREFIX pr: <http://www.semanticweb.org/krish/ontologies/2016/9/flickdeals_promotion#>" 
				+"PREFIX mv: <https://archive.org/details/flickdeals#>"
				+"prefix mr: <http://www.semanticweb.org/krish/ontologies/2016/9/flickdeals#>"
				+"select * where {"
				+"?cast cst:has_cast_id ?castId."
				+ "?cast cst:has_castimdbid ?imdbId."
				+ "?cast cst:has_castadultstatus ?adult."
				+ "?cast cst:has_popularity ?popularity."
				+ "?cast cst:has_cast_birthday ?birthday."
				+ "?cast cst:has_cast_name ?name."
				+ "?cast cst:has_castgender ?gender"
				+ " FILTER (regex(lcase(?name) ,lcase('"+castName+"'))) "
				+"} Order by "+sortBy +" (?birthday) LIMIT "+limit+" OFFSET " + offset;
		
		
		QueryExecution qexec = QueryExecutionFactory.sparqlService(getServiceurl(), q);
		
		try {
			ResultSet response = qexec.execSelect();
			while (response.hasNext()) {
				QuerySolution soln = response.nextSolution();
				RDFNode name = soln.get("?name");
				RDFNode imdbId = soln.get("?imdbId");
				RDFNode adult = soln.get("?adult");
				RDFNode popularity = soln.get("?popularity");
				RDFNode castId = soln.get("?castId");
				RDFNode birthday = soln.get("?birthday");
				RDFNode gender = soln.get("?gender");
				
				
				Person person = new Person(name.toString(), castId.toString());
				person.setGender(gender.toString());
				person.setImdbId(imdbId.toString());
				
				person.setPopularity(Float.parseFloat(popularity.toString()));
		
				person.setBirthdDate(birthday.toString());
				person.setAdult(Boolean.parseBoolean(adult.toString()));
				persons.add(person);
			}
		
		} finally {
			qexec.close();
		}

		
		return persons;
		
	}
	@Override
	public Person getCompleteCastDetail(String castId) {
		// TODO Auto-generated method stub
		Person person = null;
		String q = "PREFIX cst: <http://www.semanticweb.org/krish/ontologies/2016/9/flickdeals_people#> "
				+"PREFIX pr: <http://www.semanticweb.org/krish/ontologies/2016/9/flickdeals_promotion#>" 
				+"PREFIX mv: <https://archive.org/details/flickdeals#>"
				+"prefix mr: <http://www.semanticweb.org/krish/ontologies/2016/9/flickdeals#>"
				+ "select * where {"
				+"?cast cst:has_cast_id '"+castId+"'."
				+ "?cast cst:has_castimdbid ?imdbId."
				+ "?cast cst:has_castadultstatus ?adult."
				+ "?cast cst:has_castprofilepath ?profilePath."
				+ "?cast cst:has_popularity ?popularity."
				+ "?cast cst:has_castbiography ?bio."
				+ "?cast cst:has_castalsoknownas ?aka."
				+ "?cast cst:has_cast_birthday ?birthday."
				+ "?cast cst:has_cast_name ?name."
				+ "?cast cst:has_castgender ?gender."
				+ "?movie mv:has_movie_title ?title."
				+ "?movie mv:has_cast_name ?castN."
				+ "?movie mv:has_movie_id ?movieId."
				+ "?movie mv:has_popularity ?moviePopularity." 
				+ "?movie mv:has_release_date ?releaseDate."
				+"FILTER (contains(lcase(str(?castN)) ,lcase('"+castId+"')))"
				+"} Order by DESC (?releaseDate) Limit 25";
		
		List<Movie> movies = new ArrayList<Movie>();
		QueryExecution qexec = QueryExecutionFactory.sparqlService(getServiceurl(), q);
		
		try {
			ResultSet response = qexec.execSelect();
			while (response.hasNext()) {
				QuerySolution soln = response.nextSolution();
				RDFNode name = soln.get("?name");
				RDFNode imdbId = soln.get("?imdbId");
				RDFNode adult = soln.get("?adult");
				RDFNode profilePath = soln.get("?profilePath");
				RDFNode popularity = soln.get("?popularity");
				RDFNode bio = soln.get("?bio");
				RDFNode aka = soln.get("?aka");
				RDFNode birthday = soln.get("?birthday");
				RDFNode gender = soln.get("?gender");
				RDFNode title = soln.get("?title");
				RDFNode moviePopularity = soln.get("?moviePopularity");
				RDFNode releaseDate = soln.get("?releaseDate");
				RDFNode movieId = soln.get("?movieId");
				
				System.out.println("Name "+ name.toString());
				person = new Person(name.toString(), castId);
				person.setGender(gender.toString());
				person.setImdbId(imdbId.toString());
				person.setProfilePath(profilePath.toString());
				person.setPopularity(Float.parseFloat(popularity.toString()));
				person.setBio(bio.toString());
				person.setAlsoKnownAs(aka.toString());
				person.setBirthdDate(birthday.toString());
				person.setAdult(Boolean.parseBoolean(adult.toString()));
				
				Movie movie = new Movie(Integer.parseInt(movieId.toString()),title.toString(),releaseDate.toString(), Float.parseFloat(moviePopularity.toString()));
				movies.add(movie);
			}
		
		} finally {
			qexec.close();
		}
		if(person!=null)
			person.setMoviesDone(movies);
		return person;
		
	}
	@Override
	public List<Promotion> getAllPromotions( String movie, String location, String startDate, String endDate, int page, int limit, String sortby) {
		int	offset = (page-1)*limit;
		movie = movie ==null ? "":movie;
		location = location ==null ?"":location;
		
		String q ="PREFIX cst: <http://www.semanticweb.org/krish/ontologies/2016/9/flickdeals_people#> "
				+"PREFIX pr: <http://www.semanticweb.org/krish/ontologies/2016/9/flickdeals_promotion#>" 
				+"PREFIX mv: <https://archive.org/details/flickdeals#>"
				+"prefix mr: <http://www.semanticweb.org/krish/ontologies/2016/9/flickdeals#>"
				+ "select * where {"
				+ "?promotion pr:has_movie_title ?movieName." 
				+ "?promotion pr:has_location ?locat." 
				+ "?promotion pr:has_offer_details ?details."
				+ "?promotion pr:has_offer_source ?source." 
				+ "?promotion pr:has_posted ?date."
				+ " FILTER (regex(lcase(?locat) ,lcase('"+location+"'))) "
				+ " FILTER (regex(lcase(?movieName) ,lcase('"+movie+"')))";
		if(startDate!=null)
			q+= "FILTER ((?date >='"+startDate+"'))";
		if(endDate!=null)
			q+= "FILTER ((?date <='"+endDate+"'))";
		q+=	 "} Order By "+sortby+"(?date)  LIMIT "+limit+" OFFSET " + offset;
		
		//_promotionModel =getModel(path,"promotion");
		
		return runPromotionQuery(q);
		
	}

    private List<Promotion> runPromotionQuery(String q )
    {
    	List<Promotion> promotions = new ArrayList<Promotion>();
    	
		QueryExecution qexec = QueryExecutionFactory.sparqlService(getServiceurl(), q);
		try {
			ResultSet response = qexec.execSelect();

			while (response.hasNext()) {
				QuerySolution soln = response.nextSolution();

				RDFNode movieName = soln.get("?movieName");
				RDFNode locat = soln.get("?locat");
				RDFNode details = soln.get("?details");
				RDFNode source = soln.get("?source");
				RDFNode date = soln.get("?date");
				String sourceName = source.toString();				
				Promotion promotion = new Promotion(movieName.toString(), locat.toString(), details.toString(), sourceName.substring(7), date.toString());
				promotions.add(promotion);
				
			}
		} 
		catch(Exception e)
		{
			e.printStackTrace();
		}finally {
			qexec.close();
		}		
		return promotions;
    }

	@Override
	public int getAllPromotionsCount( String movie,
			String location, String startDate, String endDate) {
		// TODO Auto-generated method stub
		
		    
			int count = 0;

			movie = movie ==null ? "":movie;
			location = location ==null ?"":location;
			String q =
					"PREFIX cst: <http://www.semanticweb.org/krish/ontologies/2016/9/flickdeals_people#> "
					+"PREFIX pr: <http://www.semanticweb.org/krish/ontologies/2016/9/flickdeals_promotion#>" 
					+"PREFIX mv: <https://archive.org/details/flickdeals#>"
					+"prefix mr: <http://www.semanticweb.org/krish/ontologies/2016/9/flickdeals#>"
					+ "select  (count(*) as ?count)   where {"
					+ "?promotion pr:has_movie_title ?movieName." 
					+ "?promotion pr:has_location ?locat."  
					+ "?promotion pr:has_posted ?date."
					+ " FILTER (regex(lcase(?locat) ,lcase('"+location+"'))) "
					+ " FILTER (regex(lcase(?movieName) ,lcase('"+movie+"')))";
			if(startDate!=null)
				q+= "FILTER ((?date >='"+startDate+"'))";
			if(endDate!=null)
				q+= "FILTER ((?date <='"+endDate+"'))";
		    q+= " }Group By ?name Order By DESC(?date)" ;
			
			
			QueryExecution qexec = QueryExecutionFactory.sparqlService(getServiceurl(), q);
			try {
				ResultSet response = qexec.execSelect();
	            
				while (response.hasNext()) {
					QuerySolution soln = response.nextSolution();

					String name = soln.get("?count").toString();
					//System.out.println(soln.get("?name").toString());
					int index = name.indexOf("^");
					System.out.println(name.substring(0,index));
					count  = Integer.parseInt(name.substring(0, index));

				}
			
			} finally {
				qexec.close();
			}

			
			return count;
		}
	

	
}
