package com.semantic.movies.ontology;

import org.semanticweb.HermiT.ReasonerFactory;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;

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
        OWLClass oscarAward  = df.getOWLClass(IRI.create(ns + "OscarAward"));
        OWLClass cannesAward = df.getOWLClass(IRI.create(ns + "CannesAward"));
        OWLClass masterpiece = df.getOWLClass(IRI.create(ns + "MasterpieceMovie"));

        OWLObjectProperty hasDirector    = df.getOWLObjectProperty(IRI.create(ns + "hasDirector"));
        OWLObjectProperty hasActor       = df.getOWLObjectProperty(IRI.create(ns + "hasActor"));
        OWLObjectProperty belongsToGenre = df.getOWLObjectProperty(IRI.create(ns + "belongsToGenre"));
        OWLObjectProperty wonAward       = df.getOWLObjectProperty(IRI.create(ns + "wonAward"));
        OWLObjectProperty similarTo      = df.getOWLObjectProperty(IRI.create(ns + "similarTo"));
        OWLObjectProperty freqCollab     = df.getOWLObjectProperty(IRI.create(ns + "frequentCollaborator"));

        SWRLVariable m1 = df.getSWRLVariable(IRI.create(ns + "m1"));
        SWRLVariable m2 = df.getSWRLVariable(IRI.create(ns + "m2"));
        SWRLVariable m  = df.getSWRLVariable(IRI.create(ns + "m"));
        SWRLVariable a  = df.getSWRLVariable(IRI.create(ns + "a"));
        SWRLVariable d  = df.getSWRLVariable(IRI.create(ns + "d"));
        SWRLVariable g  = df.getSWRLVariable(IRI.create(ns + "g"));
        SWRLVariable o  = df.getSWRLVariable(IRI.create(ns + "o"));
        SWRLVariable c  = df.getSWRLVariable(IRI.create(ns + "c"));
        SWRLVariable a1 = df.getSWRLVariable(IRI.create(ns + "a1"));
        SWRLVariable a2 = df.getSWRLVariable(IRI.create(ns + "a2"));

        SWRLRule rule1 = df.getSWRLRule(
                Arrays.asList(
                        df.getSWRLClassAtom(movie, m1),
                        df.getSWRLClassAtom(movie, m2),
                        df.getSWRLObjectPropertyAtom(hasDirector, m1, d),
                        df.getSWRLObjectPropertyAtom(hasDirector, m2, d),
                        df.getSWRLDifferentIndividualsAtom(m1, m2)),
                Arrays.asList(df.getSWRLObjectPropertyAtom(similarTo, m1, m2)));
        mgr.addAxiom(ont, rule1);

        SWRLRule rule2 = df.getSWRLRule(
                Arrays.asList(
                        df.getSWRLClassAtom(movie, m1),
                        df.getSWRLClassAtom(movie, m2),
                        df.getSWRLObjectPropertyAtom(wonAward, m1, a1),
                        df.getSWRLClassAtom(oscarAward, a1),
                        df.getSWRLObjectPropertyAtom(wonAward, m2, a2),
                        df.getSWRLClassAtom(oscarAward, a2),
                        df.getSWRLObjectPropertyAtom(belongsToGenre, m1, g),
                        df.getSWRLObjectPropertyAtom(belongsToGenre, m2, g),
                        df.getSWRLDifferentIndividualsAtom(m1, m2)),
                Arrays.asList(df.getSWRLObjectPropertyAtom(similarTo, m1, m2)));
        mgr.addAxiom(ont, rule2);

        SWRLRule rule3 = df.getSWRLRule(
                Arrays.asList(
                        df.getSWRLClassAtom(movie, m),
                        df.getSWRLObjectPropertyAtom(wonAward, m, o),
                        df.getSWRLClassAtom(oscarAward, o),
                        df.getSWRLObjectPropertyAtom(wonAward, m, c),
                        df.getSWRLClassAtom(cannesAward, c)),
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
