package com.semantic.movies.model;

public class Movie {
    private final String uri;
    private final String title;

    public Movie(String uri, String title) {
        this.uri = uri;
        this.title = title;
    }

    public String getUri()   { return uri; }
    public String getTitle() { return title; }

    @Override public String toString() { return title; }
}
