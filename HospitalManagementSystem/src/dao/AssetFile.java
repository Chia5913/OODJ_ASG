package dao;

import model.HospitalAsset;
import util.FileManager;

import java.util.ArrayList;
import java.util.List;

/**
 * DAO for HospitalAsset – text-file persistence.
 * Teammate: add filters as needed.
 */
public class AssetFile {

    public List<HospitalAsset> getAll() {
        List<HospitalAsset> list = new ArrayList<>();
        for (String line : FileManager.readAllLines(FileManager.ASSETS_FILE)) {
            HospitalAsset obj = HospitalAsset.fromFileString(line);
            if (obj != null) list.add(obj);
        }
        return list;
    }

    public HospitalAsset findById(String id) {
        for (HospitalAsset o : getAll()) {
            if (o.getAssetId().equals(id)) return o;
        }
        return null;
    }

    public void save(HospitalAsset obj) {
        List<HospitalAsset> all = getAll();
        boolean updated = false;
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getAssetId().equals(obj.getAssetId())) {
                all.set(i, obj);
                updated = true;
                break;
            }
        }
        if (!updated) all.add(obj);
        writeAll(all);
    }

    public void delete(String id) {
        List<HospitalAsset> all = getAll();
        all.removeIf(o -> o.getAssetId().equals(id));
        writeAll(all);
    }

    private void writeAll(List<HospitalAsset> list) {
        List<String> lines = new ArrayList<>();
        for (HospitalAsset o : list) lines.add(o.toFileString());
        FileManager.writeAllLines(FileManager.ASSETS_FILE, FileManager.HEADER_ASSETS, lines);
    }
}
