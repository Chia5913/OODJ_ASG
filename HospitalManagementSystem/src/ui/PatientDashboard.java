package ui;

import dao.AppointmentFile;
import dao.ConsultationFile;
import dao.DoctorFile;
import dao.FeedbackFile;
import dao.PatientFile;
import dao.PrescriptionFile;
import dao.ShiftRosterFile;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import model.Appointment;
import model.Consultation;
import model.Doctor;
import model.Feedback;
import model.Patient;
import model.Prescription;
import model.ShiftRoster;
import util.FileManager;
import util.Theme;
import util.Validation;

public class PatientDashboard extends JFrame {

    private Patient currentUser;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private String activeCard = "HOME";

    private ArrayList<JButton> navButtons = new ArrayList<>();
    private ArrayList<Doctor> availableDoctors = new ArrayList<>();

    private DoctorFile doctorFile = new DoctorFile();
    private ShiftRosterFile rosterFile = new ShiftRosterFile();
    private AppointmentFile appointmentFile = new AppointmentFile();
    private ConsultationFile consultationFile = new ConsultationFile();
    private PrescriptionFile prescriptionFile = new PrescriptionFile();
    private FeedbackFile feedbackFile = new FeedbackFile();
    private PatientFile patientFile = new PatientFile();

    private DefaultTableModel appointmentModel;
    private JTable appointmentTable;
    private DefaultTableModel historyModel;
    private JTable historyTable;
    private DefaultTableModel prescriptionModel;
    private JTable prescriptionTable;

    public PatientDashboard(Patient user) {

        currentUser = user;

        setTitle("Patient Portal - " + currentUser.getName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setMinimumSize(new Dimension(900, 600));

        Theme.styleFrame(this);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Theme.BG);

        mainPanel.add(buildSidebar(), BorderLayout.WEST);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(Theme.BG);
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        contentPanel.add(buildHomePanel(), "HOME");
        contentPanel.add(buildBookAppointmentPanel(), "BOOK");
        contentPanel.add(buildAppointmentsPanel(), "APPTS");

        contentPanel.add(buildMedicalHistoryPanel(), "HISTORY");

        contentPanel.add(buildPrescriptionsPanel(), "PRESCRIPTIONS");

        contentPanel.add(buildFeedbackPanel(), "FEEDBACK");

        contentPanel.add(buildProfilePanel(), "PROFILE");

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        setContentPane(mainPanel);
        setLocationRelativeTo(null);

        cardLayout.show(contentPanel, "HOME");
        refreshPatientAppointments();
        refreshMedicalHistory();
        refreshPrescriptions();
    }

    private JPanel buildSidebar() {

        JPanel side = new JPanel();
        side.setPreferredSize(new Dimension(248, 0));
        side.setBackground(Theme.SIDEBAR);
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setBorder(new EmptyBorder(28, 18, 24, 18));

        JLabel title = new JLabel("HMS");
        title.setFont(Theme.FONT_HEADING);
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel role = new JLabel("Patient Portal");
        role.setFont(Theme.FONT_SMALL);
        role.setForeground(Theme.SIDEBAR_MUTED);
        role.setAlignmentX(Component.LEFT_ALIGNMENT);

        side.add(title);
        side.add(Box.createVerticalStrut(4));
        side.add(role);
        side.add(Box.createVerticalStrut(25));

        side.add(navButton("Home", "HOME"));
        side.add(Box.createVerticalStrut(6));
        side.add(navButton("Book Appointment", "BOOK"));
        side.add(Box.createVerticalStrut(6));
        side.add(navButton("My Appointments", "APPTS"));
        side.add(Box.createVerticalStrut(6));
        side.add(navButton("Medical History", "HISTORY"));
        side.add(Box.createVerticalStrut(6));
        side.add(navButton("Prescriptions", "PRESCRIPTIONS"));
        side.add(Box.createVerticalStrut(6));
        side.add(navButton("Feedback", "FEEDBACK"));
        side.add(Box.createVerticalStrut(6));
        side.add(navButton("My Profile", "PROFILE"));

        side.add(Box.createVerticalGlue());

        JLabel patientName = new JLabel(currentUser.getName());
        patientName.setFont(Theme.FONT_SMALL);
        patientName.setForeground(Theme.SIDEBAR_TEXT);
        patientName.setAlignmentX(Component.LEFT_ALIGNMENT);

        side.add(patientName);
        side.add(Box.createVerticalStrut(10));

        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(Theme.FONT_BUTTON);
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setBackground(new Color(51, 65, 85));
        logoutButton.setFocusPainted(false);
        logoutButton.setBorderPainted(false);
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        logoutButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));

        logoutButton.addActionListener(e -> {
            if (Validation.confirm(this, "Logout?")) {
                dispose();
                new LoginFrame().setVisible(true);
            }
        });

        side.add(logoutButton);

        return side;
    }

    private JButton navButton(String text, String cardName) {

        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {

                Graphics2D g2 = (Graphics2D) g.create();

                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );

                if (cardName.equals(activeCard)) {
                    g2.setColor(Theme.SIDEBAR_ACTIVE);
                    g2.fillRoundRect(0, 4, getWidth(), getHeight() - 8, 10, 10);
                } else if (getModel().isRollover()) {
                    g2.setColor(Theme.SIDEBAR_ACCENT);
                    g2.fillRoundRect(0, 4, getWidth(), getHeight() - 8, 10, 10);
                }

                g2.dispose();
                super.paintComponent(g);
            }
        };

        button.setFont(
                new Font(
                        Theme.FONT_BODY.getFamily(),
                        Font.BOLD,
                        13
                )
        );

        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorder(new EmptyBorder(0, 14, 0, 12));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);

        button.addActionListener(e -> {
            activeCard = cardName;
            cardLayout.show(contentPanel, cardName);

            if (cardName.equals("PRESCRIPTIONS")) {
                refreshPrescriptions();
            }

            if (cardName.equals("APPTS")) {
                refreshPatientAppointments();
            }

            if (cardName.equals("HISTORY")) {
                refreshMedicalHistory();
            }

            for (JButton b : navButtons) {
                b.repaint();
            }
        });

        navButtons.add(button);

        return button;
    }

    private JPanel buildHomePanel() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JPanel card = Theme.createCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Welcome, " + currentUser.getName());
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.TEXT_PRIMARY);

        JLabel text = new JLabel(
                "<html>"
                        + "Welcome to the Patient Portal.<br><br>"
                        + "You can book appointments, view your appointments, "
                        + "check your medical history and prescriptions, "
                        + "submit feedback and manage your profile."
                        + "</html>"
        );

        text.setFont(Theme.FONT_BODY);
        text.setForeground(Theme.TEXT_SECONDARY);

        card.add(title);
        card.add(Box.createVerticalStrut(10));
        card.add(text);

        panel.add(card, BorderLayout.CENTER);

        return panel;
    }

    private JPanel buildBookAppointmentPanel() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JPanel card = Theme.createCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Book Appointment");
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.TEXT_PRIMARY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel smallText = new JLabel("Choose your doctor, date and time.");
        smallText.setFont(Theme.FONT_BODY);
        smallText.setForeground(Theme.TEXT_SECONDARY);
        smallText.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(title);
        card.add(Box.createVerticalStrut(5));
        card.add(smallText);
        card.add(Box.createVerticalStrut(25));

        JLabel doctorLabel = new JLabel("Doctor");
        doctorLabel.setFont(Theme.FONT_LABEL);
        doctorLabel.setForeground(Theme.TEXT_PRIMARY);
        doctorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JComboBox<String> doctorBox = Theme.createComboBox(
                new String[]{"Select Doctor"}
        );

        doctorBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        doctorBox.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(doctorLabel);
        card.add(Box.createVerticalStrut(6));
        card.add(doctorBox);
        card.add(Box.createVerticalStrut(18));

        JLabel dateLabel = new JLabel("Date");
        dateLabel.setFont(Theme.FONT_LABEL);
        dateLabel.setForeground(Theme.TEXT_PRIMARY);
        dateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JComboBox<String> dateBox = Theme.createComboBox(
                new String[]{"Select Date"}
        );

        dateBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        dateBox.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(dateLabel);
        card.add(Box.createVerticalStrut(6));
        card.add(dateBox);
        card.add(Box.createVerticalStrut(18));

        JLabel timeLabel = new JLabel("Time");
        timeLabel.setFont(Theme.FONT_LABEL);
        timeLabel.setForeground(Theme.TEXT_PRIMARY);
        timeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JComboBox<String> timeBox = Theme.createComboBox(
                new String[]{"Select Time"}
        );

        timeBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        timeBox.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(timeLabel);
        card.add(Box.createVerticalStrut(6));
        card.add(timeBox);
        card.add(Box.createVerticalStrut(18));

        JLabel notesLabel = new JLabel("Reason / Notes");
        notesLabel.setFont(Theme.FONT_LABEL);
        notesLabel.setForeground(Theme.TEXT_PRIMARY);
        notesLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextArea notesArea = Theme.createTextArea(5, 30);

        JScrollPane notesScroll = new JScrollPane(notesArea);
        notesScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        notesScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        notesScroll.setBorder(BorderFactory.createLineBorder(Theme.BORDER));

        card.add(notesLabel);
        card.add(Box.createVerticalStrut(6));
        card.add(notesScroll);
        card.add(Box.createVerticalStrut(25));

        JButton bookButton = Theme.createPrimaryButton("Book Appointment");
        bookButton.setAlignmentX(Component.LEFT_ALIGNMENT);

        loadDoctors(doctorBox);

        doctorBox.addActionListener(e -> {

            dateBox.removeAllItems();
            dateBox.addItem("Select Date");
            dateBox.setEnabled(true);

            timeBox.removeAllItems();
            timeBox.addItem("Select Time");
            timeBox.setEnabled(true);

            int index = doctorBox.getSelectedIndex();

            if (index > 0 && index - 1 < availableDoctors.size()) {
                Doctor doctor = availableDoctors.get(index - 1);
                loadDoctorDates(doctor, dateBox);
            }
        });

        dateBox.addActionListener(e -> {

            timeBox.removeAllItems();
            timeBox.addItem("Select Time");
            timeBox.setEnabled(true);

            int doctorIndex = doctorBox.getSelectedIndex();
            int dateIndex = dateBox.getSelectedIndex();

            if (doctorIndex > 0
                    && doctorIndex - 1 < availableDoctors.size()
                    && dateIndex > 0) {

                Doctor doctor = availableDoctors.get(doctorIndex - 1);
                String date = (String) dateBox.getSelectedItem();

                loadDoctorTimes(doctor, date, timeBox, null);
            }
        });

        bookButton.addActionListener(e -> {

            if (doctorBox.getSelectedIndex() <= 0) {
                Validation.showError(this, "Please select a doctor.");
                return;
            }

            if (dateBox.getSelectedIndex() <= 0) {
                Validation.showError(this, "Please select a date.");
                return;
            }

            if (timeBox.getSelectedIndex() <= 0) {
                Validation.showError(this, "Please select a time.");
                return;
            }

            Doctor doctor = availableDoctors.get(
                    doctorBox.getSelectedIndex() - 1
            );

            String date = (String) dateBox.getSelectedItem();
            String time = (String) timeBox.getSelectedItem();
            String notes = notesArea.getText().trim();

            if (notes.contains("|")) {
                Validation.showError(this, "Please do not use the | symbol in the notes.");
                return;
            }

            if (!isSlotAvailable(doctor.getId(), date, time, null)) {
                Validation.showError(this, "This time slot is already booked. Please choose another time.");
                loadDoctorTimes(doctor, date, timeBox, null);
                return;
            }

            if (patientHasAppointment(date, time, null)) {
                Validation.showError(this, "You already have another appointment at this date and time.");
                return;
            }

            String message = "Doctor: Dr. " + doctor.getName()
                    + "\nDate: " + date
                    + "\nTime: " + time
                    + "\nFee: RM" + String.format("%.2f", doctor.getConsultationFee())
                    + "\n\nConfirm this appointment?";

            if (!Validation.confirm(this, message)) {
                return;
            }

            Appointment appointment = new Appointment(
                    FileManager.generateId("APT"),
                    currentUser.getId(),
                    doctor.getId(),
                    date,
                    time,
                    "PENDING",
                    notes
            );

            appointmentFile.save(appointment);

            Validation.showSuccess(
                    this,
                    "Appointment booked successfully.\nStatus: PENDING"
            );

            notesArea.setText("");
            loadDoctorTimes(doctor, date, timeBox, null);
            refreshPatientAppointments();
        });

        card.add(bookButton);

        JScrollPane scroll = new JScrollPane(card);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Theme.BG);

        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private JPanel buildAppointmentsPanel() {

        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.setOpaque(false);

        JPanel header = createSectionHeader(
                "My Appointments",
                "View, reschedule or cancel your appointments."
        );

        JButton refreshButton = Theme.createSecondaryButton("Refresh");
        refreshButton.addActionListener(e -> refreshPatientAppointments());
        header.add(refreshButton, BorderLayout.EAST);

        String[] columns = {
                "ID",
                "Doctor",
                "Date",
                "Time",
                "Status",
                "Notes"
        };

        appointmentModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        appointmentTable = createStyledTable(appointmentModel);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        actions.setOpaque(false);

        JButton rescheduleButton = Theme.createPrimaryButton("Reschedule");
        JButton cancelButton = Theme.createDangerButton("Cancel Appointment");

        rescheduleButton.addActionListener(e -> {

            Appointment appointment = getSelectedAppointment();

            if (appointment == null) {
                return;
            }

            String status = appointment.getStatus();

            if (status.equalsIgnoreCase("CANCELLED")
                    || status.equalsIgnoreCase("COMPLETED")) {

                Validation.showError(
                        this,
                        "This appointment cannot be rescheduled."
                );
                return;
            }

            showRescheduleDialog(appointment);
        });

        cancelButton.addActionListener(e -> {

            Appointment appointment = getSelectedAppointment();

            if (appointment == null) {
                return;
            }

            if (appointment.getStatus().equalsIgnoreCase("CANCELLED")) {
                Validation.showError(this, "This appointment is already cancelled.");
                return;
            }

            if (appointment.getStatus().equalsIgnoreCase("COMPLETED")) {
                Validation.showError(this, "A completed appointment cannot be cancelled.");
                return;
            }

            if (!Validation.confirm(
                    this,
                    "Cancel appointment " + appointment.getAppointmentId() + "?"
            )) {
                return;
            }

            appointment.setStatus("CANCELLED");
            appointmentFile.save(appointment);

            refreshPatientAppointments();

            Validation.showSuccess(
                    this,
                    "Appointment cancelled successfully."
            );
        });

        actions.add(rescheduleButton);
        actions.add(cancelButton);

        panel.add(header, BorderLayout.NORTH);
        panel.add(styledScroll(appointmentTable), BorderLayout.CENTER);
        panel.add(actions, BorderLayout.SOUTH);

        return panel;
    }

    private Appointment getSelectedAppointment() {

        int row = appointmentTable.getSelectedRow();

        if (row < 0) {
            Validation.showError(this, "Please select an appointment first.");
            return null;
        }

        String appointmentId = (String) appointmentModel.getValueAt(row, 0);
        return appointmentFile.findById(appointmentId);
    }

    private void showRescheduleDialog(Appointment appointment) {

        Doctor doctor = doctorFile.findById(appointment.getDoctorId());

        if (doctor == null) {
            Validation.showError(this, "Doctor record could not be found.");
            return;
        }

        JDialog dialog = new JDialog(this, "Reschedule Appointment", true);
        dialog.setSize(430, 330);
        dialog.setResizable(false);
        Theme.styleDialog(dialog);

        JPanel root = new JPanel();
        root.setBackground(Theme.BG);
        root.setBorder(new EmptyBorder(20, 20, 20, 20));
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));

        JLabel doctorLabel = new JLabel("Doctor: Dr. " + doctor.getName());
        doctorLabel.setFont(Theme.FONT_BODY);
        doctorLabel.setForeground(Theme.TEXT_PRIMARY);
        doctorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel dateLabel = new JLabel("New Date");
        dateLabel.setFont(Theme.FONT_LABEL);
        dateLabel.setForeground(Theme.TEXT_SECONDARY);
        dateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JComboBox<String> dateBox = Theme.createComboBox(
                new String[]{"Select Date"}
        );
        dateBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        dateBox.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel timeLabel = new JLabel("New Time");
        timeLabel.setFont(Theme.FONT_LABEL);
        timeLabel.setForeground(Theme.TEXT_SECONDARY);
        timeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JComboBox<String> timeBox = Theme.createComboBox(
                new String[]{"Select Time"}
        );
        timeBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        timeBox.setAlignmentX(Component.LEFT_ALIGNMENT);

        loadDoctorDates(doctor, dateBox);

        dateBox.addActionListener(e -> {

            timeBox.removeAllItems();
            timeBox.addItem("Select Time");

            if (dateBox.getSelectedIndex() > 0) {
                String date = (String) dateBox.getSelectedItem();
                loadDoctorTimes(
                        doctor,
                        date,
                        timeBox,
                        appointment.getAppointmentId()
                );
            }
        });

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.setOpaque(false);
        buttons.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton closeButton = Theme.createSecondaryButton("Close");
        JButton saveButton = Theme.createPrimaryButton("Save");

        closeButton.addActionListener(e -> dialog.dispose());

        saveButton.addActionListener(e -> {

            if (dateBox.getSelectedIndex() <= 0) {
                Validation.showError(dialog, "Please select a date.");
                return;
            }

            if (timeBox.getSelectedIndex() <= 0) {
                Validation.showError(dialog, "Please select a time.");
                return;
            }

            String newDate = (String) dateBox.getSelectedItem();
            String newTime = (String) timeBox.getSelectedItem();

            if (!isSlotAvailable(
                    doctor.getId(),
                    newDate,
                    newTime,
                    appointment.getAppointmentId()
            )) {
                Validation.showError(dialog, "That time slot is no longer available.");
                return;
            }

            if (patientHasAppointment(
                    newDate,
                    newTime,
                    appointment.getAppointmentId()
            )) {
                Validation.showError(dialog, "You already have another appointment at that time.");
                return;
            }

            if (!Validation.confirm(
                    dialog,
                    "Move this appointment to " + newDate + " at " + newTime + "?"
            )) {
                return;
            }

            appointment.setDate(newDate);
            appointment.setTime(newTime);
            appointmentFile.save(appointment);

            refreshPatientAppointments();
            Validation.showSuccess(dialog, "Appointment rescheduled successfully.");
            dialog.dispose();
        });

        buttons.add(closeButton);
        buttons.add(saveButton);

        root.add(doctorLabel);
        root.add(Box.createVerticalStrut(20));
        root.add(dateLabel);
        root.add(Box.createVerticalStrut(5));
        root.add(dateBox);
        root.add(Box.createVerticalStrut(15));
        root.add(timeLabel);
        root.add(Box.createVerticalStrut(5));
        root.add(timeBox);
        root.add(Box.createVerticalStrut(20));
        root.add(buttons);

        dialog.setContentPane(root);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void loadDoctors(JComboBox<String> doctorBox) {

        List<Doctor> doctors = doctorFile.getAll();

        availableDoctors.clear();

        for (Doctor doctor : doctors) {
            if (doctor.isActive()) {
                availableDoctors.add(doctor);
                doctorBox.addItem(doctor.getDisplayInfo());
            }
        }

        if (availableDoctors.isEmpty()) {
            doctorBox.removeAllItems();
            doctorBox.addItem("No doctors available");
            doctorBox.setEnabled(false);
        }
    }

    private void loadDoctorDates(Doctor doctor, JComboBox<String> dateBox) {

        List<ShiftRoster> rosters = rosterFile.findByDoctorId(doctor.getId());
        ArrayList<String> dates = new ArrayList<>();

        for (ShiftRoster roster : rosters) {
            if (!dates.contains(roster.getShiftDate())) {
                dates.add(roster.getShiftDate());
            }
        }

        Collections.sort(dates);

        for (String date : dates) {
            dateBox.addItem(date);
        }

        if (dates.isEmpty()) {
            dateBox.removeAllItems();
            dateBox.addItem("No available dates");
            dateBox.setEnabled(false);
        } else {
            dateBox.setEnabled(true);
        }
    }

    private void loadDoctorTimes(
            Doctor doctor,
            String date,
            JComboBox<String> timeBox,
            String ignoreAppointmentId) {

        timeBox.removeAllItems();
        timeBox.addItem("Select Time");

        List<ShiftRoster> rosters = rosterFile.findByDoctorId(doctor.getId());
        TreeSet<String> times = new TreeSet<>();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm");

        for (ShiftRoster roster : rosters) {

            if (!roster.getShiftDate().equals(date)) {
                continue;
            }

            try {
                LocalTime start = LocalTime.parse(roster.getStartTime(), format);
                LocalTime end = LocalTime.parse(roster.getEndTime(), format);

                while (start.isBefore(end)) {
                    times.add(start.format(format));
                    start = start.plusMinutes(30);
                }

            } catch (Exception ignored) {
            }
        }

        List<Appointment> doctorAppointments = appointmentFile.getByDoctorId(doctor.getId());

        for (Appointment appointment : doctorAppointments) {

            if (ignoreAppointmentId != null
                    && appointment.getAppointmentId().equals(ignoreAppointmentId)) {
                continue;
            }

            if (appointment.getDate().equals(date)
                    && !appointment.getStatus().equalsIgnoreCase("CANCELLED")) {

                times.remove(appointment.getTime());
            }
        }

        for (String time : times) {
            timeBox.addItem(time);
        }

        if (times.isEmpty()) {
            timeBox.removeAllItems();
            timeBox.addItem("No available times");
            timeBox.setEnabled(false);
        } else {
            timeBox.setEnabled(true);
        }
    }

    private boolean isSlotAvailable(
            String doctorId,
            String date,
            String time,
            String ignoreAppointmentId) {

        List<Appointment> appointments = appointmentFile.getByDoctorId(doctorId);

        for (Appointment appointment : appointments) {

            if (ignoreAppointmentId != null
                    && appointment.getAppointmentId().equals(ignoreAppointmentId)) {
                continue;
            }

            if (appointment.getDate().equals(date)
                    && appointment.getTime().equals(time)
                    && !appointment.getStatus().equalsIgnoreCase("CANCELLED")) {

                return false;
            }
        }

        return true;
    }

    private boolean patientHasAppointment(
            String date,
            String time,
            String ignoreAppointmentId) {

        List<Appointment> appointments = appointmentFile.getByPatientId(currentUser.getId());

        for (Appointment appointment : appointments) {

            if (ignoreAppointmentId != null
                    && appointment.getAppointmentId().equals(ignoreAppointmentId)) {
                continue;
            }

            if (appointment.getDate().equals(date)
                    && appointment.getTime().equals(time)
                    && !appointment.getStatus().equalsIgnoreCase("CANCELLED")) {

                return true;
            }
        }

        return false;
    }

    private void refreshPatientAppointments() {

        if (appointmentModel == null) {
            return;
        }

        appointmentModel.setRowCount(0);

        List<Appointment> appointments =
                appointmentFile.getByPatientId(currentUser.getId());

        for (Appointment appointment : appointments) {

            Doctor doctor = doctorFile.findById(appointment.getDoctorId());
            String doctorName = appointment.getDoctorId();

            if (doctor != null) {
                doctorName = "Dr. " + doctor.getName();
            }

            appointmentModel.addRow(
                    new Object[]{
                            appointment.getAppointmentId(),
                            doctorName,
                            appointment.getDate(),
                            appointment.getTime(),
                            appointment.getStatus(),
                            appointment.getNotes()
                    }
            );
        }
    }

    private JPanel buildMedicalHistoryPanel() {

        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.setOpaque(false);

        JPanel header = createSectionHeader(
                "Medical History",
                "View your past consultation records."
        );

        JButton refreshButton = Theme.createSecondaryButton("Refresh");
        refreshButton.addActionListener(e -> refreshMedicalHistory());
        header.add(refreshButton, BorderLayout.EAST);

        String[] columns = {
                "Consultation ID",
                "Doctor",
                "Date / Time",
                "Vital Signs",
                "Diagnosis"
        };

        historyModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        historyTable = createStyledTable(historyModel);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        buttons.setOpaque(false);

        JButton viewButton = Theme.createPrimaryButton("View Details");

        viewButton.addActionListener(e -> showMedicalHistoryDetails());

        buttons.add(viewButton);

        panel.add(header, BorderLayout.NORTH);
        panel.add(styledScroll(historyTable), BorderLayout.CENTER);
        panel.add(buttons, BorderLayout.SOUTH);

        return panel;
    }

    private void refreshMedicalHistory() {

        if (historyModel == null) {
            return;
        }

        historyModel.setRowCount(0);

        List<Consultation> consultations =
                consultationFile.getByPatientId(currentUser.getId());

        for (Consultation consultation : consultations) {

            Doctor doctor = doctorFile.findById(consultation.getDoctorId());
            String doctorName = consultation.getDoctorId();

            if (doctor != null) {
                doctorName = "Dr. " + doctor.getName();
            }

            historyModel.addRow(new Object[]{
                    consultation.getConsultationId(),
                    doctorName,
                    consultation.getDateTime(),
                    consultation.getVitalSigns(),
                    consultation.getDiagnosis()
            });
        }
    }

    private void showMedicalHistoryDetails() {

        int row = historyTable.getSelectedRow();

        if (row < 0) {
            Validation.showError(this, "Please select a medical record first.");
            return;
        }

        String consultationId = (String) historyModel.getValueAt(row, 0);
        Consultation consultation = consultationFile.findById(consultationId);

        if (consultation == null) {
            Validation.showError(this, "Medical record could not be found.");
            return;
        }

        Doctor doctor = doctorFile.findById(consultation.getDoctorId());
        String doctorName = consultation.getDoctorId();

        if (doctor != null) {
            doctorName = "Dr. " + doctor.getName();
        }

        JTextArea details = new JTextArea();
        details.setEditable(false);
        details.setLineWrap(true);
        details.setWrapStyleWord(true);
        details.setFont(Theme.FONT_BODY);
        details.setBackground(Theme.CARD_BG);

        details.setText(
                "Consultation ID: " + consultation.getConsultationId()
                        + "\nAppointment ID: " + consultation.getAppointmentId()
                        + "\nDoctor: " + doctorName
                        + "\nDate / Time: " + consultation.getDateTime()
                        + "\nVital Signs: " + consultation.getVitalSigns()
                        + "\nDiagnosis: " + consultation.getDiagnosis()
                        + "\n\nClinical Notes:\n" + consultation.getClinicalNotes()
        );

        JScrollPane scroll = new JScrollPane(details);
        scroll.setPreferredSize(new Dimension(500, 280));

        JOptionPane.showMessageDialog(
                this,
                scroll,
                "Medical Record",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private JPanel buildPrescriptionsPanel() {

        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.setOpaque(false);

        JPanel header = createSectionHeader(
                "Prescriptions",
                "View the medicines prescribed by your doctor."
        );

        JButton refreshButton = Theme.createSecondaryButton("Refresh");
        refreshButton.addActionListener(e -> refreshPrescriptions());
        header.add(refreshButton, BorderLayout.EAST);

        String[] columns = {
                "Prescription ID",
                "Doctor",
                "Medication",
                "Dosage",
                "Duration",
                "Date Issued"
        };

        prescriptionModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        prescriptionTable = createStyledTable(prescriptionModel);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        buttons.setOpaque(false);

        JButton viewButton = Theme.createPrimaryButton("View Details");
        viewButton.addActionListener(e -> showPrescriptionDetails());

        buttons.add(viewButton);

        panel.add(header, BorderLayout.NORTH);
        panel.add(styledScroll(prescriptionTable), BorderLayout.CENTER);
        panel.add(buttons, BorderLayout.SOUTH);

        return panel;
    }

    private void refreshPrescriptions() {

        if (prescriptionModel == null) {
            return;
        }

        prescriptionModel.setRowCount(0);

        List<Prescription> prescriptions =
                prescriptionFile.getByPatientId(currentUser.getId());

        for (Prescription prescription : prescriptions) {

            Doctor doctor = doctorFile.findById(prescription.getDoctorId());
            String doctorName = prescription.getDoctorId();

            if (doctor != null) {
                doctorName = "Dr. " + doctor.getName();
            }

            prescriptionModel.addRow(new Object[]{
                    prescription.getPrescriptionId(),
                    doctorName,
                    prescription.getMedication(),
                    prescription.getDosage(),
                    prescription.getDuration(),
                    prescription.getDateIssued()
            });
        }
    }

    private void showPrescriptionDetails() {

        int row = prescriptionTable.getSelectedRow();

        if (row < 0) {
            Validation.showError(this, "Please select a prescription first.");
            return;
        }

        String prescriptionId =
                (String) prescriptionModel.getValueAt(row, 0);

        Prescription selected = null;

        List<Prescription> prescriptions =
                prescriptionFile.getByPatientId(currentUser.getId());

        for (Prescription prescription : prescriptions) {
            if (prescription.getPrescriptionId().equals(prescriptionId)) {
                selected = prescription;
                break;
            }
        }

        if (selected == null) {
            Validation.showError(this, "Prescription could not be found.");
            return;
        }

        Doctor doctor = doctorFile.findById(selected.getDoctorId());
        String doctorName = selected.getDoctorId();

        if (doctor != null) {
            doctorName = "Dr. " + doctor.getName();
        }

        JTextArea details = new JTextArea();
        details.setEditable(false);
        details.setLineWrap(true);
        details.setWrapStyleWord(true);
        details.setFont(Theme.FONT_BODY);
        details.setBackground(Theme.CARD_BG);

        details.setText(
                "Prescription ID: " + selected.getPrescriptionId()
                        + "\nConsultation ID: " + selected.getConsultationId()
                        + "\nDoctor: " + doctorName
                        + "\nDate Issued: " + selected.getDateIssued()
                        + "\n\nMedication: " + selected.getMedication()
                        + "\nDosage: " + selected.getDosage()
                        + "\nDuration: " + selected.getDuration()
                        + "\n\nInstructions:\n" + selected.getInstructions()
        );

        JScrollPane scroll = new JScrollPane(details);
        scroll.setPreferredSize(new Dimension(500, 280));

        JOptionPane.showMessageDialog(
                this,
                scroll,
                "Prescription Details",
                JOptionPane.INFORMATION_MESSAGE
        );
    }


    private JPanel buildFeedbackPanel() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JPanel card = Theme.createCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Feedback");
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.TEXT_PRIMARY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel text = new JLabel("Rate a completed appointment and leave a comment.");
        text.setFont(Theme.FONT_BODY);
        text.setForeground(Theme.TEXT_SECONDARY);
        text.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(title);
        card.add(Box.createVerticalStrut(5));
        card.add(text);
        card.add(Box.createVerticalStrut(25));

        JLabel appointmentLabel = new JLabel("Completed Appointment");
        appointmentLabel.setFont(Theme.FONT_LABEL);
        appointmentLabel.setForeground(Theme.TEXT_PRIMARY);
        appointmentLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JComboBox<String> appointmentBox = Theme.createComboBox(
                new String[]{"Select Appointment"}
        );
        appointmentBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        appointmentBox.setAlignmentX(Component.LEFT_ALIGNMENT);

        ArrayList<Appointment> completedAppointments = new ArrayList<>();
        List<Appointment> appointments = appointmentFile.getByPatientId(currentUser.getId());

        for (Appointment appointment : appointments) {
            if (appointment.getStatus().equalsIgnoreCase("COMPLETED")) {

                Doctor doctor = doctorFile.findById(appointment.getDoctorId());
                String doctorName = appointment.getDoctorId();

                if (doctor != null) {
                    doctorName = "Dr. " + doctor.getName();
                }

                completedAppointments.add(appointment);
                appointmentBox.addItem(
                        appointment.getAppointmentId()
                                + " - " + doctorName
                                + " - " + appointment.getDate()
                );
            }
        }

        if (completedAppointments.isEmpty()) {
            appointmentBox.removeAllItems();
            appointmentBox.addItem("No completed appointments");
            appointmentBox.setEnabled(false);
        }

        card.add(appointmentLabel);
        card.add(Box.createVerticalStrut(6));
        card.add(appointmentBox);
        card.add(Box.createVerticalStrut(18));

        JLabel ratingLabel = new JLabel("Rating");
        ratingLabel.setFont(Theme.FONT_LABEL);
        ratingLabel.setForeground(Theme.TEXT_PRIMARY);
        ratingLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JComboBox<String> ratingBox = Theme.createComboBox(
                new String[]{"1", "2", "3", "4", "5"}
        );
        ratingBox.setSelectedIndex(4);
        ratingBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        ratingBox.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(ratingLabel);
        card.add(Box.createVerticalStrut(6));
        card.add(ratingBox);
        card.add(Box.createVerticalStrut(18));

        JLabel commentLabel = new JLabel("Comment");
        commentLabel.setFont(Theme.FONT_LABEL);
        commentLabel.setForeground(Theme.TEXT_PRIMARY);
        commentLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextArea commentArea = Theme.createTextArea(5, 30);

        JScrollPane commentScroll = new JScrollPane(commentArea);
        commentScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        commentScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        commentScroll.setBorder(BorderFactory.createLineBorder(Theme.BORDER));

        card.add(commentLabel);
        card.add(Box.createVerticalStrut(6));
        card.add(commentScroll);
        card.add(Box.createVerticalStrut(25));

        JButton submitButton = Theme.createPrimaryButton("Submit Feedback");
        submitButton.setAlignmentX(Component.LEFT_ALIGNMENT);

        submitButton.addActionListener(e -> {

            if (completedAppointments.isEmpty()) {
                Validation.showError(this, "You do not have a completed appointment yet.");
                return;
            }

            int selected = appointmentBox.getSelectedIndex();

            if (selected <= 0) {
                Validation.showError(this, "Please select a completed appointment.");
                return;
            }

            String comment = commentArea.getText().trim();

            if (comment.isEmpty()) {
                Validation.showError(this, "Please enter a comment.");
                return;
            }

            if (comment.contains("|")) {
                Validation.showError(this, "Please do not use the | symbol in the comment.");
                return;
            }

            Appointment appointment = completedAppointments.get(selected - 1);

            for (Feedback oldFeedback : feedbackFile.getAll()) {
                if (oldFeedback.getPatientId().equals(currentUser.getId())
                        && oldFeedback.getAppointmentId().equals(appointment.getAppointmentId())) {

                    Validation.showError(
                            this,
                            "You already submitted feedback for this appointment."
                    );
                    return;
                }
            }

            int rating = Integer.parseInt((String) ratingBox.getSelectedItem());

            Feedback feedback = new Feedback(
                    FileManager.generateId("FDB"),
                    currentUser.getId(),
                    appointment.getDoctorId(),
                    appointment.getAppointmentId(),
                    rating,
                    comment,
                    LocalDate.now().toString()
            );

            feedbackFile.save(feedback);

            Validation.showSuccess(this, "Feedback submitted successfully.");

            commentArea.setText("");
            ratingBox.setSelectedIndex(4);
            appointmentBox.setSelectedIndex(0);
        });

        card.add(submitButton);

        JScrollPane scroll = new JScrollPane(card);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Theme.BG);

        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }


    private JPanel buildProfilePanel() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JPanel card = Theme.createCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("My Profile");
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.TEXT_PRIMARY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel text = new JLabel("View and update your personal information.");
        text.setFont(Theme.FONT_BODY);
        text.setForeground(Theme.TEXT_SECONDARY);
        text.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(title);
        card.add(Box.createVerticalStrut(5));
        card.add(text);
        card.add(Box.createVerticalStrut(20));

        JLabel idLabel = new JLabel("Patient ID: " + currentUser.getId());
        idLabel.setFont(Theme.FONT_BODY);
        idLabel.setForeground(Theme.TEXT_SECONDARY);
        idLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(idLabel);
        card.add(Box.createVerticalStrut(20));

        JTextField nameField = addProfileField(card, "Full Name", currentUser.getName());
        JTextField emailField = addProfileField(card, "Email", currentUser.getEmail());
        JTextField phoneField = addProfileField(card, "Phone", currentUser.getPhone());
        JTextField bloodTypeField = addProfileField(card, "Blood Type", currentUser.getBloodType());
        JTextField allergiesField = addProfileField(card, "Allergies", currentUser.getAllergies());
        JTextField insuranceField = addProfileField(card, "Insurance Provider", currentUser.getInsuranceProvider());
        JTextField emergencyField = addProfileField(card, "Emergency Contact", currentUser.getEmergencyContact());

        JLabel passwordLabel = new JLabel("New Password (leave blank to keep current password)");
        passwordLabel.setFont(Theme.FONT_LABEL);
        passwordLabel.setForeground(Theme.TEXT_PRIMARY);
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPasswordField passwordField = Theme.createPasswordField(20);
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(passwordLabel);
        card.add(Box.createVerticalStrut(6));
        card.add(passwordField);
        card.add(Box.createVerticalStrut(25));

        JButton saveButton = Theme.createPrimaryButton("Save Changes");
        saveButton.setAlignmentX(Component.LEFT_ALIGNMENT);

        saveButton.addActionListener(e -> {

            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();
            String bloodType = bloodTypeField.getText().trim();
            String allergies = allergiesField.getText().trim();
            String insurance = insuranceField.getText().trim();
            String emergency = emergencyField.getText().trim();
            String newPassword = new String(passwordField.getPassword()).trim();

            if (Validation.isEmpty(name)
                    || Validation.isEmpty(email)
                    || Validation.isEmpty(phone)) {
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

            if (!Validation.isEmpty(emergency) && !Validation.isValidPhone(emergency)) {
                Validation.showError(this, "Please enter a valid emergency contact number.");
                return;
            }

            String[] values = {
                    name, email, phone, bloodType, allergies, insurance, emergency, newPassword
            };

            for (String value : values) {
                if (value.contains("|")) {
                    Validation.showError(this, "Please do not use the | symbol in profile details.");
                    return;
                }
            }

            for (Patient patient : patientFile.getAll()) {
                if (!patient.getId().equals(currentUser.getId())
                        && patient.getEmail().equalsIgnoreCase(email)) {
                    Validation.showError(this, "That email is already used by another patient.");
                    return;
                }
            }

            currentUser.setName(name);
            currentUser.setEmail(email);
            currentUser.setPhone(phone);
            currentUser.setBloodType(bloodType);
            currentUser.setAllergies(allergies);
            currentUser.setInsuranceProvider(insurance);
            currentUser.setEmergencyContact(emergency);

            if (!Validation.isEmpty(newPassword)) {
                currentUser.setPassword(newPassword);
            }

            patientFile.save(currentUser);
            passwordField.setText("");
            setTitle("Patient Portal - " + currentUser.getName());

            Validation.showSuccess(this, "Profile updated successfully.");
        });

        card.add(saveButton);

        JScrollPane scroll = new JScrollPane(card);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Theme.BG);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private JTextField addProfileField(JPanel card, String labelText, String value) {

        JLabel label = new JLabel(labelText);
        label.setFont(Theme.FONT_LABEL);
        label.setForeground(Theme.TEXT_PRIMARY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField field = Theme.createTextField(20);
        field.setText(value != null ? value : "");
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(label);
        card.add(Box.createVerticalStrut(6));
        card.add(field);
        card.add(Box.createVerticalStrut(16));

        return field;
    }

    private JPanel createSectionHeader(String title, String subtitle) {

        JPanel header = new JPanel(new BorderLayout(14, 0));
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(0, 0, 4, 0));

        JPanel accent = new JPanel();
        accent.setBackground(Theme.PRIMARY);
        accent.setPreferredSize(new Dimension(4, 0));

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));

        JLabel heading = new JLabel(title);
        heading.setFont(Theme.FONT_TITLE);
        heading.setForeground(Theme.TEXT_PRIMARY);

        JLabel info = new JLabel(subtitle);
        info.setFont(Theme.FONT_BODY);
        info.setForeground(Theme.TEXT_SECONDARY);

        text.add(heading);
        text.add(Box.createVerticalStrut(4));
        text.add(info);

        JPanel left = new JPanel(new BorderLayout(14, 0));
        left.setOpaque(false);
        left.add(accent, BorderLayout.WEST);
        left.add(text, BorderLayout.CENTER);

        header.add(left, BorderLayout.WEST);

        return header;
    }

    private JTable createStyledTable(DefaultTableModel model) {

        JTable table = new JTable(model);
        table.setFont(Theme.FONT_BODY);
        table.setRowHeight(40);
        table.setSelectionBackground(Theme.PRIMARY_LIGHT);
        table.setSelectionForeground(Theme.TEXT_PRIMARY);
        table.setGridColor(new Color(241, 245, 249));
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setBackground(Theme.CARD_BG);
        table.setFillsViewportHeight(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        table.getTableHeader().setFont(Theme.FONT_LABEL);
        table.getTableHeader().setBackground(new Color(248, 250, 252));
        table.getTableHeader().setForeground(Theme.TEXT_SECONDARY);
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));
        table.getTableHeader().setReorderingAllowed(false);

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table,
                    Object value,
                    boolean selected,
                    boolean focused,
                    int row,
                    int column) {

                Component component = super.getTableCellRendererComponent(
                        table,
                        value,
                        selected,
                        focused,
                        row,
                        column
                );

                setBorder(new EmptyBorder(0, 12, 0, 12));

                if (!selected) {
                    component.setBackground(
                            row % 2 == 0
                                    ? Theme.CARD_BG
                                    : new Color(248, 250, 252)
                    );
                    component.setForeground(Theme.TEXT_PRIMARY);
                }

                return component;
            }
        };

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        return table;
    }

    private JScrollPane styledScroll(JTable table) {

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(Theme.BORDER, 1, true));
        scroll.getViewport().setBackground(Theme.CARD_BG);

        return scroll;
    }

    private JPanel buildSimplePanel(String pageTitle, String message) {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JPanel card = Theme.createCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel title = new JLabel(pageTitle);
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.TEXT_PRIMARY);

        JLabel text = new JLabel("<html>" + message + "</html>");
        text.setFont(Theme.FONT_BODY);
        text.setForeground(Theme.TEXT_SECONDARY);

        card.add(title);
        card.add(Box.createVerticalStrut(10));
        card.add(text);

        panel.add(card, BorderLayout.CENTER);

        return panel;
    }
}
