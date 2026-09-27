package ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.Admin;
import model.Doctor;
import model.MedicalManager;
import model.Patient;
import service.AdminService;
import util.Theme;


public class AdminDashboard extends JFrame {

    private Admin currentUser;
    private AdminService aS = new AdminService();

    public AdminDashboard(Admin user) {
        this.currentUser = user;

        setTitle("Admin Portal – " + user.getAdminFirstName() + " " + user.getAdminLastName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setMinimumSize(new Dimension(900, 600));
        setLayout(null);
        setResizable(false);
        
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(51, 65, 85));
        headerPanel.setBounds(0,0,1100,55);
        headerPanel.setLayout(null);
        add(headerPanel);

        JLabel headerLeftLabel = new JLabel();
        headerLeftLabel.setText("HMS" + " " + "-" + " " + user.getAdminFirstName() + " " + user.getAdminLastName());
        headerLeftLabel.setForeground(new Color(255,255,255));
        headerLeftLabel.setFont(Theme.FONT_TITLE);
        headerLeftLabel.setBounds(20,15,300,20);
        headerPanel.add(headerLeftLabel);

        JButton logoutButton = new JButton();
        logoutButton.setText("Log out");
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.setBounds(980,17,90,20);
        logoutButton.addActionListener( e -> {dispose(); SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));});
        headerPanel.add(logoutButton);

        JPanel navPanel = new JPanel();
        navPanel.setBackground(new Color(0x335255));
        navPanel.setBounds(0,55,200,645);
        navPanel.setLayout(null);
        add(navPanel);

        JButton userManagement = new JButton();
        userManagement.setText("Manage Users");
        userManagement.setBounds(20, 40, 157,34);
        userManagement.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        navPanel.add(userManagement);

        JButton manageDoctor = new JButton();
        manageDoctor.setText("Manage Doctors");
        manageDoctor.setBounds(20, 124, 157,34);
        manageDoctor.setCursor(new Cursor(Cursor.HAND_CURSOR));
        navPanel.add(manageDoctor);   

        JButton manageRoom = new JButton();
        manageRoom.setText("Manage Rooms & Assets");
        manageRoom.setBounds(20, 208, 157,34);
        manageRoom.setCursor(new Cursor(Cursor.HAND_CURSOR));
        navPanel.add(manageRoom);

        JButton manageInsurance = new JButton();
        manageInsurance.setText("Insurance & Base Rate");
        manageInsurance.setBounds(20, 297, 157,34);
        manageInsurance.setCursor(new Cursor(Cursor.HAND_CURSOR));
        navPanel.add(manageInsurance);

        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(Theme.BG);
        contentPanel.setBounds(200,55,900,645);
        contentPanel.setLayout(null);
        add(contentPanel);
        
        userManagement.addActionListener(e -> displayUserTable(contentPanel));

        displayUserTable(contentPanel);
    }

    private void displayUserTable(JPanel contentPanel) {
        contentPanel.removeAll();

        JLabel title = new JLabel();
        title.setText("User management page");
        title.setBounds(10, 5, 220, 30);
        title.setFont(Theme.FONT_HEADING);
        contentPanel.add(title);

        JLabel roleType = new JLabel();
        roleType.setText("Role selected: ");
        roleType.setFont(Theme.FONT_BODY);
        roleType.setBounds(10, 50, 100, 30);
        contentPanel.add(roleType);

        String[] roles = {"Admin","Medical Manager","Doctor","Patient"};
        JComboBox<String> rolesDropDown = new JComboBox<>(roles);
        rolesDropDown.setBounds(110,52, 140,25 );
        contentPanel.add(rolesDropDown);


        JButton addUser = new JButton();
        addUser.setText("Add User");
        addUser.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addUser.setBounds(580,52, 100, 25);
        contentPanel.add(addUser);

        JButton editUser = new JButton();
        editUser.setText("Edit User");
        editUser.setCursor(new Cursor(Cursor.HAND_CURSOR));
        editUser.setBounds(680,52, 100, 25);
        editUser.setEnabled(false);
        contentPanel.add(editUser);

        JButton deleteUser = new JButton();
        deleteUser.setText("Delete User");
        deleteUser.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deleteUser.setBounds(780,52, 100, 25);
        deleteUser.setEnabled(false);
        contentPanel.add(deleteUser);

        String userRole = (String) rolesDropDown.getSelectedItem();
        loadTable(contentPanel, userRole);

        addUser.addActionListener(e -> {});
        editUser.addActionListener(e -> {});
        deleteUser.addActionListener(e -> {});
        rolesDropDown.addActionListener(e -> loadTable(contentPanel, (String) rolesDropDown.getSelectedItem()));

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private String[] getUserHeader(String userRole) {
        String[] userHeader;
        if (userRole.equals("Admin")) {
            String header = "user_id,user_name,user_email,user_hash_password,admin_first_name,admin_last_name,admin_salary,is_active";
            userHeader = header.split(",");
            return userHeader;
        } else if (userRole.equals("Medical Manager")) {
            String header = "id|name|email|phone|password|role|managedDepartmentId|active";
            userHeader = header.split("\\|");
            return userHeader;
        } else if (userRole.equals("Doctor")) {
            String header = "id|name|email|phone|password|role|specialty|departmentId|managerId|fee|shift|active";
            userHeader = header.split("\\|");
            return userHeader;            
        } else if (userRole.equals("Patient")) {
            String header = "id|name|email|phone|password|role|bloodType|allergies|insuranceProvider|emergencyContact";
            userHeader = header.split("\\|");
            return userHeader;
        } else {
            String header = "user_id,user_name,user_email,user_hash_password,admin_first_name,admin_last_name,admin_salary,is_active";
            userHeader = header.split(",");
            return userHeader;
        }
    }

    private void loadTable(JPanel contentPanel, String userRole) {
        for (Component c : contentPanel.getComponents()) {
            if (c instanceof JScrollPane) {
                contentPanel.remove(c);
            }
        }

        String[] headerList = getUserHeader(userRole);
        DefaultTableModel model = new DefaultTableModel(headerList, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }            
        };

        if (userRole.equals("Admin")) {
            for (Admin a: aS.getAdminList()) {
                model.addRow(new Object[]{a.getUserId(),a.getUserName(),a.getUserEmail(),a.getUserHashPassword(),a.getAdminFirstName(),a.getAdminLastName(),a.getAdminSalary(),a.getIsActive()});
            }
        } else if (userRole.equals("Medical Manager")) {
            for (MedicalManager a: aS.getMedicalManager()) {
                model.addRow(new Object[]{a.getId(),a.getName(),a.getEmail(),a.getPhone(),a.getPassword(),a.getRole(),a.getManagedDepartmentId(),a.isActive()});
            }
        } else if (userRole.equals("Doctor")) {
            for (Doctor a: aS.getDoctors()) {
                model.addRow(new Object[]{a.getId(),a.getName(),a.getEmail(),a.getPhone(),a.getPassword(),a.getRole(),a.getSpecialty(),a.getDepartmentId(),a.getManagerId(),a.getConsultationFee(),a.getShift(),a.isActive()});
            }
           
        } else if (userRole.equals("Patient")) {
            for (Patient a: aS.getPatient()) {
                model.addRow(new Object[]{a.getId(),a.getName(),a.getEmail(),a.getPhone(),a.getPassword(),a.getRole(),a.getBloodType(),a.getAllergies(),a.getInsuranceProvider(),a.getEmergencyContact()});
            }
        } else {
            for (Admin a: aS.getAdminList()) {
                model.addRow(new Object[]{a.getUserId(),a.getUserName(),a.getUserEmail(),a.getUserHashPassword(),a.getAdminFirstName(),a.getAdminLastName(),a.getAdminSalary(),a.getIsActive()});
            }
        }

        JTable userTable = new JTable(model);
        userTable.setRowHeight(32);
        userTable.setFont(Theme.FONT_BODY);

        JScrollPane scrollPaneTable = new JScrollPane(userTable);
        scrollPaneTable.setBounds(10, 85, 870, 500); 
        contentPanel.add(scrollPaneTable);

        contentPanel.revalidate();
        contentPanel.repaint();
    }

}
