package com.example.protocolcompanion;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class StudyViewModel extends ViewModel {

    private final MutableLiveData<String> mName = new MutableLiveData<>(" ");
    private final MutableLiveData<String> mId = new MutableLiveData<>("0");
    private final MutableLiveData<Boolean> mGPS = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> mAcceleration = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> mHR = new MutableLiveData<>(false);
    private final MutableLiveData<String> mRegion = new MutableLiveData<>(" ");
    private final MutableLiveData<String> mBucket = new MutableLiveData<>(" ");
    private final MutableLiveData<String> mFolder = new MutableLiveData<>(" ");

    public JSONObject fullJSONObject;
    private Study currentStudy;

    public StudyViewModel() {
        fullJSONObject = new JSONObject();
    }

    public void setCurrentStudy(Study currentStudy) {
        this.currentStudy = currentStudy;
        setText("id", this.currentStudy.getId());
        setText("name", this.currentStudy.getName());
        setText("region", this.currentStudy.getRegion());
        setText("bucket", this.currentStudy.getBucket());
        setText("folder", this.currentStudy.getFolder());
        setSwitch("gps", this.currentStudy.getGps());
        setSwitch("acceleration", this.currentStudy.getAcceleration());
        setSwitch("hr", this.currentStudy.getHr());
    }

    public void updateCurrentStudy() {
        this.currentStudy.setName(getText("name").getValue());
        this.currentStudy.setRegion(getText("region").getValue());
        this.currentStudy.setBucket(getText("bucket").getValue());
        this.currentStudy.setFolder(getText("folder").getValue());
        this.currentStudy.setGps(getSwitch("gps").getValue());
        this.currentStudy.setAcceleration(getSwitch("acceleration").getValue());
        this.currentStudy.setHr(getSwitch("hr").getValue());
        Study.updateItem(currentStudy);
    }

    public void updateAndAddCurrentStudy() {
        this.currentStudy.setId(getText("id").getValue());
        this.currentStudy.setName(getText("name").getValue());
        this.currentStudy.setRegion(getText("region").getValue());
        this.currentStudy.setBucket(getText("bucket").getValue());
        this.currentStudy.setFolder(getText("folder").getValue());
        this.currentStudy.setGps(getSwitch("gps").getValue());
        this.currentStudy.setAcceleration(getSwitch("acceleration").getValue());
        this.currentStudy.setHr(getSwitch("hr").getValue());
        Study.addNewItem(currentStudy);
    }

    public void deleteCurrentStudy() {
        Study.removeItem(this.currentStudy);
    }

    public LiveData<String> getText(String name) {
        switch (name) {
            case "name":
                return mName;
            case "id":
                return mId;
            case "region":
                return mRegion;
            case "bucket":
                return mBucket;
            case "folder":
                return mFolder;
            default:
                MutableLiveData<String> mDefault = new MutableLiveData<>();
                mDefault.setValue("Not available");
                return mDefault;
        }
    }

    public void setText(String name, String value) {
        switch (name) {
            case "id":
                mId.setValue(value);
                break;
            case "name":
                mName.setValue(value);
                break;
            case "region":
                mRegion.setValue(value);
                break;
            case "bucket":
                mBucket.setValue(value);
                break;
            case "folder":
                mFolder.setValue(value);
                break;
        }
    }

    public LiveData<Boolean> getSwitch(String name) {
        switch (name) {
            case "gps":
                return mGPS;
            case "acceleration":
                return mAcceleration;
            case "hr":
                return mHR;
            default:
                MutableLiveData<Boolean> mDefault = new MutableLiveData<>();
                mDefault.setValue(false);
                return mDefault;
        }
    }

    public void setSwitch(String name, Boolean value) {
        switch (name) {
            case "gps":
                mGPS.setValue(value);
                break;
            case "acceleration":
                mAcceleration.setValue(value);
                break;
            case "hr":
                mHR.setValue(value);
                break;
        }
    }

    // TODO: doesn't include id (that's done when added to fullJSONObject in fragments
    public JSONObject exportJSON() {
        // Create JSON objects
        JSONObject root = new JSONObject();
        JSONObject storage = new JSONObject();
        JSONObject probes = new JSONObject();

        try {
            // Build storage object
            storage.put("region", mRegion.getValue());
            storage.put("bucket", mBucket.getValue());
            storage.put("folder", mFolder.getValue());


            // Build probes object
            probes.put("acceleration", mAcceleration.getValue());
            probes.put("gps", mGPS.getValue());
            probes.put("hr", mHR.getValue());

            // Add storage and probes objects as well as the study name to create info object
            root.put("name", mName.getValue());
            root.put("storage", storage);
            root.put("probes",probes);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        return root;

    }

    void importJSON(String jsonString) {
        if (jsonString.isEmpty()) {
            return;
        }
        try {
            JSONObject root = new JSONObject(jsonString);
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

}