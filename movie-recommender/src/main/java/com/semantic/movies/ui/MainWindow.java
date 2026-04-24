package com.semantic.movies.ui;

import com.semantic.movies.model.SearchResult;
import com.semantic.movies.search.SearchController;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Top-level window. Hosts the search panel, results panel and a status bar.
 */
public class MainWindow extends JFrame {

    private final SearchController searchController;
    private final ResultsPanel resultsPanel;
    private final JLabel statusBar;

    public MainWindow(SearchController searchController) {
        super("Semantic Movie Recommender");
        this.searchController = searchController;

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 650);
        setMinimumSize(new Dimension(600, 400));
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);

        setLayout(new BorderLayout());

        SearchPanel searchPanel = new SearchPanel(this::onSearch);
        add(searchPanel, BorderLayout.NORTH);

        resultsPanel = new ResultsPanel(this::onMovieClicked);
        add(resultsPanel, BorderLayout.CENTER);

        statusBar = new JLabel("  Ready");
        statusBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(0xDDDDDD)));
        statusBar.setOpaque(true);
        statusBar.setBackground(new Color(0xF5F5F5));
        statusBar.setPreferredSize(new Dimension(0, 26));
        add(statusBar, BorderLayout.SOUTH);
    }

    /** Callback invoked by the search panel when the user runs a search. */
    private void onSearch(String query) {
        setStatus("Searching...");
        resultsPanel.showLoading();

        new SwingWorker<List<SearchResult>, Void>() {
            @Override protected List<SearchResult> doInBackground() {
                return searchController.search(query);
            }

            @Override protected void done() {
                try {
                    List<SearchResult> results = get();
                    resultsPanel.showResults(results);
                    setStatus(results.size() + " result" + (results.size() == 1 ? "" : "s")
                            + " for \"" + query + "\"");
                } catch (InterruptedException | ExecutionException ex) {
                    resultsPanel.showError(ex.getMessage());
                    setStatus("Error: " + ex.getMessage());
                }
            }
        }.execute();
    }

    /** Callback when a movie title in the list is clicked - show similar movies. */
    private void onMovieClicked(SearchResult clicked) {
        setStatus("Loading movies similar to \"" + clicked.getTitle() + "\"...");
        resultsPanel.showLoading();

        new SwingWorker<List<SearchResult>, Void>() {
            @Override protected List<SearchResult> doInBackground() {
                return searchController.findSimilar(clicked.getTitle());
            }

            @Override protected void done() {
                try {
                    List<SearchResult> similar = get();
                    resultsPanel.showResults(similar);
                    if (similar.isEmpty()) {
                        setStatus("No similar movies found for \"" + clicked.getTitle() + "\"");
                    } else {
                        setStatus("Showing movies similar to: " + clicked.getTitle());
                    }
                } catch (InterruptedException | ExecutionException ex) {
                    resultsPanel.showError(ex.getMessage());
                    setStatus("Error: " + ex.getMessage());
                }
            }
        }.execute();
    }

    private void setStatus(String text) { statusBar.setText("  " + text); }
}
