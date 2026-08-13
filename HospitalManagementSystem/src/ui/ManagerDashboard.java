package ui;

import model.MedicalManager;
import util.Theme;
import util.Validation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Manager Portal Dashboard – scaffold.
 * UI style matches DoctorDashboard (sidebar + CardLayout + Theme).
 *
 * TEAMMATE TODO:
 * 1. Replace buildPlaceholderPanel panels with real feature panels (tables, forms).
 * 2. Add dialogs under ui/ for create/edit actions (same pattern as ProfileDialog).
 * 3. Wire DAO calls (dao/*File) for load/save.
 * 4. Keep Theme / Validation – do not invent a new colour system.
 */
public class ManagerDashboard extends JFrame {

    private final MedicalManager currentUser;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private String activeCard = "HOME";
    private final java.util.List<JButton> navButtons = new java.util.ArrayList<>();

    public ManagerDashboard(MedicalManager user) {
        this.currentUser = user;

        setTitle("Manager Portal – " + user.getName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setMinimumSize(new Dimension(900, 600));
        Theme.styleFrame(this);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Theme.BG);
        root.add(buildSidebar(), BorderLayout.WEST);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(Theme.BG);
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        contentPanel.add(buildHomePanel(), "HOME");
        contentPanel.add(buildPlaceholderPanel("Departments", "Teammate: implement this panel. Same Theme + scroll pattern as Doctor module."), "DEPTS");
        contentPanel.add(buildPlaceholderPanel("Shift Rosters", "Teammate: implement this panel. Same Theme + scroll pattern as Doctor module."), "ROSTERS");
        contentPanel.add(buildPlaceholderPanel("Reports", "Teammate: implement this panel. Same Theme + scroll pattern as Doctor module."), "REPORTS");
        contentPanel.add(buildPlaceholderPanel("My Profile", "Teammate: implement this panel. Same Theme + scroll pattern as Doctor module."), "PROFILE");

        root.add(contentPanel, BorderLayout.CENTER);
        setContentPane(root);
        setLocationRelativeTo(null);

        cardLayout.show(contentPanel, "HOME");
    }

    private JPanel buildSidebar() {
        JPanel side = new JPanel();
        side.setPreferredSize(new Dimension(248, 0));
        side.setBackground(Theme.SIDEBAR);
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setBorder(new EmptyBorder(28, 18, 24, 18));

        JLabel brand = new JLabel("HMS");
        brand.setFont(Theme.FONT_HEADING);
        brand.setForeground(Color.WHITE);
        brand.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel roleLbl = new JLabel("Manager Portal");
        roleLbl.setFont(Theme.FONT_SMALL);
        roleLbl.setForeground(Theme.SIDEBAR_MUTED);
        roleLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        side.add(brand);
        side.add(Box.createVerticalStrut(4));
        side.add(roleLbl);
        side.add(Box.createVerticalStrut(24));

        side.add(navButton("Home", "HOME"));
        side.add(Box.createVerticalStrut(6));
        side.add(navButton("Departments", "DEPTS"));
        side.add(Box.createVerticalStrut(6));
        side.add(navButton("Shift Rosters", "ROSTERS"));
        side.add(Box.createVerticalStrut(6));
        side.add(navButton("Reports", "REPORTS"));
        side.add(Box.createVerticalStrut(6));
        side.add(navButton("My Profile", "PROFILE"));
        side.add(Box.createVerticalStrut(6));

        side.add(Box.createVerticalGlue());

        JLabel userLbl = new JLabel(currentUser.getName());
        userLbl.setFont(Theme.FONT_SMALL);
        userLbl.setForeground(Theme.SIDEBAR_TEXT);
        userLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        side.add(userLbl);
        side.add(Box.createVerticalStrut(10));

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setFont(Theme.FONT_BUTTON);
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setBackground(new Color(51, 65, 85));
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        logoutBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        logoutBtn.addActionListener(e -> {
            if (Validation.confirm(this, "Logout?")) {
                dispose();
                SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
            }
        });
        side.add(logoutBtn);
        return side;
    }

    private JButton navButton(String text, String cardName) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                boolean active = cardName.equals(activeCard);
                if (active) {
                    g2.setColor(Theme.SIDEBAR_ACTIVE);
                    g2.fillRoundRect(0, 4, getWidth(), getHeight() - 8, 10, 10);
                } else if (getModel().isRollover()) {
                    g2.setColor(Theme.SIDEBAR_ACCENT);
                    g2.fillRoundRect(0, 4, getWidth(), getHeight() - 8, 10, 10);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font(Theme.FONT_BODY.getFamily(), Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(new EmptyBorder(0, 14, 0, 12));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.addActionListener(e -> {
            activeCard = cardName;
            cardLayout.show(contentPanel, cardName);
            for (JButton b : navButtons) b.repaint();
        });
        navButtons.add(btn);
        return btn;
    }

    private JPanel buildHomePanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.setOpaque(false);

        JLabel title = new JLabel("Welcome, " + currentUser.getName());
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.TEXT_PRIMARY);

        JLabel sub = new JLabel("<html>Medical Manager scaffold. Implement: create/update departments, design doctor shift rosters, view hospital metrics and revenue summary reports.<br/>"
                + "Use the sidebar to open feature pages. Replace this home panel when ready.</html>");
        sub.setFont(Theme.FONT_BODY);
        sub.setForeground(Theme.TEXT_SECONDARY);

        JPanel card = Theme.createCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.add(title);
        card.add(Box.createVerticalStrut(8));
        card.add(sub);

        JScrollPane scroll = new JScrollPane(card);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Theme.BG);

        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildPlaceholderPanel(String titleText, String hint) {
        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.setOpaque(false);

        JLabel title = new JLabel(titleText);
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.TEXT_PRIMARY);

        JLabel hintLbl = new JLabel("<html>" + hint + "</html>");
        hintLbl.setFont(Theme.FONT_BODY);
        hintLbl.setForeground(Theme.TEXT_SECONDARY);

        JPanel card = Theme.createCard();
        card.setLayout(new BorderLayout(0, 12));
        card.add(title, BorderLayout.NORTH);
        card.add(hintLbl, BorderLayout.CENTER);

        // Empty table shell – teammate fills model/columns
        String[] cols = {"Column 1", "Column 2", "Column 3", "Column 4"};
        JTable table = new JTable(new javax.swing.table.DefaultTableModel(cols, 0));
        table.setRowHeight(36);
        table.setFont(Theme.FONT_BODY);
        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBorder(BorderFactory.createLineBorder(Theme.BORDER, 1, true));
        card.add(tableScroll, BorderLayout.SOUTH);

        JScrollPane scroll = new JScrollPane(card);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Theme.BG);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }
}
