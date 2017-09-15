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
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;

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
    private boolean isGeologyRequest = false;
    private boolean isLandslideRequest = false;
    private boolean isProclaimedRequest = false;
    private boolean isBoreHoleRequest = false;
    private boolean isMineralRequest = false;
    private boolean canNavigate = true;
    private GoogleMap mMap;
    private ArrayList<LayerType> layers = ParametersHelper.layerTypes();
    private String geometryType;
    private String finalRequestString;
    private String PARAM1;
    private String PARAM2;
    private String PARAM3;
    private String PARAM4;
    LayerType selectedType = null;
    private boolean canMakeServerRequest = true;
    private ProgressBar progressBar;
    private LatLng initialPosition;
    private final int ZOOM_LEVEL = 17;
    private TextView callout;

    private String layerName;

    // location stuff
    private LocationCallback mLocationCallback;
    private boolean mRequestingLocationUpdates = true;
    private FusedLocationProviderClient mFusedLocationClient;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    LocationRequest mLocationRequest = new LocationRequest();

    // arraylist for markers
    ArrayList<Marker> markers = new ArrayList<>();

    /**
     * URL to query
     */
    // query parameters
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        callout = (TextView) findViewById(R.id.text_callout);
        callout.setMovementMethod(LinkMovementMethod.getInstance());
        callout.setVisibility(View.INVISIBLE);

        // get initial position from shared preferences, and check if navigation allowed
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        canNavigate = prefs.getBoolean("canNavigate", true);
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
        layerName = createIntent.getStringExtra("layerName");
        // see if it was a geology layer
        ArrayList<String> geologyLayers = ParametersHelper.getGeologyLayers();
        if (geologyLayers.contains(layerName)) {
            String geologyString = makeGeologyString(layerName, generateEnvelope());
            // Log.i(LOG_TAG, geologyString);
            isGeologyRequest = true;
            finalRequestString = geologyString;
        } else {
            for (LayerType type : layers) {
                if (type.containsName(layerName)) {
                    selectedType = type;
                }
            }
        }

        switch (layerName) {
            case "mrtwfs:LandSlidePoly":
                isLandslideRequest = true;
                break;
            case "mrtwfs:ProclaimedAreasPoly":
                isProclaimedRequest = true;
                break;
            case "mrtwfs:Boreholes":
                isBoreHoleRequest = true;
                break;
            case "mrtwfs:MineralOccurences":
                isMineralRequest = true;
                break;
        }

        if (!isGeologyRequest) {
            finalRequestString = generateString(selectedType, envelopeArray);
            Log.i(LOG_TAG, finalRequestString);
            geometryType = selectedType.getGeometryType(); // rings, paths or none
        }

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
                        if (!isGeologyRequest) {
                            finalRequestString = generateString(selectedType, generateEnvelope());
                        } else {
                            finalRequestString = makeGeologyString(layerName, generateEnvelope());
                        }
                        // make new server request
                        chooseTaskAndExecute();
                    }
                }
            }
        };

        if (checkLocationPermission() && canNavigate) {
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

        chooseTaskAndExecute();
        progressBar.setVisibility(View.VISIBLE);
    }

    private void chooseTaskAndExecute() {
        LISTMapAsyncTask task = new LISTMapAsyncTask();
        task.execute();
    }

    protected void createLocationRequest() {
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(2000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private String[] generateEnvelope() {
        // build envelope from initialPosition
        double delta;
        if (isGeologyRequest) {
            delta = 0.01;
        } else {
            delta = 0.01;
        }
        String lowerLeftLat = String.valueOf(initialPosition.latitude - delta);
        String lowerLeftLon = String.valueOf(initialPosition.longitude - delta);
        String upperRightLat = String.valueOf(initialPosition.latitude + delta);
        String upperRightLon = String.valueOf(initialPosition.longitude + delta);
        return new String[]{lowerLeftLat, lowerLeftLon, upperRightLat, upperRightLon};
    }

    private String makeGeologyString(String layer, String[] envelope) {
        String MRT_WEB_SERVICES_PART1 = "http://www.mrt.tas.gov.au/web-services/ows?service=wfs&version=1.1.0&request=GetFeature&typeName=";
        String MRT_WEB_SERVICES_PART2 = "&styles=default&SRS=EPSG:4326&bbox=";
        String comma = ",";
        String MRT_WEB_SERVICES_PART3 = "&outputFormat=JSON";

        return MRT_WEB_SERVICES_PART1 + layer + MRT_WEB_SERVICES_PART2 +
                envelope[0] +
                comma +
                envelope[1] +
                comma +
                envelope[2] +
                comma +
                envelope[3] +
                MRT_WEB_SERVICES_PART3;
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
        PARAM3 = type.getParam3();
        PARAM4 = type.getParam4();

        String param3String = "";
        String param4String = "";

        if (PARAM3 != null) {
            param3String = separator + PARAM3;
        }
        if (PARAM4 != null) {
            param4String = separator + PARAM4;
        }

        return LIST_REQUEST_URL_PART1 +
                type.getClassification() +
                LIST_REQUEST_URL_PART2 +
                type.getLayerID() +
                LIST_REQUEST_URL_PART3 +
                PARAM1 +
                separator +
                PARAM2 +
                param3String +
                param4String +
                LIST_REQUEST_URL_PART5;
    }

    /**
     * Manipulates the map once available.
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
                if (!isGeologyRequest) {
                    finalRequestString = generateString(selectedType, generateEnvelope());
                } else {
                    finalRequestString = makeGeologyString(layerName, generateEnvelope());
                }
                Log.i(LOG_TAG, finalRequestString);
                // make new server request
                chooseTaskAndExecute();
            }
        });
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        for (Marker eachMarker : markers) {
            eachMarker.setIcon(BitmapDescriptorFactory.defaultMarker());
        }
        if (marker.getTag() != null && !marker.getTag().equals("Added location")) {
            String textToSet = marker.getTag().toString();
            if (textToSet.contains("SALT_FOR_HASHMAP$")) {
                String trimmedText = textToSet.substring(textToSet.indexOf("$") + 1);
                callout.setText(trimmedText);
            } else {
                callout.setText(textToSet);
            }
            callout.setVisibility(View.VISIBLE);
            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
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
            if (!isGeologyRequest) {
                return extractFeatureFromJson(jsonResponse);
            } else {
                return extractGeologyFromJson(jsonResponse);
            }
        }

        /**
         */
        @Override
        protected void onPostExecute(DataCollection testCollection) {
            if (testCollection == null) {
                return;
            }
            int collectionCount = testCollection.getGeometryLength();
            if (collectionCount == 0) {
                Toast.makeText(getBaseContext(), "No results for this view", Toast.LENGTH_SHORT).show();
            }

            if (isGeologyRequest && !isBoreHoleRequest && !isMineralRequest) {
                geometryType = "rings";
            } else if (isBoreHoleRequest || isMineralRequest) {
                geometryType = "none";
            }

            switch (geometryType) {
                case "rings": // for rings or MRT data
                    final ArrayList<Polygon> polyFeatures = new ArrayList<>();
                    for (int i = 0; i < collectionCount; i++) {
                        int numberOfKeys = testCollection.getGeometries().get(i).keySet().size();
                        // testCollection.logGeometries(); // debug
                        for (int j = 0; j < numberOfKeys; j++) {
                            // Instantiates a new Polygon object and adds points to define a rectangle
                            PolygonOptions rectOptions = new PolygonOptions()
                                    .add(new LatLng(37.35, -122.0),
                                            new LatLng(37.45, -122.0),
                                            new LatLng(37.35, -122.2),
                                            new LatLng(37.35, -122.0));
                            // Get back the mutable Polygon
                            Polygon polygon = mMap.addPolygon(rectOptions);
                            final String keyValue = testCollection.getGeometries().get(i).keySet().toArray()[j].toString();
                            polygon.setPoints(testCollection.getGeometries().get(i).get(keyValue));
                            if (SphericalUtil.computeSignedArea(polygon.getPoints()) > 0 && !isGeologyRequest) {
                                polygon.remove();
                                continue;
                            }
                            polygon.setTag(keyValue);
                            polygon.setClickable(true);
                            polygon.setStrokeWidth(3);
                            polyFeatures.add(polygon);
                            mMap.setOnPolygonClickListener(polygon1 -> {
                                for (Polygon feature : polyFeatures) {
                                    feature.setFillColor(Color.argb(0, 0, 0, 0));
                                }
                                polygon1.setFillColor(Color.argb(100, 255, 250, 205));
                                if (polygon1.getTag() != null) {
                                    String textToSet = polygon1.getTag().toString();
                                    if (textToSet.contains("SALT_FOR_HASHMAP$")) {
                                        String trimmedText = textToSet.substring(textToSet.indexOf("$") + 1);
                                        callout.setText(trimmedText);
                                    } else {
                                        callout.setText(textToSet);
                                    }
                                }
                                callout.setVisibility(View.VISIBLE);
                            });
                        }
                    }
                    break;
                case "paths":
                    final ArrayList<Polyline> lineFeatures = new ArrayList<>();
                    for (int i = 0; i < collectionCount; i++) {
                        int numberOfKeys = testCollection.getGeometries().get(i).keySet().size();
                        for (int j = 0; j < numberOfKeys; j++) {
                            // Instantiates a new Polyline object
                            PolylineOptions polyOptions = new PolylineOptions()
                                    .add(new LatLng(37.35, -122.0),
                                            new LatLng(37.45, -122.0));
                            // Get back the mutable Polyline
                            Polyline polyline = mMap.addPolyline(polyOptions);
                            final String keyValue = testCollection.getGeometries().get(i).keySet().toArray()[j].toString();
                            polyline.setPoints(testCollection.getGeometries().get(i).get(keyValue));
                            polyline.setTag(keyValue);
                            polyline.setClickable(true);
                            polyline.setWidth(3);
                            lineFeatures.add(polyline);
                            mMap.setOnPolylineClickListener(polyline1 -> {
                                for (Polyline feature : lineFeatures) {
                                    feature.setWidth(3);
                                }
                                polyline1.setWidth(7);
                                if (polyline1.getTag() != null) {
                                    String textToSet = polyline1.getTag().toString();
                                    if (textToSet.contains("SALT_FOR_HASHMAP$")) {
                                        String trimmedText = textToSet.substring(textToSet.indexOf("$") + 1);
                                        callout.setText(trimmedText);
                                    } else {
                                        callout.setText(textToSet);
                                    }
                                }
                                callout.setVisibility(View.VISIBLE);
                            });
                        }
                    }
                    break;
                case "none":
                    markers.clear();
                    for (int i = 0; i < collectionCount; i++) {
                        final String keyValue = testCollection.getGeometries().get(i).keySet().toArray()[0].toString();
                        Log.i(LOG_TAG, keyValue);
                        // Instantiates a new marker
                        MarkerOptions markerOptions = new MarkerOptions()
                                .position(testCollection.getGeometries().get(i).get(keyValue).get(0))
                                .title(keyValue);
                        Marker mMarker = mMap.addMarker(markerOptions);
                        mMarker.setTag(keyValue);
                        markers.add(mMarker);
                    }
                    break;
            }
            // make links clickable, hopefully
            callout.setAutoLinkMask(Linkify.WEB_URLS);
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
                    String param3 = "empty";
                    String param4 = "empty";
                    if (PARAM3 != null) {
                        param3 = paramAttributes.getString(PARAM3);
                    }
                    if (PARAM4 != null) {
                        param4 = paramAttributes.getString(PARAM4);
                    }
                    String tagToSet = setTags(param1, param2, param3, param4);

                    HashMap<String, ArrayList<LatLng>> mapToSet = new HashMap<>();

                    JSONObject geometry = attributes.getJSONObject("geometry");
                    /*
                    * We can treat polygons and polylines, aka rings and paths, as equivalent
                    * because the only real difference is that polygons close by having the same
                    * end point as they started with. Internally in java and as far as the JSON goes
                    * they look the same otherwise
                     */

                    if (geometryType.equals("rings") || geometryType.equals("paths")) {
                        JSONArray geometryArray = geometry.getJSONArray(geometryType);
                        // If there are results in the features array
                        if (geometryArray.length() > 0) {
                            for (int counter = 0; counter < geometryArray.length(); counter++) {
                                ArrayList<LatLng> newGeometryFeature = new ArrayList<>();
                                JSONArray workingArray = geometryArray.getJSONArray(counter);
                                int featureLength = workingArray.length();
                                for (int j = 0; j < featureLength; j++) {
                                    try {
                                        double Lon = (workingArray.getJSONArray(j).getDouble(0));
                                        double Lat = (workingArray.getJSONArray(j).getDouble(1));
                                        LatLng toAppend = new LatLng(Lat, Lon);
                                        newGeometryFeature.add(toAppend);
                                    } catch (NullPointerException e) {
                                        Log.i(LOG_TAG, "value was null");
                                    }
                                }
                                if (counter == 0) {
                                    mapToSet.put(tagToSet, newGeometryFeature);
                                } else {
                                    String holeTag = String.valueOf(counter) + "SALT_FOR_HASHMAP$" + tagToSet;
                                    mapToSet.put(holeTag, newGeometryFeature);
                                }
                            }
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

    /**
     * Return an {@link DataCollection} object by parsing out information
     * about ALL THE THINGS
     */
    private DataCollection extractGeologyFromJson(String listMapJSON) {
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
                JSONObject features = featureArray.getJSONObject(i);

                // string tags for datacollection
                JSONObject properties = features.getJSONObject("properties");
                String tagToSet = geoTags(properties);
                // Log.i("CHECK STRING", tagToSet);

                HashMap<String, ArrayList<LatLng>> mapToSet = new HashMap<>();

                JSONObject geometry = features.getJSONObject("geometry");

                if (!isBoreHoleRequest && !isMineralRequest) {
                    JSONArray geometryArray = geometry.getJSONArray("coordinates");
                    // If there are results in the features array
                    if (geometryArray.length() > 0) {
                        for (int counter = 0; counter < geometryArray.length(); counter++) {
                            ArrayList<LatLng> newGeometryFeature = new ArrayList<>();
                            // note extra level of array nesting for MRT data
                            JSONArray workingArray = geometryArray.getJSONArray(counter).getJSONArray(0);
                            int featureLength = workingArray.length();
                            for (int j = 0; j < featureLength; j++) {
                                try {
                                    double Lon = (workingArray.getJSONArray(j).getDouble(0));
                                    double Lat = (workingArray.getJSONArray(j).getDouble(1));
                                    LatLng toAppend = new LatLng(Lat, Lon);
                                    newGeometryFeature.add(toAppend);
                                } catch (NullPointerException e) {
                                    Log.i(LOG_TAG, "value was null");
                                }
                            }
                            if (counter == 0) {
                                mapToSet.put(tagToSet, newGeometryFeature);
                            } else {
                                String holeTag = String.valueOf(counter) + "SALT_FOR_HASHMAP$" + tagToSet;
                                mapToSet.put(holeTag, newGeometryFeature);
                            }
                        }
                    }
                } else {
                    JSONArray geometryArray = geometry.getJSONArray("coordinates");
                    Double x = (Double) geometryArray.get(0);
                    Double y = (Double) geometryArray.get(1);
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

    private String geoTags(JSONObject json) {
        // string tags for datacollection
        String[] stringArray;
        String[] landslip = {"NAME", "FEATURE_TYPE", "ACTIVITY_STATE", "CLASSIFICATION_MATERIAL_TYPE", "DETAILS"};
        String[] licence = {"NAME", "OWNER", "STATUS", "EXPIREDATE", "DETAILS"};
        String[] proclaimed = {"GID", "AREA_CLASSIFICATION", "STATUTORY_RULE"};
        String[] borehole = {"NAME", "PURPOSE", "OPERATOR", "DRILLDATE", "DETAILS"};
        String[] minerals = {"DEPOSIT_NAME", "TYPE", "GEOLOGICALUNIT", "COMMODITY", "DETAILS"};

        if (isLandslideRequest) {
            stringArray = landslip;
        } else if (isProclaimedRequest) {
            stringArray = proclaimed;
        } else if (isBoreHoleRequest) {
            stringArray = borehole;
        } else if (isMineralRequest) {
            stringArray = minerals;
        } else {
            stringArray = licence;
        }

        try {
            String strOne;
            if (isProclaimedRequest) {
                strOne = String.valueOf(json.getInt(stringArray[0]));
            } else {
                strOne = json.getString(stringArray[0]);
            }
            String strTwo = json.getString(stringArray[1]);
            String strThree = json.getString(stringArray[2]);
            if (!isProclaimedRequest) {
                String strFour = json.getString(stringArray[3]);
                String strFive = json.getString(stringArray[4]);
                String tempFive = strFive.replace("<a target=\"_blank\" href=\"", "");
                String trimFive = tempFive.substring(0, tempFive.indexOf("\""));
                return stringArray[0] + ": " + strOne + "\n" +
                        stringArray[1] + ": " + strTwo + "\n" +
                        stringArray[2] + ": " + strThree + "\n" +
                        stringArray[3] + ": " + strFour + "\n" +
                        stringArray[4] + ": " + trimFive;
            } else {
                return stringArray[0] + ": " + strOne + "\n" +
                        stringArray[1] + ": " + strTwo + "\n" +
                        stringArray[2] + ": " + strThree;
            }

        } catch (JSONException e) {
            Log.i("JSONException", e.getLocalizedMessage());
            return "error";
        }
    }

    private String setTags(String param1, String param2, String param3, String param4) {
        String param1out;
        String param2out;
        String param3out;
        String param4out;
        if (param1.equals("null")) {
            param1out = "No " + PARAM1;
        } else {
            param1out = PARAM1 + ": " + param1;
        }
        if (param2.equals("null")) {
            param2out = "No " + PARAM2;
        } else {
            param2out = PARAM2 + ": " + param2;
        }
        switch (param3) {
            case "null":
                param3out = "No " + PARAM3;
                break;
            case "empty":
                param3out = "";
                break;
            default:
                param3out = PARAM3 + ": " + param3;
                break;
        }
        switch (param4) {
            case "null":
                param4out = "No " + PARAM4;
                break;
            case "empty":
                param4out = "";
                break;
            default:
                param4out = PARAM4 + ": " + param4;
                break;
        }
        if (param3out.equals("")) {
            return param1out + "\n" + param2out;
        } else if (param4out.equals("")) {
            return param1out + "\n" + param2out + "\n" + param3out;
        } else {
            return param1out + "\n" + param2out + "\n" + param3out + "\n" + param4out;
        }
    }

    private void setLastLocation() {
        LatLng newLocation = mMap.getCameraPosition().target;
        SharedPreferences.Editor prefEditor =
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
        prefEditor.putString("lat", String.valueOf(newLocation.latitude));
        prefEditor.putString("lon", String.valueOf(newLocation.longitude));
        prefEditor.apply();
    }

    // location stuff
    @Override
    protected void onPause() {
        super.onPause();
        if (mRequestingLocationUpdates) {
            stopLocationUpdates();
        }
    }

    private void stopLocationUpdates() {
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        setLastLocation();
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
