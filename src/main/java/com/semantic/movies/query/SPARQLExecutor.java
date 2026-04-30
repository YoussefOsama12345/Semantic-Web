package com.semantic.movies.query;

import com.semantic.movies.model.SearchResult;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.RDFDataMgr;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SPARQLExecutor {

    private final Model model;

    public SPARQLExecutor(String inferredOntologyPath) {
        this.model = ModelFactory.createDefaultModel();
        RDFDataMgr.read(model, inferredOntologyPath);
        System.out.println("[SPARQLExecutor] Loaded inferred model, triples=" + model.size());
    }

    public List<SearchResult> runTemplate(String template, Map<String, String> params) {
        String sparql = template;
        for (var e : params.entrySet()) {
            sparql = sparql.replace("${" + e.getKey() + "}", escape(e.getValue()));
        }
        return select(sparql);
    }

    private List<SearchResult> select(String sparql) {
        LinkedHashMap<String, SearchResult> byUri = new LinkedHashMap<>();
        Query query = QueryFactory.create(sparql);
        try (QueryExecution qe = QueryExecutionFactory.create(query, model)) {
            ResultSet rs = qe.execSelect();
            while (rs.hasNext()) {
                QuerySolution sol = rs.nextSolution();
                String uri   = sol.contains("movie") ? sol.getResource("movie").getURI() : "";
                String title = sol.contains("title") ? sol.getLiteral("title").getString() : uri;
                SearchResult r = byUri.computeIfAbsent(uri, u -> new SearchResult(u, title));

                if (r.getYear() == null && sol.contains("year"))
                    r.setYear(sol.getLiteral("year").getLexicalForm());
                if (r.getDirector() == null && sol.contains("directorName"))
                    r.setDirector(sol.getLiteral("directorName").getString());
                if (r.getGenre() == null && sol.contains("genreLabel"))
                    r.setGenre(sol.getLiteral("genreLabel").getString());
                if (r.getImdbRating() == null && sol.contains("rating"))
                    r.setImdbRating(sol.getLiteral("rating").getDouble());
                if (r.getDuration() == null && sol.contains("duration"))
                    r.setDuration(sol.getLiteral("duration").getInt());
                if (r.getPlot() == null && sol.contains("plot"))
                    r.setPlot(sol.getLiteral("plot").getString());
            }
        }
        return new ArrayList<>(byUri.values());
    }

    private static String escape(String input) {
        return input == null ? "" : input.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
