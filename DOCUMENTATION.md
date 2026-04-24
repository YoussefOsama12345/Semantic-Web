# Semantic Movie Recommendation System

## Team Members

| #  | Full Name       | ID            |
|----|-----------------|---------------|
| 1  | Member 1        | ID 1          |
| 2  | Member 2        | ID 2          |
| 3  | Member 3        | ID 3          |
| 4  | Member 4        | ID 4          |
| 5  | Member 5        | ID 5          |

<div style="page-break-after: always;"></div>

# 1. Problem

Conventional movie search engines (IMDb, Google, streaming catalogues) match **keywords**, not meaning. A query such as *"movies similar to The Dark Knight"* returns the page of *The Dark Knight* itself because the engine does not understand the relation *similar to*. Implicit knowledge, for example that two films sharing a director are related, is never surfaced unless manually tagged, and there is no logical layer capable of inferring new facts or guaranteeing consistency.

# 2. Methodology

The system replaces keyword search with a Semantic-Web pipeline:

1. The movie domain is modelled as an **OWL 2 ontology** with classes, properties, individuals and restrictions.
2. Domain knowledge is encoded both as **OWL axioms** (defined classes, functional, symmetric, and inverse properties) and as four declarative **SWRL rules**.
3. The **HermiT** reasoner classifies the ontology, enforces consistency, and materialises every inferred axiom.
4. **Apache Jena** loads the enriched model and runs parameterised **SPARQL** queries against both explicit and inferred triples.
5. A minimalist **Java Swing** interface exposes the result through a single realtime, debounced search bar.

# 3. Results

- The inferred ontology contains all `similarTo` pairs and all `MasterpieceMovie` memberships derived by the rules and OWL definitions, none of them hand-written.
- Reasoning completes in under two seconds at startup.
- Per-keystroke query latency is sub-second thanks to a 280 ms debounce and a preloaded model.
- Six SPARQL templates (title, actor, director, genre, similar, masterpiece) return enriched rows with title, year, director, and genre.
- The ontology is verified logically consistent by HermiT on every launch.

# 4. Real-World Impact

- **End-users** discover films through *semantic* relationships instead of opaque "people also watched" feeds.
- **Streaming platforms** could supplement statistical recommenders with *explainable* suggestions, for example *"recommended because it shares a director with X"*.
- **Education** — a compact, self-contained illustration of the Semantic Web stack.
- The system is **extensible with zero code changes**: adding a movie is one more cluster of triples, adding a rule is one more SWRL axiom, adding a query is one more template.

<div style="page-break-after: always;"></div>

# 5. Ontology of the Project and How We Built It

## 5.1 Overview

The ontology is written in **OWL 2 DL**, authored in Protégé and driven programmatically through the OWL API at runtime. It lives under a single namespace:

```
http://www.semanticweb.org/movies#     (prefix mov:)
```

Every class, property, and individual in the system is identified by an IRI in this namespace. The ontology therefore forms a closed, self-contained vocabulary, but remains fully compatible with external Linked-Data sources should future work require them.

## 5.2 Class Taxonomy

The whole model is a tree rooted in the universal class `owl:Thing`. Four top-level branches partition the domain into movies, people, genres, and auxiliary entities (awards, production companies, countries).

### 5.2.1 The `Movie` Branch

```
owl:Thing
└── Movie
    ├── ActionMovie
    ├── DramaMovie
    ├── ComedyMovie
    ├── ThrillerMovie
    ├── SciFiMovie
    ├── HorrorMovie
    ├── RomanceMovie
    ├── AnimationMovie
    │
    ├── ClassicMovie         (defined class)
    ├── AcclaimedMovie       (defined class)
    └── MasterpieceMovie     (defined class)
```

The eight asserted sub-classes represent genres. The three **defined classes** (`ClassicMovie`, `AcclaimedMovie`, `MasterpieceMovie`) are not asserted on any individual — their membership is computed by the reasoner from class-equivalence axioms.

### 5.2.2 The `Person` Branch

```
owl:Thing
└── Person
    ├── Actor
    ├── Director
    ├── Writer
    └── Producer
```

A single human being can be both an `Actor` and a `Director`; the classes are not declared disjoint because the movie domain naturally allows overlap.

### 5.2.3 The `Award` Branch

```
owl:Thing
└── Award
    ├── Oscar
    ├── GoldenGlobe
    └── Cannes
```

### 5.2.4 Auxiliary Classes

```
owl:Thing
├── Genre
├── ProductionCompany
└── Country
```

`Genre` is reified as its own class so that genre individuals can carry labels and participate in `belongsToGenre` triples, allowing SPARQL to match genre names independently of the class hierarchy.

## 5.3 Defined Classes and Class Restrictions

The defined classes are the core of the OWL reasoning demonstration. Each one is stated as a class equivalence (*iff* relation). Once the reasoner runs, every individual satisfying the right-hand side is automatically typed with the class on the left-hand side.

**ClassicMovie**

```
ClassicMovie ≡ Movie ⊓ ( releaseYear < 1980 )
```

**AcclaimedMovie**

```
AcclaimedMovie ≡ Movie ⊓ ∃ wonAward . Oscar
```

**MasterpieceMovie**

```
MasterpieceMovie ≡ Movie ⊓ ( imdbRating ≥ 8.5 ) ⊓ ∃ wonAward . Award
```

These equivalences show three flavours of OWL restriction:

- **Datatype restrictions** on numeric data properties (`releaseYear`, `imdbRating`).
- **Existential restrictions** on object properties (`∃ wonAward . …`).
- **Intersection** of a named class with one or more restrictions.

## 5.4 Object Properties

Object properties connect pairs of individuals. In addition to their domain and range, each property carries logical **characteristics** that the reasoner exploits during inference.

| Property               | Domain    | Range             | Characteristic                          |
|------------------------|-----------|-------------------|-----------------------------------------|
| `hasCastMember`        | Movie     | Person            | super-property                          |
| `hasActor`             | Movie     | Actor             | sub-property of `hasCastMember`         |
| `hasDirector`          | Movie     | Director          | **Functional**                          |
| `hasWriter`            | Movie     | Writer            |                                         |
| `belongsToGenre`       | Movie     | Genre             |                                         |
| `wonAward`             | Movie     | Award             |                                         |
| `producedBy`           | Movie     | ProductionCompany |                                         |
| `producedIn`           | Movie     | Country           |                                         |
| `similarTo`            | Movie     | Movie             | **Symmetric**                           |
| `actedIn`              | Actor     | Movie             | **Inverse of** `hasActor`               |
| `directed`             | Director  | Movie             | **Inverse of** `hasDirector`            |
| `frequentCollaborator` | Person    | Person            | **Symmetric**                           |

The three property characteristics play a concrete role:

- **Functional** guarantees a movie has at most one director. Two distinct director assertions on the same film would be flagged as an inconsistency.
- **Symmetric** turns a single `similarTo` assertion (produced by a rule) into a bidirectional relationship, so the SPARQL layer gets both orientations for free.
- **Inverse** closes the loop between `hasActor` and `actedIn`, and between `hasDirector` and `directed`, so that person-centred queries (*"films acted in by X"*) succeed without any dedicated assertion.

## 5.5 Data Properties

Data properties carry typed literal values on individuals. They cover both movie metadata and personal details.

| Property      | Attached to | Datatype     |
|---------------|-------------|--------------|
| `title`       | Movie       | string       |
| `releaseYear` | Movie       | integer      |
| `duration`    | Movie       | integer      |
| `imdbRating`  | Movie       | double       |
| `plot`        | Movie       | string       |
| `fullName`    | Person      | string       |
| `birthYear`   | Person      | integer      |
| `nationality` | Person      | string       |

These literals are used both for human-readable display in the user interface and as the numeric inputs consumed by OWL datatype restrictions and SWRL rules (for example the `imdbRating ≥ 8.5` test inside `MasterpieceMovie`).

## 5.6 Key Axioms

The ontology is backed by a small but expressive set of axiom categories. They are the formal statements that make reasoning possible.

1. **Subclass axioms** place every genre class under `Movie` and every role class under `Person`, establishing the hierarchies of §5.2.
2. **Equivalent-class axioms** define `ClassicMovie`, `AcclaimedMovie`, and `MasterpieceMovie` as shown in §5.3.
3. **Property domain and range axioms** give every object and data property a precise signature (see §5.4 and §5.5).
4. **Sub-property axioms** place `hasActor` under `hasCastMember`.
5. **Functional property axioms** assert that `hasDirector` has at most one value per subject.
6. **Symmetric property axioms** are declared for `similarTo` and `frequentCollaborator`.
7. **Inverse property axioms** link `hasActor`/`actedIn` and `hasDirector`/`directed`.
8. **Class assertion axioms** declare every individual (movies, people, awards, genres) as a member of its primary class.
9. **Property assertion axioms** state the explicit facts on each individual (who directed what, which actors appeared in which film, which films won which awards, and so on).

Together these axioms form a theory in OWL 2 DL that HermiT can check for consistency and from which it can derive further axioms.

## 5.7 Instance Data

The ontology is populated with **thirty-two real-world movies** drawn from a broad range of eras and genres — Inception, The Godfather, The Dark Knight, Interstellar, Parasite, The Matrix, Goodfellas, Fight Club, Shutter Island, The Prestige, Titanic, The Shawshank Redemption, Gladiator, Lord of the Rings, Schindler's List, Saving Private Ryan, The Silence of the Lambs, Avatar, The Departed, Casablanca, Citizen Kane, Psycho, The Lion King, Toy Story, Spirited Away, The Green Mile, Memento, Joker, Whiplash, The Wolf of Wall Street, Forrest Gump, and Pulp Fiction — together with their directors, leading actors, genres, release years, ratings, and awards. Each film contributes a small cluster of RDF triples, and the union of these clusters is a densely connected graph that reveals non-obvious relationships once the reasoner runs.

<div style="page-break-after: always;"></div>

## 5.8 Rule Layer (SWRL)

Four **SWRL rules** extend the purely terminological knowledge with rule-based entailments. All rules are DL-safe, which is the decidable fragment HermiT natively supports.

**Rule 1 — Similar by shared director**

> If two distinct movies share the same director, they are similar to each other.

**Rule 2 — Similar by shared genre**

> If two distinct movies belong to the same genre, they are similar to each other.

**Rule 3 — Masterpiece classification**

> Any movie that has a numeric rating and has won at least one award is typed as a MasterpieceMovie.

**Rule 4 — Frequent collaborator**

> If the same actor and the same director both appear in at least two different movies, they are frequent collaborators.

Without rules, relations such as `similarTo` and `frequentCollaborator` would have to be hand-asserted for every applicable pair, which does not scale. With rules, the knowledge base remains small and focused on primary facts, and the reasoner derives the secondary facts automatically.

## 5.9 How the Project Was Built

The construction of the ontology-driven system followed seven concrete phases.

**Phase 1 — Domain conceptualisation.**
We first identified the real-world entities the system had to reason about: movies, the people who make them, the genres films belong to, and the awards they collect. For each entity type we listed the attributes that genuinely matter for recommendation (title, year, rating, genre, director, cast, awards) and we deliberately excluded everything that did not contribute to the reasoning goals (box-office numbers, marketing data, language tracks, and so on).

**Phase 2 — Taxonomy design.**
We converted the conceptual model into a class hierarchy, starting from `owl:Thing`, branching into `Movie`, `Person`, `Genre`, and auxiliary classes, and then breaking each branch into meaningful specialisations (genres for movies, roles for people, kinds of award). Wherever the reasoner could replace manual tagging, we added a **defined class** instead of an asserted one; this gave birth to `ClassicMovie`, `AcclaimedMovie`, and `MasterpieceMovie`.

**Phase 3 — Property modelling.**
For every relationship between classes we created an object property, and for every attribute of a class we created a data property. Each property received a precise domain, range, and wherever appropriate a logical characteristic (functional, symmetric, inverse). This phase is what turned the taxonomy from a list of names into a true *graph* of connected entities.

**Phase 4 — Instance population.**
We populated the ontology with thirty-two real-world films and the people, genres, and awards they relate to. Each individual was declared as a member of its primary class and connected to its neighbours through property assertions. The resulting ABox is intentionally compact: we relied on reasoning and rules to amplify it rather than entering every derived fact by hand.

**Phase 5 — Programmatic integration.**
We moved from Protégé into Java, using the OWL API to load the ontology, run a reasoner over it, and serialise the result. The reasoner of choice was HermiT, because of its OWL 2 DL completeness and its native support for DL-safe SWRL.

**Phase 6 — Rule-based extension.**
Four SWRL rules were added to the system at runtime, encoding the recommendation logic directly in the reasoning layer instead of in imperative code. HermiT classifies the ontology together with the rules, enforces consistency, and produces every entailed axiom.

**Phase 7 — Materialisation and query layer.**
After classification, we materialised the inferred axioms into an enriched ontology file that contains both the original assertions and everything the reasoner derived from them. Apache Jena loads that enriched file as an RDF model, and a fixed library of parameterised SPARQL queries — covering search by title, by actor, by director, by genre, similarity, and masterpiece classification — serves the user interface. Because the queries run against the inferred model, the UI automatically benefits from the OWL definitions and the SWRL rules without any additional code.

The end result is a system where **data, knowledge, and rules evolve independently of the application logic**. Adding a new movie, a new rule, or a new query never requires touching the user interface or the reasoning pipeline — which is precisely the promise of a Semantic Web solution.

<div style="page-break-after: always;"></div>

# 6. References

## 6.1 Standards and Specifications

1. W3C. *OWL 2 Web Ontology Language — Primer (Second Edition).* W3C Recommendation, 11 December 2012. https://www.w3.org/TR/owl2-primer/
2. W3C. *OWL 2 Web Ontology Language — Structural Specification and Functional-Style Syntax (Second Edition).* W3C Recommendation, 11 December 2012. https://www.w3.org/TR/owl2-syntax/
3. W3C. *OWL 2 Web Ontology Language — Direct Semantics (Second Edition).* W3C Recommendation, 11 December 2012. https://www.w3.org/TR/owl2-direct-semantics/
4. W3C. *SPARQL 1.1 Query Language.* W3C Recommendation, 21 March 2013. https://www.w3.org/TR/sparql11-query/
5. W3C. *RDF 1.1 Concepts and Abstract Syntax.* W3C Recommendation, 25 February 2014. https://www.w3.org/TR/rdf11-concepts/
6. W3C. *RDF Schema 1.1.* W3C Recommendation, 25 February 2014. https://www.w3.org/TR/rdf-schema/
7. W3C Member Submission. *SWRL: A Semantic Web Rule Language Combining OWL and RuleML.* 21 May 2004. https://www.w3.org/Submission/SWRL/
8. W3C. *RIF Overview (Second Edition).* W3C Working Group Note, 5 February 2013. https://www.w3.org/TR/rif-overview/

## 6.2 Academic Literature

9. Horridge, M., and Bechhofer, S. *The OWL API: A Java API for OWL Ontologies.* Semantic Web Journal, vol. 2, no. 1, pp. 11–21, 2011.
10. Glimm, B., Horrocks, I., Motik, B., Stoilos, G., and Wang, Z. *HermiT: An OWL 2 Reasoner.* Journal of Automated Reasoning, vol. 53, no. 3, pp. 245–269, 2014.
11. Motik, B., Sattler, U., and Studer, R. *Query Answering for OWL-DL with Rules.* Journal of Web Semantics, vol. 3, no. 1, pp. 41–60, 2005. (foundations of DL-safe SWRL)
12. Baader, F., Calvanese, D., McGuinness, D. L., Nardi, D., and Patel-Schneider, P. F. (eds.). *The Description Logic Handbook: Theory, Implementation, and Applications.* 2nd edition, Cambridge University Press, 2007.
13. Hitzler, P., Krötzsch, M., and Rudolph, S. *Foundations of Semantic Web Technologies.* Chapman & Hall / CRC, 2009.
14. Allemang, D., and Hendler, J. *Semantic Web for the Working Ontologist.* 3rd edition, Morgan Kaufmann, 2020.
15. Gruber, T. R. *A Translation Approach to Portable Ontology Specifications.* Knowledge Acquisition, vol. 5, no. 2, pp. 199–220, 1993. (classical definition of "ontology")

## 6.3 Tools and Libraries

16. The OWL API project. https://github.com/owlcs/owlapi
17. HermiT OWL Reasoner. http://www.hermit-reasoner.com/
18. Apache Jena — A free and open-source Java framework for building Semantic Web and Linked Data applications. https://jena.apache.org/
19. Apache Jena documentation — ARQ (SPARQL) and Model APIs. https://jena.apache.org/documentation/
20. Protégé ontology editor. https://protege.stanford.edu/
21. SWRLAPI — Protégé project. https://github.com/protegeproject/swrlapi

## 6.4 Supporting Resources

22. Bizer, C., Heath, T., and Berners-Lee, T. *Linked Data — The Story So Far.* International Journal on Semantic Web and Information Systems, vol. 5, no. 3, pp. 1–22, 2009.
23. DBpedia. https://www.dbpedia.org/
24. Wikidata. https://www.wikidata.org/
25. IMDb — The Internet Movie Database (used as a human-readable reference when curating the film individuals). https://www.imdb.com/
