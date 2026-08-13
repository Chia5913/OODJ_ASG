package ui;

import dao.DoctorFile;
import model.Doctor;
import util.Theme;
import util.Validation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Pop-up dialog for a Doctor to edit personal profile details.
 * Scrollable so every field stays visible on smaller screens.
 */
public class ProfileDialog extends JDialog {

    private final Doctor doctor;
    private final Runnable onSaved;
    private final DoctorFile doctorFile = new DoctorFile();

    private JTextField nameField, emailField, phoneField, specialtyField, shiftField, feeField;
    private JPasswordField passwordField;

    public ProfileDialog(Frame owner, Doctor doctor, Runnable onSaved) {
        super(owner, "Edit Profile", true);
        this.doctor = doctor;
        this.onSaved = onSaved;

        setSize(460, 560);
        setMinimumSize(new Dimension(400, 420));
        setResizable(true);
        Theme.styleDialog(this);

        JPanel root = new JPanel(new BorderLayout(0, 12));
        root.setBackground(Theme.BG);
        root.setBorder(new EmptyBorder(20, 20, 16, 20));

        JLabel title = new JLabel("Edit My Profile");
        title.setFont(Theme.FONT_HEADING);
        title.setForeground(Theme.PRIMARY);

        // ----- Scrollable form -----
        JPanel form = new JPanel();
        form.setOpaque(false);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));

        // Read-only identity fields
        form.add(readOnlyRow("Doctor ID", doctor.getId()));
        form.add(Box.createVerticalStrut(10));
        form.add(readOnlyRow("Department",
                doctor.getDepartmentId() != null ? doctor.getDepartmentId() : "-"));
        form.add(Box.createVerticalStrut(10));
        form.add(readOnlyRow("Manager ID",
                doctor.getManagerId() != null ? doctor.getManagerId() : "-"));
        form.add(Box.createVerticalStrut(14));

        nameField      = addEditableField(form, "Full Name", doctor.getName());
        emailField     = addEditableField(form, "Email", doctor.getEmail());
        phoneField     = addEditableField(form, "Phone", doctor.getPhone());
        specialtyField = addEditableField(form, "Specialty", doctor.getSpecialty());
        shiftField     = addEditableField(form, "Shift", doctor.getShift());
        feeField       = addEditableField(form, "Consultation Fee (RM)",
                String.valueOf(doctor.getConsultationFee()));

        JLabel passLbl = new JLabel("New Password (leave blank to keep current)");
        passLbl.setFont(Theme.FONT_SMALL);
        passLbl.setForeground(Theme.TEXT_SECONDARY);
        passLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(passLbl);
        form.add(Box.createVerticalStrut(4));
        passwordField = Theme.createPasswordField(20);
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(passwordField);
        form.add(Box.createVerticalStrut(8));

        JScrollPane scroll = new JScrollPane(form);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Theme.BG);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        // ----- Buttons (always visible) -----
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttons.setOpaque(false);

        JButton cancel = Theme.createSecondaryButton("Cancel");
        cancel.addActionListener(e -> dispose());

        JButton save = Theme.createPrimaryButton("Save Changes");
        save.addActionListener(e -> saveProfile());

        buttons.add(cancel);
        buttons.add(save);

        root.add(title, BorderLayout.NORTH);
        root.add(scroll, BorderLayout.CENTER);
        root.add(buttons, BorderLayout.SOUTH);
        setContentPane(root);
        setLocationRelativeTo(owner);
    }

    private JPanel readOnlyRow(String label, String value) {
        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));

        JLabel lbl = new JLabel(label + ":");
        lbl.setFont(Theme.FONT_SMALL);
        lbl.setForeground(Theme.TEXT_SECONDARY);
        lbl.setPreferredSize(new Dimension(110, 24));

        JLabel val = new JLabel(value != null ? value : "-");
        val.setFont(Theme.FONT_BODY);
        val.setForeground(Theme.TEXT_PRIMARY);

        row.add(lbl, BorderLayout.WEST);
        row.add(val, BorderLayout.CENTER);
        return row;
    }

    private JTextField addEditableField(JPanel form, String label, String value) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(Theme.FONT_SMALL);
        lbl.setForeground(Theme.TEXT_SECONDARY);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(lbl);
        form.add(Box.createVerticalStrut(4));

        JTextField tf = Theme.createTextField(20);
        tf.setText(value != null ? value : "");
        tf.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        tf.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(tf);
        form.add(Box.createVerticalStrut(12));
        return tf;
    }

    private void saveProfile() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String specialty = specialtyField.getText().trim();
        String shift = shiftField.getText().trim();
        String feeStr = feeField.getText().trim();
        String newPass = new String(passwordField.getPassword());

        if (Validation.isEmpty(name) || Validation.isEmpty(email) || Validation.isEmpty(phone)) {
            Validation.showError(this, "Name, email and phone are required.");
            return;
        }
        if (!Validation.isValidEmail(email)) {
            Validation.showError(this, "Please enter a valid email address.");
            return;
        }
        if (!Validation.isValidPhone(phone)) {
            Validation.showError(this, "Please enter a valid phone number.");
            return;
        }
        if (!Validation.isPositiveNumber(feeStr)) {
            Validation.showError(this, "Consultation fee must be a positive number.");
            return;
        }

        doctor.setName(name);
        doctor.setEmail(email);
        doctor.setPhone(phone);
        doctor.setSpecialty(specialty);
        doctor.setShift(shift);
        doctor.setConsultationFee(Double.parseDouble(feeStr));
        if (!Validation.isEmpty(newPass)) {
            doctor.setPassword(newPass);
        }

        doctorFile.save(doctor);
        Validation.showSuccess(this, "Profile updated successfully.");
        if (onSaved != null) onSaved.run();
        dispose();
    }
}
