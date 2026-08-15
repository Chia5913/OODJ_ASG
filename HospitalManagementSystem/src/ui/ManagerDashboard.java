package ui;

import dao.DepartmentFile;
import dao.ShiftRosterFile;
import dao.DoctorFile;

import model.Department;
import model.MedicalManager;
import model.ShiftRoster;
import model.Doctor;

import util.Theme;
import util.Validation;

import service.HospitalReportService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Medical Manager dashboard for the Hospital Management System.
 *
 * Provides access to:
 * - Department management
 * - Doctor shift roster management
 * - Hospital reports
 * - Personal profile management
 */
public class ManagerDashboard extends JFrame {

    private final MedicalManager currentUser;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private String activeCard = "HOME";
    private final java.util.List<JButton> navButtons = new java.util.ArrayList<>();

    //Department Table
    private JTable departmentTable;
    private javax.swing.table.DefaultTableModel departmentTableModel;

    private JTable shiftRosterTable;
    private javax.swing.table.DefaultTableModel shiftRosterTableModel;

    private JLabel profileNameValue;
    private JLabel profileEmailValue;
    private JLabel profilePhoneValue;

    // Report page
    private JLabel reportDepartmentsValue;
    private JLabel reportDoctorsValue;
    private JLabel reportAppointmentsValue;
    private JLabel reportCompletedValue;
    private JLabel reportRostersValue;
    private JLabel reportRevenueValue;
    private JLabel reportAverageRevenueValue;

    // Home page
    private JLabel homeDepartmentsValue;
    private JLabel homeDoctorsValue;
    private JLabel homeRostersValue;

    // Sidebar
    private JLabel sidebarUserLabel;

    public ManagerDashboard(MedicalManager user) {
        this.currentUser = user;

        setTitle("Manager Portal – " + user.getName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setMinimumSize(new Dimension(900, 600));
        Theme.styleFrame(this);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Theme.BG);
        root.add(buildSidebar(), BorderLayout.WEST);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(Theme.BG);
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        contentPanel.add(buildHomePanel(), "HOME");
        contentPanel.add(buildDepartmentPanel(), "DEPTS");
        contentPanel.add(buildShiftRosterPanel(), "ROSTERS");
        contentPanel.add(buildReportPanel(), "REPORTS");
        contentPanel.add(buildProfilePanel(), "PROFILE");

        root.add(contentPanel, BorderLayout.CENTER);
        setContentPane(root);
        setLocationRelativeTo(null);

        cardLayout.show(contentPanel, "HOME");
    }

    private JPanel buildSidebar() {
        JPanel side = new JPanel();
        side.setPreferredSize(new Dimension(248, 0));
        side.setBackground(Theme.SIDEBAR);
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setBorder(new EmptyBorder(28, 18, 24, 18));

        JLabel brand = new JLabel("HMS");
        brand.setFont(Theme.FONT_HEADING);
        brand.setForeground(Color.WHITE);
        brand.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel roleLbl = new JLabel("Manager Portal");
        roleLbl.setFont(Theme.FONT_SMALL);
        roleLbl.setForeground(Theme.SIDEBAR_MUTED);
        roleLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        side.add(brand);
        side.add(Box.createVerticalStrut(4));
        side.add(roleLbl);
        side.add(Box.createVerticalStrut(24));

        side.add(navButton("Home", "HOME"));
        side.add(Box.createVerticalStrut(6));
        side.add(navButton("Departments", "DEPTS"));
        side.add(Box.createVerticalStrut(6));
        side.add(navButton("Shift Rosters", "ROSTERS"));
        side.add(Box.createVerticalStrut(6));
        side.add(navButton("Reports", "REPORTS"));
        side.add(Box.createVerticalStrut(6));
        side.add(navButton("My Profile", "PROFILE"));
        side.add(Box.createVerticalStrut(6));

        side.add(Box.createVerticalGlue());

        sidebarUserLabel = new JLabel(currentUser.getName());
        sidebarUserLabel.setFont(Theme.FONT_SMALL);
        sidebarUserLabel.setForeground(Theme.SIDEBAR_TEXT);
        sidebarUserLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        side.add(sidebarUserLabel);
        side.add(Box.createVerticalStrut(10));

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setFont(Theme.FONT_BUTTON);
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setBackground(new Color(51, 65, 85));
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        logoutBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        logoutBtn.addActionListener(e -> {
            if (Validation.confirm(this, "Logout?")) {
                dispose();
                SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
            }
        });
        side.add(logoutBtn);
        return side;
    }

    private JButton navButton(String text, String cardName) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                boolean active = cardName.equals(activeCard);
                if (active) {
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
        btn.setFont(new Font(Theme.FONT_BODY.getFamily(), Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(new EmptyBorder(0, 14, 0, 12));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.addActionListener(e -> {
            activeCard = cardName;

            //Refresh pages that depend on changing data.
            if ("HOME".equals(cardName)) {
                refreshHomeData();
            }

            if ("REPORTS".equals(cardName)) {
                refreshReportData();
            }

            if ("DEPTS".equals(cardName)) {
                loadDepartmentTable();
            }

            if ("ROSTERS".equals(cardName)) {
                loadShiftRosterTable();
            }

            cardLayout.show(contentPanel, cardName);
            for (JButton b : navButtons) {
                b.repaint();
            }
        });

        navButtons.add(btn);
        return btn;
    }

    private JPanel buildHomePanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.setOpaque(false);

        //=========================================
        //WELCOME HEADER
        //=========================================

        JLabel title = new JLabel("Welcome, " + currentUser.getName());
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.TEXT_PRIMARY);

        JLabel subtitle = new JLabel("Manage your departments, doctors' shift rosters and hospital operations.");
        subtitle.setFont(Theme.FONT_BODY);
        subtitle.setForeground(Theme.TEXT_SECONDARY);

        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        header.add(title);
        header.add(Box.createVerticalStrut(5));
        header.add(subtitle);

        //=========================================
        //MANAGER SUMMARY
        //=========================================
        JPanel managerCard = Theme.createCard();
        managerCard.setLayout(new GridLayout(2, 2, 15, 12));
        managerCard.add(createProfileLabel("Manager ID"));
        managerCard.add(createProfileValue(currentUser.getId()));
        managerCard.add(createProfileLabel("Role"));
        managerCard.add(createProfileValue(currentUser.getRole()));

        //=========================================
        //QUICK STATISTICS
        //=========================================
        JPanel statisticsPanel = new JPanel(new GridLayout(1, 3, 16, 0));
        statisticsPanel.setOpaque(false);
        homeDepartmentsValue = new JLabel("0");
        homeDoctorsValue = new JLabel("0");
        homeRostersValue = new JLabel("0");
        statisticsPanel.add(createReportMetricCard("My Departments", homeDepartmentsValue));
        statisticsPanel.add(createReportMetricCard("My Doctors",homeDoctorsValue));
        statisticsPanel.add(createReportMetricCard("My Shift Rosters", homeRostersValue));

        //=========================================
        //CONTENT
        //=========================================
        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        header.setAlignmentX(Component.LEFT_ALIGNMENT);
        managerCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        statisticsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        managerCard.setMaximumSize(new Dimension(Integer.MAX_VALUE,130));
        statisticsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        content.add(header);
        content.add(Box.createVerticalStrut(20));
        content.add(managerCard);
        content.add(Box.createVerticalStrut(20));
        content.add(statisticsPanel);

        //Load current statistics.
        refreshHomeData();

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Theme.BG);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildDepartmentPanel() {

        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.setOpaque(false);

        //=========================================
        // PAGE HEADER
        //=========================================
        JPanel headerPanel = new JPanel(new BorderLayout(12, 0));
        headerPanel.setOpaque(false);

        JLabel title = new JLabel("Departments");
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.TEXT_PRIMARY);

        JLabel subtitle = new JLabel("Create and manage the clinical departments assigned to you.");
        subtitle.setFont(Theme.FONT_BODY);
        subtitle.setForeground(Theme.TEXT_SECONDARY);

        JPanel titleGroup = new JPanel();
        titleGroup.setOpaque(false);
        titleGroup.setLayout(new BoxLayout(titleGroup,BoxLayout.Y_AXIS));

        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        titleGroup.add(title);
        titleGroup.add(Box.createVerticalStrut(4));
        titleGroup.add(subtitle);

        //Add Department button.
        JButton addButton = Theme.createPrimaryButton("+ Add Department");
        addButton.addActionListener(e -> showAddDepartmentDialog());
        JButton editButton = Theme.createSecondaryButton("Edit Selected");
        editButton.addActionListener(e -> editSelectedDepartment());
        JButton deleteButton = Theme.createDangerButton("Delete Selected");
        deleteButton.addActionListener(e -> deleteSelectedDepartment());
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT,10, 0));
        actionPanel.setOpaque(false);
        actionPanel.add(deleteButton);
        actionPanel.add(editButton);
        actionPanel.add(addButton);
        headerPanel.add(titleGroup, BorderLayout.WEST);
        headerPanel.add(actionPanel, BorderLayout.EAST);

        //=========================================
        //DEPARTMENT TABLE
        //=========================================

        String[] columnNames = {"Department ID", "Department Name", "Description", "Manager ID"};
        departmentTableModel = new javax.swing.table.DefaultTableModel(columnNames,0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        departmentTable = new JTable(departmentTableModel);
        departmentTable.setRowHeight(36);
        departmentTable.setFont(Theme.FONT_BODY);
        departmentTable.getTableHeader().setFont(Theme.FONT_LABEL);

        departmentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane tableScroll = new JScrollPane(departmentTable);
        tableScroll.setBorder(BorderFactory.createLineBorder(Theme.BORDER, 1, true));
        tableScroll.getViewport().setBackground(Theme.CARD_BG);

        //=========================================
        // CARD
        //=========================================
        JPanel card = Theme.createCard();
        card.setLayout(new BorderLayout(0, 16));
        card.add(headerPanel, BorderLayout.NORTH);
        card.add(tableScroll, BorderLayout.CENTER);

        //Load department data.
        loadDepartmentTable();

        //=========================================
        //SCROLL
        //=========================================
        JScrollPane scroll = new JScrollPane(card);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Theme.BG);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildShiftRosterPanel() {

        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.setOpaque(false);

        //=========================================
        //PAGE HEADER
        //========================================
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel title = new JLabel("Shift Rosters");
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.TEXT_PRIMARY);

        JLabel subtitle = new JLabel("View doctor shifts under your managed departments.");
        subtitle.setFont(Theme.FONT_BODY);
        subtitle.setForeground(Theme.TEXT_SECONDARY);

        JPanel titleGroup = new JPanel();
        titleGroup.setOpaque(false);
        titleGroup.setLayout(new BoxLayout(titleGroup, BoxLayout.Y_AXIS));

        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        titleGroup.add(title);
        titleGroup.add(Box.createVerticalStrut(4));
        titleGroup.add(subtitle);

        //Add Shift Rosters button.
        JButton addButton = Theme.createPrimaryButton("+ Add Shift Roster");
        addButton.addActionListener(e -> showAddShiftRosterDialog());
        JButton editButton = Theme.createSecondaryButton("Edit Selected");
        editButton.addActionListener(e -> editSelectedShiftRoster());
        JButton deleteButton = Theme.createDangerButton("Delete Selected");
        deleteButton.addActionListener(e -> deleteSelectedShiftRoster());
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT,10, 0));
        actionPanel.setOpaque(false);
        actionPanel.add(deleteButton);
        actionPanel.add(editButton);
        actionPanel.add(addButton);
        headerPanel.add(titleGroup, BorderLayout.WEST);
        headerPanel.add(actionPanel, BorderLayout.EAST);

        //=========================================
        //TABLE
        //=========================================
        String[] columnNames = {"Roster ID", "Doctor ID", "Department ID", "Shift Date", "Start Time", "End Time", "Shift Type"};
        shiftRosterTableModel = new javax.swing.table.DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        shiftRosterTable = new JTable(shiftRosterTableModel);
        shiftRosterTable.setRowHeight(36);
        shiftRosterTable.setFont(Theme.FONT_BODY);
        shiftRosterTable.getTableHeader().setFont(Theme.FONT_LABEL);
        shiftRosterTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane tableScroll = new JScrollPane(shiftRosterTable);
        tableScroll.setBorder(BorderFactory.createLineBorder(Theme.BORDER,1, true));
        tableScroll.getViewport().setBackground(Theme.CARD_BG);

        //=========================================
        //CARD
        //=========================================
        JPanel card = Theme.createCard();
        card.setLayout(new BorderLayout(0, 16));
        card.add(headerPanel, BorderLayout.NORTH);
        card.add(tableScroll,BorderLayout.CENTER);

        //Load roster data.
        loadShiftRosterTable();

        //=========================================
        //MAIN SCROLL
        //=========================================
        JScrollPane scroll = new JScrollPane(card);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Theme.BG);

        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private void loadShiftRosterTable() {
        //Clear currently displayed rows.
        shiftRosterTableModel.setRowCount(0);
        ShiftRosterFile shiftRosterFile = new ShiftRosterFile();
        //Only load rosters belonging to departments managed by the currently logged-in manager.
        java.util.List<ShiftRoster> rosters = shiftRosterFile.findByManagerId(currentUser.getId());

        for (ShiftRoster roster : rosters) {
            Object[] row = {roster.getRosterId(), roster.getDoctorId(), roster.getDepartmentId(), roster.getShiftDate(), roster.getStartTime(), roster.getEndTime(), roster.getShiftType()};
            shiftRosterTableModel.addRow(row);
        }
    }

    private void loadDepartmentTable() {

        //Remove old rows first
        departmentTableModel.setRowCount(0);
        DepartmentFile departmentFile = new DepartmentFile();
        java.util.List<Department> departments = departmentFile.findByManagerId(currentUser.getId());

        for (Department department : departments) {
            Object[] row = {department.getDepartmentId(), department.getName(), department.getDescription(), department.getManagerId()};
            departmentTableModel.addRow(row);
        }
    }

    private void editSelectedDepartment() {
        //Get the selected JTable row
        int selectedRow = departmentTable.getSelectedRow();

        //If selectedRow == -1, no row has been selected.
        if (selectedRow == -1) {
            Validation.showError(this, "Please select a department to edit.");
            return;
        }

        //Get Department ID from column 0.
        String departmentId = departmentTableModel.getValueAt(selectedRow, 0).toString();
        DepartmentFile departmentFile = new DepartmentFile();

        //Get the real Department object from departments.txt.
        Department department = departmentFile.findById(departmentId);

        if (department == null) {
            Validation.showError(this, "Department record could not be found.");
            loadDepartmentTable();
            return;
        }

        //Security / authorization check.
        //The logged-in manager should only edit their own department.
        if (!department.getManagerId().equalsIgnoreCase(currentUser.getId())) {
            Validation.showError(this, "You are not allowed to edit this department.");
            return;
        }

        //Open the edit dialog.
        showEditDepartmentDialog(department);
    }

    private void editSelectedShiftRoster() {
        //Get currently selected row.
        int selectedRow = shiftRosterTable.getSelectedRow();

        //-1 means the user did not select any roster.
        if (selectedRow == -1) {
            Validation.showError(this, "Please select a shift roster to edit.");
            return;
        }

        //Roster ID is stored in column 0.
        String rosterId = shiftRosterTableModel.getValueAt(selectedRow, 0).toString();

        ShiftRosterFile shiftRosterFile = new ShiftRosterFile();

        //Get the real object from file.
        ShiftRoster roster = shiftRosterFile.findById(rosterId);

        if (roster == null) {
            Validation.showError(this, "Shift roster record could not be found.");
            loadShiftRosterTable();
            return;
        }

        //=========================================
        //ACCESS CONTROL
        //=========================================
        DepartmentFile departmentFile = new DepartmentFile();
        Department department = departmentFile.findById(roster.getDepartmentId());

        //Manager may only edit rosters belonging to their own departments.
        if (department == null || !department.getManagerId().equalsIgnoreCase(currentUser.getId())) {
            Validation.showError(this, "You are not allowed to edit this shift roster.");
            return;
        }

        //Open Edit dialog.
        showEditShiftRosterDialog(roster);
    }

    private void showAddDepartmentDialog() {
        //=========================================
        //CREATE DIALOG
        //=========================================
        JDialog dialog = new JDialog(this, "Add Department", true);
        dialog.setSize(520, 430);
        dialog.setResizable(false);
        Theme.styleDialog(dialog);

        JPanel root = new JPanel(new BorderLayout(0, 20));
        root.setBackground(Theme.BG);
        root.setBorder(new EmptyBorder(22, 22, 22, 22));

        //=========================================
        //TITLE
        //=========================================
        JLabel title = new JLabel("Add Department");
        title.setFont(Theme.FONT_HEADING);
        title.setForeground(Theme.TEXT_PRIMARY);

        JLabel subtitle = new JLabel("Create a new clinical department.");
        subtitle.setFont(Theme.FONT_BODY);
        subtitle.setForeground(Theme.TEXT_SECONDARY);
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        header.add(title);
        header.add(Box.createVerticalStrut(4));
        header.add(subtitle);

        //=========================================
        //FORM
        //=========================================

        JPanel formCard = Theme.createCard();
        formCard.setLayout(new GridLayout(4, 2, 12, 14));
        JTextField idField = Theme.createTextField(20);
        JTextField nameField = Theme.createTextField(20);
        JTextArea descriptionArea = Theme.createTextArea(3, 20);
        JTextField managerField = Theme.createTextField(20);

        //Manager ID comes automatically from the logged-in Medical Manager.
        managerField.setText(currentUser.getId());
        managerField.setEditable(false);
        JLabel idLabel = new JLabel("Department ID");
        idLabel.setFont(Theme.FONT_LABEL);

        JLabel nameLabel = new JLabel("Department Name");
        nameLabel.setFont(Theme.FONT_LABEL);

        JLabel descriptionLabel = new JLabel("Description");
        descriptionLabel.setFont(Theme.FONT_LABEL);

        JLabel managerLabel =new JLabel("Manager ID");
        managerLabel.setFont(Theme.FONT_LABEL);

        formCard.add(idLabel);
        formCard.add(idField);
        formCard.add(nameLabel);
        formCard.add(nameField);
        formCard.add(descriptionLabel);
        formCard.add(new JScrollPane(descriptionArea));
        formCard.add(managerLabel);
        formCard.add(managerField);

        //=========================================
        //BUTTONS
        //=========================================
        JButton cancelButton = Theme.createSecondaryButton("Cancel");
        JButton saveButton = Theme.createPrimaryButton("Add Department");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT,10, 0));

        buttonPanel.setOpaque(false);
        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);
        cancelButton.addActionListener(e -> dialog.dispose());

        //=========================================
        //SAVE ACTION
        //=========================================
        saveButton.addActionListener(e -> {
            String departmentId = idField.getText().trim();
            String departmentName = nameField.getText().trim();
            String description = descriptionArea.getText().trim();

            //Validation: all fields required
            if (Validation.isEmpty(departmentId) || Validation.isEmpty( departmentName) || Validation.isEmpty(description)) {
                Validation.showError(dialog, "Please complete all fields.");
                    return;
            }

            DepartmentFile departmentFile = new DepartmentFile();

            //Check duplicate Department ID
            Department existingDepartment = departmentFile.findById(departmentId);

            if (existingDepartment != null) {
                Validation.showError(dialog, "Department ID already exists.");
                idField.requestFocus();
                return;
            }

            //Create Department object.
            //Manager ID is NOT typed manually.
            //It comes from currentUser.
            Department department = new Department(departmentId, departmentName, description, currentUser.getId());

            //Save into departments.txt
            departmentFile.save(department);

            Validation.showSuccess(dialog, "Department added successfully.");

            //Refresh JTable.
            loadDepartmentTable();

            //Close Add dialog.
            dialog.dispose();
        });

        //=========================================
        //BUILD ROOT
        //=========================================
        root.add(header, BorderLayout.NORTH);
        root.add(formCard, BorderLayout.CENTER);
        root.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setContentPane(root);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void showAddShiftRosterDialog() {

        JDialog dialog = new JDialog(this, "Add Shift Roster", true);
        dialog.setSize(580, 620);
        dialog.setResizable(false);
        Theme.styleDialog(dialog);

        JPanel root = new JPanel(new BorderLayout(0, 20));
        root.setBackground(Theme.BG);
        root.setBorder(new EmptyBorder(22, 22, 22, 22));

        //=========================================
        //HEADER
        //=========================================
        JLabel title = new JLabel("Add Shift Roster");
        title.setFont(Theme.FONT_HEADING);
        title.setForeground(Theme.TEXT_PRIMARY);

        JLabel subtitle = new JLabel("Assign a doctor to an operational shift.");
        subtitle.setFont(Theme.FONT_BODY);
        subtitle.setForeground(Theme.TEXT_SECONDARY);

        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));

        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        header.add(title);
        header.add(Box.createVerticalStrut(4));
        header.add(subtitle);

        //=========================================
        //FORM FIELDS
        //=========================================
        JTextField rosterIdField = Theme.createTextField(20);
        JComboBox<String> doctorComboBox = new JComboBox<>();
        JComboBox<String> departmentComboBox = new JComboBox<>();
        JTextField dateField = Theme.createTextField(20);
        JTextField startTimeField = Theme.createTextField(20);
        JTextField endTimeField = Theme.createTextField(20);
        JComboBox<String> shiftTypeComboBox = new JComboBox<>(new String[]{"Morning", "Evening", "Night"});

        //=========================================
        //LOAD MANAGER DEPARTMENTS
        //=========================================
        DepartmentFile departmentFile = new DepartmentFile();
        java.util.List<Department> departments =departmentFile.findByManagerId(currentUser.getId());

        for (Department department : departments) {
            departmentComboBox.addItem(department.getDepartmentId() + " - " + department.getName());
        }   

        //=========================================
        //LOAD DOCTORS
        //=========================================
        DoctorFile doctorFile = new DoctorFile();
        java.util.List<Doctor> doctors = doctorFile.findByManagerId(currentUser.getId());

        for (Doctor doctor : doctors) {
            doctorComboBox.addItem(doctor.getId() + " - " + doctor.getName());
        }

        //=========================================
        //FORM PANEL
        //=========================================
        JPanel formCard = Theme.createCard();
        formCard.setLayout(new GridLayout(7, 2, 12, 14));

        JLabel rosterIdLabel = new JLabel("Roster ID");
        JLabel doctorLabel = new JLabel("Doctor");
        JLabel departmentLabel = new JLabel("Department");
        JLabel dateLabel = new JLabel("Shift Date (yyyy-MM-dd)");
        JLabel startTimeLabel = new JLabel("Start Time (HH:mm)");
        JLabel endTimeLabel = new JLabel("End Time (HH:mm)");
        JLabel shiftTypeLabel = new JLabel( "Shift Type");
        JLabel[] labels = {rosterIdLabel, doctorLabel, departmentLabel, dateLabel, startTimeLabel, endTimeLabel, shiftTypeLabel};

        for (JLabel label : labels) {
            label.setFont(Theme.FONT_LABEL);
        }

        formCard.add(rosterIdLabel);
        formCard.add(rosterIdField);

        formCard.add(doctorLabel);
        formCard.add(doctorComboBox);

        formCard.add(departmentLabel);
        formCard.add(departmentComboBox);

        formCard.add(dateLabel);
        formCard.add(dateField);

        formCard.add(startTimeLabel);
        formCard.add(startTimeField);

        formCard.add(endTimeLabel);
        formCard.add(endTimeField);

        formCard.add(shiftTypeLabel);
        formCard.add(shiftTypeComboBox);

        //=========================================
        //BUTTONS
        //=========================================
        JButton cancelButton = Theme.createSecondaryButton("Cancel");
        JButton saveButton = Theme.createPrimaryButton("Add Shift Roster");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);
        cancelButton.addActionListener(e -> dialog.dispose());

        //=========================================
        //SAVE ACTION
        //=========================================
        saveButton.addActionListener(e -> {
            String rosterId = rosterIdField.getText().trim();
            String shiftDate = dateField.getText().trim();
            String startTime = startTimeField.getText().trim();
            String endTime = endTimeField.getText().trim();
            String shiftType = shiftTypeComboBox.getSelectedItem().toString();

            //Basic required field validation.
            if (Validation.isEmpty(rosterId) || Validation.isEmpty(shiftDate) || Validation.isEmpty(startTime) || Validation.isEmpty(endTime)) {
                Validation.showError(dialog,"Please complete all fields.");
                    return;
            }

            //Check available doctors/departments.
            if (doctorComboBox.getSelectedItem() == null) {
                Validation.showError(dialog, "No doctor is available.");
                    return;
            }

            if (departmentComboBox.getSelectedItem() == null) {
                Validation.showError(dialog, "You do not manage any department.");
                    return;
            }

            //Extract ID from: DOC-001 - Doctor Name 
            String doctorSelection = doctorComboBox.getSelectedItem().toString();
            String doctorId = doctorSelection.split(" - ", 2)[0];

            //Extract ID from: DEPT-CARD - Cardiology
            String departmentSelection = departmentComboBox.getSelectedItem().toString();
            String departmentId = departmentSelection.split(" - ", 2)[0];
            ShiftRosterFile shiftRosterFile = new ShiftRosterFile();

            //Duplicate Roster ID validation.
            ShiftRoster existingRoster = shiftRosterFile.findById(rosterId);
            if (existingRoster != null) {
                Validation.showError(dialog, "Roster ID already exists.");
                rosterIdField.requestFocus();
                return;
            }

            // Date validation.
            if (!isValidDate(shiftDate)) {
                Validation.showError(dialog, "Invalid date. Please use yyyy-MM-dd.");
                dateField.requestFocus();
                return;
            }

            //Time validation.
            if (!isValidTime(startTime) || !isValidTime( endTime)) {
                Validation.showError(dialog, "Invalid time. Please use HH:mm.");
                return;
            }

            //For now, require end time to be later than start time on the same day.
            if (!isEndTimeAfterStartTime(startTime, endTime)) {
                Validation.showError(dialog, "End time must be later than start time.");
                return;
            }

            //Verify department still belongs to the current manager.
            Department selectedDepartment = departmentFile.findById(departmentId);
            if (selectedDepartment == null || !selectedDepartment.getManagerId().equalsIgnoreCase(currentUser.getId())) {
                Validation.showError(dialog, "You are not allowed to manage this department.");
                return;
            }

            //Verify doctor really exists.
            Doctor selectedDoctor = doctorFile.findById(doctorId);
            if (selectedDoctor == null) {
                Validation.showError(dialog, "Selected doctor could not be found.");
                return;
            }

            //Doctor must be assigned to the current Medical Manager
            if (!selectedDoctor.getManagerId().equalsIgnoreCase(currentUser.getId())){
                Validation.showError(dialog, "This doctor is not assigned to you.");
                return;
            }

            //Doctor must belong to the selected department.
            if (!selectedDoctor.getDepartmentId().equalsIgnoreCase(departmentId)){
                Validation.showError(dialog, "The selected doctor does not belong to the selected department,");
                return;
            }

            //Create roster object
            ShiftRoster roster = new ShiftRoster(rosterId, doctorId, departmentId, shiftDate, startTime, endTime, shiftType);

            // Save into shift_rosters.txt.
            shiftRosterFile.save(roster);
            Validation.showSuccess(dialog,"Shift roster added successfully.");

            //Refresh table.
            loadShiftRosterTable();

            dialog.dispose();
        });

        //=========================================
        //BUILD DIALOG
        //=========================================
        root.add(header, BorderLayout.NORTH);
        root.add(formCard, BorderLayout.CENTER);
        root.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setContentPane(root);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void showEditDepartmentDialog(Department department) {
        //=========================================
        //CREATE DIALOG
        //=========================================
        JDialog dialog = new JDialog(
            this,
            "Edit Department",
            true
        );

        dialog.setSize(520, 430);
        dialog.setResizable(false);
        Theme.styleDialog(dialog);
        JPanel root = new JPanel(new BorderLayout(0, 20));
        root.setBackground(Theme.BG);
        root.setBorder(new EmptyBorder(22, 22, 22, 22));

        //=========================================
        //HEADER
        //=========================================
        JLabel title = new JLabel("Edit Department");
        title.setFont(Theme.FONT_HEADING);
        title.setForeground(Theme.TEXT_PRIMARY);

        JLabel subtitle = new JLabel("Update the selected clinical department.");
        subtitle.setFont(Theme.FONT_BODY);
        subtitle.setForeground(Theme.TEXT_SECONDARY);
        JPanel header = new JPanel();

        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        header.add(title);
        header.add(Box.createVerticalStrut(4));
        header.add(subtitle);

        //=========================================
        //FORM
        //=========================================
        JPanel formCard = Theme.createCard();
        formCard.setLayout(new GridLayout(4, 2, 12, 14));
        JTextField idField = Theme.createTextField(20);
        JTextField nameField = Theme.createTextField(20);
        JTextArea descriptionArea = Theme.createTextArea(3, 20);
        JTextField managerField = Theme.createTextField(20);

        //Load current department information.
        idField.setText(department.getDepartmentId());
        nameField.setText(department.getName());
        descriptionArea.setText(department.getDescription());
        managerField.setText(department.getManagerId());

        //Department ID is the permanent identifier.
        //Manager ownership also cannot be changed by the Medical Manager.
        idField.setEditable(false);
        managerField.setEditable(false);

        JLabel idLabel = new JLabel("Department ID");
        idLabel.setFont(Theme.FONT_LABEL);

        JLabel nameLabel = new JLabel("Department Name");
        nameLabel.setFont(Theme.FONT_LABEL);

        JLabel descriptionLabel = new JLabel("Description");
        descriptionLabel.setFont(Theme.FONT_LABEL);

        JLabel managerLabel = new JLabel("Manager ID");
        managerLabel.setFont(Theme.FONT_LABEL);

        formCard.add(idLabel);
        formCard.add(idField);

        formCard.add(nameLabel);
        formCard.add(nameField);

        formCard.add(descriptionLabel);

        JScrollPane descriptionScroll = new JScrollPane(descriptionArea);
        formCard.add(descriptionScroll);

        formCard.add(managerLabel);
        formCard.add(managerField);

        //=========================================
        //BUTTONS
        //=========================================

        JButton cancelButton = Theme.createSecondaryButton("Cancel");
        JButton saveButton = Theme.createPrimaryButton("Save Changes");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT,10, 0));
        buttonPanel.setOpaque(false);

        buttonPanel.add(cancelButton);

        buttonPanel.add(saveButton);

        cancelButton.addActionListener(e -> dialog.dispose());

        //=========================================
        //SAVE UPDATE
        //=========================================
        saveButton.addActionListener(e -> {
            String newName = nameField.getText().trim();
            String newDescription = descriptionArea.getText().trim();

            //Validate editable fields.
            if (Validation.isEmpty(newName) || Validation.isEmpty(newDescription)) {
                Validation.showError(dialog,"Department name and description are required.");
                return;
            }

            //Ask before saving.
            if (!Validation.confirm(dialog, "Save changes to " + department.getDepartmentId() + "?")) {
                return;
            }

            //Change the existing object.
            department.setName(newName);
            department.setDescription(newDescription);

            //Manager ID remains current owner.
            //We deliberately do not allow it to be edited here.
            department.setManagerId(currentUser.getId());
            DepartmentFile departmentFile = new DepartmentFile();

            //save() finds the matching Department ID and replaces it.
            departmentFile.save(department);
            Validation.showSuccess(dialog, "Department updated successfully.");

            // Refresh the current table.
            loadDepartmentTable();

            dialog.dispose();
        }
    );

        //=========================================
        //BUILD DIALOG
        //=========================================
        root.add(header, BorderLayout.NORTH);
        root.add(formCard, BorderLayout.CENTER);
        root.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setContentPane(root);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void showEditShiftRosterDialog(ShiftRoster roster) {
        JDialog dialog = new JDialog(this, "Edit Shift Roster", true);
        dialog.setSize(580, 620);

        dialog.setResizable(false);
        Theme.styleDialog(dialog);
        JPanel root = new JPanel(new BorderLayout(0, 20));
        root.setBackground(Theme.BG);
        root.setBorder(new EmptyBorder(22, 22, 22, 22));

        //=========================================
        //HEADER
        //=========================================
        JLabel title = new JLabel("Edit Shift Roster");
        title.setFont(Theme.FONT_HEADING);
        title.setForeground(Theme.TEXT_PRIMARY);

        JLabel subtitle = new JLabel("Modify the selected doctor's operational shift.");
        subtitle.setFont(Theme.FONT_BODY);
        subtitle.setForeground(Theme.TEXT_SECONDARY);

        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));

        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        header.add(title);
        header.add(Box.createVerticalStrut(4));
        header.add(subtitle);

        //=========================================
        //FORM COMPONENTS
        //=========================================
        JTextField rosterIdField = Theme.createTextField(20);
        JComboBox<String> doctorComboBox = new JComboBox<>();
        JComboBox<String> departmentComboBox = new JComboBox<>();
        JTextField dateField = Theme.createTextField(20);
        JTextField startTimeField = Theme.createTextField(20);
        JTextField endTimeField = Theme.createTextField(20);
        JComboBox<String> shiftTypeComboBox = new JComboBox<>(new String[]{"Morning", "Evening", "Night"});

        //Roster ID cannot be changed.
        rosterIdField.setText(roster.getRosterId());
        rosterIdField.setEditable(false);

        //Existing date/time.
        dateField.setText(roster.getShiftDate());
        startTimeField.setText(roster.getStartTime());
        endTimeField.setText(roster.getEndTime());

        //Existing shift type.
        shiftTypeComboBox.setSelectedItem(roster.getShiftType());

        //=========================================
        //LOAD DOCTORS
        //=========================================
        DoctorFile doctorFile = new DoctorFile();
        java.util.List<Doctor> doctors = doctorFile.findByManagerId(currentUser.getId());

        for (Doctor doctor : doctors) {
            String item = doctor.getId() + " - "  + doctor.getName();
            doctorComboBox.addItem(item);

            //Select currently assigned doctor.
            if (doctor.getId().equalsIgnoreCase(roster.getDoctorId())) {
                doctorComboBox.setSelectedItem(item);
            }
        }

        //=========================================
        //LOAD CURRENT MANAGER'S DEPARTMENTS
        //=========================================
        DepartmentFile departmentFile = new DepartmentFile();
        java.util.List<Department> departments = departmentFile.findByManagerId(currentUser.getId());

        for (Department department : departments) {
            String item = department.getDepartmentId() + " - "  + department.getName();
            departmentComboBox.addItem(item);

            //Select currently assigned department.
            if (department.getDepartmentId().equalsIgnoreCase(roster.getDepartmentId())) {

                departmentComboBox.setSelectedItem(item);
            }
        }

        //=========================================
        //FORM
        //=========================================
        JPanel formCard = Theme.createCard();
        formCard.setLayout(new GridLayout(7, 2, 12, 14));
        JLabel rosterIdLabel = new JLabel("Roster ID");
        JLabel doctorLabel = new JLabel("Doctor");
        JLabel departmentLabel = new JLabel("Department");
        JLabel dateLabel = new JLabel("Shift Date (yyyy-MM-dd)");
        JLabel startTimeLabel = new JLabel("Start Time (HH:mm)");
        JLabel endTimeLabel = new JLabel( "End Time (HH:mm)");
        JLabel shiftTypeLabel = new JLabel("Shift Type");

        JLabel[] labels = {rosterIdLabel, doctorLabel, departmentLabel, dateLabel, startTimeLabel, endTimeLabel, shiftTypeLabel};

        for (JLabel label : labels) {
            label.setFont(Theme.FONT_LABEL);
        }

        formCard.add(rosterIdLabel);
        formCard.add(rosterIdField);

        formCard.add(doctorLabel);
        formCard.add(doctorComboBox);

        formCard.add(departmentLabel);
        formCard.add(departmentComboBox);

        formCard.add(dateLabel);
        formCard.add(dateField);

        formCard.add(startTimeLabel);
        formCard.add(startTimeField);

        formCard.add(endTimeLabel);
        formCard.add(endTimeField);

        formCard.add(shiftTypeLabel);
        formCard.add(shiftTypeComboBox);

        //=========================================
        // BUTTONS
        //=========================================
        JButton cancelButton = Theme.createSecondaryButton("Cancel");
        JButton saveButton = Theme.createPrimaryButton("Save Changes");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));

        buttonPanel.setOpaque(false);
        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);
        cancelButton.addActionListener(e -> dialog.dispose());

        //=========================================
        //SAVE UPDATE
        //=========================================
        saveButton.addActionListener(e -> {
            String shiftDate = dateField.getText().trim();
            String startTime = startTimeField.getText().trim();
            String endTime = endTimeField.getText().trim();

            //Required fields.
            if (Validation.isEmpty(shiftDate) || Validation.isEmpty(startTime) || Validation.isEmpty(endTime)) {
                Validation.showError(dialog, "Please complete all fields.");
                return;
            }

            //Doctor must exist
            if (doctorComboBox.getSelectedItem() == null) {
                Validation.showError(dialog, "Please select a doctor.");
                return;
            }

            //Department must exist
            if (departmentComboBox.getSelectedItem() == null) {
                Validation.showError(dialog, "Please select a department.");
                return;
            }

            //Extract Doctor ID.
            String doctorSelection = doctorComboBox.getSelectedItem().toString();
            String doctorId = doctorSelection.split(" - ", 2)[0];

            //Extract Department ID
            String departmentSelection = departmentComboBox.getSelectedItem().toString();
            String departmentId = departmentSelection .split(" - ", 2)[0];
            String shiftType = shiftTypeComboBox.getSelectedItem().toString();

           //=========================================
           // DATE VALIDATION
           //=========================================
            if (!isValidDate(shiftDate)) {
                Validation.showError(dialog,"Invalid date. Please use yyyy-MM-dd.");
                return;
            }

            //=========================================
            // TIME VALIDATION
            //=========================================
            if (!isValidTime(startTime) || !isValidTime(endTime)) {
                Validation.showError(dialog, "Invalid time. Please use HH:mm.");
                return;
            }

            if (!isEndTimeAfterStartTime(startTime, endTime)) {
                Validation.showError(dialog, "End time must be later than start time.");
                return;
            }

            //=========================================
            //DEPARTMENT ACCESS CHECK
            //=========================================
            Department selectedDepartment = departmentFile.findById(departmentId);
            if (selectedDepartment == null || !selectedDepartment.getManagerId().equalsIgnoreCase(currentUser.getId())) {
                Validation.showError(dialog, "You are not allowed to manage this department.");
                return;
            }

            //=========================================
            //DOCTOR EXISTS CHECK
            //=========================================
            Doctor selectedDoctor = doctorFile.findById(doctorId);
            if (selectedDoctor == null) {
                Validation.showError(dialog, "Selected doctor could not be found.");
                return;
            }

            if (!selectedDoctor.getManagerId().equalsIgnoreCase(currentUser.getId())) {
                Validation.showError(dialog, "This doctor is not assigned to you.");
                return;
            }

            if (!selectedDoctor.getDepartmentId().equalsIgnoreCase(departmentId)) {
                Validation.showError(dialog, "The selected doctor does not belong to the selected department.");
                return;
            }

            //=========================================
            //CONFIRM UPDATE
            //=========================================
            if (!Validation.confirm(dialog, "Save changes to " + roster.getRosterId() + "?")) {
                return;
            }

            //=========================================
            // UPDATE EXISTING OBJECT
            //=========================================
            roster.setDoctorId(doctorId);
            roster.setDepartmentId(departmentId);
            roster.setShiftDate(shiftDate);
            roster.setStartTime(startTime);
            roster.setEndTime(endTime);
            roster.setShiftType(shiftType);

           //Roster ID is NOT changed.
            ShiftRosterFile shiftRosterFile = new ShiftRosterFile();
            shiftRosterFile.save(roster);
            Validation.showSuccess(dialog, "Shift roster updated successfully.");
            loadShiftRosterTable();
            dialog.dispose();
        });

        //=========================================
        //BUILD DIALOG
        //=========================================
        root.add(header, BorderLayout.NORTH);
        root.add(formCard, BorderLayout.CENTER);
        root.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setContentPane(root);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private boolean isValidDate(String date) {
        try {
            java.time.LocalDate.parse(date, java.time.format.DateTimeFormatter.ofPattern("uuuu-MM-dd").withResolverStyle(java.time.format.ResolverStyle.STRICT));
            return true;

        } catch (
            java.time.format.DateTimeParseException e) {
            return false;
        }
    }

    private boolean isValidTime(String time) {
        try {
            java.time.LocalTime.parse(time, java.time.format.DateTimeFormatter.ofPattern("HH:mm"));
            return true;
        } catch (
            java.time.format.DateTimeParseException e) {
            return false;
        }
    }

    private boolean isEndTimeAfterStartTime(String startTime, String endTime) {
        try {
            java.time.LocalTime start = java.time.LocalTime.parse(startTime);
            java.time.LocalTime end = java.time.LocalTime.parse(endTime);
            return end.isAfter(start);
        } catch (
            java.time.format.DateTimeParseException e) {
            return false;
        }
    }

    private JPanel buildProfilePanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.setOpaque(false);

        //=========================================
        //HEADER
        //=========================================
        JPanel headerPanel = new JPanel(new BorderLayout(12, 0));
        headerPanel.setOpaque(false);
        JLabel title = new JLabel("My Profile");
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.TEXT_PRIMARY);

        JLabel subtitle =new JLabel("View and update your personal information.");
        subtitle.setFont(Theme.FONT_BODY);
        subtitle.setForeground(Theme.TEXT_SECONDARY);

        JPanel titleGroup = new JPanel();
        titleGroup.setOpaque(false);
        titleGroup.setLayout(new BoxLayout(titleGroup,BoxLayout.Y_AXIS));

        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        titleGroup.add(title);
        titleGroup.add(Box.createVerticalStrut(4));
        titleGroup.add(subtitle);

        //Edit button.
        JButton editButton = Theme.createPrimaryButton("Edit Profile");
        editButton.addActionListener(e -> showManagerProfileDialog());
        headerPanel.add(titleGroup, BorderLayout.WEST);
        headerPanel.add(editButton, BorderLayout.EAST);

        //=========================================
        //PROFILE INFORMATION CARD
        //=========================================
        JPanel profileCard = Theme.createCard();
        profileCard.setLayout(new GridLayout(7, 2, 18, 18));

        //Manager ID.
        profileCard.add(createProfileLabel("Manager ID"));
        profileCard.add(createProfileValue(currentUser.getId()));

        //Name.
        profileCard.add(createProfileLabel("Full Name"));
        profileNameValue = createProfileValue(currentUser.getName());
        profileCard.add(profileNameValue);

        //Email.
        profileCard.add(createProfileLabel("Email"));
        profileEmailValue = createProfileValue(currentUser.getEmail());
        profileCard.add(profileEmailValue);

        //Phone.
        profileCard.add(createProfileLabel("Phone"));
        profilePhoneValue = createProfileValue(currentUser.getPhone());
        profileCard.add(profilePhoneValue);

        //Role.
        profileCard.add(createProfileLabel("Role"));
        profileCard.add(createProfileValue(currentUser.getRole()));

        //Primary managed department.
        profileCard.add(createProfileLabel("Managed Department"));

        profileCard.add(createProfileValue(currentUser.getManagedDepartmentId() != null ? currentUser.getManagedDepartmentId() : "-"));

        //Account status.
        profileCard.add(createProfileLabel("Account Status"));
        profileCard.add(createProfileValue(currentUser.isActive() ? "Active" : "Inactive"));

        //=========================================
        //MAIN CARD
        //=========================================
        JPanel card = Theme.createCard();
        card.setLayout(new BorderLayout(0, 20));
        card.add(headerPanel, BorderLayout.NORTH);
        card.add(profileCard, BorderLayout.CENTER);

        JScrollPane scroll = new JScrollPane(card);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Theme.BG);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private JLabel createProfileLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(Theme.FONT_LABEL);
        label.setForeground(Theme.TEXT_SECONDARY);
        return label;
    }

    private JLabel createProfileValue(String text) {
        JLabel label = new JLabel(text != null ? text : "-");
        label.setFont(Theme.FONT_BODY);
        label.setForeground(Theme.TEXT_PRIMARY);
        return label;
    }

    private void showManagerProfileDialog() {
        ManagerProfileDialog dialog = new ManagerProfileDialog(this, currentUser, () -> refreshProfileDisplay());
        dialog.setVisible(true);
    }

    private void refreshProfileDisplay() {
        profileNameValue.setText(currentUser.getName());
        profileEmailValue.setText(currentUser.getEmail());
        profilePhoneValue.setText(currentUser.getPhone());
        sidebarUserLabel.setText(currentUser.getName());
        setTitle("Manager Portal – " + currentUser.getName());
    }

    private JPanel buildReportPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.setOpaque(false);

        //=========================================
        //HEADER
        //=========================================
        JPanel headerPanel = new JPanel(new BorderLayout(12, 0));
        headerPanel.setOpaque(false);

        JLabel title = new JLabel("Hospital Reports");
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.TEXT_PRIMARY);

        JLabel subtitle = new JLabel("View hospital metrics and consultation revenue summaries.");
        subtitle.setFont(Theme.FONT_BODY);
        subtitle.setForeground(Theme.TEXT_SECONDARY);

        JPanel titleGroup = new JPanel();
        titleGroup.setOpaque(false);
        titleGroup.setLayout(new BoxLayout(titleGroup, BoxLayout.Y_AXIS));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        titleGroup.add(title);
        titleGroup.add(Box.createVerticalStrut(4));
        titleGroup.add(subtitle);

        //Refresh button.
        JButton refreshButton = Theme.createSecondaryButton("Refresh");
        refreshButton.addActionListener(e -> refreshReportData());
        headerPanel.add(titleGroup,BorderLayout.WEST);
        headerPanel.add(refreshButton,BorderLayout.EAST);

        //=========================================
        //METRICS
        //=========================================
        JPanel metricsPanel = new JPanel(new GridLayout(2, 3, 16, 16));
        metricsPanel.setOpaque(false);
        reportDepartmentsValue = new JLabel("0");
        reportDoctorsValue = new JLabel("0");
        reportAppointmentsValue = new JLabel("0");
        reportCompletedValue = new JLabel("0");
        reportRostersValue = new JLabel("0");
        reportRevenueValue = new JLabel("RM 0.00");
        metricsPanel.add(createReportMetricCard("Departments", reportDepartmentsValue));
        metricsPanel.add(createReportMetricCard("Doctors", reportDoctorsValue));
        metricsPanel.add(createReportMetricCard("Appointments", reportAppointmentsValue));
        metricsPanel.add(createReportMetricCard("Completed", reportCompletedValue));
        metricsPanel.add(createReportMetricCard("Shift Rosters", reportRostersValue));
        metricsPanel.add(createReportMetricCard("Revenue", reportRevenueValue));

        //=========================================
        //REVENUE SUMMARY CARD
        //=========================================
        JPanel revenueCard = Theme.createCard();
        revenueCard.setLayout(new BorderLayout(0, 12));

        JLabel revenueTitle = new JLabel("Revenue Summary");
        revenueTitle.setFont(Theme.FONT_HEADING);
        revenueTitle.setForeground(Theme.TEXT_PRIMARY);
        reportAverageRevenueValue = new JLabel("RM 0.00");
        reportAverageRevenueValue.setFont(Theme.FONT_HEADING);
        reportAverageRevenueValue.setForeground(Theme.PRIMARY);

        JLabel averageLabel = new JLabel("Average revenue per completed appointment");
        averageLabel.setFont(Theme.FONT_BODY);
        averageLabel.setForeground(Theme.TEXT_SECONDARY);

        JPanel averagePanel = new JPanel();
        averagePanel.setOpaque(false);
        averagePanel.setLayout(new BoxLayout(averagePanel, BoxLayout.Y_AXIS));
        averageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        reportAverageRevenueValue.setAlignmentX(Component.LEFT_ALIGNMENT);
        averagePanel.add(averageLabel);
        averagePanel.add(Box.createVerticalStrut(8));
        averagePanel.add(reportAverageRevenueValue);
        revenueCard.add(revenueTitle, BorderLayout.NORTH);
        revenueCard.add(averagePanel, BorderLayout.CENTER);

        //=========================================
        //CONTENT
        //=========================================
        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        metricsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        revenueCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        metricsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));
        revenueCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
        content.add(headerPanel);
        content.add(Box.createVerticalStrut(20));
        content.add(metricsPanel);
        content.add(Box.createVerticalStrut(20));
        content.add(revenueCard);

        //Load initial data.
        refreshReportData();

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Theme.BG);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createReportMetricCard(String labelText, JLabel valueLabel) {
        JPanel card = Theme.createCard();
        card.setLayout(new BorderLayout(0, 8));

        JLabel label = new JLabel(labelText);
        label.setFont(Theme.FONT_BODY);
        label.setForeground(Theme.TEXT_SECONDARY);

        valueLabel.setFont(Theme.FONT_HEADING);
        valueLabel.setForeground(Theme.PRIMARY);
        card.add(label,BorderLayout.NORTH);
        card.add(valueLabel,BorderLayout.CENTER);
        return card;
    }

    private void refreshReportData() {
        HospitalReportService reportService = new HospitalReportService();
        int totalDepartments = reportService.getTotalDepartments();
        int totalDoctors = reportService.getTotalDoctors();
        int totalAppointments = reportService.getTotalAppointments();
        int completedAppointments = reportService.getCompletedAppointments();
        int totalRosters = reportService.getTotalShiftRosters();
        double totalRevenue = reportService.getTotalConsultationRevenue();
        double averageRevenue = reportService.getAverageRevenuePerCompletedAppointment();

        reportDepartmentsValue.setText(String.valueOf(totalDepartments));
        reportDoctorsValue.setText(String.valueOf(totalDoctors));
        reportAppointmentsValue.setText(String.valueOf(totalAppointments));
        reportCompletedValue.setText(String.valueOf(completedAppointments));
        reportRostersValue.setText(String.valueOf(totalRosters));
        reportRevenueValue.setText(String.format("RM %.2f", totalRevenue));
        reportAverageRevenueValue.setText(String.format("RM %.2f", averageRevenue));
    }

    private void refreshHomeData() {
        DepartmentFile departmentFile = new DepartmentFile();
        DoctorFile doctorFile = new DoctorFile();
        ShiftRosterFile shiftRosterFile = new ShiftRosterFile();

        int myDepartments = departmentFile.findByManagerId(currentUser.getId()).size();
        int myDoctors = doctorFile.findByManagerId(currentUser.getId()).size();

        int myRosters = shiftRosterFile.findByManagerId(currentUser.getId()).size();
        homeDepartmentsValue.setText(String.valueOf(myDepartments));
        homeDoctorsValue.setText(String.valueOf(myDoctors));
        homeRostersValue.setText(String.valueOf(myRosters));
    }

    private void deleteSelectedShiftRoster() {
        int selectedRow = shiftRosterTable.getSelectedRow();

        //No selected row.
        if (selectedRow == -1) {
            Validation.showError(this, "Please select a shift roster to delete.");
            return;
        }

        //Roster ID is stored in column 0.
        String rosterId = shiftRosterTableModel.getValueAt(selectedRow, 0).toString();
        ShiftRosterFile shiftRosterFile = new ShiftRosterFile();
        ShiftRoster roster = shiftRosterFile.findById(rosterId);

        //Record may have been removed from the text file already.
        if (roster == null) {
            Validation.showError(this, "Shift roster record could not be found.");
            loadShiftRosterTable();
            return;
        }

        //=========================================
        //OWNERSHIP CHECK
        //=========================================
        DepartmentFile departmentFile = new DepartmentFile();
        Department department = departmentFile.findById(roster.getDepartmentId());
        if (department == null || !department.getManagerId().equalsIgnoreCase(currentUser.getId())) {
            Validation.showError(this, "You are not allowed to delete this shift roster.");
            return;
        }

        //=========================================
        //CONFIRM DELETE
        //=========================================

        if (!Validation.confirm(this, "Delete shift roster " + rosterId + "?")) {
            return;
        }

        //=========================================
        //DELETE
        //=========================================
        shiftRosterFile.delete(rosterId);
        Validation.showSuccess(this, "Shift roster deleted successfully.");

        //Refresh table.
        loadShiftRosterTable();

        //Refresh Home statistics.
        refreshHomeData();
    }

    private void deleteSelectedDepartment() {
        //=========================================
        // GET SELECTED ROW
        //=========================================
        int selectedRow = departmentTable.getSelectedRow();

        if (selectedRow == -1) {
            Validation.showError(this, "Please select a department to delete.");
            return;
        }

        //Department ID is column 0.
        String departmentId = departmentTableModel.getValueAt(selectedRow, 0).toString();
        DepartmentFile departmentFile = new DepartmentFile();
        Department department = departmentFile.findById(departmentId);

        //Record may have been removed manually from the text file.
        if (department == null) {
            Validation.showError(this, "Department record could not be found.");
            loadDepartmentTable();
            return;
        }

        //=========================================
        //OWNERSHIP CHECK
        //=========================================
        if (!department.getManagerId().equalsIgnoreCase(currentUser.getId())) {
            Validation.showError(this,"You are not allowed to delete this department.");
            return;
        }

        //=========================================
        //CHECK DOCTORS
        //=========================================
        DoctorFile doctorFile = new DoctorFile();
        for (Doctor doctor : doctorFile.getAll()) {
            if (doctor.getDepartmentId().equalsIgnoreCase(departmentId)) {
                Validation.showError(this, "This department cannot be deleted because one or more doctors are still assigned to it.");
                return;
            }
        }

        //=========================================
        //CHECK SHIFT ROSTERS
        //=========================================
        ShiftRosterFile shiftRosterFile = new ShiftRosterFile();
        java.util.List<ShiftRoster> rosters = shiftRosterFile.findByDepartmentId(departmentId);
        if (!rosters.isEmpty()) {
            Validation.showError(this, "This department cannot be deleted because shift rosters still reference it.");
            return;
        }

        //=========================================
        //CONFIRM DELETE
        //=========================================
        if (!Validation.confirm(this, "Delete department " + departmentId + "?")) {
            return;
        }

        //=========================================
        //DELETE
        //=========================================
        departmentFile.delete(departmentId);
        Validation.showSuccess(this, "Department deleted successfully.");

        //Refresh Department table.
        loadDepartmentTable();

        //Refresh Home statistics.
        refreshHomeData();

        //Refresh Report totals too.
        refreshReportData();
    }
}
