package util;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Central place for modern UI colours, fonts and helper methods.
 * Keeps every dialog / frame consistent (modern flat, clinical look).
 *
 * The palette uses a calm medical teal as the brand colour paired with
 * clean slate neutrals - a trustworthy, contemporary healthcare tone.
 */
public class Theme {

    // ---------------------------------------------------------------------
    // Colour palette - modern medical / clinical tone
    // ---------------------------------------------------------------------
    public static final Color PRIMARY       = new Color(13, 148, 136);  // teal-600
    public static final Color PRIMARY_DARK  = new Color(15, 118, 110);  // teal-700
    public static final Color PRIMARY_LIGHT = new Color(204, 251, 241); // teal-100
    public static final Color ACCENT        = new Color(20, 184, 166);  // teal-500
    public static final Color SUCCESS       = new Color(5, 150, 105);   // emerald-600
    public static final Color WARNING       = new Color(217, 119, 6);   // amber-600
    public static final Color DANGER        = new Color(220, 38, 38);   // red-600
    public static final Color DANGER_DARK   = new Color(185, 28, 28);   // red-700
    public static final Color BG            = new Color(241, 245, 249); // slate-100
    public static final Color CARD_BG       = Color.WHITE;
    public static final Color TEXT_PRIMARY  = new Color(15, 23, 42);    // slate-900
    public static final Color TEXT_SECONDARY= new Color(100, 116, 139); // slate-500
    public static final Color BORDER        = new Color(226, 232, 240); // slate-200
    public static final Color FIELD_BG      = new Color(248, 250, 252); // slate-50

    // Sidebar - deep teal-tinted slate for a clinical, premium feel
    public static final Color SIDEBAR         = new Color(15, 42, 46);   // deep teal-slate
    public static final Color SIDEBAR_ACCENT  = new Color(17, 54, 59);   // hover
    public static final Color SIDEBAR_ACTIVE  = new Color(13, 148, 136); // active pill
    public static final Color SIDEBAR_TEXT    = new Color(203, 213, 225);// slate-300
    public static final Color SIDEBAR_MUTED   = new Color(100, 130, 132);

    // ---------------------------------------------------------------------
    // Fonts
    // ---------------------------------------------------------------------
    private static final String FAMILY = pickFont();

    public static final Font FONT_DISPLAY  = new Font(FAMILY, Font.BOLD, 26);
    public static final Font FONT_TITLE    = new Font(FAMILY, Font.BOLD, 21);
    public static final Font FONT_HEADING  = new Font(FAMILY, Font.BOLD, 16);
    public static final Font FONT_BODY     = new Font(FAMILY, Font.PLAIN, 13);
    public static final Font FONT_SMALL    = new Font(FAMILY, Font.PLAIN, 11);
    public static final Font FONT_LABEL    = new Font(FAMILY, Font.BOLD, 11);
    public static final Font FONT_BUTTON   = new Font(FAMILY, Font.BOLD, 13);

    private static String pickFont() {
        String[] preferred = {"Segoe UI", "SF Pro Text", "Helvetica Neue", "Inter", "Roboto"};
        java.util.List<String> available = java.util.Arrays.asList(
                GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
        for (String p : preferred) if (available.contains(p)) return p;
        return "SansSerif";
    }

    // ---------------------------------------------------------------------
    // Buttons - custom painted for smooth rounded corners
    // ---------------------------------------------------------------------
    private static final int RADIUS = 12;

    private static JButton makeRoundButton(String text, Color base, Color hover, Color fg, boolean outline) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight();
                Color bg = getModel().isPressed()
                        ? hover.darker()
                        : (getModel().isRollover() ? hover : base);
                if (outline) {
                    g2.setColor(getModel().isRollover() ? PRIMARY_LIGHT : CARD_BG);
                    g2.fillRoundRect(0, 0, w - 1, h - 1, RADIUS, RADIUS);
                    g2.setColor(PRIMARY);
                    g2.setStroke(new BasicStroke(1.4f));
                    g2.drawRoundRect(0, 0, w - 2, h - 2, RADIUS, RADIUS);
                } else {
                    g2.setColor(bg);
                    g2.fillRoundRect(0, 0, w, h, RADIUS, RADIUS);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(FONT_BUTTON);
        btn.setForeground(fg);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(9, 18, 9, 18));
        btn.setPreferredSize(new Dimension(150, 40));
        return btn;
    }

    /** Modern flat primary (teal) button. */
    public static JButton createPrimaryButton(String text) {
        return makeRoundButton(text, PRIMARY, PRIMARY_DARK, Color.WHITE, false);
    }

    /** Secondary / outline button. */
    public static JButton createSecondaryButton(String text) {
        return makeRoundButton(text, CARD_BG, PRIMARY_LIGHT, PRIMARY, true);
    }

    /** Danger (red) button. */
    public static JButton createDangerButton(String text) {
        return makeRoundButton(text, DANGER, DANGER_DARK, Color.WHITE, false);
    }

    // ---------------------------------------------------------------------
    // Inputs - rounded, focus-aware
    // ---------------------------------------------------------------------
    private static void styleTextComponent(JComponent c) {
        c.setFont(FONT_BODY);
        c.setBackground(FIELD_BG);
        c.setForeground(TEXT_PRIMARY);
        c.setBorder(new RoundedInputBorder());
        if (c instanceof JTextField) ((JTextField) c).setCaretColor(PRIMARY);
    }

    public static JTextField createTextField(int columns) {
        JTextField tf = new JTextField(columns);
        styleTextComponent(tf);
        addFocusHighlight(tf);
        return tf;
    }

    public static JPasswordField createPasswordField(int columns) {
        JPasswordField pf = new JPasswordField(columns);
        styleTextComponent(pf);
        addFocusHighlight(pf);
        return pf;
    }

    public static JTextArea createTextArea(int rows, int cols) {
        JTextArea ta = new JTextArea(rows, cols);
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        styleTextComponent(ta);
        addFocusHighlight(ta);
        return ta;
    }

    public static JComboBox<String> createComboBox(String[] items) {
        JComboBox<String> cb = new JComboBox<>(items);
        cb.setFont(FONT_BODY);
        cb.setBackground(CARD_BG);
        cb.setForeground(TEXT_PRIMARY);
        cb.setBorder(new RoundedInputBorder());
        return cb;
    }

    private static void addFocusHighlight(JComponent c) {
        c.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                c.setBorder(new RoundedInputBorder(PRIMARY, 1.6f));
                c.setBackground(CARD_BG);
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                c.setBorder(new RoundedInputBorder());
                c.setBackground(FIELD_BG);
            }
        });
    }

    // ---------------------------------------------------------------------
    // Frames / dialogs
    // ---------------------------------------------------------------------
    public static void styleFrame(JFrame frame) {
        frame.getContentPane().setBackground(BG);
        frame.setLocationRelativeTo(null);
    }

    public static void styleDialog(JDialog dialog) {
        dialog.getContentPane().setBackground(BG);
        dialog.setLocationRelativeTo(dialog.getParent());
    }

    // ---------------------------------------------------------------------
    // Cards - rounded panel with a soft drop shadow
    // ---------------------------------------------------------------------
    public static JPanel createCard() {
        return createCard(20);
    }

    public static JPanel createCard(int pad) {
        RoundedPanel card = new RoundedPanel(16);
        card.setBackground(CARD_BG);
        card.setBorder(new EmptyBorder(pad, pad, pad, pad));
        return card;
    }

    /** A small coloured status pill / badge. */
    public static JLabel createBadge(String text, Color fg, Color bg) {
        JLabel badge = new JLabel(text, SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        badge.setOpaque(false);
        badge.setForeground(fg);
        badge.setFont(FONT_LABEL);
        badge.setBorder(new EmptyBorder(4, 12, 4, 12));
        return badge;
    }

    // ---------------------------------------------------------------------
    // Reusable painted components
    // ---------------------------------------------------------------------

    /** A panel that paints a rounded background plus a soft shadow. */
    public static class RoundedPanel extends JPanel {
        private final int arc;
        public RoundedPanel(int arc) {
            this.arc = arc;
            setOpaque(false);
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();
            // soft shadow
            for (int i = 6; i > 0; i--) {
                g2.setColor(new Color(15, 23, 42, 4));
                g2.fill(new RoundRectangle2D.Float(i, i + 1, w - i * 2, h - i * 2, arc, arc));
            }
            g2.setColor(getBackground());
            g2.fill(new RoundRectangle2D.Float(2, 1, w - 5, h - 6, arc, arc));
            g2.setColor(BORDER);
            g2.setStroke(new BasicStroke(1f));
            g2.draw(new RoundRectangle2D.Float(2, 1, w - 5, h - 6, arc, arc));
            g2.dispose();
            super.paintComponent(g);
        }
    }

    /** Rounded border for input fields. */
    public static class RoundedInputBorder extends AbstractBorder {
        private final Color color;
        private final float thickness;
        public RoundedInputBorder() { this(BORDER, 1f); }
        public RoundedInputBorder(Color color, float thickness) {
            this.color = color;
            this.thickness = thickness;
        }
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(thickness));
            g2.draw(new RoundRectangle2D.Float(x + 1, y + 1, w - 3, h - 3, 10, 10));
            g2.dispose();
        }
        @Override
        public Insets getBorderInsets(Component c) { return new Insets(9, 12, 9, 12); }
        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.set(9, 12, 9, 12);
            return insets;
        }
    }

    /** A panel with a teal gradient - used for hero / header areas. */
    public static class GradientPanel extends JPanel {
        private final Color from, to;
        private final int arc;
        public GradientPanel(Color from, Color to, int arc) {
            this.from = from; this.to = to; this.arc = arc;
            setOpaque(false);
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setPaint(new GradientPaint(0, 0, from, getWidth(), getHeight(), to));
            if (arc > 0) g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
            else g2.fillRect(0, 0, getWidth(), getHeight());
            g2.dispose();
            super.paintComponent(g);
        }
    }
}
