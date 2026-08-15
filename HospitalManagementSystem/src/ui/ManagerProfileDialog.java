package ui;

import dao.ManagerFile;
import model.MedicalManager;
import util.Theme;
import util.Validation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ManagerProfileDialog extends JDialog {

    private final MedicalManager manager;
    private final Runnable onSaved;

    private final ManagerFile managerFile = new ManagerFile();

    private JTextField nameField;
    private JTextField emailField;
    private JTextField phoneField;

    private JPasswordField passwordField;

    public ManagerProfileDialog(Frame owner, MedicalManager manager, Runnable onSaved) {
        super(owner, "Edit Profile", true);
        this.manager = manager;
        this.onSaved = onSaved;

        setSize(460, 520);
        setMinimumSize(new Dimension(400, 420));
        setResizable(true);
        Theme.styleDialog(this);

        JPanel root = new JPanel(new BorderLayout(0, 12));
        root.setBackground(Theme.BG);
        root.setBorder(new EmptyBorder(20, 20, 16, 20));

        //=========================================
        //TITLE
        //=========================================
        JLabel title = new JLabel("Edit My Profile");
        title.setFont(Theme.FONT_HEADING);
        title.setForeground(Theme.PRIMARY);

        //=========================================
        //FORM
        //=========================================
        JPanel form = new JPanel();
        form.setOpaque(false);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));

        //Read-only information.
        form.add(readOnlyRow("Manager ID", manager.getId()));
        form.add(Box.createVerticalStrut(10));
        form.add(readOnlyRow("Role", manager.getRole()));
        form.add(Box.createVerticalStrut(10));
        form.add(readOnlyRow("Managed Department", manager.getManagedDepartmentId() != null ? manager.getManagedDepartmentId() : "-"));
        form.add(Box.createVerticalStrut(16));

        //Editable personal information.
        nameField = addEditableField(form, "Full Name", manager.getName());
        emailField = addEditableField(form, "Email", manager.getEmail());
        phoneField = addEditableField(form, "Phone", manager.getPhone());

        //Password.
        JLabel passwordLabel = new JLabel("New Password (leave blank to keep current)");
        passwordLabel.setFont(Theme.FONT_SMALL);
        passwordLabel.setForeground( Theme.TEXT_SECONDARY);
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        form.add(passwordLabel);
        form.add(Box.createVerticalStrut(4));

        passwordField = Theme.createPasswordField(20);
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);

        form.add(passwordField);

        //=========================================
        //SCROLL
        //=========================================
        JScrollPane scroll = new JScrollPane(form);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Theme.BG);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        //=========================================
        //BUTTONS
        //=========================================
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttons.setOpaque(false);
        JButton cancelButton = Theme.createSecondaryButton("Cancel");
        JButton saveButton = Theme.createPrimaryButton("Save Changes");

        cancelButton.addActionListener(e -> dispose());
        saveButton.addActionListener(e -> saveProfile());
        buttons.add(cancelButton);
        buttons.add(saveButton);

        //=========================================
        //BUILD DIALOG
        //=========================================
        root.add(title, BorderLayout.NORTH);
        root.add(scroll, BorderLayout.CENTER);
        root.add(buttons, BorderLayout.SOUTH);
        setContentPane(root);
        setLocationRelativeTo(owner);
    }

    //Read-only information row.
    private JPanel readOnlyRow( String label, String value) {
        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));

        JLabel labelComponent = new JLabel(label + ":");
        labelComponent.setFont(Theme.FONT_SMALL);
        labelComponent.setForeground(Theme.TEXT_SECONDARY);
        labelComponent.setPreferredSize(new Dimension(140, 24));

        JLabel valueComponent = new JLabel(value != null ? value : "-");
        valueComponent.setFont(Theme.FONT_BODY);
        valueComponent.setForeground(Theme.TEXT_PRIMARY);
        row.add(labelComponent,BorderLayout.WEST);
        row.add(valueComponent, BorderLayout.CENTER);

        return row;
    }

    //Create one editable text field.
    private JTextField addEditableField(JPanel form, String label, String value) {
        JLabel fieldLabel = new JLabel(label);
        fieldLabel.setFont(Theme.FONT_SMALL);
        fieldLabel.setForeground(Theme.TEXT_SECONDARY);
        fieldLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        form.add(fieldLabel);
        form.add(Box.createVerticalStrut(4));

        JTextField textField =Theme.createTextField(20);
        textField.setText(value != null ? value : "");
        textField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        textField.setAlignmentX(Component.LEFT_ALIGNMENT);

        form.add(textField);
        form.add(Box.createVerticalStrut(12));

        return textField;
    }

    //Save profile changes.
    private void saveProfile() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String newPassword = new String(passwordField.getPassword()).trim();

        //Required fields.
        if (Validation.isEmpty(name) || Validation.isEmpty(email) || Validation.isEmpty(phone)) {
            Validation.showError(this,"Name, email and phone are required.");
            return;
        }

        //Email validation.
        if (!Validation.isValidEmail(email)) {
            Validation.showError(this, "Please enter a valid email address.");
            return;
        }

        //Phone validation
        if (!Validation.isValidPhone(phone)) {
            Validation.showError(this, "Please enter a valid phone number.");
            return;
        }

        //Check whether another manager already uses this email.
        MedicalManager existingManager = managerFile.findByEmail(email);
        if (existingManager != null && !existingManager.getId().equalsIgnoreCase(manager.getId())) {
            Validation.showError(this,"This email address is already used by another Medical Manager.");
            return;
        }

        //Update personal information.
        manager.setName(name);
        manager.setEmail(email);
        manager.setPhone(phone);

        //Blank password means: keep current password.
        if (!Validation.isEmpty(newPassword)) {
            manager.setPassword(newPassword);
        }

        //Save updated manager.
        managerFile.save(manager);
        Validation.showSuccess(this, "Profile updated successfully.");

        //Tell ManagerDashboard to refresh
        if (onSaved != null) {
            onSaved.run();
        }
        dispose();
    }
}