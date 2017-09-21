package exnihilum.com.au.listdataviewer;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by David on 20/09/2017.
 *
 */

public class JSONPolyfeature {

    private String name;
    private boolean hasHoles;
    private ArrayList<LatLng> geometry;
    private ArrayList<ArrayList<LatLng>> holes;

    public JSONPolyfeature(String name, ArrayList<LatLng> geometry) {
        this.geometry = geometry;
        this.name = name;
        this.hasHoles = false;
        this.holes = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean hasHoles() {
        return hasHoles;
    }

    public void setHasHoles(boolean hasHoles) {
        this.hasHoles = hasHoles;
    }

    public ArrayList<LatLng> getGeometry() {
        return geometry;
    }

    public void setGeometry(ArrayList<LatLng> geometry) {
        this.geometry = geometry;
    }

    public ArrayList<ArrayList<LatLng>> getHoles() {
        return holes;
    }

    public void setHoles(ArrayList<ArrayList<LatLng>> holes) {
        this.holes = holes;
    }

    public void addHoleToList(ArrayList<LatLng> newHole) {
        this.holes.add(newHole);
    }
}



