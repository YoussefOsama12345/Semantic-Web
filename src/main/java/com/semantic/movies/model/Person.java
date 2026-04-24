package com.semantic.movies.model;

public class Person {
    private final String uri;
    private final String fullName;

    public Person(String uri, String fullName) {
        this.uri = uri;
        this.fullName = fullName;
    }

    public String getUri()      { return uri; }
    public String getFullName() { return fullName; }

    @Override public String toString() { return fullName; }
}
