package com.semantic.movies;

import com.semantic.movies.ontology.OntologyManager;
import com.semantic.movies.ontology.ReasoningEngine;
import com.semantic.movies.query.SPARQLExecutor;
import com.semantic.movies.search.SearchController;
import com.semantic.movies.ui.MainWindow;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public final class Main {

    private Main() { }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) { }

        try {
            OntologyManager ontology = new OntologyManager();
            ontology.loadOntology("/ontology/movies.owl");

            ReasoningEngine reasoner = new ReasoningEngine(ontology);
            reasoner.loadSwrlRules();
            reasoner.runReasoning();

            String inferredPath = "movies-inferred.owl";
            ontology.saveInferred(inferredPath, reasoner);

            SPARQLExecutor sparql = new SPARQLExecutor(inferredPath);
            SearchController search = new SearchController(sparql);

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
