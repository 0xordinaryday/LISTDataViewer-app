package exnihilum.com.au.listdataviewer;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by david on 28/07/2017.
 * // collection of whatever (polygons) provided by API call
 */

public class DataCollection {

    private String symbol = "";
    private ArrayList<HashMap<String,ArrayList<LatLng>>> geometries;

    public DataCollection() {
        this.symbol = "";
        this.geometries = null;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public ArrayList<HashMap<String,ArrayList<LatLng>>> getGeometries() {
        return geometries;
    }

    public void setGeometries(ArrayList<HashMap<String,ArrayList<LatLng>>> geometries) {
        this.geometries = geometries;
    }

    public void addMapToGeometry(HashMap<String,ArrayList<LatLng>> mapToAdd) {
        this.geometries.add(mapToAdd);
    }

    public int getGeometryLength() {
        return this.geometries.size();
    }

    @Override
    public String toString() {
        return "DataCollection{" +
                "symbol='" + symbol + '\'' +
                ", geometries=" + geometries +
                '}';
    }
}
