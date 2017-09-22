package exnihilum.com.au.listdataviewer;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by David on 20/09/2017.
 *
 */

class JSONPolyfeature {

    private String name;
    private boolean hasHoles;
    private boolean isPoint;
    private LatLng pointCoords;
    private ArrayList<LatLng> geometry;
    private ArrayList<ArrayList<LatLng>> holes;

    public JSONPolyfeature() {
        this.geometry = null;
        this.name = null;
        this.hasHoles = false;
        this.holes = null;
        this.isPoint = false;
        this.pointCoords = null;
    }

    public JSONPolyfeature(String name, LatLng coords) {
        this.name = name;
        this.isPoint = true;
        this.hasHoles = false;
        this.geometry = null;
        this.holes = null;
        this.pointCoords = coords;
    }

    String getName() {
        return name;
    }

    public LatLng getPointCoords() {
        return pointCoords;
    }

    void setPointCoords(LatLng pointCoords) {
        this.pointCoords = pointCoords;
    }

    void setName(String name) {
        this.name = name;
    }

    boolean hasHoles() {
        return hasHoles;
    }

    void setHasHoles(boolean hasHoles) {
        this.hasHoles = hasHoles;
    }

    ArrayList<LatLng> getGeometry() {
        return geometry;
    }

    void setGeometry(ArrayList<LatLng> geometry) {
        this.geometry = geometry;
    }

    ArrayList<ArrayList<LatLng>> getHoles() {
        return holes;
    }

    void setHoles(ArrayList<ArrayList<LatLng>> holes) {
        this.holes = holes;
    }

    void addHoleToList(ArrayList<LatLng> newHole) {
        this.holes.add(newHole);
    }
}



