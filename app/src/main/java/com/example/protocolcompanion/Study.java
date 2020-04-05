package com.example.protocolcompanion;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

public class Study {
    private String id;
    private String name;
    private Boolean gps;
    private Boolean acceleration;
    private Boolean hr;
    private String region;
    private String bucket;
    private String folder;

    public Study(String id) {
        this.id = id;
        this.name = "";
        this.gps = false;
        this.acceleration = false;
        this.hr = false;
        this.region = "";
        this.bucket = "";
        this.folder = "";
    }

    public static HashMap<String, Study> ITEMS = new HashMap<>();

    // Adds new item to study map and returns a JSON object for export
    static JSONObject addNewItem(Study s) {
        ITEMS.put(s.getId(), s);
        return s.exportToJSON();
    }


    static JSONObject updateItem(Study s) {
        ITEMS.remove(s.getId());
        ITEMS.put(s.getId(), s);
        return s.exportToJSON();
    }

    // Exports a Study to a JSON object
    // Builds entire object except for study ID (fullJSONObject contains set of {id : JSONObject} to lookup by study ID)
    private JSONObject exportToJSON() {
        // Create JSON objects
        JSONObject root = new JSONObject();
        JSONObject storage = new JSONObject();
        JSONObject probes = new JSONObject();

        try {
            // Build storage object
            storage.put("region", this.region);
            storage.put("bucket", this.bucket);
            storage.put("folder", this.folder);


            // Build probes object
            probes.put("acceleration", this.acceleration);
            probes.put("gps", this.gps);
            probes.put("hr", this.hr);

            // Add storage and probes objects as well as the study name to create info object
            root.put("name", this.name);
            root.put("storage", storage);
            root.put("probes",probes);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return root;
    }

    // Imports a string representing a JSON object to a Study and returns the JSON Object
    static JSONObject importJSON(String jsonString) {
        JSONObject root = new JSONObject();
        if(!jsonString.isEmpty()) {
            try {
                root = new JSONObject(jsonString);
                for (Iterator<String> it = root.keys(); it.hasNext(); ) {
                    String id = it.next();
                    JSONObject info = root.getJSONObject(String.valueOf(id));
                    Study s = new Study(String.valueOf(id));
                    s.setName(info.get("name").toString());
                    JSONObject storageVals = (JSONObject) info.get("storage");
                    JSONObject probesVals = (JSONObject) info.get("probes");
                    s.setAcceleration(Boolean.parseBoolean(probesVals.get("acceleration").toString()));
                    s.setGps(Boolean.parseBoolean(probesVals.get("gps").toString()));
                    s.setHr(Boolean.parseBoolean(probesVals.get("hr").toString()));
                    s.setRegion(storageVals.get("region").toString());
                    s.setBucket(storageVals.get("bucket").toString());
                    s.setFolder(storageVals.get("folder").toString());
                    Study.addNewItem(s);
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return root;
    }

    static void removeItem(Study s) {
        ITEMS.remove(s.getId());
    }

    public static int getSize() {
        return ITEMS.size();
    }

    public static int getMaxID() {
        return Integer.parseInt(Collections.max(ITEMS.keySet()));
    }

    public static Study getStudy(String id) {
        return ITEMS.get(id);
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    Boolean getGps() {
        return gps;
    }

    void setGps(Boolean gps) {
        this.gps = gps;
    }

    Boolean getAcceleration() {
        return acceleration;
    }

    void setAcceleration(Boolean acceleration) {
        this.acceleration = acceleration;
    }

    Boolean getHr() {
        return hr;
    }

    void setHr(Boolean hr) {
        this.hr = hr;
    }

    String getRegion() {
        return region;
    }

    void setRegion(String region) {
        this.region = region;
    }

    String getBucket() {
        return bucket;
    }

    void setBucket(String bucket) {
        this.bucket = bucket;
    }

    String getFolder() {
        return folder;
    }

    void setFolder(String folder) {
        this.folder = folder;
    }
}
