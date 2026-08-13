package dao;

import model.Feedback;
import util.FileManager;

import java.util.ArrayList;
import java.util.List;

/**
 * DAO for Feedback – text-file persistence.
 * Teammate: add filters as needed.
 */
public class FeedbackFile {

    public List<Feedback> getAll() {
        List<Feedback> list = new ArrayList<>();
        for (String line : FileManager.readAllLines(FileManager.FEEDBACK_FILE)) {
            Feedback obj = Feedback.fromFileString(line);
            if (obj != null) list.add(obj);
        }
        return list;
    }

    public Feedback findById(String id) {
        for (Feedback o : getAll()) {
            if (o.getFeedbackId().equals(id)) return o;
        }
        return null;
    }

    public void save(Feedback obj) {
        List<Feedback> all = getAll();
        boolean updated = false;
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getFeedbackId().equals(obj.getFeedbackId())) {
                all.set(i, obj);
                updated = true;
                break;
            }
        }
        if (!updated) all.add(obj);
        writeAll(all);
    }

    public void delete(String id) {
        List<Feedback> all = getAll();
        all.removeIf(o -> o.getFeedbackId().equals(id));
        writeAll(all);
    }

    private void writeAll(List<Feedback> list) {
        List<String> lines = new ArrayList<>();
        for (Feedback o : list) lines.add(o.toFileString());
        FileManager.writeAllLines(FileManager.FEEDBACK_FILE, FileManager.HEADER_FEEDBACK, lines);
    }
}
