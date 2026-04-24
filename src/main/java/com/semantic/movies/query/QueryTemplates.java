package com.semantic.movies.query;

/**
 * All SPARQL query templates used by the application.
 * Tokens of the form ${name} are replaced at runtime by SPARQLExecutor.
 */
public final class QueryTemplates {

    public static final String PREFIXES =
            "PREFIX mov:  <http://www.semanticweb.org/movies#>\n" +
            "PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
            "PREFIX xsd:  <http://www.w3.org/2001/XMLSchema#>\n";

    /** Q1 - search by movie title (partial, case-insensitive). */
    public static final String Q_BY_TITLE = PREFIXES +
            "SELECT DISTINCT ?movie ?title WHERE {\n" +
            "  ?movie a mov:Movie ;\n" +
            "         mov:title ?title .\n" +
            "  FILTER(CONTAINS(LCASE(STR(?title)), LCASE(\"${query}\")))\n" +
            "} ORDER BY ?title";

    /** Q2 - search by actor name. */
    public static final String Q_BY_ACTOR = PREFIXES +
            "SELECT DISTINCT ?movie ?title WHERE {\n" +
            "  ?actor a mov:Actor ;\n" +
            "         mov:fullName ?actorName .\n" +
            "  ?movie mov:hasActor ?actor ;\n" +
            "         mov:title ?title .\n" +
            "  FILTER(CONTAINS(LCASE(STR(?actorName)), LCASE(\"${query}\")))\n" +
            "} ORDER BY ?title";

    /** Q3 - search by director name. */
    public static final String Q_BY_DIRECTOR = PREFIXES +
            "SELECT DISTINCT ?movie ?title WHERE {\n" +
            "  ?director a mov:Director ;\n" +
            "            mov:fullName ?directorName .\n" +
            "  ?movie mov:hasDirector ?director ;\n" +
            "         mov:title ?title .\n" +
            "  FILTER(CONTAINS(LCASE(STR(?directorName)), LCASE(\"${query}\")))\n" +
            "} ORDER BY ?title";

    /** Q4 - search by genre label. */
    public static final String Q_BY_GENRE = PREFIXES +
            "SELECT DISTINCT ?movie ?title WHERE {\n" +
            "  ?movie a mov:Movie ;\n" +
            "         mov:belongsToGenre ?genre ;\n" +
            "         mov:title ?title .\n" +
            "  ?genre rdfs:label ?genreLabel .\n" +
            "  FILTER(CONTAINS(LCASE(STR(?genreLabel)), LCASE(\"${query}\")))\n" +
            "} ORDER BY ?title";

    /** Q5 - similar movies (uses inferred similarTo from SWRL). */
    public static final String Q_SIMILAR = PREFIXES +
            "SELECT DISTINCT ?movie ?title WHERE {\n" +
            "  ?source mov:title \"${movieTitle}\" .\n" +
            "  ?source mov:similarTo ?movie .\n" +
            "  ?movie  mov:title ?title .\n" +
            "  FILTER(?source != ?movie)\n" +
            "} ORDER BY ?title";

    /** Q6 - all masterpiece movies (uses OWL + SWRL reasoning). */
    public static final String Q_MASTERPIECES = PREFIXES +
            "SELECT DISTINCT ?movie ?title WHERE {\n" +
            "  ?movie a mov:MasterpieceMovie ;\n" +
            "         mov:title ?title .\n" +
            "} ORDER BY ?title";

    private QueryTemplates() { }
}
