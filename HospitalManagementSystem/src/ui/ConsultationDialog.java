package ui;

import dao.AppointmentFile;
import dao.ConsultationFile;
import model.Appointment;
import model.Consultation;
import model.Doctor;
import util.FileManager;
import util.Theme;
import util.Validation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Pop-up dialog for logging patient vital signs and clinical notes.
 * Form is scrollable so all fields remain reachable.
 */
public class ConsultationDialog extends JDialog {

    private final Appointment linkedAppointment;
    private final Doctor doctor;
    private final Runnable onSaved;
    private final ConsultationFile consultationFile = new ConsultationFile();
    private final AppointmentFile appointmentFile = new AppointmentFile();

    private JTextField patientIdField, vitalField, diagnosisField;
    private JTextArea notesArea;

    public ConsultationDialog(Frame owner, Appointment appointment, Doctor doctor, Runnable onSaved) {
        super(owner, "Consultation Notes", true);
        this.linkedAppointment = appointment;
        this.doctor = doctor;
        this.onSaved = onSaved;

        setSize(500, 540);
        setMinimumSize(new Dimension(420, 400));
        setResizable(true);
        Theme.styleDialog(this);

        JPanel root = new JPanel(new BorderLayout(0, 12));
        root.setBackground(Theme.BG);
        root.setBorder(new EmptyBorder(20, 20, 16, 20));

        JLabel title = new JLabel("Log Consultation");
        title.setFont(Theme.FONT_HEADING);
        title.setForeground(Theme.PRIMARY);

        JPanel form = new JPanel();
        form.setOpaque(false);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));

        patientIdField = addLabeledField(form, "Patient ID",
                appointment != null ? appointment.getPatientId() : "");
        if (appointment != null) patientIdField.setEditable(false);

        vitalField = addLabeledField(form, "Vital Signs (e.g. BP 120/80, HR 72, Temp 36.8)", "");
        diagnosisField = addLabeledField(form, "Diagnosis", "");

        JLabel notesLbl = new JLabel("Clinical Notes");
        notesLbl.setFont(Theme.FONT_SMALL);
        notesLbl.setForeground(Theme.TEXT_SECONDARY);
        notesLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(notesLbl);
        form.add(Box.createVerticalStrut(4));

        notesArea = Theme.createTextArea(6, 30);
        notesArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        // Prefer a reasonable preferred size so scroll works well
        notesArea.setRows(6);
        form.add(notesArea);
        form.add(Box.createVerticalStrut(8));

        JScrollPane scroll = new JScrollPane(form);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Theme.BG);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttons.setOpaque(false);

        JButton cancel = Theme.createSecondaryButton("Cancel");
        cancel.addActionListener(e -> dispose());

        JButton save = Theme.createPrimaryButton("Save Consultation");
        save.addActionListener(e -> saveConsultation());

        buttons.add(cancel);
        buttons.add(save);

        root.add(title, BorderLayout.NORTH);
        root.add(scroll, BorderLayout.CENTER);
        root.add(buttons, BorderLayout.SOUTH);
        setContentPane(root);
        setLocationRelativeTo(owner);
    }

    private JTextField addLabeledField(JPanel form, String label, String value) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(Theme.FONT_SMALL);
        lbl.setForeground(Theme.TEXT_SECONDARY);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(lbl);
        form.add(Box.createVerticalStrut(4));

        JTextField tf = Theme.createTextField(25);
        tf.setText(value != null ? value : "");
        tf.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        tf.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(tf);
        form.add(Box.createVerticalStrut(12));
        return tf;
    }

    private void saveConsultation() {
        String patientId = patientIdField.getText().trim();
        String vitals = vitalField.getText().trim();
        String diagnosis = diagnosisField.getText().trim();
        String notes = notesArea.getText().trim();

        if (Validation.isEmpty(patientId)) {
            Validation.showError(this, "Patient ID is required.");
            return;
        }
        if (Validation.isEmpty(vitals) && Validation.isEmpty(notes)) {
            Validation.showError(this, "Please enter vital signs or clinical notes.");
            return;
        }

        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        String aptId = linkedAppointment != null ? linkedAppointment.getAppointmentId() : "";

        Consultation c = new Consultation(
                FileManager.generateId("CON"),
                aptId,
                patientId,
                doctor.getId(),
                now,
                vitals,
                notes,
                diagnosis
        );
        consultationFile.save(c);

        if (linkedAppointment != null) {
            linkedAppointment.setStatus("COMPLETED");
            appointmentFile.save(linkedAppointment);
        }

        Validation.showSuccess(this, "Consultation saved successfully.");
        if (onSaved != null) onSaved.run();
        dispose();
    }
}
