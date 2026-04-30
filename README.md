# Semantic Movie Recommender

A university project for a **Semantic Web & Ontology** course.
The system recommends movies using an **OWL 2 ontology** authored in
**Protégé**, **SWRL rules** that run inside the ontology, the **HermiT**
OWL reasoner, **SPARQL** queries via **Apache Jena**, and a minimalist
**Java Swing** GUI.

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
| Ontology authoring     | Protégé 5.x                               |
| Ontology I/O           | OWL API 5.5.0 (RDF/XML)                   |
| OWL DL + SWRL reasoning| HermiT 1.4.5.519                          |
| SPARQL querying        | Apache Jena 4.10.0                        |
| UI                     | Java Swing (JDK)                          |
| Build system           | Maven (Java 17)                           |

---

## Build & Run

```bash
mvn clean install
mvn exec:java -Dexec.mainClass="com.semantic.movies.Main"
```

Or run the shaded JAR:

```bash
java -jar target/movie-recommender-1.0.0.jar
```

---

## Project structure

```
.
├── pom.xml
├── README.md
├── DOCUMENTATION.md
├── src/main/
│   ├── java/com/semantic/movies/
│   │   ├── Main.java                       # entry point
│   │   ├── ontology/
│   │   │   ├── OntologyManager.java        # OWL API: load + save inferred
│   │   │   └── ReasoningEngine.java        # HermiT runner
│   │   ├── query/
│   │   │   ├── SPARQLExecutor.java         # Jena SPARQL engine
│   │   │   └── QueryTemplates.java         # parameterised SPARQL templates
│   │   ├── search/
│   │   │   └── SearchController.java       # genre detection + query routing
│   │   ├── model/
│   │   │   └── SearchResult.java           # row returned to the UI
│   │   └── ui/
│   │       ├── Theme.java                  # colors + fonts
│   │       ├── MainWindow.java             # CardLayout (search ↔ detail)
│   │       ├── SearchPanel.java            # search bar + Acclaimed button
│   │       ├── ResultsPanel.java           # results list / placeholder
│   │       ├── MovieListItem.java          # row card (badge, title, ⭐, duration)
│   │       └── MovieDetailPanel.java       # full-page detail + Back button
│   └── resources/ontology/
│       └── movies.owl                      # Protégé export (RDF/XML)
```

---

## Flow — From Protégé to the running app

```
┌─────────────────────────────────┐
│ 1. Author in Protégé             │
│   • classes, properties           │
│   • individuals (32 movies)       │
│   • OWL restrictions              │
│   • SWRL rules (SWRLTab)          │
│   • Test with HermiT in Protégé   │
└──────────────┬──────────────────┘
               │  File > Save As → RDF/XML
               ▼
┌─────────────────────────────────┐
│ 2. movies.owl                    │
│   Placed in src/main/resources/   │
│      ontology/movies.owl          │
└──────────────┬──────────────────┘
               │  classpath load
               ▼
┌─────────────────────────────────┐
│ 3. OntologyManager (OWL API)     │
│   loadOntology("/ontology/        │
│                movies.owl")       │
└──────────────┬──────────────────┘
               │
               ▼
┌─────────────────────────────────┐
│ 4. ReasoningEngine (HermiT)      │
│   Precomputes:                    │
│   • class hierarchy               │
│   • class assertions              │
│   • object/data property values   │
│   • SWRL rule entailments         │
│   isConsistent() check            │
└──────────────┬──────────────────┘
               │
               ▼
┌─────────────────────────────────┐
│ 5. saveInferred()                │
│   Generators add:                 │
│   • inferred subclasses           │
│   • inferred class memberships    │
│     (AcclaimedMovie,              │
│      MasterpieceMovie, …)         │
│   Writes target/movies-inferred   │
│   .owl as RDF/XML                 │
└──────────────┬──────────────────┘
               │
               ▼
┌─────────────────────────────────┐
│ 6. SPARQLExecutor (Jena)         │
│   Loads the inferred file into a  │
│   Jena Model. Asserted + inferred │
│   triples are now query-able.     │
└──────────────┬──────────────────┘
               │
               ▼
┌─────────────────────────────────┐
│ 7. Swing GUI                     │
│   • Type → SearchController       │
│     routes to title/actor/        │
│     director/genre query          │
│   • Acclaimed button → Q_ACCLAIMED│
│   • Click result → full-page      │
│     detail view + Back button     │
└─────────────────────────────────┘
```

The key idea: **knowledge lives in the ontology**, not in Java code.
SWRL rules and OWL definitions are authored in Protégé and shipped inside
`movies.owl`. The Java application never hard-codes a rule or an inference;
it just loads the file, asks HermiT to reason, materialises the result and
queries it with SPARQL.

---

## Ontology at a glance

- Namespace: `http://www.semanticweb.org/movies#` (`mov:`)
- **Classes:** `Movie` and 8 genre sub-classes (Action, Drama, Comedy,
  Thriller, SciFi, Horror, Romance, Animation); `Person / Actor / Director /
  Writer / Producer`; `Genre`, `Award` (+ Oscar / GoldenGlobe / Cannes),
  `ProductionCompany`, `Country`.
- **Defined classes** (computed by HermiT from class-equivalence axioms):
  `ClassicMovie`, `AcclaimedMovie`, `MasterpieceMovie`.
- **Object properties:** `hasActor` (⊑ `hasCastMember`), `hasDirector`
  (Functional), `hasWriter`, `belongsToGenre`, `wonAward`, `producedBy`,
  `producedIn`, `similarTo` (Symmetric), `actedIn` (inverseOf `hasActor`),
  `directed` (inverseOf `hasDirector`), `frequentCollaborator` (Symmetric).
- **Data properties:** `title`, `releaseYear`, `duration`, `imdbRating`,
  `plot`, `fullName`, `birthYear`, `nationality`.
- **32 real-world movies** with their actors, directors, genres, years,
  ratings, plots, durations, and awards.

## SWRL rules (authored in Protégé)

The SWRL rules live inside `movies.owl` (Protégé's SWRLTab writes them
under `swrl:Imp` axioms). HermiT applies them during reasoning. They
collectively derive:

- `MasterpieceMovie` memberships from award / rating combinations.
- `AcclaimedMovie` memberships (also expressible as an OWL equivalent class).
- `frequentCollaborator` between actors and directors who appear in
  multiple films together.

Editing or adding rules is done in Protégé — no Java code change required.

## SPARQL queries

Defined in `QueryTemplates.java`:

| ID            | Purpose                                                   |
|---------------|-----------------------------------------------------------|
| `Q_BY_TITLE`  | Movies whose title contains the user query                |
| `Q_BY_ACTOR`  | Movies whose actor full-name contains the user query      |
| `Q_BY_DIRECTOR`| Movies whose director full-name contains the user query  |
| `Q_BY_GENRE`  | Movies in a given genre label                             |
| `Q_MASTERPIECES`| All `MasterpieceMovie` individuals (uses OWL+SWRL inference) |
| `Q_ACCLAIMED` | All `AcclaimedMovie` individuals, ordered by rating       |

Every query returns the same projection (`title`, `year`, `directorName`,
`genreLabel`, `rating`, `duration`, `plot`) so a single result row type
serves the whole UI.

---

## How to update the ontology

1. Open `src/main/resources/ontology/movies.owl` in **Protégé**.
2. Edit classes, properties, individuals, restrictions, or SWRL rules.
3. (Optional) run HermiT inside Protégé to verify consistency and inferences.
4. **File → Save As → RDF/XML** back to `src/main/resources/ontology/movies.owl`.
5. Re-run the Java app — no code change needed.

---

## Credits & references

- Protégé – https://protege.stanford.edu/
- OWL API – https://github.com/owlcs/owlapi
- HermiT reasoner – http://www.hermit-reasoner.com/
- Apache Jena – https://jena.apache.org/
- W3C OWL 2 & SWRL standards
