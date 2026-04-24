package com.semantic.movies.query;

import com.semantic.movies.model.SearchResult;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.RDFDataMgr;

import java.util.ArrayList;
import java.util.List;

/**
 * Runs SPARQL queries against the inferred ontology file using Apache Jena.
 */
public class SPARQLExecutor {

    private final Model model;

    public SPARQLExecutor(String inferredOntologyPath) {
        this.model = ModelFactory.createDefaultModel();
        RDFDataMgr.read(model, inferredOntologyPath);
        System.out.println("[SPARQLExecutor] Loaded inferred model, triples=" + model.size());
    }

    /** Replace ${key} placeholders, then run a SELECT query. */
    public List<SearchResult> runTemplate(String template, java.util.Map<String, String> params) {
        String sparql = template;
        for (var e : params.entrySet()) {
            sparql = sparql.replace("${" + e.getKey() + "}", escape(e.getValue()));
        }
        return select(sparql);
    }

    private List<SearchResult> select(String sparql) {
        List<SearchResult> out = new ArrayList<>();
        Query query = QueryFactory.create(sparql);
        try (QueryExecution qe = QueryExecutionFactory.create(query, model)) {
            ResultSet rs = qe.execSelect();
            while (rs.hasNext()) {
                QuerySolution sol = rs.nextSolution();
                String uri = sol.contains("movie") ? sol.getResource("movie").getURI() : "";
                String title = sol.contains("title") ? sol.getLiteral("title").getString() : uri;
                out.add(new SearchResult(uri, title));
            }
        }
        return out;
    }

    private static String escape(String input) {
        // Escape quotes and backslashes for SPARQL literals.
        return input == null ? "" : input.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
