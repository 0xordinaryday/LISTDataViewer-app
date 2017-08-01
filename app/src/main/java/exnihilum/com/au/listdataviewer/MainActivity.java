package exnihilum.com.au.listdataviewer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

import Utilities.ParametersHelper;


/**
 * Created by david on 28/07/2017.
 *
 */

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private boolean userIsInteracting;
    private final static String LOG_TAG = "debug_tag_main";
    ArrayList<LayerType> layers = ParametersHelper.layerTypes();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<String> layerLabels = new ArrayList<>(layers.size());
        for (LayerType type: layers) {
            layerLabels.add(type.getLayerName());
        }

        Spinner spinner = (Spinner) findViewById(R.id.layers_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter (this, android.R.layout.simple_spinner_item,
                layerLabels);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        userIsInteracting = true;
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        String item = (String) parent.getItemAtPosition(pos);
        if (userIsInteracting) {
            for (LayerType type : layers) {
                if (type.containsName(item)) {
                    Intent intent = new Intent(this, MapsActivity.class);
                    intent.putExtra("layerName", type.getLayerName());
                    startActivity(intent);
                }
            }
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }



}
