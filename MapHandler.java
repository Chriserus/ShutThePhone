package com.student.krborowi.shutthephone;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;
import java.util.List;

public class MapHandler {

    private List<LatLng> universityLatLngList;
    private LatLng universityLoc;

    public List<LatLng> getUniversityLatLngList() {
        return universityLatLngList;
    }

    public LatLng getUniversityLoc() {
        return universityLoc;
    }

    public boolean isOnUniversity(LatLng userLocation) {
        return PolyUtil.containsLocation(userLocation, universityLatLngList, false);
    }

    public void setAllMapProperties(){
        universityLoc = new LatLng(51.108980, 17.061714);
        //adding all LatLng points to list
        universityLatLngList = new ArrayList<>();
        universityLatLngList.add(new LatLng(51.108955, 17.053984));
        universityLatLngList.add(new LatLng(51.107353, 17.056216));
        universityLatLngList.add(new LatLng(51.107086, 17.063868));
        universityLatLngList.add(new LatLng(51.108671, 17.068253));
        universityLatLngList.add(new LatLng(51.112091, 17.060255));
    }

    //TODO: implement functionality for getting user location point
    public LatLng getUserLocationPoint(){
        LatLng userLocationPoint = new LatLng(0,0);
        return userLocationPoint;
    }
}
