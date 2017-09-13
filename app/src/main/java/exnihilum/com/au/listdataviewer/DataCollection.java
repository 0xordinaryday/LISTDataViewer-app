package exnihilum.com.au.listdataviewer;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by david on 28/07/2017.
 * // collection of whatever (polygons) provided by API call
 */

class DataCollection {

    private String symbol = "";
    private ArrayList<HashMap<String,ArrayList<LatLng>>> geometries;

    DataCollection() {
        this.symbol = "";
        this.geometries = null;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    ArrayList<HashMap<String,ArrayList<LatLng>>> getGeometries() {
        return geometries;
    }

    void setGeometries(ArrayList<HashMap<String,ArrayList<LatLng>>> geometries) {
        this.geometries = geometries;
    }

    void addMapToGeometry(HashMap<String,ArrayList<LatLng>> mapToAdd) {
        this.geometries.add(mapToAdd);
    }

    ArrayList<String> getKeys() {
        ArrayList<String> allKeys = new ArrayList<>();
        for (HashMap hashMap:geometries) {
            for (Object key:hashMap.keySet()) {
                allKeys.add(key.toString());
            }
        }
        return allKeys;
    }

    int getGeometryLength() {
        return this.geometries.size();
    }

    void logGeometries() {
        for (HashMap<String,ArrayList<LatLng>> geometry:this.geometries) {
            for (String key:geometry.keySet()) {
                Log.i(key, Arrays.toString(geometry.get(key).toArray()));
            }
        }
    }

    @Override
    public String toString() {
        return "DataCollection{" +
                "symbol='" + symbol + '\'' +
                ", geometries=" + geometries +
                '}';
    }
}
