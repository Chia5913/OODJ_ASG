package ui;

import dao.PrescriptionFile;
import model.Consultation;
import model.Doctor;
import model.Prescription;
import util.FileManager;
import util.Theme;
import util.Validation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Pop-up dialog for issuing a digital medication prescription.
 * Scrollable form so all fields stay accessible.
 */
public class PrescriptionDialog extends JDialog {

    private final Consultation consultation;
    private final Doctor doctor;
    private final Runnable onSaved;
    private final PrescriptionFile prescriptionFile = new PrescriptionFile();

    private JTextField patientIdField, medicationField, dosageField, durationField;
    private JTextArea instructionsArea;

    public PrescriptionDialog(Frame owner, Consultation consultation, Doctor doctor, Runnable onSaved) {
        super(owner, "Issue Prescription", true);
        this.consultation = consultation;
        this.doctor = doctor;
        this.onSaved = onSaved;

        setSize(480, 520);
        setMinimumSize(new Dimension(400, 380));
        setResizable(true);
        Theme.styleDialog(this);

        JPanel root = new JPanel(new BorderLayout(0, 12));
        root.setBackground(Theme.BG);
        root.setBorder(new EmptyBorder(20, 20, 16, 20));

        JLabel title = new JLabel("Digital Prescription");
        title.setFont(Theme.FONT_HEADING);
        title.setForeground(Theme.PRIMARY);

        JPanel form = new JPanel();
        form.setOpaque(false);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));

        patientIdField = addField(form, "Patient ID",
                consultation != null ? consultation.getPatientId() : "");
        if (consultation != null) patientIdField.setEditable(false);

        medicationField = addField(form, "Medication (e.g. Amoxicillin 500mg)", "");
        dosageField = addField(form, "Dosage (e.g. 1 tablet twice daily)", "");
        durationField = addField(form, "Duration (e.g. 7 days)", "");

        JLabel instrLbl = new JLabel("Special Instructions");
        instrLbl.setFont(Theme.FONT_SMALL);
        instrLbl.setForeground(Theme.TEXT_SECONDARY);
        instrLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(instrLbl);
        form.add(Box.createVerticalStrut(4));

        instructionsArea = Theme.createTextArea(4, 25);
        instructionsArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(instructionsArea);
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

        JButton save = Theme.createPrimaryButton("Issue Prescription");
        save.addActionListener(e -> savePrescription());

        buttons.add(cancel);
        buttons.add(save);

        root.add(title, BorderLayout.NORTH);
        root.add(scroll, BorderLayout.CENTER);
        root.add(buttons, BorderLayout.SOUTH);
        setContentPane(root);
        setLocationRelativeTo(owner);
    }

    private JTextField addField(JPanel form, String label, String value) {
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

    private void savePrescription() {
        String patientId = patientIdField.getText().trim();
        String medication = medicationField.getText().trim();
        String dosage = dosageField.getText().trim();
        String duration = durationField.getText().trim();
        String instructions = instructionsArea.getText().trim();

        if (Validation.isEmpty(patientId) || Validation.isEmpty(medication)
                || Validation.isEmpty(dosage) || Validation.isEmpty(duration)) {
            Validation.showError(this, "Patient ID, Medication, Dosage and Duration are required.");
            return;
        }

        String today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
        String consultId = consultation != null ? consultation.getConsultationId() : "";

        Prescription p = new Prescription(
                FileManager.generateId("RX"),
                consultId,
                patientId,
                doctor.getId(),
                medication,
                dosage,
                duration,
                instructions,
                today
        );
        prescriptionFile.save(p);

        Validation.showSuccess(this, "Prescription issued successfully.");
        if (onSaved != null) onSaved.run();
        dispose();
    }
}
