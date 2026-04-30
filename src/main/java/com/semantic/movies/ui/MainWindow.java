package com.semantic.movies.ui;

import com.semantic.movies.model.SearchResult;
import com.semantic.movies.search.SearchController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

public class MainWindow extends JFrame {

    private static final Color ERROR_COLOR = new Color(0xB91C1C);

    private final SearchController searchController;
    private final ResultsPanel resultsPanel;
    private final MovieDetailPanel detailPanel;
    private final JLabel statusBar;

    public MainWindow(SearchController searchController) {
        super("Semantic Movie Recommender");
        this.searchController = searchController;

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1100, 700);
        setMinimumSize(new Dimension(780, 480));
        setLocationRelativeTo(null);
        getContentPane().setBackground(Theme.BG);
        setLayout(new BorderLayout());

        resultsPanel = new ResultsPanel(this::onMovieClicked);
        detailPanel  = new MovieDetailPanel();
        detailPanel.setVisible(false);
        statusBar    = buildStatusBar();

        add(buildTop(),   BorderLayout.NORTH);
        add(resultsPanel, BorderLayout.CENTER);
        add(detailPanel,  BorderLayout.EAST);
        add(statusBar,    BorderLayout.SOUTH);
    }

    private JPanel buildTop() {
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(Theme.BG);
        top.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER));
        top.add(new SearchPanel(this::onSearch, this::onAcclaimed), BorderLayout.CENTER);
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
        hideDetail();
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

    private void onAcclaimed() {
        hideDetail();
        setStatus("Loading acclaimed movies...");
        runAsync(
                () -> searchController.findAcclaimed(),
                results -> setStatus(results.isEmpty()
                        ? "No acclaimed movies found."
                        : results.size() + " acclaimed movie" + (results.size() == 1 ? "" : "s")));
    }

    private void onMovieClicked(SearchResult clicked) {
        detailPanel.show(clicked);
        if (!detailPanel.isVisible()) {
            detailPanel.setVisible(true);
            revalidate();
        }
        setStatus("Loading movies similar to \"" + clicked.getTitle() + "\"...");
        runAsync(
                () -> searchController.findSimilar(clicked.getTitle()),
                similar -> setStatus(similar.isEmpty()
                        ? "No similar movies found for \"" + clicked.getTitle() + "\""
                        : "Showing movies similar to: " + clicked.getTitle()));
    }

    private void hideDetail() {
        if (detailPanel.isVisible()) {
            detailPanel.setVisible(false);
            revalidate();
        }
    }

    private void runAsync(Supplier<List<SearchResult>> task,
                          java.util.function.Consumer<List<SearchResult>> onDone) {
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
