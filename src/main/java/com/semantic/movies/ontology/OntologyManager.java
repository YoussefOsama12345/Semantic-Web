package com.semantic.movies.ontology;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.RDFXMLDocumentFormat;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.InferredAxiomGenerator;
import org.semanticweb.owlapi.util.InferredClassAssertionAxiomGenerator;
import org.semanticweb.owlapi.util.InferredOntologyGenerator;
import org.semanticweb.owlapi.util.InferredSubClassAxiomGenerator;

import java.io.File;
import java.io.InputStream;
import java.util.List;

public class OntologyManager {

    public static final String NAMESPACE = "http://www.semanticweb.org/movies#";

    private final OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
    private OWLOntology ontology;

    public OWLOntology getOntology() { return ontology; }
    public OWLOntologyManager getManager() { return manager; }

    public void loadOntology(String classpathResource) throws OWLOntologyCreationException {
        InputStream in = getClass().getResourceAsStream(classpathResource);
        if (in == null) {
            throw new OWLOntologyCreationException(
                    "Ontology resource not found on classpath: " + classpathResource);
        }
        this.ontology = manager.loadOntologyFromOntologyDocument(in);
    }

    public void saveInferred(String outputPath, ReasoningEngine engine) throws Exception {
        List<InferredAxiomGenerator<? extends OWLAxiom>> generators = List.of(
                new InferredSubClassAxiomGenerator(),
                new InferredClassAssertionAxiomGenerator());
        new InferredOntologyGenerator(engine.getReasoner(), generators)
                .fillOntology(manager.getOWLDataFactory(), ontology);

        File out = new File(outputPath);
        if (out.getParentFile() != null) out.getParentFile().mkdirs();
        manager.saveOntology(ontology, new RDFXMLDocumentFormat(), IRI.create(out.toURI()));
        System.out.println("[OntologyManager] Inferred ontology saved to: " + out.getAbsolutePath());
    }
}
