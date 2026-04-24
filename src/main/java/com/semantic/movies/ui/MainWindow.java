package com.semantic.movies.ui;

import com.semantic.movies.model.SearchResult;
import com.semantic.movies.search.SearchController;


import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

public class MainWindow extends JFrame {

    private static final Color ERROR_COLOR = new Color(0xB91C1C);

    private final SearchController searchController;
    private final ResultsPanel resultsPanel;
    private final JLabel statusBar;

    public MainWindow(SearchController searchController) {
        super("Semantic Movie Recommender");
        this.searchController = searchController;

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(960, 700);
        setMinimumSize(new Dimension(680, 480));
        setLocationRelativeTo(null);
        getContentPane().setBackground(Theme.BG);
        setLayout(new BorderLayout());

        resultsPanel = new ResultsPanel(this::onMovieClicked);
        statusBar    = buildStatusBar();

        add(buildTop(),    BorderLayout.NORTH);
        add(resultsPanel,  BorderLayout.CENTER);
        add(statusBar,     BorderLayout.SOUTH);
    }

    private JPanel buildTop() {
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(Theme.BG);
        top.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER));
        top.add(new SearchPanel(this::onSearch), BorderLayout.CENTER);
        return top;
    }

    private JLabel buildStatusBar() {
        JLabel bar = new JLabel("  Ready");
        bar.setFont(Theme.FONT_STATUS);
        bar.setForeground(Theme.TEXT_SECONDARY);
        bar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, Theme.BORDER),
                new EmptyBorder(6, 18, 6, 18)));
        bar.setOpaque(true);
        bar.setBackground(Theme.CARD);
        bar.setPreferredSize(new Dimension(0, 28));
        return bar;
    }

    private void onSearch(String query) {
        if (query == null || query.isBlank()) {
            resultsPanel.reset();
            setStatus("Ready");
            return;
        }
        setStatus("Searching...");
        runAsync(
                () -> searchController.search(query),
                results -> setStatus(results.size() + " result" + (results.size() == 1 ? "" : "s")
                        + " for \"" + query + "\""));
    }

    private void onMovieClicked(SearchResult clicked) {
        setStatus("Loading movies similar to \"" + clicked.getTitle() + "\"...");
        runAsync(
                () -> searchController.findSimilar(clicked.getTitle()),
                similar -> setStatus(similar.isEmpty()
                        ? "No similar movies found for \"" + clicked.getTitle() + "\""
                        : "Showing movies similar to: " + clicked.getTitle()));
    }

    private void runAsync(Supplier<List<SearchResult>> task, java.util.function.Consumer<List<SearchResult>> onDone) {
        resultsPanel.showLoading();
        new SwingWorker<List<SearchResult>, Void>() {
            @Override protected List<SearchResult> doInBackground() { return task.get(); }

            @Override protected void done() {
                try {
                    List<SearchResult> results = get();
                    resultsPanel.showResults(results);
                    onDone.accept(results);
                } catch (InterruptedException | ExecutionException ex) {
                    resultsPanel.showError(ex.getMessage());
                    setError(ex.getMessage());
                }
            }
        }.execute();
    }

    private void setStatus(String text) {
        statusBar.setText("  " + text);
        statusBar.setForeground(Theme.TEXT_SECONDARY);
    }

    private void setError(String text) {
        statusBar.setText("  Error: " + text);
        statusBar.setForeground(ERROR_COLOR);
    }
}
