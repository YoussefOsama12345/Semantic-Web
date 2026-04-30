package com.semantic.movies.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

public class SearchPanel extends JPanel {

    private static final String PLACEHOLDER = "🔎  Search movies, actors, directors, or genres...";
    private static final int DEBOUNCE_MS = 280;

    private final JTextField field = new JTextField();
    private final JLabel clearIcon = new JLabel("✕", SwingConstants.CENTER);
    private final Consumer<String> onSearch;
    private final Timer debounce;

    public SearchPanel(Consumer<String> onSearch) {
        this.onSearch = onSearch;
        this.debounce = new Timer(DEBOUNCE_MS, e -> fireSearch());
        debounce.setRepeats(false);

        setBackground(Theme.BG);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(28, 36, 20, 36));

        add(buildSearchBar(), BorderLayout.CENTER);
    }

    private JPanel buildSearchBar() {
        RoundedBar bar = new RoundedBar();
        bar.setLayout(new BorderLayout(10, 0));
        bar.setBorder(new EmptyBorder(0, 18, 0, 14));
        bar.setPreferredSize(new Dimension(0, Theme.INPUT_HEIGHT));

        configureField();
        configureClearIcon();

        bar.add(field,     BorderLayout.CENTER);
        bar.add(clearIcon, BorderLayout.EAST);
        return bar;
    }

    private void configureField() {
        field.setFont(Theme.FONT_INPUT);
        field.setBorder(null);
        field.setOpaque(false);
        field.setForeground(Theme.TEXT_PRIMARY);
        field.setCaretColor(Theme.PRIMARY);

        field.addActionListener(e -> { debounce.stop(); fireSearch(); });
        field.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                if (field.getText().equals(PLACEHOLDER)) {
                    field.setText("");
                    field.setForeground(Theme.TEXT_PRIMARY);
                }
            }
            @Override public void focusLost(FocusEvent e) {
                if (field.getText().isBlank()) showPlaceholder();
            }
        });
        field.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e)  { onTextChanged(); }
            @Override public void removeUpdate(DocumentEvent e)  { onTextChanged(); }
            @Override public void changedUpdate(DocumentEvent e) { onTextChanged(); }
        });
        showPlaceholder();
    }

    private void configureClearIcon() {
        clearIcon.setFont(new Font("SansSerif", Font.BOLD, 14));
        clearIcon.setForeground(Theme.TEXT_MUTED);
        clearIcon.setPreferredSize(new Dimension(22, 22));
        clearIcon.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        clearIcon.setVisible(false);
        clearIcon.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { clearIcon.setForeground(Theme.PRIMARY); }
            @Override public void mouseExited(MouseEvent e)  { clearIcon.setForeground(Theme.TEXT_MUTED); }
            @Override public void mouseClicked(MouseEvent e) {
                field.setText("");
                showPlaceholder();
                field.requestFocusInWindow();
            }
        });
    }

    private void onTextChanged() {
        String text = field.getText();
        boolean hasRealText = !text.isBlank() && !text.equals(PLACEHOLDER);
        clearIcon.setVisible(hasRealText);
        debounce.restart();
    }

    private void fireSearch() {
        String text = field.getText();
        if (text.equals(PLACEHOLDER) || text.isBlank()) { onSearch.accept(""); return; }
        onSearch.accept(text.trim());
    }

    private void showPlaceholder() {
        field.setText(PLACEHOLDER);
        field.setForeground(Theme.TEXT_MUTED);
        clearIcon.setVisible(false);
    }

    private static class RoundedBar extends JPanel {
        RoundedBar() { setOpaque(false); }

        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Theme.CARD);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
            g2.setColor(Theme.BORDER);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
            g2.dispose();
        }
    }
}
