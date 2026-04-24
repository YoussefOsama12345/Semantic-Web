package com.semantic.movies.search;

public class EntityResolver {

    public enum EntityType { TITLE, ACTOR, DIRECTOR, GENRE, UNKNOWN }

    public EntityType resolve(String query) {
        if (query == null) return EntityType.UNKNOWN;
        String q = query.trim().toLowerCase();

        if (q.isEmpty()) return EntityType.UNKNOWN;

        String[] genres = {
                "action", "drama", "comedy", "thriller", "sci-fi", "scifi",
                "horror", "romance", "animation"
        };
        for (String g : genres) {
            if (q.equals(g) || q.contains(g)) return EntityType.GENRE;
        }
        return EntityType.UNKNOWN;
    }
}
