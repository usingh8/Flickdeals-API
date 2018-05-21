package edu.asu.ser594.flickdeals.model;

import java.util.List;

public class Person {
private String personId;
private String name;
private String characterName;
private String bio;
private String birthdDate;
private String alsoKnownAs;
private float popularity;
private String imdbId;
private String gender;
private List<Movie> moviesDone;

private String profilePath;
private boolean adult;

public Person(String personId, String name, String characterName) {
	super();
	this.personId = personId;
	this.name = name;
	this.characterName = characterName;
}
public Person(String personId, String name, String characterName, float popularity) {
	super();
	this.personId = personId;
	this.name = name;
	this.characterName = characterName;
	this.popularity = popularity;
}
public Person(String name, String personId)
{
	this.name = name;
	this.personId = personId;
}
public List<Movie> getMoviesDone() {
	return moviesDone;
}
public void setMoviesDone(List<Movie> moviesDone) {
	this.moviesDone = moviesDone;
}
public boolean isAdult() {
	return adult;
}
public void setAdult(boolean adult) {
	this.adult = adult;
}
public String getGender() {
	return gender;
}
public void setGender(String gender) {
	this.gender = gender;
}
public String getPersonId() {
	return personId;
}
public String getBio() {
	return bio;
}
public void setBio(String bio) {
	this.bio = bio;
}
public String getBirthdDate() {
	return birthdDate;
}
public void setBirthdDate(String birthdDate) {
	this.birthdDate = birthdDate;
}
public String getAlsoKnownAs() {
	return alsoKnownAs;
}
public void setAlsoKnownAs(String alsoKnownAs) {
	this.alsoKnownAs = alsoKnownAs;
}
public float getPopularity() {
	return popularity;
}
public void setPopularity(float popularity) {
	this.popularity = popularity;
}
public String getImdbId() {
	return imdbId;
}
public void setImdbId(String imdbId) {
	this.imdbId = imdbId;
}
public String getProfilePath() {
	return profilePath;
}
public void setProfilePath(String profilePath) {
	this.profilePath = profilePath;
}
public void setPersonId(String personId) {
	this.personId = personId;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getCharacterName() {
	return characterName;
}
public void setCharacterName(String characterName) {
	this.characterName = characterName;
}
}
