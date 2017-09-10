package exnihilum.com.au.listdataviewer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import Utilities.ParametersHelper;


/**
 * Created by david on 28/07/2017.
 *
 */

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private final static String LOG_TAG = "debug_tag_main";
    ArrayList<LayerType> layers = ParametersHelper.layerTypes();
    ArrayList<String> categories = ParametersHelper.getCategories();
    ArrayList<String> layerLabels = new ArrayList<>();
    ArrayAdapter<CharSequence> detailAdapter;
    TextView goButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner spinnerDetail = (Spinner) findViewById(R.id.layers_spinner);
        Spinner spinnerCategory = (Spinner) findViewById(R.id.category_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        detailAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, layerLabels);

        ArrayAdapter<CharSequence> categoryAdapter =
                new ArrayAdapter(this, android.R.layout.simple_spinner_item, categories);
        // Specify the layout to use when the list of choices appears
        detailAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerDetail.setAdapter(detailAdapter);
        spinnerCategory.setAdapter(categoryAdapter);
        spinnerDetail.setOnItemSelectedListener(this);
        spinnerCategory.setOnItemSelectedListener(this);

        // set initial values for detail
        String category = spinnerCategory.getSelectedItem().toString();
        for (LayerType type : layers) {
            if (type.getClassification().equals(category)) {
                layerLabels.add(type.getLayerName());
            }
        }

        goButton = (TextView) findViewById(R.id.go);
        goButton.setAlpha((float) 0.4);
        goButton.setOnClickListener(view -> {
                    String item = spinnerDetail.getSelectedItem().toString();
                    Log.i(LOG_TAG, item);
                    for (LayerType type : layers) {
                        if (type.containsName(item)) {
                            Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                            intent.putExtra("layerName", type.getLayerName());
                            startActivity(intent);
                        }
                    }
                }
        );

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        Spinner spinner = (Spinner) parent;
        if (spinner.getId() == R.id.category_spinner) {
            String item = (String) parent.getItemAtPosition(pos);
            layerLabels.clear();
            for (LayerType type : layers) {
                if (type.getClassification().equals(item)) {
                    layerLabels.add(type.getLayerName());
                }
            }
            detailAdapter.notifyDataSetChanged();
        } else {
            goButton.setAlpha(1);
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // don't do anything
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.actionbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.action_about:
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }


}
