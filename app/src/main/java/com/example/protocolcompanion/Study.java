package com.example.protocolcompanion;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Study implements Comparable<Study> {
    private String id;
    private String name;
    private Boolean gps;
    private Boolean acceleration;
    private Boolean hr;
    private String region;
    private String bucket;
    private String folder;

    private Study(String id, String name, Boolean gps, Boolean acceleration, Boolean hr, String region, String bucket, String folder) {
        this.id = id;
        this.name = name;
        this.gps = gps;
        this.acceleration = acceleration;
        this.hr = hr;
        this.region = region;
        this.bucket = bucket;
        this.folder = folder;
    }

    public Study(String id) {
        this.id = id;
        this.name = " ";
        this.gps = false;
        this.acceleration = false;
        this.hr = false;
        this.region = " ";
        this.bucket = " ";
        this.folder = " ";
    }

    public static HashMap<String, Study> ITEMS = new HashMap<>();

    static void addNewItem(Study s) {
        ITEMS.put(s.getId(), s);
    }

    static void updateItem(Study s) {
        ITEMS.remove(s.getId());
        ITEMS.put(s.getId(), s);
    }

    static void removeItem(Study s) {
        ITEMS.remove(s.getId());
    }

    public static int getSize() {
        return ITEMS.size();
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

    @Override
    public int compareTo(@NonNull Study study) {
        if (getId() == null || study.getId() == null) {
            return 0;
        }
        return getId().compareTo(study.getId());
    }
}
