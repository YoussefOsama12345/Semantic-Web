package com.semantic.movies.model;

import java.util.Objects;

public class SearchResult {
    private final String uri;
    private final String title;
    private String year;
    private String director;
    private String genre;
    private Double imdbRating;
    private Integer duration;
    private String plot;

    public SearchResult(String uri, String title) {
        this.uri = uri;
        this.title = title;
    }

    public String  getUri()        { return uri; }
    public String  getTitle()      { return title; }
    public String  getYear()       { return year; }
    public String  getDirector()   { return director; }
    public String  getGenre()      { return genre; }
    public Double  getImdbRating() { return imdbRating; }
    public Integer getDuration()   { return duration; }
    public String  getPlot()       { return plot; }

    public void setYear(String year)           { this.year = year; }
    public void setDirector(String director)   { this.director = director; }
    public void setGenre(String genre)         { this.genre = genre; }
    public void setImdbRating(Double rating)   { this.imdbRating = rating; }
    public void setDuration(Integer duration)  { this.duration = duration; }
    public void setPlot(String plot)           { this.plot = plot; }

    public String getSubtitle() {
        StringBuilder sb = new StringBuilder();
        if (year != null && !year.isBlank()) sb.append(year);
        if (director != null && !director.isBlank()) {
            if (sb.length() > 0) sb.append("  •  ");
            sb.append("Dir. ").append(director);
        }
        if (genre != null && !genre.isBlank()) {
            if (sb.length() > 0) sb.append("  •  ");
            sb.append(genre);
        }
        return sb.toString();
    }

    public String getRatingLabel() {
        return imdbRating != null ? String.format("⭐ %.1f", imdbRating) : "";
    }

    public String getDurationLabel() {
        return duration != null ? duration + " min" : "";
    }

    @Override public boolean equals(Object o) {
        if (!(o instanceof SearchResult other)) return false;
        return Objects.equals(uri, other.uri);
    }

    @Override public int hashCode() { return Objects.hash(uri); }
}
