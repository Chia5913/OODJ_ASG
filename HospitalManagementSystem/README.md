# Hospital Management System (HMS) – Doctor Module

APU Medical Centre | Object-Oriented Programming Coursework  
AI Usage Level: Yellow (Restricted)

## How to Run

```bash
cd HospitalManagementSystem
javac -encoding UTF-8 -d bin $(find src -name "*.java")
java -cp bin Main
```

Or open the `src` folder in IntelliJ / NetBeans / Eclipse and run `Main.java`.

## Demo Login

| Role   | Email            | Password   |
|--------|------------------|------------|
| Doctor | doctor@hms.com   | doctor123  |
| Doctor | doctor2@hms.com  | doctor123  |

## Data Storage (.txt files only – assignment requirement)

All data is stored in plain text files under the `data/` folder (created automatically on first run).  
**No database** (no Access, Oracle, SQL Server, etc.).

| File | Content |
|------|---------|
| `data/doctors.txt` | Doctor profiles |
| `data/patients.txt` | Patient records |
| `data/appointments.txt` | Appointments & status history |
| `data/consultations.txt` | Consultation notes / vitals |
| `data/prescriptions.txt` | Prescriptions |
| `data/lab_requests.txt` | Lab / imaging requests |

### Format

- One record per line  
- Fields separated by `|`  
- First line is a `#` comment header (ignored by the program) so you can edit files by hand  

**Example `doctors.txt`:**
```text
# id|name|email|phone|password|role|specialty|departmentId|managerId|fee|shift|active
DOC-001|Aisha Rahman|doctor@hms.com|012-3456789|doctor123|DOCTOR|Cardiology|DEPT-CARD|MGR-001|150.0|Morning|true
```

### Manual edit

1. Close the application (or finish the current action).  
2. Open the `.txt` file in Notepad / VS Code.  
3. Change values, add or delete lines (keep the same number of `|` fields).  
4. Save the file.  
5. Run the app again – it will load your changes.

## Features (Doctor Role)

- Modern Swing GUI (sidebar + pop-up dialogs, scrollable forms)
- Login with validation
- View / update appointment status
- Log consultation (vital signs + clinical notes + diagnosis)
- Issue digital prescriptions
- Submit lab / imaging requests
- Edit personal profile
- All changes persisted to `.txt` files immediately

## Project Structure

```
HospitalManagementSystem/
├── src/
│   ├── Main.java
│   ├── model/     Person, Doctor, Patient, Appointment, Consultation, Prescription, LabRequest
│   ├── dao/       File-based DAOs (text only)
│   ├── ui/        LoginFrame, DoctorDashboard, Dialogs
│   └── util/      FileManager, Theme, Validation
└── data/          auto-created .txt files at runtime
```

## OOP Concepts

| Concept        | Where |
|----------------|--------|
| Abstraction    | `Person` abstract + `getDisplayInfo()` |
| Inheritance    | `Doctor` / `Patient` extend `Person` |
| Encapsulation  | Private fields + getters/setters |
| Polymorphism   | `getDisplayInfo()` overridden in subclasses |
