package com.semantic.movies.ontology;

import org.semanticweb.HermiT.ReasonerFactory;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.vocab.OWL2Datatype;

import java.util.Arrays;

public class ReasoningEngine {

    private final OntologyManager ontologyManager;
    private final OWLDataFactory df;
    private OWLReasoner reasoner;

    public ReasoningEngine(OntologyManager ontologyManager) {
        this.ontologyManager = ontologyManager;
        this.df = ontologyManager.getManager().getOWLDataFactory();
    }

    public OWLReasoner getReasoner() { return reasoner; }

    public void loadSwrlRules() {
        OWLOntology ont = ontologyManager.getOntology();
        OWLOntologyManager mgr = ontologyManager.getManager();
        String ns = OntologyManager.NAMESPACE;

        OWLClass movie       = df.getOWLClass(IRI.create(ns + "Movie"));
        OWLClass actor       = df.getOWLClass(IRI.create(ns + "Actor"));
        OWLClass director    = df.getOWLClass(IRI.create(ns + "Director"));
        OWLClass masterpiece = df.getOWLClass(IRI.create(ns + "MasterpieceMovie"));

        OWLObjectProperty hasDirector  = df.getOWLObjectProperty(IRI.create(ns + "hasDirector"));
        OWLObjectProperty hasActor     = df.getOWLObjectProperty(IRI.create(ns + "hasActor"));
        OWLObjectProperty belongsToGenre = df.getOWLObjectProperty(IRI.create(ns + "belongsToGenre"));
        OWLObjectProperty similarTo    = df.getOWLObjectProperty(IRI.create(ns + "similarTo"));
        OWLObjectProperty freqCollab   = df.getOWLObjectProperty(IRI.create(ns + "frequentCollaborator"));

        OWLDataProperty rating = df.getOWLDataProperty(IRI.create(ns + "imdbRating"));

        SWRLVariable m1 = df.getSWRLVariable(IRI.create(ns + "m1"));
        SWRLVariable m2 = df.getSWRLVariable(IRI.create(ns + "m2"));
        SWRLVariable m  = df.getSWRLVariable(IRI.create(ns + "m"));
        SWRLVariable a  = df.getSWRLVariable(IRI.create(ns + "a"));
        SWRLVariable d  = df.getSWRLVariable(IRI.create(ns + "d"));
        SWRLVariable g  = df.getSWRLVariable(IRI.create(ns + "g"));
        SWRLVariable r  = df.getSWRLVariable(IRI.create(ns + "r"));
        SWRLVariable r1 = df.getSWRLVariable(IRI.create(ns + "r1"));
        SWRLVariable r2 = df.getSWRLVariable(IRI.create(ns + "r2"));

        SWRLRule rule1 = df.getSWRLRule(
                Arrays.asList(
                        df.getSWRLClassAtom(movie, m1),
                        df.getSWRLClassAtom(movie, m2),
                        df.getSWRLObjectPropertyAtom(hasDirector, m1, d),
                        df.getSWRLObjectPropertyAtom(hasDirector, m2, d),
                        df.getSWRLDifferentIndividualsAtom(m1, m2)),
                Arrays.asList(df.getSWRLObjectPropertyAtom(similarTo, m1, m2)));
        mgr.addAxiom(ont, rule1);

        SWRLLiteralArgument eight = df.getSWRLLiteralArgument(
                df.getOWLLiteral("8.0", OWL2Datatype.XSD_DOUBLE));
        SWRLRule rule2 = df.getSWRLRule(
                Arrays.asList(
                        df.getSWRLClassAtom(movie, m1),
                        df.getSWRLClassAtom(movie, m2),
                        df.getSWRLObjectPropertyAtom(belongsToGenre, m1, g),
                        df.getSWRLObjectPropertyAtom(belongsToGenre, m2, g),
                        df.getSWRLDataPropertyAtom(rating, m1, r1),
                        df.getSWRLDataPropertyAtom(rating, m2, r2),
                        df.getSWRLBuiltInAtom(IRI.create("http://www.w3.org/2003/11/swrlb#greaterThan"),
                                Arrays.asList(r1, eight)),
                        df.getSWRLBuiltInAtom(IRI.create("http://www.w3.org/2003/11/swrlb#greaterThan"),
                                Arrays.asList(r2, eight)),
                        df.getSWRLDifferentIndividualsAtom(m1, m2)),
                Arrays.asList(df.getSWRLObjectPropertyAtom(similarTo, m1, m2)));
        mgr.addAxiom(ont, rule2);

        SWRLLiteralArgument nine = df.getSWRLLiteralArgument(
                df.getOWLLiteral("9.0", OWL2Datatype.XSD_DOUBLE));
        SWRLRule rule3 = df.getSWRLRule(
                Arrays.asList(
                        df.getSWRLClassAtom(movie, m),
                        df.getSWRLDataPropertyAtom(rating, m, r),
                        df.getSWRLBuiltInAtom(IRI.create("http://www.w3.org/2003/11/swrlb#greaterThan"),
                                Arrays.asList(r, nine))),
                Arrays.asList(df.getSWRLClassAtom(masterpiece, m)));
        mgr.addAxiom(ont, rule3);

        SWRLRule rule4 = df.getSWRLRule(
                Arrays.asList(
                        df.getSWRLClassAtom(actor, a),
                        df.getSWRLClassAtom(director, d),
                        df.getSWRLClassAtom(movie, m1),
                        df.getSWRLClassAtom(movie, m2),
                        df.getSWRLObjectPropertyAtom(hasActor, m1, a),
                        df.getSWRLObjectPropertyAtom(hasDirector, m1, d),
                        df.getSWRLObjectPropertyAtom(hasActor, m2, a),
                        df.getSWRLObjectPropertyAtom(hasDirector, m2, d),
                        df.getSWRLDifferentIndividualsAtom(m1, m2)),
                Arrays.asList(df.getSWRLObjectPropertyAtom(freqCollab, a, d)));
        mgr.addAxiom(ont, rule4);

        System.out.println("[ReasoningEngine] Registered 4 SWRL rules.");
    }

    public void runReasoning() {
        OWLReasonerFactory factory = new ReasonerFactory();
        this.reasoner = factory.createReasoner(ontologyManager.getOntology());
        reasoner.precomputeInferences(
                InferenceType.CLASS_HIERARCHY,
                InferenceType.CLASS_ASSERTIONS,
                InferenceType.OBJECT_PROPERTY_ASSERTIONS,
                InferenceType.DATA_PROPERTY_ASSERTIONS);

        boolean consistent = reasoner.isConsistent();
        System.out.println("[ReasoningEngine] Ontology consistent: " + consistent);
    }
}
