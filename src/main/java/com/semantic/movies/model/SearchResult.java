package com.semantic.movies.model;

import java.util.Objects;

public class SearchResult {
    private final String uri;
    private final String title;
    private String year;
    private String director;
    private String genre;

    public SearchResult(String uri, String title) {
        this.uri = uri;
        this.title = title;
    }

    public String getUri()      { return uri; }
    public String getTitle()    { return title; }
    public String getYear()     { return year; }
    public String getDirector() { return director; }
    public String getGenre()    { return genre; }

    public void setYear(String year)         { this.year = year; }
    public void setDirector(String director) { this.director = director; }
    public void setGenre(String genre)       { this.genre = genre; }

    public String getSubtitle() {
        StringBuilder sb = new StringBuilder();
        if (year != null && !year.isBlank()) sb.append(year);
        if (director != null && !director.isBlank()) {
            if (sb.length() > 0) sb.append("  \u2022  ");
            sb.append("Dir. ").append(director);
        }
        if (genre != null && !genre.isBlank()) {
            if (sb.length() > 0) sb.append("  \u2022  ");
            sb.append(genre);
        }
        return sb.toString();
    }

    @Override public boolean equals(Object o) {
        if (!(o instanceof SearchResult other)) return false;
        return Objects.equals(uri, other.uri);
    }

    @Override public int hashCode() { return Objects.hash(uri); }
}
