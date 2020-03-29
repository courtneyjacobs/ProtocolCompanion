package com.example.protocolcompanion;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class StudyViewModel extends ViewModel {

    private final MutableLiveData<String> mName = new MutableLiveData<>("");
    private final MutableLiveData<String> mId = new MutableLiveData<>("0");
    private final MutableLiveData<Boolean> mGPS = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> mAcceleration = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> mHR = new MutableLiveData<>(false);
    private final MutableLiveData<String> mRegion = new MutableLiveData<>("");
    private final MutableLiveData<String> mBucket = new MutableLiveData<>("");
    private final MutableLiveData<String> mFolder = new MutableLiveData<>("");

    public JSONObject fullJSONObject;
    public Study currentStudy;

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

    public JSONObject exportJSON() {
        // Create JSON objects
        JSONObject root = new JSONObject();
        JSONObject info = new JSONObject();
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
            info.put("name", mName.getValue());
            info.put("storage", storage);
            info.put("probes",probes);

            // Add all info with study id to create root object
            root.put(Objects.requireNonNull(mId.getValue()), info);

            Study s = new Study(mId.getValue(), mName.getValue(), mGPS.getValue(),
                    mAcceleration.getValue(), mHR.getValue(), mRegion.getValue(),
                    mBucket.getValue(), mFolder.getValue());
            System.out.println("BEFORE: " + Study.ITEMS.size());
            Study.ITEMS.add(s);
            System.out.println("AFTER: " + Study.ITEMS.size());
            for (Study s1: Study.ITEMS
                 ) {
                System.out.println("id " + s1.getId());
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        return root;

    }

    public void importJSON(JSONObject root, String id) {
        try {
            JSONObject info = root.getJSONObject(id);
            mId.setValue(id);
            mName.setValue(info.get("name").toString());
            JSONObject storageVals = (JSONObject) info.get("storage");
            JSONObject probesVals = (JSONObject) info.get("probes");
            mAcceleration.setValue(Boolean.parseBoolean(probesVals.get("acceleration").toString()));
            mGPS.setValue(Boolean.parseBoolean(probesVals.get("gps").toString()));
            mHR.setValue(Boolean.parseBoolean(probesVals.get("hr").toString()));
            mRegion.setValue(storageVals.get("region").toString());
            mBucket.setValue(storageVals.get("bucket").toString());
            mFolder.setValue(storageVals.get("folder").toString());
            Study s = new Study(Objects.requireNonNull(mId.getValue()), mName.getValue(), mGPS.getValue(),
                    mAcceleration.getValue(), mHR.getValue(), mRegion.getValue(),
                    mBucket.getValue(), mFolder.getValue());
            Study.ITEMS.add(s);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

}