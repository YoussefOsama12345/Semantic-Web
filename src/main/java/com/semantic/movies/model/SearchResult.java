package com.semantic.movies.model;

import java.util.Objects;

public class SearchResult {
    private final String uri;
    private final String title;

    public SearchResult(String uri, String title) {
        this.uri = uri;
        this.title = title;
    }

    public String getUri()   { return uri; }
    public String getTitle() { return title; }

    @Override public boolean equals(Object o) {
        if (!(o instanceof SearchResult other)) return false;
        return Objects.equals(uri, other.uri);
    }

    @Override public int hashCode() { return Objects.hash(uri); }
}
