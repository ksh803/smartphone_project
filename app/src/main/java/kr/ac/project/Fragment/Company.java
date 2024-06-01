package kr.ac.project;

import com.google.android.gms.maps.model.LatLng;

public class Company {
    private String name;
    private String info;
    private LatLng location;

    public Company(String name, String info, LatLng location) {
        this.name = name;
        this.info = info;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public String getInfo() {
        return info;
    }

    public LatLng getLocation() {
        return location;
    }
}
