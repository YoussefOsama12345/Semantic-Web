package com.semantic.movies.search;

/**
 * Classifies a free-text query into a probable entity type.
 * The classification is heuristic: we try each SPARQL query in order
 * and let the caller fall back if a query returns no results.
 */
public class EntityResolver {

    public enum EntityType { TITLE, ACTOR, DIRECTOR, GENRE, UNKNOWN }

    /** Rough upfront heuristic. We always also fall back to running all queries. */
    public EntityType resolve(String query) {
        if (query == null) return EntityType.UNKNOWN;
        String q = query.trim().toLowerCase();

        if (q.isEmpty()) return EntityType.UNKNOWN;

        // Known genre keywords
        String[] genres = {
                "action", "drama", "comedy", "thriller", "sci-fi", "scifi",
                "horror", "romance", "animation"
        };
        for (String g : genres) {
            if (q.equals(g) || q.contains(g)) return EntityType.GENRE;
        }
        return EntityType.UNKNOWN; // caller will just try all queries
    }
}
