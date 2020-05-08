package com.example.protocolcompanion;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

// This view model connects the Study class with the views and acts as a controller in the MVC framework.
// The MutableLiveData objects are for two-way data binding with the fragment views.
// The fullJSONObject is a JSONObject that represents all loaded studies in local storage (protocols.json)
// The currentStudy is a Study object that represents the currently selected study, and is updated each
// time a user selects a study from the list so the selected study's details can be viewed and edited.
public class StudyViewModel extends ViewModel {

    private final MutableLiveData<String> mName = new MutableLiveData<>("");
    private final MutableLiveData<String> mId = new MutableLiveData<>("0");

    private final MutableLiveData<Boolean> mAccelerometer = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> mAmbientLight = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> mBluetooth = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> mBreath = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> mCompass = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> mGps = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> mGyroscope = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> mHr = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> mLinearAcceleration = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> mOffBody = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> mPosture = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> mPpg = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> mSleep = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> mStepCount = new MutableLiveData<>(false);

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

    // Gets a new study from imported json file, imports it using
    // Study.importJSON and adds to the study list and the fullJSONObject
    void appendToFullJSONObject(String jsonString) {
        try {
            JSONObject root = Study.importJSON(jsonString);
            System.out.println("STUDY BEING IMPORTED: " + root);
            for (Iterator<String> it = root.keys(); it.hasNext(); ) {
                String id = it.next();
                JSONObject newStudy = root.getJSONObject(String.valueOf(id));
                fullJSONObject.put(String.valueOf(id), newStudy);
                System.out.println("FULL JSON OBJ: " + fullJSONObject);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setCurrentStudy(Study s) {
        currentStudy = s;
        setText("id", currentStudy.getId());
        setText("name", currentStudy.getName());
        setText("region", currentStudy.getRegion());
        setText("bucket", currentStudy.getBucket());
        setText("folder", currentStudy.getFolder());

        setSwitch("accelerometer", currentStudy.getAccelerometer());
        setSwitch("ambientLight", currentStudy.getAmbientLight());
        setSwitch("bluetooth", currentStudy.getBluetooth());
        setSwitch("breath", currentStudy.getBreath());
        setSwitch("compass", currentStudy.getCompass());
        setSwitch("gps", currentStudy.getGps());
        setSwitch("gyroscope", currentStudy.getGyroscope());
        setSwitch("hr", currentStudy.getHr());
        setSwitch("linearAcceleration", currentStudy.getLinearAcceleration());
        setSwitch("offBody", currentStudy.getOffBody());
        setSwitch("posture", currentStudy.getPosture());
        setSwitch("ppg", currentStudy.getPpg());
        setSwitch("sleep", currentStudy.getSleep());
        setSwitch("stepCount", currentStudy.getStepCount());
    }

    public void updateCurrentStudy(Boolean add) {
        currentStudy.setName(getText("name").getValue());
        currentStudy.setRegion(getText("region").getValue());
        currentStudy.setBucket(getText("bucket").getValue());
        currentStudy.setFolder(getText("folder").getValue());

        currentStudy.setAccelerometer(getSwitch("accelerometer").getValue());
        currentStudy.setAmbientLight(getSwitch("ambientLight").getValue());
        currentStudy.setBluetooth(getSwitch("bluetooth").getValue());
        currentStudy.setBreath(getSwitch("breath").getValue());
        currentStudy.setCompass(getSwitch("compass").getValue());
        currentStudy.setGps(getSwitch("gps").getValue());
        currentStudy.setGyroscope(getSwitch("gyroscope").getValue());
        currentStudy.setHr(getSwitch("hr").getValue());
        currentStudy.setLinearAcceleration(getSwitch("linearAcceleration").getValue());
        currentStudy.setOffBody(getSwitch("offBody").getValue());
        currentStudy.setPosture(getSwitch("posture").getValue());
        currentStudy.setPpg(getSwitch("ppg").getValue());
        currentStudy.setSleep(getSwitch("sleep").getValue());
        currentStudy.setStepCount(getSwitch("stepCount").getValue());
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
            case "accelerometer":
                return mAccelerometer;
            case "ambientLight":
                return mAmbientLight;
            case "bluetooth":
                return mBluetooth;
            case "breath":
                return mBreath;
            case "compass":
                return mCompass;
            case "gps":
                return mGps;
            case "gyroscope":
                return mGyroscope;
            case "hr":
                return mHr;
            case "linearAcceleration":
                return mLinearAcceleration;
            case "offBody":
                return mOffBody;
            case "posture":
                return mPosture;
            case "ppg":
                return mPpg;
            case "sleep":
                return mSleep;
            case "stepCount":
                return mStepCount;
            default:
                MutableLiveData<Boolean> mDefault = new MutableLiveData<>();
                mDefault.setValue(false);
                return mDefault;
        }
    }

    public void setSwitch(String name, Boolean value) {
        switch (name) {
            case "accelerometer":
                mAccelerometer.setValue(value);
                break;
            case "ambientLight":
                mAmbientLight.setValue(value);
                break;
            case "bluetooth":
                mBluetooth.setValue(value);
                break;
            case "breath":
                mBreath.setValue(value);
                break;
            case "compass":
                mCompass.setValue(value);
                break;
            case "gps":
                mGps.setValue(value);
                break;
            case "gyroscope":
                mGyroscope.setValue(value);
                break;
            case "hr":
                mHr.setValue(value);
                break;
            case "linearAcceleration":
                mLinearAcceleration.setValue(value);
                break;
            case "offBody":
                mOffBody.setValue(value);
                break;
            case "posture":
                mPosture.setValue(value);
                break;
            case "ppg":
                mPpg.setValue(value);
                break;
            case "sleep":
                mSleep.setValue(value);
                break;
            case "stepCount":
                mStepCount.setValue(value);
                break;
        }
    }

}