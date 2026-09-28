package ui;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.Admin;
import model.Doctor;
import model.HospitalRoom;
import model.HospitalRoomRole;
import model.Insurance;
import model.MedicalManager;
import model.Patient;
import service.AdminService;
import service.InsuranceService;
import service.RoomRoleService;
import service.RoomService;
import util.Theme;


public class AdminDashboard extends JFrame {

    private Admin currentUser;
    private AdminService aS = new AdminService();
    private String selectedUserId;
    private JButton addUser = new JButton();
    private JButton editUser = new JButton();
    private JButton deleteUser = new JButton(); 
    private DefaultTableModel model;
    private List<Doctor> doctorObjectList = new ArrayList<>();                
    private List<MedicalManager> medicalManagerObjectList = new ArrayList<>();
    private int selectedFloorNumber;
    private int selectedRoomId;
    private RoomService rS = new RoomService();
    private RoomRoleService rRS = new RoomRoleService();
    private int selectedRoomRoleId;
    private String roomRoleAction = "update";

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
        manageDoctor.addActionListener(e -> displayManageDoctor(contentPanel));
        manageRoom.addActionListener(e -> displayRoom(contentPanel));

        displayUserTable(contentPanel);
    }

    private void displayRoom(JPanel contentPanel) {
        contentPanel.removeAll();


        for (Component c : contentPanel.getComponents()) {
            if (c instanceof JScrollPane) {
                contentPanel.remove(c);
            }
        }

        JLabel title = new JLabel();
        title.setText("Room management page");
        title.setBounds(10, 5, 220, 30);
        title.setFont(Theme.FONT_HEADING);
        contentPanel.add(title); 

        JLabel selectFloor = new JLabel();
        selectFloor.setText("Select Floor: ");
        selectFloor.setFont(Theme.FONT_BODY);
        selectFloor.setBounds(10, 50, 80, 30);
        contentPanel.add(selectFloor);

        String[] floorsList = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39"};
        JComboBox<String> selectFloorDropDown = new JComboBox<>(floorsList);
        selectFloorDropDown.setBounds(90,52,60,25);
        contentPanel.add(selectFloorDropDown);

        JButton addRoom = new JButton();
        addRoom.setText("Add Room");
        addRoom.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addRoom.setBounds(480,52, 100, 25);
        contentPanel.add(addRoom);

        JButton editRoom = new JButton();
        editRoom.setText("Edit Room");
        editRoom.setCursor(new Cursor(Cursor.HAND_CURSOR));
        editRoom.setBounds(580,52, 100, 25);
        editRoom.setEnabled(false);
        contentPanel.add(editRoom);

        JButton deleteRoom = new JButton();
        deleteRoom.setText("Delete Room");
        deleteRoom.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deleteRoom.setBounds(680,52, 100, 25);
        deleteRoom.setEnabled(false);
        contentPanel.add(deleteRoom);

        JButton viewAssets = new JButton();
        viewAssets.setText("View Room");
        viewAssets.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewAssets.setBounds(780,52, 100, 25);
        viewAssets.setEnabled(false);
        contentPanel.add(viewAssets);

        this.selectedFloorNumber = Integer.parseInt((String) selectFloorDropDown.getSelectedItem());
        String roomHeader = "room_door_number_id,room_designated_name,room_role,room_status";
        String[] roomHeaderList = roomHeader.split(",");
        DefaultTableModel roomModel = new DefaultTableModel(roomHeaderList, 0) {
            @Override 
            public boolean isCellEditable(int r ,  int c) {
                return false;
            }
        };

        List<HospitalRoom> hospitalRoomObjectList = rS.getHospitalRoomList(this.selectedFloorNumber);
        for (HospitalRoom a: hospitalRoomObjectList) {
            roomModel.addRow(new Object[] {a.getDoorNumberId(),a.getRoomName(),a.getRoomRole(),a.getRoomStatus()});
        }

        JTable roomTable = new JTable(roomModel);
        roomTable.setRowHeight(32);
        roomTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = roomTable.getSelectedRow();
            if(selectedRow != -1) {
                Object id = roomTable.getValueAt(selectedRow,0);
                this.selectedRoomId = Integer.parseInt(String.valueOf(id));
                editRoom.setEnabled(true);
                deleteRoom.setEnabled(true);
                viewAssets.setEnabled(true);
            } else {
                editRoom.setEnabled(false);
                deleteRoom.setEnabled(false);
                viewAssets.setEnabled(false);
            }
        });
        JScrollPane roomScrollPane = new JScrollPane(roomTable);
        roomScrollPane.setBounds(5, 85, 870,510);
        contentPanel.add(roomScrollPane);

        selectFloorDropDown.addActionListener(e -> {
            roomModel.setRowCount(0);
            System.out.println(this.selectedFloorNumber);
            this.selectedFloorNumber = Integer.parseInt((String) selectFloorDropDown.getSelectedItem());
            List<HospitalRoom> hospitalRoomObjectList1 = rS.getHospitalRoomList(this.selectedFloorNumber);
            for (HospitalRoom a: hospitalRoomObjectList1) {
                roomModel.addRow(new Object[] {a.getDoorNumberId(),a.getRoomName(),a.getRoomRole(),a.getRoomStatus()});
            }
            editRoom.setEnabled(false);
            deleteRoom.setEnabled(false);
            viewAssets.setEnabled(false);
        });

        addRoom.addActionListener(e -> {    
            displayAddUpdateRoom(contentPanel, "add");
        });

        editRoom.addActionListener(e -> {
            displayAddUpdateRoom(contentPanel, "update");
        });

        deleteRoom.addActionListener(e -> {

        });

        viewAssets.addActionListener(e -> {

        });

        contentPanel.revalidate();
        contentPanel.repaint();
    }
//900 width 600height
    public void displayAddUpdateRoom(JPanel contentPanel, String action) {
        contentPanel.removeAll();

        JLabel roomIdLabel = new JLabel();
        roomIdLabel.setText("Room Number ID:");
        roomIdLabel.setFont(Theme.FONT_BODY);
        roomIdLabel.setBounds(10, 50, 220,30);
        contentPanel.add(roomIdLabel);

        JTextField roomIdField = new JTextField();
        roomIdField.setFont(Theme.FONT_BODY);
        roomIdField.setBounds(10, 90, 220,30);
        contentPanel.add(roomIdField);

        JLabel roomNameLabel = new JLabel();
        roomNameLabel.setText("Room Designated Name:");
        roomNameLabel.setFont(Theme.FONT_BODY);
        roomNameLabel.setBounds(10, 160, 220,30);
        contentPanel.add(roomNameLabel);

        JTextField roomNameField = new JTextField();
        roomNameField.setFont(Theme.FONT_BODY);
        roomNameField.setBounds(10, 200, 220,30);
        contentPanel.add(roomNameField);

        JLabel roomRoleLabel = new JLabel();
        roomRoleLabel.setText("Room Designated Role:");
        roomRoleLabel.setFont(Theme.FONT_BODY);
        roomRoleLabel.setBounds(10, 280, 220,30);
        contentPanel.add(roomRoleLabel);

        JComboBox<String> roomRoleField = new JComboBox<>();
        List<HospitalRoomRole> roomRoleList = rRS.getRoomRoleList();
        for(HospitalRoomRole a: roomRoleList) {
            roomRoleField.addItem(a.getRoomRoleName());
        }
        roomRoleField.setFont(Theme.FONT_BODY);
        roomRoleField.setBounds(10, 320, 220,30);
        contentPanel.add(roomRoleField);

        JLabel roomStatusLabel = new JLabel();
        roomStatusLabel.setText("Room status:");
        roomStatusLabel.setFont(Theme.FONT_BODY);
        roomStatusLabel.setBounds(10, 400, 220,30);
        contentPanel.add(roomStatusLabel);

        String[] booleanList = {"true", "false"};
        JComboBox<String> roomStatusField = new JComboBox<>(booleanList);
        roomStatusField.setFont(Theme.FONT_BODY);
        roomStatusField.setBounds(10, 440, 220,30);
        contentPanel.add(roomStatusField);

        JButton saveButton = new JButton();
        saveButton.setText("Save");
        saveButton.setFont(Theme.FONT_BUTTON);
        saveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveButton.setBounds(60,500,80,30);
        contentPanel.add(saveButton);

        JButton backButton = new JButton();
        backButton.setText("Back");
        backButton.setFont(Theme.FONT_BUTTON);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.setBounds(180,500,80,30);
        contentPanel.add(backButton);

        JButton addRoomRoleButton = new JButton();
        addRoomRoleButton.setText("Add room role");
        addRoomRoleButton.setFont(Theme.FONT_BUTTON);
        addRoomRoleButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addRoomRoleButton.setBounds(250,320,150,30);
        contentPanel.add(addRoomRoleButton);

        if (action.equals("add")) {
            JLabel title = new JLabel();
            title.setText("Add new room");
            title.setFont(Theme.FONT_TITLE);
            title.setBounds(10,5,220,30);
            contentPanel.add(title);

            roomIdField.setText(String.valueOf(rS.getLatestRoomId(this.selectedFloorNumber)));
            roomIdField.setEditable(false);

        } else if (action.equals("update")) {
            List<HospitalRoom> hospitalRoomListTemp = rS.getHospitalRoomList(this.selectedFloorNumber);
            HospitalRoom targetObject = null;
            for (HospitalRoom a: hospitalRoomListTemp) {
                if (a.getDoorNumberId() == this.selectedRoomId) {
                    targetObject = a;
                }
            }
            JLabel title = new JLabel();
            title.setText("Update existing room");
            title.setFont(Theme.FONT_TITLE);
            title.setBounds(10,5,220,30);
            contentPanel.add(title);
            roomIdField.setEditable(false);
            roomIdField.setText(String.valueOf(targetObject.getDoorNumberId()));
            roomNameField.setText(targetObject.getRoomName());
            roomRoleField.setSelectedItem(targetObject.getRoomRole());
            roomStatusField.setSelectedItem(targetObject.getRoomStatus());

        } else {
            return;
        }

        saveButton.addActionListener(e -> {
            int savingRoomId = Integer.parseInt(roomIdField.getText());
            String savingRoomName = roomNameField.getText();
            String savingRoomRole = (String) roomRoleField.getSelectedItem();
            boolean savingRoomStatus = Boolean.parseBoolean((String) roomStatusField.getSelectedItem());

            if (action.equals("add")) {
                String status = rS.appendFile(savingRoomName, savingRoomRole, savingRoomStatus);
            } else if (action.equals("update")) {
                String status = rS.updateFile(action, savingRoomId, savingRoomName, savingRoomRole, savingRoomStatus);
            }
            displayRoom(contentPanel);
        });

        backButton.addActionListener(e -> {
            displayRoom(contentPanel);
        });

        addRoomRoleButton.addActionListener(e -> {
            displayAddRoomRole(contentPanel);
        });

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public void displayAddRoomRole(JPanel contentPanel) {
        contentPanel.removeAll();

        JLabel title = new JLabel();
        title.setText("Room Role Management Page");
        title.setBounds(7, 5, 300, 28);
        title.setFont(Theme.FONT_HEADING);
        contentPanel.add(title); 

        JLabel roomRoleIdLabel = new JLabel();
        roomRoleIdLabel.setText("Room Role ID:");
        roomRoleIdLabel.setFont(Theme.FONT_BODY);
        roomRoleIdLabel.setBounds(10,20, 220, 30);
        contentPanel.add(roomRoleIdLabel);
        
        JTextField roomRoleIdField = new JTextField();
        roomRoleIdField.setBounds(10,50,220,30);
        contentPanel.add(roomRoleIdField);


        JLabel roomRoleNameLabel = new JLabel();
        roomRoleNameLabel.setText("Room Role Name:");
        roomRoleNameLabel.setFont(Theme.FONT_BODY);
        roomRoleNameLabel.setBounds(250,20, 220, 30);
        contentPanel.add(roomRoleNameLabel);

        JTextField roomRoleNameField = new JTextField();
        roomRoleNameField.setBounds(250,50,250,30);
        contentPanel.add(roomRoleNameField);

        JButton saveRoleButton = new JButton();
        saveRoleButton.setText("Save");
        saveRoleButton.setBounds(580, 50, 80, 30);
        contentPanel.add(saveRoleButton);

        JButton addNewRoleButton = new JButton();
        addNewRoleButton.setText("New Role");
        addNewRoleButton.setBounds(670,50,80,30);

        JButton editExistingRoleButton = new JButton();
        editExistingRoleButton.setText("Edit Mode");
        editExistingRoleButton.setBounds(670,50,80,30);
        contentPanel.add(editExistingRoleButton);

        JButton deleteRoleButton = new JButton();
        deleteRoleButton.setText("Delete");
        deleteRoleButton.setBounds(760,50,80,30);

        JButton backButton = new JButton();
        backButton.setText("Back");
        backButton.setBounds(760, 5, 80, 30);
        contentPanel.add(backButton);

        String roleListHeader = "room_role_id,room_role_name";
        String[] roleListHeaderList = roleListHeader.split(",");
        DefaultTableModel roleListModel = new DefaultTableModel(roleListHeaderList, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        JTable roleListTable = new JTable(roleListModel);
        roleListTable.setRowHeight(32);

        JScrollPane roleListScrollPane = new JScrollPane(roleListTable);
        roleListScrollPane.setBounds(5, 90, 880, 550);
        contentPanel.add(roleListScrollPane);

        List<HospitalRoomRole> roomRoleListTemp = rRS.getRoomRoleList();
        JComboBox<String> idForExistingDropDown = new JComboBox<>();
        for (HospitalRoomRole a : roomRoleListTemp) {
            roleListModel.addRow(new Object[]{a.getRoomRoleId(),a.getRoomRoleName()});
            idForExistingDropDown.addItem(String.valueOf(a.getRoomRoleId()));
        }
        idForExistingDropDown.setBounds(10,50,180,30);

        roomRoleIdField.setEditable(false);
        roomRoleIdField.setText(String.valueOf(rRS.getRoomRoleLatestId()));

        idForExistingDropDown.addActionListener(e -> {
            this.selectedRoomRoleId = Integer.parseInt((String) idForExistingDropDown.getSelectedItem());
        });

        saveRoleButton.addActionListener(e -> {
            String newRoomRoleName = roomRoleNameField.getText();
            if (this.roomRoleAction.equals("update")) {
                this.selectedRoomRoleId = Integer.parseInt((String) idForExistingDropDown.getSelectedItem());
                String status = rRS.updateFile(this.roomRoleAction, this.selectedRoomRoleId, newRoomRoleName);
            } else if (this.roomRoleAction.equals("append")) {
                this.selectedRoomRoleId = Integer.parseInt(roomRoleIdField.getText());
                String status = rRS.appendFile(this.selectedRoomRoleId, newRoomRoleName);
            }
            displayAddRoomRole(contentPanel);
        });

        backButton.addActionListener(e -> {
            displayAddUpdateRoom(contentPanel, "add");
        });

        editExistingRoleButton.addActionListener(e -> {
            contentPanel.remove(editExistingRoleButton);
            contentPanel.add(deleteRoleButton);
            contentPanel.add(addNewRoleButton);
            contentPanel.remove(roomRoleIdField);
            contentPanel.add(idForExistingDropDown);
            contentPanel.revalidate();
            contentPanel.repaint();
            this.roomRoleAction = "update";
        });

        deleteRoleButton.addActionListener(e -> {
            this.roomRoleAction = "delete";

            this.selectedRoomRoleId = Integer.parseInt((String) idForExistingDropDown.getSelectedItem());
            String status = rRS.updateFile(this.roomRoleAction, this.selectedRoomRoleId, "");
            roleListModel.setRowCount(0);
            List<HospitalRoomRole> roomRoleListTemp1 = rRS.getRoomRoleList();
            for (HospitalRoomRole a : roomRoleListTemp1) {
                roleListModel.addRow(new Object[]{a.getRoomRoleId(),a.getRoomRoleName()});
                idForExistingDropDown.addItem(String.valueOf(a.getRoomRoleId()));
        }
        });

        addNewRoleButton.addActionListener(e -> {
            this.roomRoleAction = "append";
            contentPanel.add(editExistingRoleButton);
            contentPanel.remove(deleteRoleButton);
            contentPanel.remove(addNewRoleButton);
            contentPanel.add(roomRoleIdField);
            contentPanel.remove(idForExistingDropDown);
            contentPanel.revalidate();
            contentPanel.repaint();

        });

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void displayManageDoctor(JPanel contentPanel) {
        contentPanel.removeAll();
        for (Component c : contentPanel.getComponents()) {
            if (c instanceof JScrollPane) {
                contentPanel.remove(c);
            }
        }
        
        JLabel title = new JLabel();
        title.setText("Doctor management page");
        title.setBounds(10, 5, 220, 30);
        title.setFont(Theme.FONT_HEADING);
        contentPanel.add(title);

        JLabel doctorList = new JLabel();
        doctorList.setText("Doctor selected: ");
        doctorList.setFont(Theme.FONT_BODY);
        doctorList.setBounds(10, 50, 120, 30);
        contentPanel.add(doctorList);


        JComboBox<String> doctorDropDown = new JComboBox<>();
        this.doctorObjectList = aS.getDoctors();
        for(Doctor a: this.doctorObjectList) {
            doctorDropDown.addItem(a.getName());
        }
        doctorDropDown.setBounds(140, 50, 180, 30);
        contentPanel.add(doctorDropDown);

        JLabel managerList = new JLabel();
        managerList.setText("Manager selected: ");
        managerList.setFont(Theme.FONT_BODY);
        managerList.setBounds(330, 50, 120, 30);
        contentPanel.add(managerList);


        JComboBox<String> managerDropDown = new JComboBox<>();
        this.medicalManagerObjectList = aS.getMedicalManager();
        for (MedicalManager a: this.medicalManagerObjectList) {
            managerDropDown.addItem(a.getName());
        }
        managerDropDown.setBounds(470, 50, 200, 30);
        contentPanel.add(managerDropDown);
    
        JButton saveDoctorManager = new JButton();
        saveDoctorManager.setText("Save");
        saveDoctorManager.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveDoctorManager.setBounds(760,52, 80, 30);
        saveDoctorManager.setEnabled(true);
        contentPanel.add(saveDoctorManager);

        String[] doctorHeader = getUserHeader("Doctor");
        DefaultTableModel modelDoctor = new DefaultTableModel(doctorHeader, 0) {
            @Override 
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        String[] medicalManagerHeader = getUserHeader("Medical Manager");
        DefaultTableModel modelMedicalManager = new DefaultTableModel(medicalManagerHeader, 0) {
            @Override 
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        JTable doctorTable = new JTable(modelDoctor);
        doctorTable.setRowHeight(16);

        JScrollPane doctorScrollPane = new JScrollPane(doctorTable);
        doctorScrollPane.setBounds(5, 90, 885, 245);
        contentPanel.add(doctorScrollPane);

        JTable medicalManagerTable = new JTable(modelMedicalManager);
        medicalManagerTable.setRowHeight(16);

        JScrollPane medicalManagerScrollPane = new JScrollPane(medicalManagerTable);
        medicalManagerScrollPane.setBounds(5, 350 , 885, 245);
        contentPanel.add(medicalManagerScrollPane);

        this.doctorObjectList = aS.getDoctors();
        for(Doctor a: this.doctorObjectList) {
            modelDoctor.addRow(new Object[]{a.getId(),a.getName(),a.getEmail(),a.getPhone(),a.getPassword(),a.getRole(),a.getSpecialty(),a.getDepartmentId(),a.getManagerId(),a.getConsultationFee(),a.getShift(),a.isActive()});
        }

        this.medicalManagerObjectList = aS.getMedicalManager();
        for (MedicalManager a: this.medicalManagerObjectList) {
            modelMedicalManager.addRow(new Object[] {a.getId(),a.getName(),a.getEmail(),a.getPhone(),a.getPassword(),a.getRole(),a.getManagedDepartmentId(),a.isActive()});
        }

        saveDoctorManager.addActionListener(e -> {
            String selectedDocName = (String) doctorDropDown.getSelectedItem();
            String selectedMgrName = (String) managerDropDown.getSelectedItem();
            this.doctorObjectList = aS.getDoctors();
            this.medicalManagerObjectList = aS.getMedicalManager();

            Doctor selectedDoctor = null;
            for (Doctor a : this.doctorObjectList) {
                if (a.getName().equals(selectedDocName)) {
                    selectedDoctor = a;
                    break;
                }
            }
            String selectedManagerId = null;
            for (MedicalManager a : this.medicalManagerObjectList) {
                if (a.getName().equals(selectedMgrName)) {
                    selectedManagerId = a.getId();
                    break;
                }
            }
            if (selectedDoctor != null && selectedManagerId != null) {
                String status = aS.saveDoctor(selectedDoctor.getId(),selectedDoctor.getName(),selectedDoctor.getEmail(),selectedDoctor.getPhone(),selectedDoctor.getPassword(),selectedDoctor.getSpecialty(),selectedDoctor.getDepartmentId(),selectedManagerId,selectedDoctor.getConsultationFee(),selectedDoctor.getShift());            
            }
            displayManageDoctor(contentPanel);
        });

        contentPanel.revalidate();
        contentPanel.repaint();
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

        addUser.setText("Add User");
        addUser.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addUser.setBounds(580,52, 100, 25);
        contentPanel.add(addUser);
       
        editUser.setText("Edit User");
        editUser.setCursor(new Cursor(Cursor.HAND_CURSOR));
        editUser.setBounds(680,52, 100, 25);
        editUser.setEnabled(false);
        contentPanel.add(editUser);

        deleteUser.setText("Delete User");
        deleteUser.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deleteUser.setBounds(780,52, 100, 25);
        deleteUser.setEnabled(false);
        contentPanel.add(deleteUser);

        for (java.awt.event.ActionListener al : addUser.getActionListeners()) addUser.removeActionListener(al);
        for (java.awt.event.ActionListener al : editUser.getActionListeners()) editUser.removeActionListener(al);
        for (java.awt.event.ActionListener al : deleteUser.getActionListeners()) deleteUser.removeActionListener(al);

        String userRole = (String) rolesDropDown.getSelectedItem();
        loadTable(contentPanel, userRole);


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
        editUser.setEnabled(false);
        deleteUser.setEnabled(false);

        for (java.awt.event.ActionListener al : addUser.getActionListeners()) addUser.removeActionListener(al);
        for (java.awt.event.ActionListener al : editUser.getActionListeners()) editUser.removeActionListener(al);
        for (java.awt.event.ActionListener al : deleteUser.getActionListeners()) deleteUser.removeActionListener(al);

        for (Component c : contentPanel.getComponents()) {
            if (c instanceof JScrollPane) {
                contentPanel.remove(c);
            }
        }

        String[] headerList = getUserHeader(userRole);
        model = new DefaultTableModel(headerList, 0) {
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
        userTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = userTable.getSelectedRow();
            if(selectedRow != -1) {
                Object id = userTable.getValueAt(selectedRow,0);
                this.selectedUserId = String.valueOf(id);
                editUser.setEnabled(true);
                deleteUser.setEnabled(true);
            } else {
                editUser.setEnabled(false);
                deleteUser.setEnabled(false);
            }
        });

        JScrollPane scrollPaneTable = new JScrollPane(userTable);
        scrollPaneTable.setBounds(10, 85, 870, 500); 
        contentPanel.add(scrollPaneTable);

        addUser.addActionListener(e -> {
            System.out.println(userRole);
            addUpdateUserForm(contentPanel, userRole, this.selectedUserId, "add");
        });
        editUser.addActionListener(e -> {
            System.out.println(userRole);
            addUpdateUserForm(contentPanel, userRole, this.selectedUserId, "update");
        });
        deleteUser.addActionListener(e -> {
            String action = "delete";
            String status = "";

            if (userRole.equals("Admin")) {
                status = aS.writeAdmin(action, Integer.parseInt(this.selectedUserId), "", "", "", false, "", "", 0);
            } else if (userRole.equals("Medical Manager")) {
                status = aS.deleteMedicalManager(this.selectedUserId);
            } else if (userRole.equals("Doctor")) {
                status = aS.deleteDoctor(this.selectedUserId);
            } else if (userRole.equals("Patient")) {
                status = aS.removePatient(this.selectedUserId);
            } else {
                status = "Delete action not successful";
            }

            JLabel statusCRUD = new JLabel();
            statusCRUD.setText(status);
            statusCRUD.setBounds(700,7,160,30);
            statusCRUD.setFont(Theme.FONT_BODY);
            contentPanel.add(statusCRUD);
            if (status.equals("Delete action not successful") || status.equals("Admin file not found") || status.equals("Error updating the admin list")) {
                statusCRUD.setForeground(new Color(255,0,0));
            } else {
                statusCRUD.setForeground(new Color(0,255,0)); 
            }

        });        

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void addUpdateUserForm(JPanel contentPanel, String userRole, String selectedId, String action) {
        contentPanel.removeAll();

        JLabel formTitle = new JLabel(action.toUpperCase() + " " + userRole.toUpperCase() + " ACCOUNT");
        formTitle.setFont(Theme.FONT_HEADING);
        formTitle.setBounds(10, 5, 500, 30);
        contentPanel.add(formTitle);

        JLabel lbl1 = new JLabel("Name / User:"); lbl1.setBounds(10, 60, 120, 25); contentPanel.add(lbl1);
        JTextField txtName = new JTextField(); txtName.setBounds(140, 60, 250, 25); contentPanel.add(txtName);

        JLabel lbl2 = new JLabel("Email Address:"); lbl2.setBounds(10, 100, 120, 25); contentPanel.add(lbl2);
        JTextField txtEmail = new JTextField(); txtEmail.setBounds(140, 100, 250, 25); contentPanel.add(txtEmail);

        JLabel lbl3 = new JLabel("Password:"); lbl3.setBounds(10, 140, 120, 25); contentPanel.add(lbl3);
        JTextField txtPass = new JTextField(); txtPass.setBounds(140, 140, 250, 25); contentPanel.add(txtPass);

        JLabel lbl4 = new JLabel("Phone Number:"); lbl4.setBounds(10, 180, 120, 25); contentPanel.add(lbl4);
        JTextField txtPhone = new JTextField(); txtPhone.setBounds(140, 180, 250, 25); contentPanel.add(txtPhone);

        JTextField txtAdminFirstName = new JTextField();
        JTextField txtAdminLastName = new JTextField();
        JTextField txtAdminSalary = new JTextField();

        dao.DepartmentFile deptFile = new dao.DepartmentFile();
        List<model.Department> deptList = deptFile.getAll();
        List<MedicalManager> mgrList = aS.getMedicalManager();

        JComboBox<String> cmbMgrDept = new JComboBox<>();
        JComboBox<String> cmbDocDept = new JComboBox<>();
        for (model.Department d : deptList) {
            String deptEntry = d.getDepartmentId() + " - " + d.getName();
            cmbMgrDept.addItem(deptEntry);
            cmbDocDept.addItem(deptEntry);
        }

        JComboBox<String> cmbDocMgr = new JComboBox<>();
        for (MedicalManager m : mgrList) {
            cmbDocMgr.addItem(m.getId() + " - " + m.getName());
        }

        JTextField txtDocSpecialty = new JTextField();
        JTextField txtDocFee = new JTextField();
        JComboBox<String> cmbDocShift = new JComboBox<>(new String[]{"Morning", "Afternoon", "Night"});

        JComboBox<String> cmbPatBlood = new JComboBox<>(new String[]{"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"});
        JTextField txtPatAllergies = new JTextField();
        JTextField txtPatEmergency = new JTextField();
        JComboBox<String> cmbPatInsurance = new JComboBox<>();

        InsuranceService iS = new InsuranceService();
        for (Insurance ins : iS.getInsuranceList()) {
            cmbPatInsurance.addItem(ins.getInsuranceName());
        }
        if (cmbPatInsurance.getItemCount() == 0) {
            cmbPatInsurance.addItem("None");
        }

        int currentY = 220;

        switch (userRole) {
            case "Admin":
                lbl4.setVisible(false);
                txtPhone.setVisible(false);
                currentY = 180;
                
                JLabel lblAdmin1 = new JLabel("First Name:"); lblAdmin1.setBounds(10, currentY, 120, 25);
                txtAdminFirstName.setBounds(140, currentY, 250, 25);
                contentPanel.add(lblAdmin1); contentPanel.add(txtAdminFirstName);
                currentY += 40;

                JLabel lblAdmin2 = new JLabel("Last Name:"); lblAdmin2.setBounds(10, currentY, 120, 25);
                txtAdminLastName.setBounds(140, currentY, 250, 25);
                contentPanel.add(lblAdmin2); contentPanel.add(txtAdminLastName);
                currentY += 40;

                JLabel lblAdmin3 = new JLabel("Salary (RM):"); lblAdmin3.setBounds(10, currentY, 120, 25);
                txtAdminSalary.setBounds(140, currentY, 250, 25);
                contentPanel.add(lblAdmin3); contentPanel.add(txtAdminSalary);
                currentY += 40;
                break;
                
            case "Medical Manager":
                JLabel lblMgr1 = new JLabel("Department:"); lblMgr1.setBounds(10, currentY, 120, 25);
                cmbMgrDept.setBounds(140, currentY, 250, 25);
                contentPanel.add(lblMgr1); contentPanel.add(cmbMgrDept);
                currentY += 40;
                break;
                
            case "Doctor":
                JLabel lblDoc1 = new JLabel("Specialty:"); lblDoc1.setBounds(10, currentY, 120, 25);
                txtDocSpecialty.setBounds(140, currentY, 250, 25);
                contentPanel.add(lblDoc1); contentPanel.add(txtDocSpecialty);
                currentY += 40;

                JLabel lblDoc2 = new JLabel("Department:"); lblDoc2.setBounds(10, currentY, 120, 25);
                cmbDocDept.setBounds(140, currentY, 250, 25);
                contentPanel.add(lblDoc2); contentPanel.add(cmbDocDept);
                currentY += 40;

                JLabel lblDoc3 = new JLabel("Manager:"); lblDoc3.setBounds(10, currentY, 120, 25);
                cmbDocMgr.setBounds(140, currentY, 250, 25);
                contentPanel.add(lblDoc3); contentPanel.add(cmbDocMgr);
                currentY += 40;

                JLabel lblDoc4 = new JLabel("Consultation Fee:"); lblDoc4.setBounds(10, currentY, 120, 25);
                txtDocFee.setBounds(140, currentY, 250, 25);
                contentPanel.add(lblDoc4); contentPanel.add(txtDocFee);
                currentY += 40;
                
                JLabel lblDoc5 = new JLabel("Shift Time:"); lblDoc5.setBounds(10, currentY, 120, 25);
                cmbDocShift.setBounds(140, currentY, 250, 25);
                contentPanel.add(lblDoc5); contentPanel.add(cmbDocShift);
                currentY += 40;
                break;
                
            case "Patient":
                JLabel lblPat1 = new JLabel("Blood Type:"); lblPat1.setBounds(10, currentY, 120, 25);
                cmbPatBlood.setBounds(140, currentY, 250, 25);
                contentPanel.add(lblPat1); contentPanel.add(cmbPatBlood);
                currentY += 40;

                JLabel lblPat2 = new JLabel("Allergies:"); lblPat2.setBounds(10, currentY, 120, 25);
                txtPatAllergies.setBounds(140, currentY, 250, 25);
                contentPanel.add(lblPat2); contentPanel.add(txtPatAllergies);
                currentY += 40;

                JLabel lblPat3 = new JLabel("Insurance:"); lblPat3.setBounds(10, currentY, 120, 25);
                cmbPatInsurance.setBounds(140, currentY, 250, 25);
                contentPanel.add(lblPat3); contentPanel.add(cmbPatInsurance);
                currentY += 40;

                JLabel lblPat4 = new JLabel("Emergency Contact:"); lblPat4.setBounds(10, currentY, 120, 25);
                txtPatEmergency.setBounds(140, currentY, 250, 25);
                contentPanel.add(lblPat4); contentPanel.add(txtPatEmergency);
                currentY += 40;
                break;
        }

        if (action.equals("update") && selectedId != null) {
            if (userRole.equals("Admin")) {
                for (model.Admin a : aS.getAdminList()) {
                    if (String.valueOf(a.getUserId()).equals(selectedId)) {
                        txtName.setText(a.getUserName()); 
                        txtEmail.setText(a.getUserEmail());
                        txtPass.setText(a.getUserHashPassword()); 
                        txtAdminFirstName.setText(a.getAdminFirstName());
                        txtAdminLastName.setText(a.getAdminLastName());
                        txtAdminSalary.setText(String.valueOf(a.getAdminSalary())); 
                        break;
                    }
                }
            } else if (userRole.equals("Medical Manager")) {
                for (model.MedicalManager m : aS.getMedicalManager()) {
                    if (m.getId().equals(selectedId)) {
                        txtName.setText(m.getName());
                        txtEmail.setText(m.getEmail());
                        txtPass.setText(m.getPassword());
                        txtPhone.setText(m.getPhone());
                        String deptId = m.getManagedDepartmentId();
                        for (int i = 0; i < cmbMgrDept.getItemCount(); i++) {
                            if (cmbMgrDept.getItemAt(i).startsWith(deptId + " - ")) {
                                cmbMgrDept.setSelectedIndex(i);
                                break;
                            }
                        }
                        break;
                    }
                }
            } else if (userRole.equals("Doctor")) {
                for (model.Doctor d : aS.getDoctors()) {
                    if (d.getId().equals(selectedId)) {
                        txtName.setText(d.getName());
                        txtEmail.setText(d.getEmail());
                        txtPass.setText(d.getPassword());
                        txtPhone.setText(d.getPhone());
                        txtDocSpecialty.setText(d.getSpecialty());
                        String deptId = d.getDepartmentId();
                        for (int i = 0; i < cmbDocDept.getItemCount(); i++) {
                            if (cmbDocDept.getItemAt(i).startsWith(deptId + " - ")) {
                                cmbDocDept.setSelectedIndex(i);
                                break;
                            }
                        }

                        String mgrId = d.getManagerId();
                        for (int i = 0; i < cmbDocMgr.getItemCount(); i++) {
                            if (cmbDocMgr.getItemAt(i).startsWith(mgrId + " - ")) {
                                cmbDocMgr.setSelectedIndex(i);
                                break;
                            }
                        }

                        txtDocFee.setText(String.valueOf(d.getConsultationFee()));
                        cmbDocShift.setSelectedItem(d.getShift());
                        break;
                    }
                }
            } else if (userRole.equals("Patient")) {
                for (model.Patient p : aS.getPatient()) {
                    if (p.getId().equals(selectedId)) {
                        txtName.setText(p.getName());
                        txtEmail.setText(p.getEmail());
                        txtPass.setText(p.getPassword());
                        txtPhone.setText(p.getPhone());
                        cmbPatBlood.setSelectedItem(p.getBloodType());
                        txtPatAllergies.setText(p.getAllergies());
                        cmbPatInsurance.setSelectedItem(p.getInsuranceProvider());
                        txtPatEmergency.setText(p.getEmergencyContact());
                        break;
                    }
                }
            }
        }

        JButton btnSubmit = new JButton(action.equals("add") ? "Save Entry" : "Apply Changes");
        btnSubmit.setBounds(140, currentY + 10, 120, 30);
        btnSubmit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnSubmit.addActionListener(e -> {
            String mgrDeptId = (cmbMgrDept.getSelectedItem() != null) ? cmbMgrDept.getSelectedItem().toString().split(" - ")[0] : "";
            String docDeptId = (cmbDocDept.getSelectedItem() != null) ? cmbDocDept.getSelectedItem().toString().split(" - ")[0] : "";
            String docMgrId = (cmbDocMgr.getSelectedItem() != null) ? cmbDocMgr.getSelectedItem().toString().split(" - ")[0] : "";

            if (action.equals("add")) {
                if (userRole.equals("Admin")) {
                    int nextId = aS.getLatestAdminId();
                    aS.appendAdmin(nextId, txtName.getText(), txtEmail.getText(), txtPass.getText(), true, txtAdminFirstName.getText(), txtAdminLastName.getText(), Double.parseDouble(txtAdminSalary.getText()));
                } else if (userRole.equals("Medical Manager")) {
                    String nextMgrId = "MGR-" + String.format("%03d", aS.getMedicalManager().size() + 1);
                    aS.saveMedicalManager(nextMgrId, txtName.getText(), txtEmail.getText(), txtPhone.getText(), txtPass.getText(), mgrDeptId);
                } else if (userRole.equals("Doctor")) {
                    String nextDocId = "DOC-" + String.format("%03d", aS.getDoctors().size() + 1);
                    aS.saveDoctor(nextDocId, txtName.getText(), txtEmail.getText(), txtPhone.getText(), txtPass.getText(), txtDocSpecialty.getText(), docDeptId, docMgrId, Double.parseDouble(txtDocFee.getText()), cmbDocShift.getSelectedItem().toString());
                } else if (userRole.equals("Patient")) {
                    String nextPatId = "PAT-" + String.format("%03d", aS.getPatient().size() + 1);
                    aS.addPatient(nextPatId, txtName.getText(), txtEmail.getText(), txtPhone.getText(), txtPass.getText(), cmbPatBlood.getSelectedItem().toString(), txtPatAllergies.getText(), cmbPatInsurance.getSelectedItem().toString(), txtPatEmergency.getText());
                }
            } else {
                if (userRole.equals("Admin")) {
                    aS.writeAdmin("update", Integer.parseInt(selectedId), txtName.getText(), txtEmail.getText(), txtPass.getText(), true, txtAdminFirstName.getText(), txtAdminLastName.getText(), Double.parseDouble(txtAdminSalary.getText()));
                } else if (userRole.equals("Medical Manager")) {
                    aS.saveMedicalManager(selectedId, txtName.getText(), txtEmail.getText(), txtPhone.getText(), txtPass.getText(), mgrDeptId);
                } else if (userRole.equals("Doctor")) {
                    aS.saveDoctor(selectedId, txtName.getText(), txtEmail.getText(), txtPhone.getText(), txtPass.getText(), txtDocSpecialty.getText(), docDeptId, docMgrId, Double.parseDouble(txtDocFee.getText()), cmbDocShift.getSelectedItem().toString());
                } else if (userRole.equals("Patient")) {
                    aS.addPatient(selectedId, txtName.getText(), txtEmail.getText(), txtPhone.getText(), txtPass.getText(), cmbPatBlood.getSelectedItem().toString(), txtPatAllergies.getText(), cmbPatInsurance.getSelectedItem().toString(), txtPatEmergency.getText());
                }
            }
            displayUserTable(contentPanel);
        });
        contentPanel.add(btnSubmit);

        JButton btnBack = new JButton("Cancel");
        btnBack.setBounds(270, currentY + 10, 100, 30);
        btnBack.addActionListener(e -> displayUserTable(contentPanel));
        contentPanel.add(btnBack);

        contentPanel.revalidate();
        contentPanel.repaint();
    }
}
