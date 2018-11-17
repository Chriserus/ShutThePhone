package com.student.krborowi.shutthephone;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;
import java.util.List;

//import com.google.maps.android.PolyUtil;

public class MapHandler {

    private List<LatLng> universityLatLngList;

    public boolean isOnUniversity(LatLng userLocation) {
        if(userLocation == null)
            return false;
        else
            return PolyUtil.containsLocation(userLocation, universityLatLngList, false);
    }

    public void setAllMapProperties(){
        //adding all LatLng points to list
        universityLatLngList = new ArrayList<>();
        universityLatLngList.add(new LatLng(51.108955, 17.053984));
        universityLatLngList.add(new LatLng(51.107353, 17.056216));
        universityLatLngList.add(new LatLng(51.107086, 17.063868));
        universityLatLngList.add(new LatLng(51.108671, 17.068253));
        universityLatLngList.add(new LatLng(51.112091, 17.060255));
    }

}
