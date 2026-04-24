package com.semantic.movies;

import com.semantic.movies.ontology.OntologyManager;
import com.semantic.movies.ontology.ReasoningEngine;
import com.semantic.movies.query.SPARQLExecutor;
import com.semantic.movies.search.SearchController;
import com.semantic.movies.ui.MainWindow;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Application entry point.
 * Loads the ontology, runs the reasoner, and launches the Swing UI.
 */
public final class Main {

    private Main() { }

    public static void main(String[] args) {
        // Use the system / light look-and-feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) { /* fall back to default */ }

        try {
            // 1. Load base ontology from classpath resource
            OntologyManager ontology = new OntologyManager();
            ontology.loadOntology("/ontology/movies.owl");

            // 2. Initialise HermiT reasoner and register SWRL rules
            ReasoningEngine reasoner = new ReasoningEngine(ontology);
            reasoner.loadSwrlRules();
            reasoner.runReasoning();

            // 3. Save the inferred ontology so Jena/SPARQL can query it
            String inferredPath = "movies-inferred.owl";
            ontology.saveInferred(inferredPath, reasoner);

            // 4. Create SPARQL executor and search controller over inferred data
            SPARQLExecutor sparql = new SPARQLExecutor(inferredPath);
            SearchController search = new SearchController(sparql);

            // 5. Launch UI on the EDT
            SwingUtilities.invokeLater(() -> {
                MainWindow window = new MainWindow(search);
                window.setVisible(true);
            });
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Failed to start the Semantic Movie Recommender:\n" + ex.getMessage(),
                    "Startup Error",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
}
