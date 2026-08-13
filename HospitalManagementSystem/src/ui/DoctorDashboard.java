package ui;

import dao.*;
import model.*;
import util.Theme;
import util.Validation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Main working area for a logged-in Doctor.
 * Modern sidebar + content layout with pop-up dialogs for all actions.
 */
public class DoctorDashboard extends JFrame {

    private final Doctor currentDoctor;
    private final AppointmentFile appointmentFile = new AppointmentFile();
    private final ConsultationFile consultationFile = new ConsultationFile();
    private final PrescriptionFile prescriptionFile = new PrescriptionFile();
    private final LabRequestFile labRequestFile = new LabRequestFile();
    private final DoctorFile doctorFile = new DoctorFile();

    private JPanel contentPanel;
    private CardLayout cardLayout;
    private String activeCard = "APPOINTMENTS";

    // Tables
    private DefaultTableModel appointmentModel;
    private DefaultTableModel consultationModel;
    private DefaultTableModel prescriptionModel;
    private DefaultTableModel labModel;

    public DoctorDashboard(Doctor doctor) {
        this.currentDoctor = doctor;

        setTitle("Doctor Dashboard - " + doctor.getName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setMinimumSize(new Dimension(900, 600));
        Theme.styleFrame(this);

        // ===== Root =====
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Theme.BG);

        // Sidebar
        root.add(buildSidebar(), BorderLayout.WEST);

        // Content area with CardLayout
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(Theme.BG);
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        contentPanel.add(buildAppointmentsPanel(), "APPOINTMENTS");
        contentPanel.add(buildConsultationsPanel(), "CONSULTATIONS");
        contentPanel.add(buildPrescriptionsPanel(), "PRESCRIPTIONS");
        contentPanel.add(buildLabRequestsPanel(), "LAB");
        contentPanel.add(buildProfileSummaryPanel(), "PROFILE");

        root.add(contentPanel, BorderLayout.CENTER);
        setContentPane(root);
        setLocationRelativeTo(null);

        // Default view
        cardLayout.show(contentPanel, "APPOINTMENTS");
        refreshAllTables();
    }

    // -------------------------------------------------------------------------
    // Sidebar
    // -------------------------------------------------------------------------
    private final java.util.List<JButton> navButtons = new java.util.ArrayList<>();

    private JPanel buildSidebar() {
        JPanel side = new JPanel();
        side.setPreferredSize(new Dimension(248, 0));
        side.setBackground(Theme.SIDEBAR);
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setBorder(new EmptyBorder(28, 18, 24, 18));

        // Brand row: cross badge + name
        JPanel brandRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        brandRow.setOpaque(false);
        brandRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        brandRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));

        JLabel mark = new JLabel("\u271A", SwingConstants.CENTER);
        mark.setOpaque(false);
        mark.setFont(new Font(Theme.FONT_HEADING.getFamily(), Font.BOLD, 24));
        mark.setForeground(Theme.PRIMARY_LIGHT);

        JPanel brandText = new JPanel();
        brandText.setOpaque(false);
        brandText.setLayout(new BoxLayout(brandText, BoxLayout.Y_AXIS));
        JLabel brand = new JLabel("APU Medical");
        brand.setFont(new Font(Theme.FONT_HEADING.getFamily(), Font.BOLD, 17));
        brand.setForeground(Color.WHITE);
        JLabel roleLbl = new JLabel("Doctor Portal");
        roleLbl.setFont(Theme.FONT_SMALL);
        roleLbl.setForeground(Theme.SIDEBAR_MUTED);
        brandText.add(brand);
        brandText.add(roleLbl);

        brandRow.add(mark);
        brandRow.add(brandText);

        side.add(brandRow);
        side.add(Box.createVerticalStrut(34));

        navButtons.clear();
        side.add(navButton("Appointments", "APPOINTMENTS"));
        side.add(Box.createVerticalStrut(6));
        side.add(navButton("Consultations", "CONSULTATIONS"));
        side.add(Box.createVerticalStrut(6));
        side.add(navButton("Prescriptions", "PRESCRIPTIONS"));
        side.add(Box.createVerticalStrut(6));
        side.add(navButton("Lab Requests", "LAB"));
        side.add(Box.createVerticalStrut(6));
        side.add(navButton("My Profile", "PROFILE"));

        side.add(Box.createVerticalGlue());

        // Doctor identity chip
        JPanel doctorChip = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        doctorChip.setBackground(Theme.SIDEBAR_ACCENT);
        doctorChip.setAlignmentX(Component.LEFT_ALIGNMENT);
        doctorChip.setMaximumSize(new Dimension(Integer.MAX_VALUE, 54));
        JLabel avatar = new JLabel(initials(currentDoctor.getName()), SwingConstants.CENTER);
        avatar.setPreferredSize(new Dimension(34, 34));
        avatar.setOpaque(true);
        avatar.setBackground(Theme.PRIMARY);
        avatar.setForeground(Color.WHITE);
        avatar.setFont(Theme.FONT_LABEL);
        JLabel docName = new JLabel(currentDoctor.getName());
        docName.setFont(Theme.FONT_SMALL);
        docName.setForeground(Theme.SIDEBAR_TEXT);
        doctorChip.add(avatar);
        doctorChip.add(docName);
        side.add(doctorChip);
        side.add(Box.createVerticalStrut(12));

        JButton logoutBtn = new JButton("Log out") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? new Color(30, 70, 74) : Theme.SIDEBAR_ACCENT);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        logoutBtn.setFont(Theme.FONT_BUTTON);
        logoutBtn.setForeground(Theme.SIDEBAR_TEXT);
        logoutBtn.setContentAreaFilled(false);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setOpaque(false);
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        logoutBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        logoutBtn.addActionListener(e -> {
            if (Validation.confirm(this, "Are you sure you want to logout?")) {
                dispose();
                SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
            }
        });
        side.add(logoutBtn);

        return side;
    }

    private String initials(String name) {
        if (name == null || name.isBlank()) return "DR";
        String[] parts = name.trim().split("\\s+");
        String s = parts[0].substring(0, 1);
        if (parts.length > 1) s += parts[parts.length - 1].substring(0, 1);
        return s.toUpperCase();
    }

    private JButton navButton(String text, String cardName) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                boolean active = cardName.equals(activeCard);
                if (active) {
                    g2.setColor(Theme.SIDEBAR_ACTIVE);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                } else if (getModel().isRollover()) {
                    g2.setColor(Theme.SIDEBAR_ACCENT);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font(Theme.FONT_BODY.getFamily(), Font.BOLD, 13));
        btn.setForeground(cardName.equals(activeCard) ? Color.WHITE : Theme.SIDEBAR_TEXT);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(new EmptyBorder(0, 14, 0, 12));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.putClientProperty("card", cardName);
        btn.addActionListener(e -> {
            activeCard = cardName;
            cardLayout.show(contentPanel, cardName);
            refreshAllTables();
            updateNavStyles();
        });
        navButtons.add(btn);
        return btn;
    }

    private void updateNavStyles() {
        for (JButton b : navButtons) {
            boolean active = activeCard.equals(b.getClientProperty("card"));
            b.setForeground(active ? Color.WHITE : Theme.SIDEBAR_TEXT);
            b.repaint();
        }
    }

    // -------------------------------------------------------------------------
    // Appointments Panel
    // -------------------------------------------------------------------------
    private JPanel buildAppointmentsPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.setOpaque(false);

        JPanel header = createSectionHeader("My Appointments",
                "View and manage consultation bookings assigned to you.");

        JButton refreshBtn = Theme.createSecondaryButton("Refresh");
        refreshBtn.addActionListener(e -> refreshAppointments());
        header.add(refreshBtn, BorderLayout.EAST);

        String[] cols = {"ID", "Patient ID", "Date", "Time", "Status", "Notes"};
        appointmentModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = createStyledTable(appointmentModel);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        actions.setOpaque(false);

        JButton viewBtn = Theme.createPrimaryButton("View / Update Status");
        viewBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                Validation.showError(this, "Please select an appointment first.");
                return;
            }
            String aptId = (String) appointmentModel.getValueAt(row, 0);
            Appointment apt = appointmentFile.findById(aptId);
            if (apt != null) {
                new AppointmentDialog(this, apt, currentDoctor, () -> refreshAppointments()).setVisible(true);
            }
        });

        JButton consultBtn = Theme.createPrimaryButton("Start Consultation");
        consultBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                Validation.showError(this, "Please select an appointment first.");
                return;
            }
            String aptId = (String) appointmentModel.getValueAt(row, 0);
            Appointment apt = appointmentFile.findById(aptId);
            if (apt != null) {
                new ConsultationDialog(this, apt, currentDoctor, () -> {
                    refreshAppointments();
                    refreshConsultations();
                }).setVisible(true);
            }
        });

        actions.add(viewBtn);
        actions.add(consultBtn);

        panel.add(header, BorderLayout.NORTH);
        panel.add(styledScroll(table), BorderLayout.CENTER);
        panel.add(actions, BorderLayout.SOUTH);
        return panel;
    }

    // -------------------------------------------------------------------------
    // Consultations Panel
    // -------------------------------------------------------------------------
    private JPanel buildConsultationsPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.setOpaque(false);

        JPanel header = createSectionHeader("Consultation Records",
                "Vital signs and clinical notes you have written.");

        String[] cols = {"ID", "Patient ID", "Date/Time", "Diagnosis", "Vital Signs"};
        consultationModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = createStyledTable(consultationModel);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        actions.setOpaque(false);

        JButton newBtn = Theme.createPrimaryButton("New Consultation");
        newBtn.addActionListener(e ->
                new ConsultationDialog(this, null, currentDoctor, this::refreshConsultations).setVisible(true));

        JButton prescribeBtn = Theme.createPrimaryButton("Issue Prescription");
        prescribeBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                Validation.showError(this, "Select a consultation first.");
                return;
            }
            String cId = (String) consultationModel.getValueAt(row, 0);
            Consultation c = consultationFile.findById(cId);
            if (c != null) {
                new PrescriptionDialog(this, c, currentDoctor, this::refreshPrescriptions).setVisible(true);
            }
        });

        JButton labBtn = Theme.createSecondaryButton("Request Lab / Imaging");
        labBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                Validation.showError(this, "Select a consultation first.");
                return;
            }
            String cId = (String) consultationModel.getValueAt(row, 0);
            Consultation c = consultationFile.findById(cId);
            if (c != null) {
                new LabRequestDialog(this, c, currentDoctor, this::refreshLabRequests).setVisible(true);
            }
        });

        actions.add(newBtn);
        actions.add(prescribeBtn);
        actions.add(labBtn);

        panel.add(header, BorderLayout.NORTH);
        panel.add(styledScroll(table), BorderLayout.CENTER);
        panel.add(actions, BorderLayout.SOUTH);
        return panel;
    }

    // -------------------------------------------------------------------------
    // Prescriptions Panel
    // -------------------------------------------------------------------------
    private JPanel buildPrescriptionsPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.setOpaque(false);

        JPanel header = createSectionHeader("Prescriptions Issued",
                "Digital medication prescriptions you have written.");

        String[] cols = {"ID", "Patient ID", "Medication", "Dosage", "Duration", "Date"};
        prescriptionModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = createStyledTable(prescriptionModel);

        JButton newBtn = Theme.createPrimaryButton("New Prescription");
        newBtn.addActionListener(e ->
                new PrescriptionDialog(this, null, currentDoctor, this::refreshPrescriptions).setVisible(true));

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actions.setOpaque(false);
        actions.add(newBtn);

        panel.add(header, BorderLayout.NORTH);
        panel.add(styledScroll(table), BorderLayout.CENTER);
        panel.add(actions, BorderLayout.SOUTH);
        return panel;
    }

    // -------------------------------------------------------------------------
    // Lab Requests Panel
    // -------------------------------------------------------------------------
    private JPanel buildLabRequestsPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.setOpaque(false);

        JPanel header = createSectionHeader("Lab & Imaging Requests",
                "Requests you have submitted for lab tests or specialised imaging.");

        String[] cols = {"ID", "Patient ID", "Test Type", "Urgency", "Status", "Date"};
        labModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = createStyledTable(labModel);

        JButton newBtn = Theme.createPrimaryButton("New Lab Request");
        newBtn.addActionListener(e ->
                new LabRequestDialog(this, null, currentDoctor, this::refreshLabRequests).setVisible(true));

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actions.setOpaque(false);
        actions.add(newBtn);

        panel.add(header, BorderLayout.NORTH);
        panel.add(styledScroll(table), BorderLayout.CENTER);
        panel.add(actions, BorderLayout.SOUTH);
        return panel;
    }

    // -------------------------------------------------------------------------
    // Profile Summary
    // -------------------------------------------------------------------------
    private JPanel buildProfileSummaryPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.setOpaque(false);

        JPanel header = createSectionHeader("My Profile",
                "View and edit your personal & professional information.");

        JPanel card = Theme.createCard();
        card.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        addProfileRow(card, gbc, 0, "Doctor ID", safe(currentDoctor.getId()));
        addProfileRow(card, gbc, 1, "Full Name", safe(currentDoctor.getName()));
        addProfileRow(card, gbc, 2, "Email", safe(currentDoctor.getEmail()));
        addProfileRow(card, gbc, 3, "Phone", safe(currentDoctor.getPhone()));
        addProfileRow(card, gbc, 4, "Specialty", safe(currentDoctor.getSpecialty()));
        addProfileRow(card, gbc, 5, "Department", safe(currentDoctor.getDepartmentId()));
        addProfileRow(card, gbc, 6, "Manager ID", safe(currentDoctor.getManagerId()));
        addProfileRow(card, gbc, 7, "Shift", safe(currentDoctor.getShift()));
        addProfileRow(card, gbc, 8, "Consultation Fee",
                "RM " + String.format("%.2f", currentDoctor.getConsultationFee()));
        addProfileRow(card, gbc, 9, "Status", currentDoctor.isActive() ? "Active" : "Inactive");

        // Scroll so every row stays visible on smaller windows
        JScrollPane scroll = new JScrollPane(card);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Theme.BG);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        JButton editBtn = Theme.createPrimaryButton("Edit Profile");
        editBtn.addActionListener(e -> {
            new ProfileDialog(this, currentDoctor, () -> {
                // Rebuild PROFILE card by name (not fragile index)
                contentPanel.remove(contentPanel.getComponent(
                        findCardIndex("PROFILE")));
                contentPanel.add(buildProfileSummaryPanel(), "PROFILE");
                cardLayout.show(contentPanel, "PROFILE");
                setTitle("Doctor Dashboard - " + currentDoctor.getName());
            }).setVisible(true);
        });

        JPanel south = new JPanel(new FlowLayout(FlowLayout.LEFT));
        south.setOpaque(false);
        south.add(editBtn);

        panel.add(header, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(south, BorderLayout.SOUTH);
        return panel;
    }

    private int findCardIndex(String name) {
        for (int i = 0; i < contentPanel.getComponentCount(); i++) {
            // CardLayout does not expose names; rebuild order is fixed:
            // 0 APPOINTMENTS, 1 CONSULTATIONS, 2 PRESCRIPTIONS, 3 LAB, 4 PROFILE
            if ("PROFILE".equals(name) && i == 4) return i;
        }
        return contentPanel.getComponentCount() - 1;
    }

    private static String safe(String v) {
        return (v == null || v.isEmpty()) ? "-" : v;
    }

    private void addProfileRow(JPanel card, GridBagConstraints gbc, int row, String label, String value) {
        gbc.gridx = 0; gbc.gridy = row;
        JLabel lbl = new JLabel(label + ":");
        lbl.setFont(Theme.FONT_BODY);
        lbl.setForeground(Theme.TEXT_SECONDARY);
        card.add(lbl, gbc);

        gbc.gridx = 1;
        JLabel val = new JLabel(value);
        val.setFont(Theme.FONT_BODY);
        val.setForeground(Theme.TEXT_PRIMARY);
        card.add(val, gbc);
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------
    private JPanel createSectionHeader(String title, String subtitle) {
        JPanel header = new JPanel(new BorderLayout(14, 0));
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(0, 0, 4, 0));

        // Left accent bar
        JPanel accent = new JPanel();
        accent.setBackground(Theme.PRIMARY);
        accent.setPreferredSize(new Dimension(4, 0));

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));

        JLabel t = new JLabel(title);
        t.setFont(Theme.FONT_TITLE);
        t.setForeground(Theme.TEXT_PRIMARY);
        t.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel s = new JLabel(subtitle);
        s.setFont(Theme.FONT_BODY);
        s.setForeground(Theme.TEXT_SECONDARY);
        s.setAlignmentX(Component.LEFT_ALIGNMENT);

        text.add(t);
        text.add(Box.createVerticalStrut(4));
        text.add(s);

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

        table.getTableHeader().setFont(Theme.FONT_LABEL);
        table.getTableHeader().setBackground(new Color(248, 250, 252));
        table.getTableHeader().setForeground(Theme.TEXT_SECONDARY);
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER));

        // Zebra striping + left padding renderer
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean sel,
                    boolean focus, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, v, sel, focus, row, col);
                setBorder(new EmptyBorder(0, 14, 0, 14));
                if (!sel) {
                    c.setBackground(row % 2 == 0 ? Theme.CARD_BG : new Color(248, 250, 252));
                    c.setForeground(Theme.TEXT_PRIMARY);
                }
                return c;
            }
        };
        renderer.setHorizontalAlignment(SwingConstants.LEFT);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }
        return table;
    }

    private JScrollPane styledScroll(JTable table) {
        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createLineBorder(Theme.BORDER, 1, true));
        sp.getViewport().setBackground(Theme.CARD_BG);
        sp.setBackground(Theme.CARD_BG);
        return sp;
    }

    private void refreshAllTables() {
        refreshAppointments();
        refreshConsultations();
        refreshPrescriptions();
        refreshLabRequests();
    }

    private void refreshAppointments() {
        appointmentModel.setRowCount(0);
        List<Appointment> list = appointmentFile.getByDoctorId(currentDoctor.getId());
        for (Appointment a : list) {
            appointmentModel.addRow(new Object[]{
                    a.getAppointmentId(), a.getPatientId(), a.getDate(),
                    a.getTime(), a.getStatus(), a.getNotes()
            });
        }
    }

    private void refreshConsultations() {
        consultationModel.setRowCount(0);
        List<Consultation> list = consultationFile.getByDoctorId(currentDoctor.getId());
        for (Consultation c : list) {
            consultationModel.addRow(new Object[]{
                    c.getConsultationId(), c.getPatientId(), c.getDateTime(),
                    c.getDiagnosis(), c.getVitalSigns()
            });
        }
    }

    private void refreshPrescriptions() {
        prescriptionModel.setRowCount(0);
        List<Prescription> list = prescriptionFile.getByDoctorId(currentDoctor.getId());
        for (Prescription p : list) {
            prescriptionModel.addRow(new Object[]{
                    p.getPrescriptionId(), p.getPatientId(), p.getMedication(),
                    p.getDosage(), p.getDuration(), p.getDateIssued()
            });
        }
    }

    private void refreshLabRequests() {
        labModel.setRowCount(0);
        List<LabRequest> list = labRequestFile.getByDoctorId(currentDoctor.getId());
        for (LabRequest lr : list) {
            labModel.addRow(new Object[]{
                    lr.getRequestId(), lr.getPatientId(), lr.getTestType(),
                    lr.getUrgency(), lr.getStatus(), lr.getDateRequested()
            });
        }
    }
}
