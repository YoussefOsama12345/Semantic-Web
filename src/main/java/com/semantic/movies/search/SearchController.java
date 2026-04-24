package com.semantic.movies.search;

import com.semantic.movies.model.SearchResult;
import com.semantic.movies.query.QueryTemplates;
import com.semantic.movies.query.SPARQLExecutor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SearchController {

    private static final String[] GENRES = {
            "action", "drama", "comedy", "thriller", "sci-fi", "scifi",
            "horror", "romance", "animation"
    };

    private final SPARQLExecutor sparql;

    public SearchController(SPARQLExecutor sparql) {
        this.sparql = sparql;
    }

    public List<SearchResult> search(String userQuery) {
        if (userQuery == null || userQuery.isBlank()) return List.of();

        String q = userQuery.trim();
        Map<String, String> params = Map.of("query", q);

        if (isGenre(q)) {
            List<SearchResult> byGenre = sparql.runTemplate(QueryTemplates.Q_BY_GENRE, params);
            if (!byGenre.isEmpty()) return byGenre;
        }

        LinkedHashMap<String, SearchResult> merged = new LinkedHashMap<>();
        addAll(merged, sparql.runTemplate(QueryTemplates.Q_BY_TITLE,    params));
        addAll(merged, sparql.runTemplate(QueryTemplates.Q_BY_ACTOR,    params));
        addAll(merged, sparql.runTemplate(QueryTemplates.Q_BY_DIRECTOR, params));
        addAll(merged, sparql.runTemplate(QueryTemplates.Q_BY_GENRE,    params));
        return new ArrayList<>(merged.values());
    }

    public List<SearchResult> findSimilar(String movieTitle) {
        if (movieTitle == null || movieTitle.isBlank()) return List.of();
        return sparql.runTemplate(QueryTemplates.Q_SIMILAR, Map.of("movieTitle", movieTitle));
    }

    public List<SearchResult> findMasterpieces() {
        return sparql.runTemplate(QueryTemplates.Q_MASTERPIECES, Map.of());
    }

    private static void addAll(Map<String, SearchResult> into, List<SearchResult> rows) {
        for (SearchResult r : rows) into.putIfAbsent(r.getUri(), r);
    }

    private static boolean isGenre(String query) {
        String q = query.toLowerCase();
        for (String g : GENRES) {
            if (q.contains(g)) return true;
        }
        return false;
    }
}
