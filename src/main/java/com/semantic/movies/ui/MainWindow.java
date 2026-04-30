package com.semantic.movies.ui;

import com.semantic.movies.model.SearchResult;
import com.semantic.movies.search.SearchController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class MainWindow extends JFrame {

    private static final Color ERROR_COLOR = new Color(0xB91C1C);

    private static final String CARD_SEARCH = "search";
    private static final String CARD_DETAIL = "detail";

    private final SearchController searchController;
    private final ResultsPanel resultsPanel;
    private final MovieDetailPanel detailPanel;
    private final JLabel statusBar;
    private final CardLayout cards = new CardLayout();
    private final JPanel cardHost = new JPanel(cards);

    public MainWindow(SearchController searchController) {
        super("Semantic Movie Recommender");
        this.searchController = searchController;

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 720);
        setMinimumSize(new Dimension(720, 480));
        setLocationRelativeTo(null);
        getContentPane().setBackground(Theme.BG);
        setLayout(new BorderLayout());

        resultsPanel = new ResultsPanel(this::onMovieClicked);
        detailPanel  = new MovieDetailPanel(this::onBack, this::onMovieClicked);
        statusBar    = buildStatusBar();

        cardHost.setBackground(Theme.BG);
        cardHost.add(buildSearchCard(), CARD_SEARCH);
        cardHost.add(detailPanel,       CARD_DETAIL);

        add(cardHost,  BorderLayout.CENTER);
        add(statusBar, BorderLayout.SOUTH);

        cards.show(cardHost, CARD_SEARCH);
    }

    private JPanel buildSearchCard() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Theme.BG);
        card.add(buildTop(),    BorderLayout.NORTH);
        card.add(resultsPanel,  BorderLayout.CENTER);
        return card;
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
        setStatus("Loading acclaimed movies...");
        runAsync(
                () -> searchController.findAcclaimed(),
                results -> setStatus(results.isEmpty()
                        ? "No acclaimed movies found."
                        : results.size() + " acclaimed movie" + (results.size() == 1 ? "" : "s")));
    }

    private void onMovieClicked(SearchResult clicked) {
        detailPanel.show(clicked);
        cards.show(cardHost, CARD_DETAIL);
        setStatus("Loading movies similar to \"" + clicked.getTitle() + "\"...");

        new SwingWorker<List<SearchResult>, Void>() {
            @Override protected List<SearchResult> doInBackground() {
                return searchController.findSimilar(clicked.getTitle());
            }
            @Override protected void done() {
                try {
                    List<SearchResult> similar = get();
                    detailPanel.setSimilar(similar);
                    setStatus(similar.isEmpty()
                            ? "No similar movies for \"" + clicked.getTitle() + "\""
                            : similar.size() + " similar movie" + (similar.size() == 1 ? "" : "s")
                                    + " for \"" + clicked.getTitle() + "\"");
                } catch (InterruptedException | ExecutionException ex) {
                    setError(ex.getMessage());
                }
            }
        }.execute();
    }

    private void onBack() {
        cards.show(cardHost, CARD_SEARCH);
        setStatus("Ready");
    }

    private void runAsync(Supplier<List<SearchResult>> task, Consumer<List<SearchResult>> onDone) {
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
