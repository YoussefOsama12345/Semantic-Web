package com.semantic.movies.query;

public final class QueryTemplates {

    public static final String PREFIXES =
            "PREFIX mov:  <http://www.semanticweb.org/movies#>\n" +
            "PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
            "PREFIX xsd:  <http://www.w3.org/2001/XMLSchema#>\n";

    public static final String Q_BY_TITLE = PREFIXES +
            "SELECT DISTINCT ?movie ?title WHERE {\n" +
            "  ?movie a mov:Movie ;\n" +
            "         mov:title ?title .\n" +
            "  FILTER(CONTAINS(LCASE(STR(?title)), LCASE(\"${query}\")))\n" +
            "} ORDER BY ?title";

    public static final String Q_BY_ACTOR = PREFIXES +
            "SELECT DISTINCT ?movie ?title WHERE {\n" +
            "  ?actor a mov:Actor ;\n" +
            "         mov:fullName ?actorName .\n" +
            "  ?movie mov:hasActor ?actor ;\n" +
            "         mov:title ?title .\n" +
            "  FILTER(CONTAINS(LCASE(STR(?actorName)), LCASE(\"${query}\")))\n" +
            "} ORDER BY ?title";

    public static final String Q_BY_DIRECTOR = PREFIXES +
            "SELECT DISTINCT ?movie ?title WHERE {\n" +
            "  ?director a mov:Director ;\n" +
            "            mov:fullName ?directorName .\n" +
            "  ?movie mov:hasDirector ?director ;\n" +
            "         mov:title ?title .\n" +
            "  FILTER(CONTAINS(LCASE(STR(?directorName)), LCASE(\"${query}\")))\n" +
            "} ORDER BY ?title";

    public static final String Q_BY_GENRE = PREFIXES +
            "SELECT DISTINCT ?movie ?title WHERE {\n" +
            "  ?movie a mov:Movie ;\n" +
            "         mov:belongsToGenre ?genre ;\n" +
            "         mov:title ?title .\n" +
            "  ?genre rdfs:label ?genreLabel .\n" +
            "  FILTER(CONTAINS(LCASE(STR(?genreLabel)), LCASE(\"${query}\")))\n" +
            "} ORDER BY ?title";

    public static final String Q_SIMILAR = PREFIXES +
            "SELECT DISTINCT ?movie ?title WHERE {\n" +
            "  ?source mov:title \"${movieTitle}\" .\n" +
            "  ?source mov:similarTo ?movie .\n" +
            "  ?movie  mov:title ?title .\n" +
            "  FILTER(?source != ?movie)\n" +
            "} ORDER BY ?title";

    public static final String Q_MASTERPIECES = PREFIXES +
            "SELECT DISTINCT ?movie ?title WHERE {\n" +
            "  ?movie a mov:MasterpieceMovie ;\n" +
            "         mov:title ?title .\n" +
            "} ORDER BY ?title";

    private QueryTemplates() { }
}
