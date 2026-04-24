# Semantic Movie Recommender

A university project for a **Semantic Web & Ontology** course.
The system recommends movies using an **OWL ontology**, **SPARQL queries**,
**SWRL rules** and the **HermiT** OWL reasoner. The UI is a minimalist
**Java Swing** application: a single search bar and a clean text-only list of
movie titles.

---

## Team members

- _Name 1_
- _Name 2_
- _Name 3_
- _Name 4_

---

## Tech stack

| Purpose                | Library                                   |
|------------------------|-------------------------------------------|
| Ontology manipulation  | OWL API 5.5.0                             |
| Reasoning              | HermiT 1.4.5.519 (with DL-safe SWRL)      |
| SWRL rule support      | SWRLAPI 2.1.0 / OWL API SWRL axioms       |
| SPARQL querying        | Apache Jena 4.10.0                        |
| UI                     | Java Swing (built into the JDK)           |
| Build system           | Maven (Java 17)                           |

---

## Build

```bash
mvn clean install
```

## Run

```bash
mvn exec:java -Dexec.mainClass="com.semantic.movies.Main"
```

Or run the shaded JAR:

```bash
java -jar target/movie-recommender-1.0.0.jar
```

---

## Project structure

```
movie-recommender/
├── pom.xml
├── README.md
├── src/main/
│   ├── java/com/semantic/movies/
│   │   ├── Main.java                       # entry point
│   │   ├── ontology/
│   │   │   ├── OntologyManager.java        # OWL API wrapper + inferred save
│   │   │   └── ReasoningEngine.java        # HermiT + SWRL rules
│   │   ├── query/
│   │   │   ├── SPARQLExecutor.java         # Jena SPARQL engine
│   │   │   └── QueryTemplates.java         # six SPARQL templates
│   │   ├── search/
│   │   │   ├── SearchController.java       # semantic routing
│   │   │   └── EntityResolver.java         # genre / free-text heuristic
│   │   ├── model/
│   │   │   ├── Movie.java | Person.java | SearchResult.java
│   │   └── ui/
│   │       ├── MainWindow.java             # 900x650 frame, BorderLayout
│   │       ├── SearchPanel.java            # search bar
│   │       ├── ResultsPanel.java           # scrollable vertical list
│   │       └── MovieListItem.java          # single title row, hover + click
│   └── resources/ontology/
│       ├── movies.owl                      # 32 movies, classes, restrictions
│       └── rules.swrl                      # human-readable rule copy
└── docs/queries/sample_queries.sparql
```

---

## Ontology at a glance

- Namespace: `http://www.semanticweb.org/movies#` (`mov:`)
- **Classes:** `Movie` and 8 sub-classes (Action, Drama, Comedy, Thriller, SciFi,
  Horror, Romance, Animation); `Person / Actor / Director / Writer / Producer`;
  `Genre`, `Award` (+ Oscar / GoldenGlobe / Cannes), `ProductionCompany`,
  `Country`.
- **Defined classes:** `ClassicMovie` (year < 1980), `AcclaimedMovie`
  (won an Oscar), `MasterpieceMovie` (rating >= 8.5 and won an award).
- **Object properties:** `hasActor` (sub-prop of `hasCastMember`),
  `hasDirector` (functional), `hasWriter`, `belongsToGenre`, `wonAward`,
  `producedBy`, `producedIn`, `similarTo` (symmetric), `actedIn`
  (inverse of `hasActor`), `directed` (inverse of `hasDirector`).
- **Data properties:** `title`, `releaseYear`, `duration`, `imdbRating`,
  `plot`, `fullName`, `birthYear`, `nationality`.
- **32 real-world movies** (Inception, The Godfather, Pulp Fiction, The Dark
  Knight, Interstellar, Parasite, Forrest Gump, The Matrix, Goodfellas, Fight
  Club, Shutter Island, The Prestige, Titanic, The Shawshank Redemption,
  Gladiator, LOTR, Schindler's List, Saving Private Ryan, The Silence of the
  Lambs, Avatar, The Departed, Casablanca, Citizen Kane, Psycho, The Lion King,
  Toy Story, Spirited Away, The Green Mile, Memento, Joker, Whiplash, The Wolf
  of Wall Street) with actors, directors, genres, years, ratings and awards.

## SWRL rules

1. **Similar by director:** two distinct movies with the same director become
   `similarTo` each other.
2. **Similar by genre + rating:** two distinct highly-rated (`> 8.0`) movies of
   the same genre become `similarTo`.
3. **Masterpiece by rating:** any movie with rating `> 9.0` is typed as
   `MasterpieceMovie`.
4. **Frequent collaborator:** an actor and a director sharing two or more
   movies become `frequentCollaborator`.

## SPARQL queries

The UI uses six templates defined in `QueryTemplates.java`:

- **Q1** – movies by title substring
- **Q2** – movies by actor name
- **Q3** – movies by director name
- **Q4** – movies by genre label
- **Q5** – similar movies (uses SWRL `similarTo` inferences)
- **Q6** – `MasterpieceMovie` individuals (uses OWL + SWRL reasoning)

Additional experimental queries (classics, acclaimed, frequent collaborators)
live in `docs/queries/sample_queries.sparql`.

---

## How the system works

1. `OntologyManager` loads `movies.owl` with the OWL API.
2. `ReasoningEngine` adds the 4 SWRL rules as `SWRLRule` axioms and runs
   **HermiT**, which natively supports DL-safe rules.
3. The inferred axioms (including all `similarTo` and `MasterpieceMovie`
   inferences) are materialised and saved to `movies-inferred.owl`.
4. `SPARQLExecutor` loads that enriched file into an Apache Jena model; all
   SPARQL queries run against the inferred triples, so reasoning results are
   directly query-able.
5. `SearchController` routes a user query to the right SPARQL template and
   merges results by URI.
6. The Swing UI shows a simple vertical list of movie titles. Clicking a title
   triggers Q5 and replaces the list with similar movies.

---

## Credits & references

- OWL API – https://github.com/owlcs/owlapi
- HermiT reasoner – http://www.hermit-reasoner.com/
- Apache Jena – https://jena.apache.org/
- SWRLAPI – https://github.com/protegeproject/swrlapi
- W3C OWL 2 & SWRL standards
