package exnihilum.com.au.listdataviewer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;

import Utilities.ParametersHelper;

import static exnihilum.com.au.listdataviewer.R.id.map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    /**
     * Tag for the log messages
     */
    public static final String LOG_TAG = MapsActivity.class.getSimpleName();
    private GoogleMap mMap;
    private ArrayList<LayerType> layers = ParametersHelper.layerTypes();
    private String geometryType;
    private String finalRequestString;
    private String PARAM1;
    private String PARAM2;
    LayerType selectedType = null;
    private boolean canMakeServerRequest = true;
    private ProgressBar progressBar;
    private LatLng initialPosition;
    private final int ZOOM_LEVEL = 17;
    private TextView callout;

    // location stuff
    private LocationCallback mLocationCallback;
    private boolean mRequestingLocationUpdates = true;
    private FusedLocationProviderClient mFusedLocationClient;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    LocationRequest mLocationRequest = new LocationRequest();

    /**
     * URL to query
     */
    // query parameters
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        callout = (TextView) findViewById(R.id.text_callout);
        callout.setVisibility(View.INVISIBLE);

        // get initial position from shared preferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String latString = prefs.getString("lat", "-41.436033");
        String lonString = prefs.getString("lon", "147.138470");
        initialPosition = new LatLng(Double.valueOf(latString), Double.valueOf(lonString));

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);

        String[] envelopeArray = generateEnvelope();

        // set progress bar
        progressBar = (ProgressBar) findViewById(R.id.indeterminateBar);

        Intent createIntent = getIntent();
        String layerName = createIntent.getStringExtra("layerName");
        for (LayerType type : layers) {
            if (type.containsName(layerName)) {
                selectedType = type;
            }
        }

        finalRequestString = generateString(selectedType, envelopeArray);
        Log.i(LOG_TAG, finalRequestString);
        geometryType = selectedType.getGeometryType(); // rings, paths or none

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    LatLng currentPosition = new LatLng(location.getLatitude(), location.getLongitude());
                    float[] results = new float[3];
                    Location.distanceBetween(
                            currentPosition.latitude,
                            currentPosition.longitude,
                            initialPosition.latitude,
                            initialPosition.longitude,
                            results);
                    if (results[0] > 100.0 && canMakeServerRequest && mRequestingLocationUpdates) {
                        initialPosition = currentPosition;
                        // save to shared preferences
                        SharedPreferences.Editor prefEditor =
                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                        prefEditor.putString("lat", String.valueOf(currentPosition.latitude));
                        prefEditor.putString("lon", String.valueOf(currentPosition.longitude));
                        prefEditor.apply();
                        progressBar.setVisibility(View.VISIBLE);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, ZOOM_LEVEL));
                        mMap.clear();
                        finalRequestString = generateString(selectedType, generateEnvelope());
                        // make new server request
                        LISTMapAsyncTask task = new LISTMapAsyncTask();
                        task.execute();
                    }
                }
            }
        };

        if (checkLocationPermission()) {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            createLocationRequest();
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Request location updates:
                mRequestingLocationUpdates = true;
                mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                        mLocationCallback,
                        null /* Looper */);
            }
        }

        // Kick off an {@link AsyncTask} to perform the network request
        LISTMapAsyncTask task = new LISTMapAsyncTask();
        task.execute();
        progressBar.setVisibility(View.VISIBLE);
    }

    protected void createLocationRequest() {
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(2000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private String[] generateEnvelope() {
        // build envelope from initialPosition
        double delta = 0.002;
        String lowerLeftLat = String.valueOf(initialPosition.latitude - delta);
        String lowerLeftLon = String.valueOf(initialPosition.longitude - delta);
        String upperRightLat = String.valueOf(initialPosition.latitude + delta);
        String upperRightLon = String.valueOf(initialPosition.longitude + delta);
        return new String[]{lowerLeftLat, lowerLeftLon, upperRightLat, upperRightLon};
    }

    private String generateString(LayerType type, String[] envelope) {
        String separator = "%2C";

        String LIST_REQUEST_URL_PART1 = "http://services.thelist.tas.gov.au/arcgis/rest/services/Public/";
        String LIST_REQUEST_URL_PART2 = "/MapServer/";
        String LIST_REQUEST_URL_PART3 = "/query?where=&text=&objectIds=&time=&geometry=" +
                envelope[1] +
                separator +
                envelope[0] +
                separator +
                envelope[3] +
                separator +
                envelope[2] +
                "&geometryType=esriGeometryEnvelope&inSR=4326&spatialRel=esriSpatialRelIntersects&relationParam=&outFields=";
        String LIST_REQUEST_URL_PART5 = "&returnGeometry=true&returnTrueCurves=false&maxAllowableOffset=&geometryPrecision=&outSR=4326&returnIdsOnly=false&returnCountOnly=false&orderByFields=&groupByFieldsForStatistics=&outStatistics=&returnZ=false&returnM=false&gdbVersion=&returnDistinctValues=false&resultOffset=&resultRecordCount=&f=pjson";

        PARAM1 = type.getParam1();
        PARAM2 = type.getParam2();

        return LIST_REQUEST_URL_PART1 +
                type.getClassification() +
                LIST_REQUEST_URL_PART2 +
                type.getLayerID() +
                LIST_REQUEST_URL_PART3 +
                PARAM1 +
                separator +
                PARAM2 +
                LIST_REQUEST_URL_PART5;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker at the inital position and move the camera
        // Marker marker = mMap.addMarker(new MarkerOptions().position(initialPosition));
        // marker.setTag("Initial Location");
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialPosition, ZOOM_LEVEL));

        // Set a listener for marker click.
        mMap.setOnMarkerClickListener(this);

        mMap.setOnCameraMoveListener(() -> callout.setVisibility(View.INVISIBLE));

        // set camera move listener
        mMap.setOnCameraIdleListener(() -> {
            LatLng newLocation = mMap.getCameraPosition().target;
            float[] results = new float[3];
            Location.distanceBetween(
                    newLocation.latitude,
                    newLocation.longitude,
                    initialPosition.latitude,
                    initialPosition.longitude,
                    results);
            if (results[0] > 100.0 && canMakeServerRequest) {
                mRequestingLocationUpdates = false; // stop moving, since we're dragging
                progressBar.setVisibility(View.VISIBLE);
                initialPosition = newLocation;
                mMap.clear();
                finalRequestString = generateString(selectedType, generateEnvelope());
                // make new server request
                LISTMapAsyncTask task = new LISTMapAsyncTask();
                task.execute();
            }
        });
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker.getTag() != null && !marker.getTag().equals("Added location")) {
            callout.setText(marker.getTag().toString());
            callout.setVisibility(View.VISIBLE);
        }
        return true;
    }

    /**
     * {@link AsyncTask} to perform the network request on a background thread, and then
     * DO THE THINGS
     */
    private class LISTMapAsyncTask extends AsyncTask<URL, Void, DataCollection> {

        @Override
        protected DataCollection doInBackground(URL... urls) {
            canMakeServerRequest = false;
            // Create URL object
            URL url = createUrl(finalRequestString);

            // Perform HTTP request to the URL and receive a JSON response back
            String jsonResponse = "";
            try {
                jsonResponse = makeHttpRequest(url);
            } catch (IOException e) {
                // TODO Handle the IOException
            }
            return extractFeatureFromJson(jsonResponse);
        }

        /**
         */
        @Override
        protected void onPostExecute(DataCollection testCollection) {
            if (testCollection == null) {
                Log.i(LOG_TAG, "collection was null");
                return;
            }
            int collectionCount = testCollection.getGeometryLength();

            switch (geometryType) {
                case "rings":
                final ArrayList<Polygon> polyFeatures = new ArrayList<>();
                for (int i = 0; i < collectionCount; i++) {
                    // Instantiates a new Polygon object and adds points to define a rectangle
                    PolygonOptions rectOptions = new PolygonOptions()
                            .add(new LatLng(37.35, -122.0),
                                    new LatLng(37.45, -122.0),
                                    new LatLng(37.45, -122.2),
                                    new LatLng(37.35, -122.2),
                                    new LatLng(37.35, -122.0));
                    // Get back the mutable Polygon
                    Polygon polygon = mMap.addPolygon(rectOptions);
                    final String keyValue = testCollection.getGeometries().get(i).keySet().toArray()[0].toString();
                    polygon.setPoints(testCollection.getGeometries().get(i).get(keyValue));
                    polygon.setTag(keyValue);
                    polygon.setClickable(true);
                    polygon.setStrokeWidth(3);
                    polyFeatures.add(polygon);
                    mMap.setOnPolygonClickListener(polygon1 -> {
                        for (Polygon feature: polyFeatures) {
                            feature.setFillColor(Color.argb(0,0,0,0));
                        }
                        polygon1.setFillColor(Color.rgb(255,250,205));
                        if (polygon1.getTag() != null) {
                            callout.setText(polygon1.getTag().toString());
                        }
                        callout.setVisibility(View.VISIBLE);
                    });
                }
                break;
                case "paths":
                final ArrayList<Polyline> lineFeatures = new ArrayList<>();
                for (int i = 0; i < collectionCount; i++) {
                    // Instantiates a new Polyline object
                    PolylineOptions polyOptions = new PolylineOptions()
                            .add(new LatLng(37.35, -122.0),
                                    new LatLng(37.45, -122.0),
                                    new LatLng(37.45, -122.2),
                                    new LatLng(37.35, -122.2),
                                    new LatLng(37.35, -122.0));
                    // Get back the mutable Polyline
                    Polyline polyline = mMap.addPolyline(polyOptions);
                    final String keyValue = testCollection.getGeometries().get(i).keySet().toArray()[0].toString();
                    polyline.setPoints(testCollection.getGeometries().get(i).get(keyValue));
                    polyline.setTag(keyValue);
                    polyline.setClickable(true);
                    polyline.setWidth(3);
                    lineFeatures.add(polyline);
                    mMap.setOnPolylineClickListener(polyline1 -> {
                        for (Polyline feature: lineFeatures) {
                            feature.setWidth(3);
                        }
                        polyline1.setWidth(7);
                        if (polyline1.getTag() != null) {
                            callout.setText(polyline1.getTag().toString());
                        }
                        callout.setVisibility(View.VISIBLE);
                    });
                }
                break;
                case "none":
                for (int i = 0; i < collectionCount; i++) {
                    final String keyValue = testCollection.getGeometries().get(i).keySet().toArray()[0].toString();
                    // Instantiates a new marker
                    MarkerOptions markerOptions = new MarkerOptions()
                            .position(testCollection.getGeometries().get(i).get(keyValue).get(0))
                            .title(keyValue);
                    Marker mMarker = mMap.addMarker(markerOptions);
                    mMarker.setTag(keyValue);
                }
            }
            Log.i(LOG_TAG, "onpostexecute completed");
            canMakeServerRequest = true;
            progressBar.setVisibility(View.INVISIBLE);
        }

        /**
         * Returns new URL object from the given string URL.
         */
        private URL createUrl(String stringUrl) {
            URL url;
            try {
                url = new URL(stringUrl);
            } catch (MalformedURLException exception) {
                Log.e(LOG_TAG, "Error with creating URL", exception);
                return null;
            }
            return url;
        }

        /**
         * Make an HTTP request to the given URL and return a String as the response.
         */
        private String makeHttpRequest(URL url) throws IOException {
            String jsonResponse = "";
            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;

            // check for null URL
            if (url == null) {
                return jsonResponse;
            }

            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(15000 /* milliseconds */);
                urlConnection.connect();
                // check response code
                int responseCode = urlConnection.getResponseCode();
                if (responseCode != 200) {
                    // invalid response
                    Log.i(LOG_TAG + " http response code", String.valueOf(responseCode));
                    return jsonResponse;
                }
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } catch (IOException e) {
                // TODO: Handle the exception
                Log.e(LOG_TAG, " exception thrown:", e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream != null) {
                    // function must handle java.io.IOException here
                    inputStream.close();
                }
            }
            return jsonResponse;
        }

        /**
         * Convert the {@link InputStream} into a String which contains the
         * whole JSON response from the server.
         */
        private String readFromStream(InputStream inputStream) throws IOException {
            StringBuilder output = new StringBuilder();
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
            }
            return output.toString();
        }

        /**
         * Return an {@link DataCollection} object by parsing out information
         * about ALL THE THINGS
         */
        private DataCollection extractFeatureFromJson(String listMapJSON) {
            if (TextUtils.isEmpty(listMapJSON)) { // checks for null and empty string
                return null;
            }
            try {
                // setup object to hold data
                DataCollection dataCollection = new DataCollection();
                ArrayList<HashMap<String, ArrayList<LatLng>>> geometryToSet =
                        new ArrayList<>();
                dataCollection.setGeometries(geometryToSet);

                JSONObject baseJsonResponse = new JSONObject(listMapJSON);
                JSONArray featureArray = baseJsonResponse.getJSONArray("features");
                int length = featureArray.length();
                for (int i = 0; i < length; i++) {
                    JSONObject attributes = featureArray.getJSONObject(i);

                    // string tags for datacollection
                    JSONObject paramAttributes = attributes.getJSONObject("attributes");
                    String param1 = paramAttributes.getString(PARAM1);
                    String param2 = paramAttributes.getString(PARAM2);
                    String tagToSet;
                    if (param1.equals("null") && param2.equals("null")) {
                        tagToSet = ("No" + PARAM1 + " or " + PARAM2);
                    } else if (param1.equals("null")) {
                        tagToSet = (PARAM2 + ": " + param2 + "\nNo " + PARAM1);
                    } else if (param2.equals("null")) {
                        tagToSet = ("No " + PARAM2 + "\n" + PARAM1 + ": " + param1);
                    } else {
                        tagToSet = (PARAM1 + ": " + param1 + "\n" + PARAM2 + ": " + param2);
                    }
                    HashMap<String, ArrayList<LatLng>> mapToSet = new HashMap<String, ArrayList<LatLng>>();

                    JSONObject geometry = attributes.getJSONObject("geometry");
                    /*
                    * We can treat polygons and polylines, aka rings and paths, as equivalent
                    * because the only real difference is that polygons close by having the same
                    * end point as they started with. Internally in java and as far as the JSON goes
                    * they look the same otherwise
                     */

                    if (geometryType.equals("rings") || geometryType.equals("paths")) {
                        JSONArray geometryArray = geometry.getJSONArray(geometryType);
                        ArrayList<LatLng> newGeometryFeature = new ArrayList<>();
                        // If there are results in the features array
                        if (geometryArray.length() > 0) {
                            // Extract out the first feature (which is a path or polygon)
                            JSONArray firstArray = geometryArray.getJSONArray(0);
                            int featureLength = firstArray.length();
                            for (int j = 0; j < featureLength; j++) {
                                try {
                                    double Lon = (firstArray.getJSONArray(j).getDouble(0));
                                    double Lat = (firstArray.getJSONArray(j).getDouble(1));
                                    LatLng toAppend = new LatLng(Lat, Lon);
                                    newGeometryFeature.add(toAppend);
                                } catch (NullPointerException e) {
                                    Log.i(LOG_TAG, "value was null");
                                }
                            }
                            mapToSet.put(tagToSet, newGeometryFeature);
                        }
                    } else if (geometryType.equals("none")) {
                        Double x = geometry.getDouble("x");
                        Double y = geometry.getDouble("y");
                        ArrayList<LatLng> newGeometryFeature = new ArrayList<>();
                        LatLng toAppend = new LatLng(y, x);
                        newGeometryFeature.add(toAppend);
                        mapToSet.put(tagToSet, newGeometryFeature);
                    }
                    dataCollection.addMapToGeometry(mapToSet);
                }
                return dataCollection;
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Problem parsing the JSON results", e);
            }
            return null;
        }
    }

    // location stuff
    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    private void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkLocationPermission();
    }

    // location permission code
    public boolean checkLocationPermission() {
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, android.Manifest.permission.
                ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle(R.string.title_location_permission)
                        .setMessage(R.string.text_location_permission)
                        .setPositiveButton(R.string.ok, (dialogInterface, i) -> {
                            //Prompt the user once explanation has been shown
                            ActivityCompat.requestPermissions(MapsActivity.this,
                                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                    MY_PERMISSIONS_REQUEST_LOCATION);
                        })
                        .create()
                        .show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback,
                                null /* Looper */);
                    }

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
            }
        }
    }

}
