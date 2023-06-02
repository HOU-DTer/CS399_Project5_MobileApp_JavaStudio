package com.am.aucklank;

import com.google.android.gms.maps.model.LatLng;

public class IncidentBean {
    private String LONGITUDE;
    private String LATITUDE;
    private String DATE;
    private String TIME;
    private String INCIDENT_type;
    private String area;

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    private String severity;

    public IncidentBean(String DATE, String TIME, String INCIDENT_type, LatLng latLng,String area,String severity) {
        this.DATE = DATE;
        this.TIME = TIME;
        this.INCIDENT_type = INCIDENT_type;
        this.latLng = latLng;
        this.area = area;
        this.severity = severity;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    private LatLng latLng;

    private int id;

    public String getLONGITUDE() {
        return LONGITUDE;
    }

    public void setLONGITUDE(String LONGITUDE) {
        this.LONGITUDE = LONGITUDE;
    }

    public String getLATITUDE() {
        return LATITUDE;
    }

    public void setLATITUDE(String LATITUDE) {
        this.LATITUDE = LATITUDE;
    }

    public String getDATE() {
        return DATE;
    }

    public void setDATE(String DATE) {
        this.DATE = DATE;
    }

    public String getTIME() {
        return TIME;
    }

    public void setTIME(String TIME) {
        this.TIME = TIME;
    }

    public String getINCIDENT_type() {
        return INCIDENT_type;
    }

    public void setINCIDENT_type(String INCIDENT_type) {
        this.INCIDENT_type = INCIDENT_type;
    }



    public String getLINK() {
        return LINK;
    }

    public void setLINK(String LINK) {
        this.LINK = LINK;
    }

    public String getGRADING() {
        return GRADING;
    }

    public void setGRADING(String GRADING) {
        this.GRADING = GRADING;
    }

    private String LINK;
    private String GRADING;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}
