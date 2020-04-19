package com.example.protocolcompanion;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

public class Study {
    // Info
    private String id;
    private String name;

    // Probes
    private Boolean accelerometer;
    private Boolean ambientLight;
    private Boolean bluetooth;
    private Boolean breath;
    private Boolean compass;
    private Boolean gps;
    private Boolean gyroscope;
    private Boolean hr;
    private Boolean linearAcceleration;
    private Boolean offBody;
    private Boolean posture;
    private Boolean ppg;
    private Boolean sleep;
    private Boolean stepCount;

    // Remote storage settings
    private String region;
    private String bucket;
    private String folder;

    public Study(String id) {
        this.id = id;
        this.name = "";
        this.accelerometer = false;
        this.ambientLight = false;
        this.bluetooth = false;
        this.breath = false;
        this.compass = false;
        this.gps = false;
        this.gyroscope = false;
        this.hr = false;
        this.linearAcceleration = false;
        this.offBody = false;
        this.posture = false;
        this.ppg = false;
        this.sleep = false;
        this.stepCount = false;
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

    public static String getItemPosition(String id) {
        ArrayList<String> arr = new ArrayList<>(Study.ITEMS.keySet());
        Collections.sort(arr);
        return String.valueOf(arr.indexOf(id));
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
            probes.put("accelerometer", this.accelerometer);
            probes.put("ambientLight", this.ambientLight);
            probes.put("bluetooth", this.bluetooth);
            probes.put("breath", this.breath);
            probes.put("compass", this.compass);
            probes.put("gps", this.gps);
            probes.put("gyroscope", this.gyroscope);
            probes.put("hr", this.hr);
            probes.put("linearAcceleration", this.linearAcceleration);
            probes.put("offBody", this.offBody);
            probes.put("posture", this.posture);
            probes.put("ppg", this.ppg);
            probes.put("sleep", this.sleep);
            probes.put("stepCount", this.stepCount);

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
                    // probes
                    s.setAccelerometer(Boolean.parseBoolean(probesVals.get("accelerometer").toString()));
                    s.setAmbientLight(Boolean.parseBoolean(probesVals.get("ambientLight").toString()));
                    s.setBluetooth(Boolean.parseBoolean(probesVals.get("bluetooth").toString()));
                    s.setBreath(Boolean.parseBoolean(probesVals.get("breath").toString()));
                    s.setCompass(Boolean.parseBoolean(probesVals.get("compass").toString()));
                    s.setGps(Boolean.parseBoolean(probesVals.get("gps").toString()));
                    s.setGyroscope(Boolean.parseBoolean(probesVals.get("gyroscope").toString()));
                    s.setHr(Boolean.parseBoolean(probesVals.get("hr").toString()));
                    s.setLinearAcceleration(Boolean.parseBoolean(probesVals.get("linearAcceleration").toString()));
                    s.setOffBody(Boolean.parseBoolean(probesVals.get("offBody").toString()));
                    s.setPosture(Boolean.parseBoolean(probesVals.get("posture").toString()));
                    s.setPpg(Boolean.parseBoolean(probesVals.get("ppg").toString()));
                    s.setSleep(Boolean.parseBoolean(probesVals.get("sleep").toString()));
                    s.setStepCount(Boolean.parseBoolean(probesVals.get("stepCount").toString()));
                    // storage
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

    Boolean getAccelerometer() {
        return accelerometer;
    }

    void setAccelerometer(Boolean accelerometer) {
        this.accelerometer = accelerometer;
    }

    Boolean getAmbientLight() {
        return ambientLight;
    }

    void setAmbientLight(Boolean ambientLight) {
        this.ambientLight = ambientLight;
    }

    Boolean getBluetooth() {
        return bluetooth;
    }

    void setBluetooth(Boolean bluetooth) {
        this.bluetooth = bluetooth;
    }

    Boolean getBreath() {
        return breath;
    }

    void setBreath(Boolean breath) {
        this.breath = breath;
    }

    Boolean getCompass() {
        return compass;
    }

    void setCompass(Boolean compass) {
        this.compass = compass;
    }

    Boolean getGps() {
        return gps;
    }

    void setGps(Boolean gps) {
        this.gps = gps;
    }

    Boolean getGyroscope() {
        return gyroscope;
    }

    void setGyroscope(Boolean gyroscope) {
        this.gyroscope = gyroscope;
    }

    Boolean getHr() {
        return hr;
    }

    void setHr(Boolean hr) {
        this.hr = hr;
    }

    Boolean getLinearAcceleration() {
        return linearAcceleration;
    }

    void setLinearAcceleration(Boolean linearAcceleration) {
        this.linearAcceleration = linearAcceleration;
    }

    Boolean getOffBody() {
        return offBody;
    }

    void setOffBody(Boolean offBody) {
        this.offBody = offBody;
    }

    Boolean getPosture() {
        return posture;
    }

    void setPosture(Boolean posture) {
        this.posture = posture;
    }

    Boolean getPpg() {
        return ppg;
    }

    void setPpg(Boolean ppg) {
        this.ppg = ppg;
    }

    Boolean getSleep() {
        return sleep;
    }

    void setSleep(Boolean sleep) {
        this.sleep = sleep;
    }

    Boolean getStepCount() {
        return stepCount;
    }

    void setStepCount(Boolean stepCount) {
        this.stepCount = stepCount;
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
