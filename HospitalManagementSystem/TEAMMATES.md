# Guide for Teammates (Admin / Manager / Patient)

This project already has the **Doctor module fully working**.  
Your job is to fill in the **scaffold** files for your role.  
UI style, login routing, and `.txt` storage are already connected.

---

## Roles & demo accounts

| Role | Email | Password | Dashboard class |
|------|-------|----------|-----------------|
| Doctor (done) | doctor@hms.com | doctor123 | `DoctorDashboard` |
| Admin Staff | admin@hms.com | admin123 | `AdminDashboard` |
| Medical Manager | manager@hms.com | manager123 | `ManagerDashboard` |
| Patient | ahmad@email.com | patient123 | `PatientDashboard` |

Login is multi-role: `LoginFrame.attemptLogin()` tries Doctor → Admin → Manager → Patient.  
**You do not need to change login** unless you add a completely new role type.

---

## Folder map (what to edit)

```
src/
├── model/
│   ├── AdminStaff.java          ← Admin teammate
│   ├── MedicalManager.java      ← Manager teammate
│   ├── Patient.java             ← Patient teammate (already exists)
│   ├── Department.java          ← Manager / Admin shared
│   ├── HospitalAsset.java       ← Admin (rooms, wards, labs)
│   └── Feedback.java            ← Patient (ratings)
├── dao/
│   ├── AdminFile.java           ← Admin .txt CRUD
│   ├── ManagerFile.java
│   ├── PatientFile.java
│   ├── DepartmentFile.java
│   ├── AssetFile.java
│   └── FeedbackFile.java
└── ui/
    ├── AdminDashboard.java      ← replace placeholder panels
    ├── ManagerDashboard.java
    ├── PatientDashboard.java
    └── (add your *Dialog.java here, copy ProfileDialog style)
```

Data files (auto-created under `data/`):

| File | Used by |
|------|---------|
| admins.txt | Admin |
| managers.txt | Manager |
| patients.txt | Patient |
| departments.txt | Manager / Admin |
| assets.txt | Admin |
| feedback.txt | Patient |
| doctors.txt / appointments.txt / ... | shared (already used by Doctor) |

---

## How to add a feature (minimal steps)

1. **Model** – add fields + update `toFileString()` / `fromFileString()` if needed.  
2. **DAO** – use existing `getAll / save / findById / delete`. Add filters only if required.  
3. **UI panel** – in your `*Dashboard.java`, replace `buildPlaceholderPanel(...)` with a real panel  
   (copy pattern from `DoctorDashboard.buildAppointmentsPanel()`).  
4. **Dialog** – copy `ProfileDialog` / `ConsultationDialog` (scrollable form + Theme buttons).  
5. **Do not change** `Theme.java` colours or `FileManager` paths unless adding a new `.txt` file.

### Example: add “Manage Users” table in Admin

```java
// Inside AdminDashboard – replace USERS placeholder:
contentPanel.add(buildUsersPanel(), "USERS");

private JPanel buildUsersPanel() {
    // 1. DefaultTableModel + JTable (see DoctorDashboard)
    // 2. Load: new DoctorFile().getAll(), new PatientFile().getAll(), ...
    // 3. Buttons open dialogs that call dao.save(...)
}
```

---

## UI rules (keep system consistent)

- Use `Theme.createPrimaryButton`, `Theme.createCard`, `Theme.FONT_*`
- Every long form/dialog: wrap body in `JScrollPane`
- Sidebar + `CardLayout` already set up – only swap panel contents
- Validation: `Validation.showError / showSuccess / confirm`

---

## Assignment feature checklist (from brief)

**Admin Staff**
- [ ] CRUD end users  
- [ ] Assign doctors to Medical Managers  
- [ ] Manage rooms / wards / labs / imaging rooms (`HospitalAsset`)  
- [ ] Configure base consultation rates & insurance networks  

**Medical Manager**
- [ ] Edit profile  
- [ ] Create/update departments (`Department`)  
- [ ] Design doctor shift rosters  
- [ ] View reports (metrics / revenue)  

**Patient**
- [ ] Edit profile  
- [ ] Book / reschedule / cancel appointments  
- [ ] View medical history & prescriptions  
- [ ] Submit ratings (`Feedback`)  

---

## Compile & run

```bash
javac -encoding UTF-8 -d bin $(find src -name "*.java")
java -cp bin Main
```
