package ui;

import dao.*;
import model.*;
import util.Theme;
import util.Validation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Modern login window - entry point of the application.
 * Currently authenticates Doctors (can be extended for other roles).
 */
public class LoginFrame extends JFrame {

    private JTextField emailField;
    private JPasswordField passwordField;
    private final DoctorFile doctorFile = new DoctorFile();

    public LoginFrame() {
        setTitle("APU Medical Centre - Hospital Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(920, 600);
        setResizable(false);
        Theme.styleFrame(this);

        // Seed demo data for all roles on first launch
        doctorFile.seedDefaultIfEmpty();
        new AppointmentFile().seedDefaultIfEmpty();
        new AdminFile().seedDefaultIfEmpty();
        new ManagerFile().seedDefaultIfEmpty();
        new PatientFile().seedDefaultIfEmpty();

        // ===== Root: split hero (left) + form (right) =====
        JPanel root = new JPanel(new GridLayout(1, 2));
        root.setBackground(Theme.CARD_BG);

        root.add(buildHero());
        root.add(buildFormPanel());

        setContentPane(root);
        setLocationRelativeTo(null);
    }

    /** Left branded hero panel with a teal gradient. */
    private JPanel buildHero() {
        Theme.GradientPanel hero = new Theme.GradientPanel(Theme.PRIMARY_DARK, Theme.ACCENT, 0);
        hero.setLayout(new GridBagLayout());
        hero.setBorder(new EmptyBorder(48, 48, 48, 48));

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JLabel logo = new JLabel("\u271A"); // heavy medical cross
        logo.setFont(new Font(Theme.FONT_DISPLAY.getFamily(), Font.BOLD, 64));
        logo.setForeground(Color.WHITE);
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel brand = new JLabel("APU Medical Centre");
        brand.setFont(Theme.FONT_DISPLAY);
        brand.setForeground(Color.WHITE);
        brand.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel tagline = new JLabel("<html>Hospital Management System<br/><span>Trusted care, organised.</span></html>");
        tagline.setFont(Theme.FONT_BODY);
        tagline.setForeground(new Color(224, 242, 241));
        tagline.setAlignmentX(Component.LEFT_ALIGNMENT);

        content.add(logo);
        content.add(Box.createVerticalStrut(18));
        content.add(brand);
        content.add(Box.createVerticalStrut(8));
        content.add(tagline);
        content.add(Box.createVerticalStrut(32));

        content.add(heroFeature("1"));
        content.add(Box.createVerticalStrut(12));
        content.add(heroFeature("2"));
        content.add(Box.createVerticalStrut(12));
        content.add(heroFeature("3"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1;
        hero.add(content, gbc);
        return hero;
    }

    private JPanel heroFeature(String text) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel dot = new JLabel("\u2713");
        dot.setFont(new Font(Theme.FONT_BODY.getFamily(), Font.BOLD, 14));
        dot.setForeground(Color.WHITE);
        JLabel lbl = new JLabel(text);
        lbl.setFont(Theme.FONT_BODY);
        lbl.setForeground(new Color(224, 242, 241));
        row.add(dot);
        row.add(lbl);
        return row;
    }

    /** Right side sign-in form. */
    private JPanel buildFormPanel() {
        JPanel wrap = new JPanel(new GridBagLayout());
        wrap.setBackground(Theme.CARD_BG);
        wrap.setBorder(new EmptyBorder(40, 56, 40, 56));

        JPanel form = new JPanel();
        form.setOpaque(false);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));

        JLabel welcome = new JLabel("Welcome back");
        welcome.setFont(Theme.FONT_TITLE);
        welcome.setForeground(Theme.TEXT_PRIMARY);
        welcome.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel formSub = new JLabel("Sign in to HMS (all roles)");
        formSub.setFont(Theme.FONT_BODY);
        formSub.setForeground(Theme.TEXT_SECONDARY);
        formSub.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel emailLbl = fieldLabel("EMAIL ADDRESS");

        emailField = Theme.createTextField(20);
        emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        emailField.setAlignmentX(Component.LEFT_ALIGNMENT);
        emailField.setText("doctor@hms.com"); // demo convenience

        JLabel passLbl = fieldLabel("PASSWORD");

        passwordField = Theme.createPasswordField(20);
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        passwordField.setText("doctor123");

        // Show / hide password toggle
        JCheckBox showPass = new JCheckBox("Show password");
        showPass.setFont(Theme.FONT_SMALL);
        showPass.setForeground(Theme.TEXT_SECONDARY);
        showPass.setOpaque(false);
        showPass.setFocusPainted(false);
        showPass.setAlignmentX(Component.LEFT_ALIGNMENT);
        showPass.setCursor(new Cursor(Cursor.HAND_CURSOR));
        final char echo = passwordField.getEchoChar();
        showPass.addActionListener(e ->
                passwordField.setEchoChar(showPass.isSelected() ? (char) 0 : echo));

        JButton loginBtn = Theme.createPrimaryButton("Sign In");
        loginBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        loginBtn.addActionListener(e -> attemptLogin());

        // Enter key triggers login
        passwordField.addActionListener(e -> attemptLogin());

        // Footer hint
        JLabel hint = new JLabel("<html><span style='color:#64748b'>"
                + "Demo: doctor@hms.com / doctor123<br/>"
                + "admin@hms.com / admin123 &nbsp;|&nbsp; manager@hms.com / manager123<br/>"
                + "ahmad@email.com / patient123"
                + "</span></html>");
        hint.setFont(Theme.FONT_SMALL);
        hint.setForeground(Theme.TEXT_SECONDARY);
        hint.setAlignmentX(Component.LEFT_ALIGNMENT);

        form.add(welcome);
        form.add(Box.createVerticalStrut(6));
        form.add(formSub);
        form.add(Box.createVerticalStrut(28));
        form.add(emailLbl);
        form.add(Box.createVerticalStrut(6));
        form.add(emailField);
        form.add(Box.createVerticalStrut(18));
        form.add(passLbl);
        form.add(Box.createVerticalStrut(6));
        form.add(passwordField);
        form.add(Box.createVerticalStrut(10));
        form.add(showPass);
        form.add(Box.createVerticalStrut(28));
        form.add(loginBtn);
        form.add(Box.createVerticalStrut(24));
        form.add(hint);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        wrap.add(form, gbc);
        return wrap;
    }

    private JLabel fieldLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(Theme.FONT_LABEL);
        l.setForeground(Theme.TEXT_SECONDARY);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    /**
     * Try each role's DAO in order. Teammates do NOT need to change this method
     * unless they add a brand-new role class.
     */
    private void attemptLogin() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (Validation.isEmpty(email) || Validation.isEmpty(password)) {
            Validation.showError(this, "Please enter both email and password.");
            return;
        }

        // 1) Doctor
        Doctor doctor = doctorFile.authenticate(email, password);
        if (doctor != null) {
            openDashboard(() -> new DoctorDashboard(doctor).setVisible(true));
            return;
        }

        // 2) Admin Staff
        AdminStaff admin = new AdminFile().authenticate(email, password);
        if (admin != null) {
            openDashboard(() -> new AdminDashboard(admin).setVisible(true));
            return;
        }

        // 3) Medical Manager
        MedicalManager manager = new ManagerFile().authenticate(email, password);
        if (manager != null) {
            openDashboard(() -> new ManagerDashboard(manager).setVisible(true));
            return;
        }

        // 4) Patient
        Patient patient = new PatientFile().authenticate(email, password);
        if (patient != null) {
            openDashboard(() -> new PatientDashboard(patient).setVisible(true));
            return;
        }

        Validation.showError(this, "Invalid credentials or inactive account.");
    }

    private void openDashboard(Runnable show) {
        dispose();
        SwingUtilities.invokeLater(show);
    }

    public static void main(String[] args) {
        // Use system look-and-feel as base, then override with our Theme
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            LoginFrame login = new LoginFrame();
            login.setVisible(true);
        });
    }
}
