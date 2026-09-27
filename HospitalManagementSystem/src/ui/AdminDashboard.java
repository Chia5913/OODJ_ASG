package ui;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import model.Admin;
import util.Theme;
import util.Validation;

/*I just modify what my teammate gave me*/

public class AdminDashboard extends JFrame {

    private Admin currentUser;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private String activeCard = "HOME";
    private List<JButton> navButtons = new ArrayList<>();

    public AdminDashboard(Admin user) {
        this.currentUser = user;

        setTitle("Admin Portal – " + user.getAdminFirstName() + " " + user.getAdminLastName());
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
        contentPanel.add(buildPlaceholderPanel("Manage Users", "Teammate: implement this panel. Same Theme + scroll pattern as Doctor module."), "USERS");
        contentPanel.add(buildPlaceholderPanel("Assign Doctors", "Teammate: implement this panel. Same Theme + scroll pattern as Doctor module."), "ASSIGN");
        contentPanel.add(buildPlaceholderPanel("Hospital Assets", "Teammate: implement this panel. Same Theme + scroll pattern as Doctor module."), "ASSETS");
        contentPanel.add(buildPlaceholderPanel("Rates & Insurance", "Teammate: implement this panel. Same Theme + scroll pattern as Doctor module."), "RATES");

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

        JLabel roleLbl = new JLabel("Admin Portal");
        roleLbl.setFont(Theme.FONT_SMALL);
        roleLbl.setForeground(Theme.SIDEBAR_MUTED);
        roleLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        side.add(brand);
        side.add(Box.createVerticalStrut(4));
        side.add(roleLbl);
        side.add(Box.createVerticalStrut(24));

        side.add(navButton("Home", "HOME"));
        side.add(Box.createVerticalStrut(6));
        side.add(navButton("Manage Users", "USERS"));
        side.add(Box.createVerticalStrut(6));
        side.add(navButton("Assign Doctors", "ASSIGN"));
        side.add(Box.createVerticalStrut(6));
        side.add(navButton("Hospital Assets", "ASSETS"));
        side.add(Box.createVerticalStrut(6));
        side.add(navButton("Rates & Insurance", "RATES"));
        side.add(Box.createVerticalStrut(6));

        side.add(Box.createVerticalGlue());

        JLabel userLbl = new JLabel(currentUser.getAdminFirstName() + " " + currentUser.getAdminLastName());
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
        JButton btn = new JButton(text);

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

        JLabel title = new JLabel("Welcome, " + currentUser.getAdminFirstName() + " " + currentUser.getAdminLastName());
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.TEXT_PRIMARY);

        JLabel sub = new JLabel("<html>Admin Staff scaffold. Implement: CRUD users, assign doctors to managers, manage rooms/wards/labs, configure consultation rates & insurance networks.<br/>"
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
