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

    private static final Color ERROR_COLOR = new Color(0xB1, 0x2A, 0x2A);

    private static final String CARD_SEARCH = "search";
    private static final String CARD_DETAIL = "detail";

    private final SearchController searchController;
    private final ResultsPanel resultsPanel;
    private final MovieDetailPanel detailPanel;
    private final JLabel statusBar;
    private final CardLayout cards = new CardLayout();
    private final JPanel cardHost = new JPanel(cards);

    public MainWindow(SearchController searchController) {
        super("Cinema");
        this.searchController = searchController;

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1100, 760);
        setMinimumSize(new Dimension(820, 540));
        setLocationRelativeTo(null);
        getContentPane().setBackground(Theme.BG);
        setLayout(new BorderLayout());

        resultsPanel = new ResultsPanel(this::onMovieClicked);
        detailPanel  = new MovieDetailPanel(this::onBack);
        statusBar    = buildStatusBar();

        cardHost.setBackground(Theme.BG);
        cardHost.add(buildSearchCard(), CARD_SEARCH);
        cardHost.add(detailPanel,       CARD_DETAIL);

        add(buildHeader(), BorderLayout.NORTH);
        add(cardHost,      BorderLayout.CENTER);
        add(statusBar,     BorderLayout.SOUTH);

        cards.show(cardHost, CARD_SEARCH);
    }

    private JPanel buildHeader() {
        return new HeaderBar(
                this::onAll,
                this::onAcclaimed,
                this::onMasterpieces,
                this::onGenre);
    }

    private JPanel buildSearchCard() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Theme.BG);
        card.add(new SearchPanel(this::onSearch), BorderLayout.NORTH);
        card.add(resultsPanel,                     BorderLayout.CENTER);
        return card;
    }

    private JLabel buildStatusBar() {
        JLabel bar = new JLabel("  Ready");
        bar.setFont(Theme.FONT_STATUS);
        bar.setForeground(Theme.TEXT_MUTED);
        bar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, Theme.BORDER),
                new EmptyBorder(8, 36, 8, 36)));
        bar.setOpaque(true);
        bar.setBackground(Theme.HEADER_BG);
        bar.setPreferredSize(new Dimension(0, 30));
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

    private void onAll() {
        setStatus("Loading all movies...");
        runAsync(
                () -> searchController.findAll(),
                results -> setStatus(results.size() + " movie" + (results.size() == 1 ? "" : "s")
                        + " in catalogue"));
    }

    private void onAcclaimed() {
        setStatus("Loading acclaimed movies...");
        runAsync(
                () -> searchController.findAcclaimed(),
                results -> setStatus(results.isEmpty()
                        ? "No acclaimed movies found."
                        : results.size() + " acclaimed movie" + (results.size() == 1 ? "" : "s")));
    }

    private void onMasterpieces() {
        setStatus("Loading masterpieces...");
        runAsync(
                () -> searchController.findMasterpieces(),
                results -> setStatus(results.isEmpty()
                        ? "No masterpieces found."
                        : results.size() + " masterpiece" + (results.size() == 1 ? "" : "s")));
    }

    private void onGenre(String genre) {
        setStatus("Loading " + genre + " movies...");
        runAsync(
                () -> searchController.search(genre),
                results -> setStatus(results.size() + " " + genre + " movie"
                        + (results.size() == 1 ? "" : "s")));
    }

    private void onMovieClicked(SearchResult clicked) {
        detailPanel.show(clicked);
        cards.show(cardHost, CARD_DETAIL);
        setStatus("Showing details for \"" + clicked.getTitle() + "\"");
    }

    private void onBack() {
        cards.show(cardHost, CARD_SEARCH);
        setStatus("Ready");
    }

    private void runAsync(Supplier<List<SearchResult>> task, Consumer<List<SearchResult>> onDone) {
        cards.show(cardHost, CARD_SEARCH);
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
        statusBar.setForeground(Theme.TEXT_MUTED);
    }

    private void setError(String text) {
        statusBar.setText("  Error: " + text);
        statusBar.setForeground(ERROR_COLOR);
    }
}
