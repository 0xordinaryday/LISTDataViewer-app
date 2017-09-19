package exnihilum.com.au.listdataviewer;

import com.google.android.gms.maps.model.Polygon;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by david on 28/07/2017.
 * // collection of whatever (polygons) provided by API call
 */

class PolygonCollection {

    private ArrayList<HashMap<String,Polygon>> polygons;

    PolygonCollection() {
        this.polygons = null;
    }

    ArrayList<HashMap<String,Polygon>> getPolygons() {
        return polygons;
    }

    void setGeometries(ArrayList<HashMap<String,Polygon>> polygons) {
        this.polygons = polygons;
    }

    void addMapToGeometry(HashMap<String,Polygon> mapToAdd) {
        this.polygons.add(mapToAdd);
    }

    ArrayList<String> getKeys() {
        ArrayList<String> allKeys = new ArrayList<>();
        for (HashMap hashMap:polygons) {
            for (Object key:hashMap.keySet()) {
                allKeys.add(key.toString());
            }
        }
        return allKeys;
    }

    int getPolygonsLength() {
        return this.polygons.size();
    }

}
