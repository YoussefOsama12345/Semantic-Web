package com.semantic.movies.ui;

import java.awt.Color;
import java.awt.Font;

public final class Theme {

    // Cinematic dark theme - v2

    // Backgrounds
    public static final Color BG          = new Color(0x0A, 0x0A, 0x0F);
    public static final Color SIDEBAR_BG  = new Color(0x0F, 0x0F, 0x18);
    public static final Color CARD        = new Color(0x1B, 0x1B, 0x26);
    public static final Color CARD_HOVER  = new Color(0x29, 0x29, 0x3A);
    public static final Color BORDER      = new Color(0x2D, 0x2D, 0x3D);

    // Accents
    public static final Color PRIMARY     = new Color(0xFF, 0xB8, 0x00);  // warm gold
    public static final Color ACCENT_SOFT = new Color(0x3A, 0x2C, 0x10);  // dim gold for badges
    public static final Color ACCENT_RED  = new Color(0xE5, 0x09, 0x14);  // signature red

    // Text
    public static final Color TEXT_PRIMARY   = new Color(0xF5, 0xF5, 0xF5);
    public static final Color TEXT_SECONDARY = new Color(0xA0, 0xA0, 0xAC);
    public static final Color TEXT_MUTED     = new Color(0x6B, 0x6B, 0x7D);

    // Fonts
    public static final Font FONT_BRAND       = new Font("SansSerif", Font.BOLD, 18);
    public static final Font FONT_TITLE       = new Font("SansSerif", Font.BOLD, 17);
    public static final Font FONT_SUBTITLE    = new Font("SansSerif", Font.PLAIN, 13);
    public static final Font FONT_INPUT       = new Font("SansSerif", Font.PLAIN, 15);
    public static final Font FONT_STATUS      = new Font("SansSerif", Font.PLAIN, 12);
    public static final Font FONT_NAV         = new Font("SansSerif", Font.BOLD, 13);
    public static final Font FONT_NAV_SECTION = new Font("SansSerif", Font.BOLD, 11);

    public static final int INPUT_HEIGHT = 44;

    private Theme() { }
}
