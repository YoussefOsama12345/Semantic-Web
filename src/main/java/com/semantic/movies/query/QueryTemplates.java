package com.semantic.movies.query;

public final class QueryTemplates {

    public static final String PREFIXES =
            "PREFIX mov:  <http://www.semanticweb.org/movies#>\n" +
            "PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
            "PREFIX xsd:  <http://www.w3.org/2001/XMLSchema#>\n";

    private static final String SELECT_MOVIE_FIELDS =
            "SELECT DISTINCT ?movie ?title ?year ?directorName ?genreLabel ?rating ?duration ?plot WHERE {\n" +
            "  ?movie a mov:Movie ;\n" +
            "         mov:title ?title .\n" +
            "  OPTIONAL { ?movie mov:releaseYear ?year . }\n" +
            "  OPTIONAL { ?movie mov:hasDirector ?director . ?director mov:fullName ?directorName . }\n" +
            "  OPTIONAL { ?movie mov:belongsToGenre ?genre . ?genre rdfs:label ?genreLabel . }\n" +
            "  OPTIONAL { ?movie mov:imdbRating ?rating . }\n" +
            "  OPTIONAL { ?movie mov:duration ?duration . }\n" +
            "  OPTIONAL { ?movie mov:plot ?plot . }\n";

    public static final String Q_BY_TITLE = PREFIXES + SELECT_MOVIE_FIELDS +
            "  FILTER(CONTAINS(LCASE(STR(?title)), LCASE(\"${query}\")))\n" +
            "} ORDER BY ?title";

    public static final String Q_BY_ACTOR = PREFIXES + SELECT_MOVIE_FIELDS +
            "  ?movie mov:hasActor ?actor .\n" +
            "  ?actor a mov:Actor ; mov:fullName ?actorName .\n" +
            "  FILTER(CONTAINS(LCASE(STR(?actorName)), LCASE(\"${query}\")))\n" +
            "} ORDER BY ?title";

    public static final String Q_BY_DIRECTOR = PREFIXES + SELECT_MOVIE_FIELDS +
            "  FILTER(CONTAINS(LCASE(STR(?directorName)), LCASE(\"${query}\")))\n" +
            "} ORDER BY ?title";

    public static final String Q_BY_GENRE = PREFIXES + SELECT_MOVIE_FIELDS +
            "  FILTER(CONTAINS(LCASE(STR(?genreLabel)), LCASE(\"${query}\")))\n" +
            "} ORDER BY ?title";

    public static final String Q_MASTERPIECES = PREFIXES +
            "SELECT DISTINCT ?movie ?title ?year ?directorName ?genreLabel ?rating ?duration ?plot WHERE {\n" +
            "  ?movie a mov:MasterpieceMovie ;\n" +
            "         mov:title ?title .\n" +
            "  OPTIONAL { ?movie mov:releaseYear ?year . }\n" +
            "  OPTIONAL { ?movie mov:hasDirector ?director . ?director mov:fullName ?directorName . }\n" +
            "  OPTIONAL { ?movie mov:belongsToGenre ?genre . ?genre rdfs:label ?genreLabel . }\n" +
            "  OPTIONAL { ?movie mov:imdbRating ?rating . }\n" +
            "  OPTIONAL { ?movie mov:duration ?duration . }\n" +
            "  OPTIONAL { ?movie mov:plot ?plot . }\n" +
            "} ORDER BY ?title";

    public static final String Q_ACCLAIMED = PREFIXES +
            "SELECT DISTINCT ?movie ?title ?year ?directorName ?genreLabel ?rating ?duration ?plot WHERE {\n" +
            "  ?movie a mov:AcclaimedMovie ;\n" +
            "         mov:title ?title .\n" +
            "  OPTIONAL { ?movie mov:releaseYear ?year . }\n" +
            "  OPTIONAL { ?movie mov:hasDirector ?director . ?director mov:fullName ?directorName . }\n" +
            "  OPTIONAL { ?movie mov:belongsToGenre ?genre . ?genre rdfs:label ?genreLabel . }\n" +
            "  OPTIONAL { ?movie mov:imdbRating ?rating . }\n" +
            "  OPTIONAL { ?movie mov:duration ?duration . }\n" +
            "  OPTIONAL { ?movie mov:plot ?plot . }\n" +
            "} ORDER BY DESC(?rating)";

    private QueryTemplates() { }
}
