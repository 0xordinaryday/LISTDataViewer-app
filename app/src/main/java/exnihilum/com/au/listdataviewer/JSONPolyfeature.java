package exnihilum.com.au.listdataviewer;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.maps.model.LatLng;
import java.util.ArrayList;

/**
 * Created by David on 20/09/2017.
 * http://www.parcelabler.com/
 */

class JSONPolyfeature implements Parcelable {

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

    protected JSONPolyfeature(Parcel in) {
        name = in.readString();
        hasHoles = in.readByte() != 0x00;
        isPoint = in.readByte() != 0x00;
        pointCoords = (LatLng) in.readValue(LatLng.class.getClassLoader());
        if (in.readByte() == 0x01) {
            geometry = new ArrayList<LatLng>();
            in.readList(geometry, LatLng.class.getClassLoader());
        } else {
            geometry = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeByte((byte) (hasHoles ? 0x01 : 0x00));
        dest.writeByte((byte) (isPoint ? 0x01 : 0x00));
        dest.writeValue(pointCoords);
        if (geometry == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(geometry);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<JSONPolyfeature> CREATOR = new Parcelable.Creator<JSONPolyfeature>() {
        @Override
        public JSONPolyfeature createFromParcel(Parcel in) {
            return new JSONPolyfeature(in);
        }

        @Override
        public JSONPolyfeature[] newArray(int size) {
            return new JSONPolyfeature[size];
        }
    };
}