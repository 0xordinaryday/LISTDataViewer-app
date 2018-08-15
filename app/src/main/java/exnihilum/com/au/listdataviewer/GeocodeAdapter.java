package exnihilum.com.au.listdataviewer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class GeocodeAdapter extends ArrayAdapter<String> {

    private Context context;
    private ArrayList<String> geocodePlaces;
    private int resource;

    // constructors
    public GeocodeAdapter(@NonNull Context context, int textViewResourceId, ArrayList<String> geocodePlaces) {
        super(context, textViewResourceId, geocodePlaces);
    }

}
