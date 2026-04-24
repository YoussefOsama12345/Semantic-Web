package com.semantic.movies.ontology;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.RDFXMLDocumentFormat;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.util.InferredAxiomGenerator;
import org.semanticweb.owlapi.util.InferredClassAssertionAxiomGenerator;
import org.semanticweb.owlapi.util.InferredOntologyGenerator;
import org.semanticweb.owlapi.util.InferredPropertyAssertionGenerator;
import org.semanticweb.owlapi.util.InferredSubClassAxiomGenerator;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Wraps the OWL API. Responsible for loading the base ontology and for
 * exporting a materialised (inferred) copy that Jena can query.
 */
public class OntologyManager {

    public static final String NAMESPACE = "http://www.semanticweb.org/movies#";

    private final OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
    private OWLOntology ontology;

    public OWLOntology getOntology() { return ontology; }
    public OWLOntologyManager getManager() { return manager; }

    /**
     * Load the ontology from a classpath resource (e.g. "/ontology/movies.owl").
     */
    public void loadOntology(String classpathResource) throws OWLOntologyCreationException {
        InputStream in = getClass().getResourceAsStream(classpathResource);
        if (in == null) {
            throw new OWLOntologyCreationException(
                    "Ontology resource not found on classpath: " + classpathResource);
        }
        this.ontology = manager.loadOntologyFromOntologyDocument(in);
    }

    /**
     * Materialise inferences and save the enriched ontology to disk in RDF/XML,
     * so it can be re-read by Apache Jena for SPARQL.
     */
    public void saveInferred(String outputPath, ReasoningEngine engine) throws Exception {
        OWLReasoner reasoner = engine.getReasoner();
        reasoner.precomputeInferences(
                InferenceType.CLASS_HIERARCHY,
                InferenceType.CLASS_ASSERTIONS,
                InferenceType.OBJECT_PROPERTY_ASSERTIONS,
                InferenceType.DATA_PROPERTY_ASSERTIONS);

        List<InferredAxiomGenerator<? extends OWLAxiom>> generators = new ArrayList<>();
        generators.add(new InferredSubClassAxiomGenerator());
        generators.add(new InferredClassAssertionAxiomGenerator());
        generators.add(new InferredPropertyAssertionGenerator());

        InferredOntologyGenerator iog = new InferredOntologyGenerator(reasoner, generators);
        iog.fillOntology(manager.getOWLDataFactory(), ontology);

        File out = new File(outputPath);
        manager.saveOntology(ontology, new RDFXMLDocumentFormat(), IRI.create(out.toURI()));
        System.out.println("[OntologyManager] Inferred ontology saved to: " + out.getAbsolutePath());
    }
}
