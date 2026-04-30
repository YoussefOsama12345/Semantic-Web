package com.semantic.movies.ontology;

import org.semanticweb.HermiT.ReasonerFactory;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

public class ReasoningEngine {

    private final OntologyManager ontologyManager;
    private OWLReasoner reasoner;

    public ReasoningEngine(OntologyManager ontologyManager) {
        this.ontologyManager = ontologyManager;
    }

    public OWLReasoner getReasoner() { return reasoner; }

    public void runReasoning() {
        this.reasoner = new ReasonerFactory().createReasoner(ontologyManager.getOntology());
        reasoner.precomputeInferences(
                InferenceType.CLASS_HIERARCHY,
                InferenceType.CLASS_ASSERTIONS,
                InferenceType.OBJECT_PROPERTY_ASSERTIONS,
                InferenceType.DATA_PROPERTY_ASSERTIONS);

        System.out.println("[ReasoningEngine] Ontology consistent: " + reasoner.isConsistent());
    }
}
