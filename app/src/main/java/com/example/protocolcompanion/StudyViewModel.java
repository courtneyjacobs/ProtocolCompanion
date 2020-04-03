package com.example.protocolcompanion;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class StudyViewModel extends ViewModel {

    private final MutableLiveData<String> mName = new MutableLiveData<>("");
    private final MutableLiveData<String> mId = new MutableLiveData<>("0");
    private final MutableLiveData<Boolean> mGPS = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> mAcceleration = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> mHR = new MutableLiveData<>(false);
    private final MutableLiveData<String> mRegion = new MutableLiveData<>("");
    private final MutableLiveData<String> mBucket = new MutableLiveData<>("");
    private final MutableLiveData<String> mFolder = new MutableLiveData<>("");

    private JSONObject fullJSONObject;
    private Study currentStudy;

    public StudyViewModel() {
        fullJSONObject = new JSONObject();
    }

    public String getFullJSONString() {
        return fullJSONObject.toString();
    }

    void setFullJSONObject(String jsonString) {
        fullJSONObject = Study.importJSON(jsonString);
    }

    public void setCurrentStudy(Study s) {
        currentStudy = s;
        setText("id", currentStudy.getId());
        setText("name", currentStudy.getName());
        setText("region", currentStudy.getRegion());
        setText("bucket", currentStudy.getBucket());
        setText("folder", currentStudy.getFolder());
        setSwitch("gps", currentStudy.getGps());
        setSwitch("acceleration", currentStudy.getAcceleration());
        setSwitch("hr", currentStudy.getHr());
    }

    public void updateCurrentStudy(Boolean add) {
        currentStudy.setName(getText("name").getValue());
        currentStudy.setRegion(getText("region").getValue());
        currentStudy.setBucket(getText("bucket").getValue());
        currentStudy.setFolder(getText("folder").getValue());
        currentStudy.setGps(getSwitch("gps").getValue());
        currentStudy.setAcceleration(getSwitch("acceleration").getValue());
        currentStudy.setHr(getSwitch("hr").getValue());
        try {
            // Add new study
            if (add) {
                JSONObject j = Study.addNewItem(currentStudy);
                fullJSONObject.put(currentStudy.getId(), j);
            }
            // Update study
            else {
                JSONObject j = Study.updateItem(currentStudy);
                fullJSONObject.remove(currentStudy.getId());
                fullJSONObject.put(currentStudy.getId(), j);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void deleteCurrentStudy() {
        Study.removeItem(currentStudy);
        fullJSONObject.remove(currentStudy.getId());
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
                mDefault.setValue("An error has occurred");
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

}