package com.semantic.movies.ui;

import java.awt.Color;
import java.awt.Font;

public final class Theme {

    // Royal Purple + Gold theme - v3

    // Backgrounds
    public static final Color BG          = new Color(0xF8, 0xF5, 0xFA);  // soft lavender-cream
    public static final Color HEADER_BG   = new Color(0xFF, 0xFF, 0xFF);  // pure white
    public static final Color CARD        = new Color(0xFF, 0xFF, 0xFF);  // pure white cards
    public static final Color CARD_HOVER  = new Color(0xF1, 0xEC, 0xF7);  // light purple tint hover
    public static final Color BORDER      = new Color(0xE8, 0xE1, 0xF0);  // soft purple border
    public static final Color BORDER_SOFT = new Color(0xF0, 0xEB, 0xF5);  // even softer

    // Accents
    public static final Color PRIMARY      = new Color(0x5B, 0x2C, 0x82);  // deep royal purple
    public static final Color PRIMARY_SOFT = new Color(0xEE, 0xE3, 0xF7);  // pale purple tint
    public static final Color GOLD         = new Color(0xC9, 0xA2, 0x27);  // rich gold
    public static final Color GOLD_SOFT    = new Color(0xFA, 0xF4, 0xD8);  // pale gold

    // Text
    public static final Color TEXT_PRIMARY   = new Color(0x1A, 0x0F, 0x2E);  // deep purple-black
    public static final Color TEXT_SECONDARY = new Color(0x5E, 0x52, 0x7A);  // muted purple-gray
    public static final Color TEXT_MUTED     = new Color(0x9E, 0x96, 0xB5);  // soft purple-gray

    // Backwards-compat aliases used by existing code
    public static final Color ACCENT_SOFT = GOLD_SOFT;

    // Fonts — modern sans-serif throughout (no serif this time)
    public static final Font FONT_BRAND    = new Font("SansSerif", Font.BOLD,  22);
    public static final Font FONT_HERO     = new Font("SansSerif", Font.PLAIN, 14);
    public static final Font FONT_TITLE    = new Font("SansSerif", Font.BOLD,  17);
    public static final Font FONT_SUBTITLE = new Font("SansSerif", Font.PLAIN, 13);
    public static final Font FONT_INPUT    = new Font("SansSerif", Font.PLAIN, 15);
    public static final Font FONT_STATUS   = new Font("SansSerif", Font.PLAIN, 12);
    public static final Font FONT_PILL     = new Font("SansSerif", Font.BOLD,  12);

    public static final int INPUT_HEIGHT = 46;

    private Theme() { }
}
