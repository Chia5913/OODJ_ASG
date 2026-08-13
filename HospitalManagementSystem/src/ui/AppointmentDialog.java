package ui;

import dao.AppointmentFile;
import model.Appointment;
import model.Doctor;
import util.Theme;
import util.Validation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Pop-up to view / update appointment status.
 * Scrollable content area.
 */
public class AppointmentDialog extends JDialog {

    private final Appointment appointment;
    private final Runnable onSaved;
    private final AppointmentFile appointmentFile = new AppointmentFile();

    private JComboBox<String> statusCombo;
    private JTextArea notesArea;

    public AppointmentDialog(Frame owner, Appointment appointment, Doctor doctor, Runnable onSaved) {
        super(owner, "Appointment Details", true);
        this.appointment = appointment;
        this.onSaved = onSaved;

        setSize(440, 460);
        setMinimumSize(new Dimension(380, 360));
        setResizable(true);
        Theme.styleDialog(this);

        JPanel root = new JPanel(new BorderLayout(0, 12));
        root.setBackground(Theme.BG);
        root.setBorder(new EmptyBorder(20, 20, 16, 20));

        JLabel title = new JLabel("Appointment - " + appointment.getAppointmentId());
        title.setFont(Theme.FONT_HEADING);
        title.setForeground(Theme.PRIMARY);

        JPanel body = new JPanel();
        body.setOpaque(false);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));

        JPanel info = Theme.createCard();
        info.setLayout(new GridLayout(0, 2, 8, 8));
        info.setAlignmentX(Component.LEFT_ALIGNMENT);
        info.add(label("Patient ID:"));
        info.add(value(appointment.getPatientId()));
        info.add(label("Date:"));
        info.add(value(appointment.getDate()));
        info.add(label("Time:"));
        info.add(value(appointment.getTime()));
        info.add(label("Doctor:"));
        info.add(value(doctor.getName()));
        body.add(info);
        body.add(Box.createVerticalStrut(14));

        JLabel statusLbl = new JLabel("Status");
        statusLbl.setFont(Theme.FONT_SMALL);
        statusLbl.setForeground(Theme.TEXT_SECONDARY);
        statusLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        body.add(statusLbl);
        body.add(Box.createVerticalStrut(4));

        statusCombo = Theme.createComboBox(new String[]{"PENDING", "CONFIRMED", "COMPLETED", "CANCELLED"});
        statusCombo.setSelectedItem(appointment.getStatus());
        statusCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        statusCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        body.add(statusCombo);
        body.add(Box.createVerticalStrut(12));

        JLabel notesLbl = new JLabel("Notes");
        notesLbl.setFont(Theme.FONT_SMALL);
        notesLbl.setForeground(Theme.TEXT_SECONDARY);
        notesLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        body.add(notesLbl);
        body.add(Box.createVerticalStrut(4));

        notesArea = Theme.createTextArea(4, 20);
        notesArea.setText(appointment.getNotes() != null ? appointment.getNotes() : "");
        notesArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        body.add(notesArea);
        body.add(Box.createVerticalStrut(8));

        JScrollPane scroll = new JScrollPane(body);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Theme.BG);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttons.setOpaque(false);

        JButton cancel = Theme.createSecondaryButton("Close");
        cancel.addActionListener(e -> dispose());

        JButton save = Theme.createPrimaryButton("Update");
        save.addActionListener(e -> {
            appointment.setStatus((String) statusCombo.getSelectedItem());
            appointment.setNotes(notesArea.getText().trim());
            appointmentFile.save(appointment);
            Validation.showSuccess(this, "Appointment updated.");
            if (onSaved != null) onSaved.run();
            dispose();
        });

        buttons.add(cancel);
        buttons.add(save);

        root.add(title, BorderLayout.NORTH);
        root.add(scroll, BorderLayout.CENTER);
        root.add(buttons, BorderLayout.SOUTH);
        setContentPane(root);
        setLocationRelativeTo(owner);
    }

    private JLabel label(String t) {
        JLabel l = new JLabel(t);
        l.setFont(Theme.FONT_SMALL);
        l.setForeground(Theme.TEXT_SECONDARY);
        return l;
    }

    private JLabel value(String t) {
        JLabel l = new JLabel(t != null ? t : "-");
        l.setFont(Theme.FONT_BODY);
        l.setForeground(Theme.TEXT_PRIMARY);
        return l;
    }
}
