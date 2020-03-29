package com.example.protocolcompanion;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Study {
    private String id;
    private String name;
    private Boolean gps;
    private Boolean acceleration;
    private Boolean hr;
    private String region;
    private String bucket;
    private String folder;

    Study(String id, String name, Boolean gps, Boolean acceleration, Boolean hr, String region, String bucket, String folder) {
        this.id = id;
        this.name = name;
        this.gps = gps;
        this.acceleration = acceleration;
        this.hr = hr;
        this.region = region;
        this.bucket = bucket;
        this.folder = folder;
    }

    public static List<Study> ITEMS = new ArrayList<>();

    static {
        // Add some sample items.
        for (int i = 0; i <= 3; i++) {
            addItem(i);
        }
    }
    private static void addItem(int i) {
        ITEMS.add(new Study(String.valueOf(i), "test" + i, true, false, false, "r" + i, "b1" + i, "f1" + i));
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

    public Boolean getGps() {
        return gps;
    }

    public void setGps(Boolean gps) {
        this.gps = gps;
    }

    public Boolean getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(Boolean acceleration) {
        this.acceleration = acceleration;
    }

    public Boolean getHr() {
        return hr;
    }

    public void setHr(Boolean hr) {
        this.hr = hr;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }
}
