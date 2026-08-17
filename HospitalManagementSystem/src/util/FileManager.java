package util;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Central utility for all text-file read/write operations.
 *
 * All HMS data is stored as plain .txt files under the "data/" folder.
 * Format: one record per line, fields separated by the pipe character '|'.
 * Lines starting with '#' are comments (ignored when reading) so you can
 * manually edit the files in Notepad / any text editor safely.
 *
 * Example doctors.txt:
 *   # id|name|email|phone|password|role|specialty|departmentId|managerId|fee|shift|active
 *   DOC-001|Aisha Rahman|doctor@hms.com|012-3456789|doctor123|DOCTOR|Cardiology|DEPT-CARD|MGR-001|150.0|Morning|true
 */
public class FileManager {

    public static final String DATA_DIR = "data";

    public static final String DOCTORS_FILE       = DATA_DIR + "/doctors.txt";
    public static final String PATIENTS_FILE      = DATA_DIR + "/patients.txt";
    public static final String APPOINTMENTS_FILE  = DATA_DIR + "/appointments.txt";
    public static final String CONSULTATIONS_FILE = DATA_DIR + "/consultations.txt";
    public static final String PRESCRIPTIONS_FILE = DATA_DIR + "/prescriptions.txt";
    public static final String LAB_REQUESTS_FILE  = DATA_DIR + "/lab_requests.txt";
    public static final String ADMINS_FILE         = DATA_DIR + "/admins.txt";
    public static final String MANAGERS_FILE       = DATA_DIR + "/managers.txt";
    public static final String DEPARTMENTS_FILE    = DATA_DIR + "/departments.txt";
    public static final String SHIFT_ROSTERS_FILE = DATA_DIR + "/shift_rosters.txt";
    public static final String ASSETS_FILE         = DATA_DIR + "/assets.txt";
    public static final String FEEDBACK_FILE       = DATA_DIR + "/feedback.txt";

    /** Header comment lines written at the top of each data file (for manual editing). */
    public static final String HEADER_DOCTORS =
            "# id|name|email|phone|password|role|specialty|departmentId|managerId|fee|shift|active";
    public static final String HEADER_PATIENTS =
            "# id|name|email|phone|password|role|bloodType|allergies|insuranceProvider|emergencyContact";
    public static final String HEADER_APPOINTMENTS =
            "# appointmentId|patientId|doctorId|date|time|status|notes";
    public static final String HEADER_CONSULTATIONS =
            "# consultationId|appointmentId|patientId|doctorId|dateTime|vitalSigns|clinicalNotes|diagnosis";
    public static final String HEADER_PRESCRIPTIONS =
            "# prescriptionId|consultationId|patientId|doctorId|medication|dosage|duration|instructions|dateIssued";
    public static final String HEADER_LAB_REQUESTS =
            "# requestId|consultationId|patientId|doctorId|testType|urgency|clinicalInfo|status|dateRequested";
    public static final String HEADER_ADMINS =
            "# id|name|email|phone|password|role|staffPosition|active";
    public static final String HEADER_MANAGERS =
            "# id|name|email|phone|password|role|managedDepartmentId|active";
    public static final String HEADER_DEPARTMENTS =
            "# departmentId|name|description|managerId";
    public static final String HEADER_SHIFT_ROSTERS =
        "# rosterId|doctorId|departmentId|shiftDate|startTime|endTime|shiftType";
    public static final String HEADER_ASSETS =
            "# assetId|name|type|location|status";
    public static final String HEADER_FEEDBACK =
            "# feedbackId|patientId|doctorId|appointmentId|rating|comments|dateSubmitted";

    static {
        try {
            Files.createDirectories(Paths.get(DATA_DIR));
        } catch (IOException e) {
            System.err.println("Could not create data directory: " + e.getMessage());
        }
    }

    /**
     * Read every non-empty, non-comment line from a file.
     * Comment lines start with '#'.
     */
    public static List<String> readAllLines(String filePath) {
        List<String> lines = new ArrayList<>();
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            return lines;
        }
        try (BufferedReader br = Files.newBufferedReader(path)) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty() && !line.startsWith("#")) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading " + filePath + ": " + e.getMessage());
        }
        return lines;
    }

    /**
     * Overwrite the entire file with a header comment + data lines.
     * Safe for manual editing: open the .txt, change a field, save, restart app.
     */
    public static void writeAllLines(String filePath, String headerComment, List<String> dataLines) {
        try {
            Files.createDirectories(Paths.get(filePath).getParent());
            try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(filePath))) {
                if (headerComment != null && !headerComment.isEmpty()) {
                    bw.write(headerComment);
                    bw.newLine();
                }
                for (String line : dataLines) {
                    if (line != null && !line.trim().isEmpty()) {
                        bw.write(line);
                        bw.newLine();
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error writing " + filePath + ": " + e.getMessage());
        }
    }

    /**
     * Overwrite without a custom header (keeps previous behaviour for simple cases).
     */
    public static void writeAllLines(String filePath, List<String> lines) {
        writeAllLines(filePath, null, lines);
    }

    /**
     * Append a single data line (does not rewrite header).
     */
    public static void appendLine(String filePath, String line) {
        try {
            Files.createDirectories(Paths.get(filePath).getParent());
            try (BufferedWriter bw = Files.newBufferedWriter(
                    Paths.get(filePath),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND)) {
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error appending to " + filePath + ": " + e.getMessage());
        }
    }

    /**
     * Generate a unique ID, e.g. DOC-1722678901234-A3F
     */
    public static String generateId(String prefix) {
        long ts = System.currentTimeMillis();
        String rnd = Integer.toHexString((int) (Math.random() * 0xFFF)).toUpperCase();
        return prefix + "-" + ts + "-" + rnd;
    }
}
