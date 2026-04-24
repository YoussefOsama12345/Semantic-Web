package com.semantic.movies.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.function.Consumer;

public class SearchPanel extends JPanel {

    private static final String PLACEHOLDER = "Search by movie, actor, director, or genre...";

    private final JTextField searchField = new JTextField();
    private final Consumer<String> onSearch;

    public SearchPanel(Consumer<String> onSearch) {
        this.onSearch = onSearch;

        setLayout(new BorderLayout(8, 0));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(16, 20, 16, 20));

        searchField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xCCCCCC), 1),
                new EmptyBorder(8, 12, 8, 12)));
        installPlaceholder();
        searchField.addActionListener(e -> fireSearch());

        JButton searchBtn = new JButton("Search");
        searchBtn.setFont(searchBtn.getFont().deriveFont(Font.BOLD));
        searchBtn.addActionListener(e -> fireSearch());

        JButton clearBtn = new JButton("Clear");
        clearBtn.addActionListener(e -> {
            searchField.setText("");
            installPlaceholder();
            searchField.requestFocusInWindow();
        });

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        buttons.setOpaque(false);
        buttons.add(searchBtn);
        buttons.add(clearBtn);

        add(searchField, BorderLayout.CENTER);
        add(buttons, BorderLayout.EAST);
    }

    private void fireSearch() {
        String text = searchField.getText();
        if (text == null || text.equals(PLACEHOLDER)) return;
        if (!text.isBlank()) onSearch.accept(text.trim());
    }

    private void installPlaceholder() {
        searchField.setText(PLACEHOLDER);
        searchField.setForeground(Color.GRAY);
        searchField.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                if (searchField.getText().equals(PLACEHOLDER)) {
                    searchField.setText("");
                    searchField.setForeground(Color.BLACK);
                }
            }
            @Override public void focusLost(FocusEvent e) {
                if (searchField.getText().isBlank()) {
                    searchField.setText(PLACEHOLDER);
                    searchField.setForeground(Color.GRAY);
                }
            }
        });
    }
}
