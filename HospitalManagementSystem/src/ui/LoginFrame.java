package ui;

import dao.AdminFile;
import dao.AppointmentFile;
import dao.DoctorFile;
import dao.ManagerFile;
import dao.PatientFile;
import model.AdminStaff;
import model.Doctor;
import model.MedicalManager;
import model.Patient;
import util.Theme;
import util.Validation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

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

        doctorFile.seedDefaultIfEmpty();
        new AppointmentFile().seedDefaultIfEmpty();
        new AdminFile().seedDefaultIfEmpty();
        new ManagerFile().seedDefaultIfEmpty();
        new PatientFile().seedDefaultIfEmpty();

        JPanel root = new JPanel(new GridLayout(1, 2));
        root.setBackground(Theme.CARD_BG);

        root.add(buildHero());
        root.add(buildFormPanel());

        setContentPane(root);
        setLocationRelativeTo(null);
    }

    private JPanel buildHero() {

        Theme.GradientPanel hero =
                new Theme.GradientPanel(
                        Theme.PRIMARY_DARK,
                        Theme.ACCENT,
                        0
                );

        hero.setLayout(new GridBagLayout());
        hero.setBorder(
                new EmptyBorder(
                        48,
                        48,
                        48,
                        48
                )
        );

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(
                new BoxLayout(
                        content,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel logo = new JLabel("+");

        logo.setFont(
                new Font(
                        Theme.FONT_DISPLAY.getFamily(),
                        Font.BOLD,
                        64
                )
        );

        logo.setForeground(Color.WHITE);
        logo.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        JLabel brand =
                new JLabel("APU Medical Centre");

        brand.setFont(
                Theme.FONT_DISPLAY
        );

        brand.setForeground(
                Color.WHITE
        );

        brand.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        JLabel tagline =
                new JLabel(
                        "<html>"
                                + "Hospital Management System"
                                + "<br>"
                                + "Trusted care, organised."
                                + "</html>"
                );

        tagline.setFont(
                Theme.FONT_BODY
        );

        tagline.setForeground(
                new Color(
                        224,
                        242,
                        241
                )
        );

        tagline.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        content.add(logo);

        content.add(
                Box.createVerticalStrut(18)
        );

        content.add(brand);

        content.add(
                Box.createVerticalStrut(8)
        );

        content.add(tagline);

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.anchor =
                GridBagConstraints.WEST;

        hero.add(
                content,
                gbc
        );

        return hero;
    }

    private JPanel buildFormPanel() {

        JPanel wrap =
                new JPanel(
                        new GridBagLayout()
                );

        wrap.setBackground(
                Theme.CARD_BG
        );

        wrap.setBorder(
                new EmptyBorder(
                        40,
                        56,
                        40,
                        56
                )
        );

        JPanel form =
                new JPanel();

        form.setOpaque(false);

        form.setLayout(
                new BoxLayout(
                        form,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel welcome =
                new JLabel(
                        "Welcome back"
                );

        welcome.setFont(
                Theme.FONT_TITLE
        );

        welcome.setForeground(
                Theme.TEXT_PRIMARY
        );

        welcome.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        JLabel subtitle =
                new JLabel(
                        "Sign in to HMS"
                );

        subtitle.setFont(
                Theme.FONT_BODY
        );

        subtitle.setForeground(
                Theme.TEXT_SECONDARY
        );

        subtitle.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        JLabel emailLabel =
                createLabel(
                        "EMAIL ADDRESS"
                );

        emailField =
                Theme.createTextField(
                        20
                );

        emailField.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        44
                )
        );

        emailField.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        JLabel passwordLabel =
                createLabel(
                        "PASSWORD"
                );

        passwordField =
                Theme.createPasswordField(
                        20
                );

        passwordField.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        44
                )
        );

        passwordField.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        JCheckBox showPassword =
                new JCheckBox(
                        "Show password"
                );

        showPassword.setFont(
                Theme.FONT_SMALL
        );

        showPassword.setForeground(
                Theme.TEXT_SECONDARY
        );

        showPassword.setOpaque(false);

        showPassword.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        final char echo =
                passwordField.getEchoChar();

        showPassword.addActionListener(
                e ->
                        passwordField.setEchoChar(
                                showPassword.isSelected()
                                        ? (char) 0
                                        : echo
                        )
        );

        JButton loginButton =
                Theme.createPrimaryButton(
                        "Sign In"
                );

        loginButton.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        46
                )
        );

        loginButton.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        loginButton.addActionListener(
                e -> attemptLogin()
        );

        JButton signUpButton =
                Theme.createSecondaryButton(
                        "Create Patient Account"
                );

        signUpButton.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        46
                )
        );

        signUpButton.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        signUpButton.addActionListener(
                e -> {

                    dispose();

                    new PatientRegistrationFrame()
                            .setVisible(true);
                }
        );

        passwordField.addActionListener(
                e -> attemptLogin()
        );

        form.add(welcome);

        form.add(
                Box.createVerticalStrut(6)
        );

        form.add(subtitle);

        form.add(
                Box.createVerticalStrut(28)
        );

        form.add(emailLabel);

        form.add(
                Box.createVerticalStrut(6)
        );

        form.add(emailField);

        form.add(
                Box.createVerticalStrut(18)
        );

        form.add(passwordLabel);

        form.add(
                Box.createVerticalStrut(6)
        );

        form.add(passwordField);

        form.add(
                Box.createVerticalStrut(10)
        );

        form.add(showPassword);

        form.add(
                Box.createVerticalStrut(28)
        );

        form.add(loginButton);

        form.add(
                Box.createVerticalStrut(10)
        );

        form.add(signUpButton);

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        gbc.weightx = 1;

        wrap.add(
                form,
                gbc
        );

        return wrap;
    }

    private JLabel createLabel(
            String text) {

        JLabel label =
                new JLabel(text);

        label.setFont(
                Theme.FONT_LABEL
        );

        label.setForeground(
                Theme.TEXT_SECONDARY
        );

        label.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        return label;
    }

    private void attemptLogin() {

        String email =
                emailField
                        .getText()
                        .trim();

        String password =
                new String(
                        passwordField
                                .getPassword()
                );

        if (Validation.isEmpty(email)
                || Validation.isEmpty(password)) {

            Validation.showError(
                    this,
                    "Please enter both email and password."
            );

            return;
        }

        Doctor doctor =
                doctorFile.authenticate(
                        email,
                        password
                );

        if (doctor != null) {

            openDashboard(
                    () ->
                            new DoctorDashboard(
                                    doctor
                            ).setVisible(true)
            );

            return;
        }

        AdminStaff admin =
                new AdminFile()
                        .authenticate(
                                email,
                                password
                        );

        if (admin != null) {

            openDashboard(
                    () ->
                            new AdminDashboard(
                                    admin
                            ).setVisible(true)
            );

            return;
        }

        MedicalManager manager =
                new ManagerFile()
                        .authenticate(
                                email,
                                password
                        );

        if (manager != null) {

            openDashboard(
                    () ->
                            new ManagerDashboard(
                                    manager
                            ).setVisible(true)
            );

            return;
        }

        Patient patient =
                new PatientFile()
                        .authenticate(
                                email,
                                password
                        );

        if (patient != null) {

            openDashboard(
                    () ->
                            new PatientDashboard(
                                    patient
                            ).setVisible(true)
            );

            return;
        }

        Validation.showError(
                this,
                "Invalid email or password."
        );
    }

    private void openDashboard(
            Runnable dashboard) {

        dispose();

        SwingUtilities
                .invokeLater(
                        dashboard
                );
    }

    public static void main(
            String[] args) {

        SwingUtilities.invokeLater(
                () -> {

                    LoginFrame login =
                            new LoginFrame();

                    login.setVisible(true);
                }
        );
    }
}