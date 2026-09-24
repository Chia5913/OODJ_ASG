package ui;

import dao.AdminFile;
import dao.DoctorFile;
import dao.ManagerFile;
import dao.PatientFile;
import model.Patient;
import util.FileManager;
import util.Theme;
import util.Validation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PatientRegistrationFrame
        extends JFrame {

    private JTextField nameField;
    private JTextField emailField;
    private JTextField phoneField;

    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;

    private JComboBox<String> bloodTypeBox;

    private JTextField allergiesField;
    private JTextField insuranceField;
    private JTextField emergencyField;

    private final PatientFile patientFile =
            new PatientFile();

    public PatientRegistrationFrame() {

        setTitle(
                "Patient Registration - APU Medical Centre"
        );

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        setSize(
                760,
                720
        );

        setMinimumSize(
                new Dimension(
                        700,
                        650
                )
        );

        Theme.styleFrame(this);

        setContentPane(
                buildMainPanel()
        );

        setLocationRelativeTo(null);
    }

    private JPanel buildMainPanel() {

        JPanel root =
                new JPanel(
                        new BorderLayout()
                );

        root.setBackground(
                Theme.BG
        );

        root.setBorder(
                new EmptyBorder(
                        22,
                        22,
                        22,
                        22
                )
        );

        JPanel card =
                Theme.createCard(26);

        card.setLayout(
                new BoxLayout(
                        card,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel title =
                new JLabel(
                        "Create Patient Account"
                );

        title.setFont(
                Theme.FONT_TITLE
        );

        title.setForeground(
                Theme.TEXT_PRIMARY
        );

        title.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        JLabel subtitle =
                new JLabel(
                        "Register as a patient to access the Patient Portal."
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

        card.add(title);

        card.add(
                Box.createVerticalStrut(5)
        );

        card.add(subtitle);

        card.add(
                Box.createVerticalStrut(22)
        );

        nameField =
                Theme.createTextField(25);

        emailField =
                Theme.createTextField(25);

        phoneField =
                Theme.createTextField(25);

        passwordField =
                Theme.createPasswordField(25);

        confirmPasswordField =
                Theme.createPasswordField(25);

        bloodTypeBox =
                Theme.createComboBox(
                        new String[]{
                                "Select Blood Type",
                                "A+",
                                "A-",
                                "B+",
                                "B-",
                                "AB+",
                                "AB-",
                                "O+",
                                "O-"
                        }
                );

        allergiesField =
                Theme.createTextField(25);

        insuranceField =
                Theme.createTextField(25);

        emergencyField =
                Theme.createTextField(25);

        addField(
                card,
                "FULL NAME",
                nameField
        );

        addField(
                card,
                "EMAIL ADDRESS",
                emailField
        );

        addField(
                card,
                "PHONE NUMBER",
                phoneField
        );

        addField(
                card,
                "PASSWORD",
                passwordField
        );

        addField(
                card,
                "CONFIRM PASSWORD",
                confirmPasswordField
        );

        addField(
                card,
                "BLOOD TYPE",
                bloodTypeBox
        );

        addField(
                card,
                "ALLERGIES",
                allergiesField
        );

        addField(
                card,
                "INSURANCE PROVIDER",
                insuranceField
        );

        addField(
                card,
                "EMERGENCY CONTACT",
                emergencyField
        );

        JCheckBox showPasswords =
                new JCheckBox(
                        "Show passwords"
                );

        showPasswords.setFont(
                Theme.FONT_SMALL
        );

        showPasswords.setForeground(
                Theme.TEXT_SECONDARY
        );

        showPasswords.setOpaque(false);

        showPasswords.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        final char passwordEcho =
                passwordField.getEchoChar();

        final char confirmEcho =
                confirmPasswordField
                        .getEchoChar();

        showPasswords
                .addActionListener(
                        e -> {

                            boolean show =
                                    showPasswords
                                            .isSelected();

                            passwordField
                                    .setEchoChar(
                                            show
                                                    ? (char) 0
                                                    : passwordEcho
                                    );

                            confirmPasswordField
                                    .setEchoChar(
                                            show
                                                    ? (char) 0
                                                    : confirmEcho
                                    );
                        }
                );

        card.add(showPasswords);

        card.add(
                Box.createVerticalStrut(20)
        );

        JPanel buttons =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                10,
                                0
                        )
                );

        buttons.setOpaque(false);

        buttons.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        JButton registerButton =
                Theme.createPrimaryButton(
                        "Create Account"
                );

        JButton backButton =
                Theme.createSecondaryButton(
                        "Back to Login"
                );

        registerButton
                .addActionListener(
                        e -> registerPatient()
                );

        backButton
                .addActionListener(
                        e -> backToLogin()
                );

        buttons.add(
                registerButton
        );

        buttons.add(
                backButton
        );

        card.add(buttons);

        JScrollPane scroll =
                new JScrollPane(card);

        scroll.setBorder(
                BorderFactory
                        .createEmptyBorder()
        );

        scroll.getViewport()
                .setBackground(
                        Theme.BG
                );

        scroll.getVerticalScrollBar()
                .setUnitIncrement(16);

        root.add(
                scroll,
                BorderLayout.CENTER
        );

        return root;
    }

    private void addField(
            JPanel panel,
            String labelText,
            JComponent field) {

        JLabel label =
                new JLabel(
                        labelText
                );

        label.setFont(
                Theme.FONT_LABEL
        );

        label.setForeground(
                Theme.TEXT_SECONDARY
        );

        label.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        field.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        42
                )
        );

        field.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        panel.add(label);

        panel.add(
                Box.createVerticalStrut(5)
        );

        panel.add(field);

        panel.add(
                Box.createVerticalStrut(14)
        );
    }

    private void registerPatient() {

        String name =
                nameField
                        .getText()
                        .trim();

        String email =
                emailField
                        .getText()
                        .trim();

        String phone =
                phoneField
                        .getText()
                        .trim();

        String password =
                new String(
                        passwordField
                                .getPassword()
                );

        String confirmPassword =
                new String(
                        confirmPasswordField
                                .getPassword()
                );

        String bloodType =
                (String)
                        bloodTypeBox
                                .getSelectedItem();

        String allergies =
                allergiesField
                        .getText()
                        .trim();

        String insurance =
                insuranceField
                        .getText()
                        .trim();

        String emergencyContact =
                emergencyField
                        .getText()
                        .trim();

        if (Validation.isEmpty(name)
                || Validation.isEmpty(email)
                || Validation.isEmpty(phone)
                || Validation.isEmpty(password)
                || Validation.isEmpty(confirmPassword)
                || bloodTypeBox.getSelectedIndex() == 0
                || Validation.isEmpty(emergencyContact)) {

            Validation.showError(
                    this,
                    "Please complete all required fields.\n"
                            + "Allergies and insurance provider may be left blank."
            );

            return;
        }

        if (!Validation.isValidEmail(email)) {

            Validation.showError(
                    this,
                    "Please enter a valid email address."
            );

            return;
        }

        if (!Validation.isValidPhone(phone)) {

            Validation.showError(
                    this,
                    "Please enter a valid phone number."
            );

            return;
        }

        if (!Validation.isValidPhone(
                emergencyContact)) {

            Validation.showError(
                    this,
                    "Please enter a valid emergency contact number."
            );

            return;
        }

        if (password.length() < 6) {

            Validation.showError(
                    this,
                    "Password must contain at least 6 characters."
            );

            return;
        }

        if (!password.equals(
                confirmPassword)) {

            Validation.showError(
                    this,
                    "The passwords do not match."
            );

            return;
        }

        if (containsPipe(name)
                || containsPipe(email)
                || containsPipe(phone)
                || containsPipe(password)
                || containsPipe(allergies)
                || containsPipe(insurance)
                || containsPipe(emergencyContact)) {

            Validation.showError(
                    this,
                    "Please do not use the | symbol in any field."
            );

            return;
        }

        if (emailAlreadyExists(email)) {

            Validation.showError(
                    this,
                    "This email address is already registered."
            );

            return;
        }

        if (allergies.isEmpty()) {

            allergies = "None";
        }

        if (insurance.isEmpty()) {

            insurance = "None";
        }

        Patient patient =
                new Patient(
                        FileManager
                                .generateId("PAT"),
                        name,
                        email,
                        phone,
                        password,
                        bloodType,
                        allergies,
                        insurance,
                        emergencyContact
                );

        patientFile.save(
                patient
        );

        Validation.showSuccess(
                this,
                "Patient account created successfully.\n"
                        + "You can now sign in."
        );

        dispose();

        new LoginFrame()
                .setVisible(true);
    }

    private boolean emailAlreadyExists(
            String email) {

        if (patientFile
                .findByEmail(email)
                != null) {

            return true;
        }

        if (new DoctorFile()
                .findByEmail(email)
                != null) {

            return true;
        }

        if (new AdminFile()
                .findByEmail(email)
                != null) {

            return true;
        }

        return new ManagerFile()
                .findByEmail(email)
                != null;
    }

    private boolean containsPipe(
            String value) {

        return value != null
                && value.contains("|");
    }

    private void backToLogin() {

        dispose();

        new LoginFrame()
                .setVisible(true);
    }
}