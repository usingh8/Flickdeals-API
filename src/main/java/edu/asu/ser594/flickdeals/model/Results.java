package edu.asu.ser594.flickdeals.model;

import java.util.List;

public class Results {

	private int totalRecords;
	private int totalPages;
	private int currentpage;
	private int recordsOnCurrentPage;
	private List<Person> people;
	private List<Movie> movies;
	private List<Promotion> promotions;
	
	public Results(int totalRecords, int totalPages, int currentpage,
			List<Movie> movies, int recordsOnCurrentPage) {
		super();
		this.totalRecords = totalRecords;
		this.totalPages = totalPages;
		this.currentpage = currentpage;
		this.movies = movies;
		this.recordsOnCurrentPage = recordsOnCurrentPage;
	}
	public int getRecordsOnCurrentPage() {
		return recordsOnCurrentPage;
	}
	public void setRecordsOnCurrentPage(int recordsOnCurrentPage) {
		this.recordsOnCurrentPage = recordsOnCurrentPage;
	}
	private Movie movie;
	public Results(int totalPages, int currentpage,
			List<Promotion> promotions,int totalRecords, int recordsOnCurrentPage) {
		super();
		this.totalRecords = totalRecords;
		this.totalPages = totalPages;
		this.currentpage = currentpage;
		this.promotions = promotions;
		this.recordsOnCurrentPage = recordsOnCurrentPage;
	}
	public Results(int totalRecords, int totalPages, int currentpage,
			Movie movie, int recordsOnCurrentPage) {
		super();
		this.totalRecords = totalRecords;
		this.totalPages = totalPages;
		this.currentpage = currentpage;
		this.movie = movie;
		this.recordsOnCurrentPage = recordsOnCurrentPage;
	}
	public Results(int totalRecords,  List<Person> people, int page,
			int totalPages, int recordsOnCurrentPage) {
		// TODO Auto-generated constructor stub
		
		this.totalRecords = totalRecords;
		this.totalPages = totalPages;
		this.currentpage = page;
		this.people = people;
		this.recordsOnCurrentPage = recordsOnCurrentPage;
	}
		

	public List<Person> getPeople() {
		return people;
	}
	public void setPeople(List<Person> people) {
		this.people = people;
	}
	public Movie getMovie() {
		return movie;
	}
	public void setMovie(Movie movie) {
		this.movie = movie;
	}

	public int getTotalRecords() {
		return totalRecords;
	}
	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
	}
	public int getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	public int getCurrentpage() {
		return currentpage;
	}
	public void setCurrentpage(int currentpage) {
		this.currentpage = currentpage;
	}
	public List<Movie> getMovies() {
		return movies;
	}
	public void setMovies(List<Movie> movies) {
		this.movies = movies;
	}
	public List<Promotion> getPromotions() {
		return promotions;
	}
	public void setPromotions(List<Promotion> promotions) {
		this.promotions = promotions;
	}
	
}
