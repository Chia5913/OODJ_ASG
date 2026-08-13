package ui;

import dao.LabRequestFile;
import model.Consultation;
import model.Doctor;
import model.LabRequest;
import util.FileManager;
import util.Theme;
import util.Validation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Pop-up dialog for a Doctor to request lab tests / X-rays / imaging.
 * Scrollable so all options remain visible.
 */
public class LabRequestDialog extends JDialog {

    private final Consultation consultation;
    private final Doctor doctor;
    private final Runnable onSaved;
    private final LabRequestFile labRequestFile = new LabRequestFile();

    private JTextField patientIdField;
    private JComboBox<String> testTypeCombo, urgencyCombo;
    private JTextArea clinicalInfoArea;

    public LabRequestDialog(Frame owner, Consultation consultation, Doctor doctor, Runnable onSaved) {
        super(owner, "Lab / Imaging Request", true);
        this.consultation = consultation;
        this.doctor = doctor;
        this.onSaved = onSaved;

        setSize(480, 500);
        setMinimumSize(new Dimension(400, 380));
        setResizable(true);
        Theme.styleDialog(this);

        JPanel root = new JPanel(new BorderLayout(0, 12));
        root.setBackground(Theme.BG);
        root.setBorder(new EmptyBorder(20, 20, 16, 20));

        JLabel title = new JLabel("Request Lab Test / Imaging");
        title.setFont(Theme.FONT_HEADING);
        title.setForeground(Theme.PRIMARY);

        JPanel form = new JPanel();
        form.setOpaque(false);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));

        JLabel pLbl = new JLabel("Patient ID");
        pLbl.setFont(Theme.FONT_SMALL);
        pLbl.setForeground(Theme.TEXT_SECONDARY);
        pLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(pLbl);
        form.add(Box.createVerticalStrut(4));
        patientIdField = Theme.createTextField(25);
        if (consultation != null) {
            patientIdField.setText(consultation.getPatientId());
            patientIdField.setEditable(false);
        }
        patientIdField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        patientIdField.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(patientIdField);
        form.add(Box.createVerticalStrut(12));

        JLabel tLbl = new JLabel("Test / Imaging Type");
        tLbl.setFont(Theme.FONT_SMALL);
        tLbl.setForeground(Theme.TEXT_SECONDARY);
        tLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(tLbl);
        form.add(Box.createVerticalStrut(4));
        testTypeCombo = Theme.createComboBox(new String[]{
                "Blood Test (Full Blood Count)",
                "Blood Test (Lipid Profile)",
                "Urine Analysis",
                "X-Ray (Chest)",
                "X-Ray (Limb)",
                "Ultrasound",
                "CT Scan",
                "MRI",
                "ECG",
                "Other"
        });
        testTypeCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        testTypeCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(testTypeCombo);
        form.add(Box.createVerticalStrut(12));

        JLabel uLbl = new JLabel("Urgency");
        uLbl.setFont(Theme.FONT_SMALL);
        uLbl.setForeground(Theme.TEXT_SECONDARY);
        uLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(uLbl);
        form.add(Box.createVerticalStrut(4));
        urgencyCombo = Theme.createComboBox(new String[]{"Routine", "Urgent", "STAT"});
        urgencyCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        urgencyCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(urgencyCombo);
        form.add(Box.createVerticalStrut(12));

        JLabel cLbl = new JLabel("Clinical Information / Reason");
        cLbl.setFont(Theme.FONT_SMALL);
        cLbl.setForeground(Theme.TEXT_SECONDARY);
        cLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(cLbl);
        form.add(Box.createVerticalStrut(4));
        clinicalInfoArea = Theme.createTextArea(4, 25);
        clinicalInfoArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(clinicalInfoArea);
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

        JButton save = Theme.createPrimaryButton("Submit Request");
        save.addActionListener(e -> saveRequest());

        buttons.add(cancel);
        buttons.add(save);

        root.add(title, BorderLayout.NORTH);
        root.add(scroll, BorderLayout.CENTER);
        root.add(buttons, BorderLayout.SOUTH);
        setContentPane(root);
        setLocationRelativeTo(owner);
    }

    private void saveRequest() {
        String patientId = patientIdField.getText().trim();
        String testType = (String) testTypeCombo.getSelectedItem();
        String urgency = (String) urgencyCombo.getSelectedItem();
        String clinicalInfo = clinicalInfoArea.getText().trim();

        if (Validation.isEmpty(patientId)) {
            Validation.showError(this, "Patient ID is required.");
            return;
        }

        String today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
        String consultId = consultation != null ? consultation.getConsultationId() : "";

        LabRequest lr = new LabRequest(
                FileManager.generateId("LAB"),
                consultId,
                patientId,
                doctor.getId(),
                testType,
                urgency,
                clinicalInfo,
                "PENDING",
                today
        );
        labRequestFile.save(lr);

        Validation.showSuccess(this, "Lab / Imaging request submitted.\nAdmin will process it shortly.");
        if (onSaved != null) onSaved.run();
        dispose();
    }
}
