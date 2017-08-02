package exnihilum.com.au.listdataviewer;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.DrawableRes;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.data.Feature;
import com.google.maps.android.data.kml.KmlLayer;


import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import Utilities.Helper;
import Utilities.ParametersHelper;

import static android.os.Environment.getExternalStorageDirectory;
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

        // storage permissions
        verifyStoragePermissions(this);

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
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 17));
                        mMap.clear();
                        finalRequestString = generateString(selectedType, generateEnvelope());
                        // make new server request
                        DownloadFileFromURL task = new DownloadFileFromURL();
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
        DownloadFileFromURL task = new DownloadFileFromURL();
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
        String[] envelopeArray = {lowerLeftLat, lowerLeftLon, upperRightLat, upperRightLon};
        return envelopeArray;
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
        String LIST_REQUEST_URL_PART5 = "&returnGeometry=true&returnTrueCurves=false&maxAllowableOffset=&geometryPrecision=&outSR=4326&returnIdsOnly=false&returnCountOnly=false&orderByFields=&groupByFieldsForStatistics=&outStatistics=&returnZ=false&returnM=false&gdbVersion=&returnDistinctValues=false&resultOffset=&resultRecordCount=&f=kmz";

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
        Marker marker = mMap.addMarker(new MarkerOptions().position(initialPosition));
        marker.setTag("Initial Location");
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialPosition, 17));

        // Set a listener for marker click.
        mMap.setOnMarkerClickListener(this);

        // set camera move listener
        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                LatLng newLocation = mMap.getCameraPosition().target;
                float[] results = new float[3];
                Location.distanceBetween(
                        newLocation.latitude,
                        newLocation.longitude,
                        initialPosition.latitude,
                        initialPosition.longitude,
                        results);
                if (results[0] > 150.0 && canMakeServerRequest) {
                    mRequestingLocationUpdates = false; // stop moving, since we're dragging
                    progressBar.setVisibility(View.VISIBLE);
                    initialPosition = newLocation;
                    mMap.clear();
                    finalRequestString = generateString(selectedType, generateEnvelope());
                    // make new server request
                    DownloadFileFromURL task = new DownloadFileFromURL();
                    task.execute();
                }
            }
        });
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (!marker.getTag().equals("Added location")) {
            Toast.makeText(getApplicationContext(), marker.getTag().toString(), Toast.LENGTH_LONG).show();
        }
        return true;
    }

    /**
     * Background Async Task to download file
     * */
    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Bar Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            canMakeServerRequest = false;
        }

        /**
         * Returns new URL object from the given string URL.
         */
        private URL createUrl(String stringUrl) {
            URL url = null;
            try {
                url = new URL(stringUrl);
            } catch (MalformedURLException exception) {
                Log.e(LOG_TAG, "Error with creating URL", exception);
                return null;
            }
            return url;
        }

        /**
         * Downloading file in background thread
         * */
        @Override
        protected String doInBackground(String... urls) {
            canMakeServerRequest = false;
            // Create URL object
            URL url = createUrl(finalRequestString);
            int count;
            try {
                if (url != null) {
                    URLConnection connection = url.openConnection();
                    connection.connect();

                    // download the file
                    InputStream input = new BufferedInputStream(url.openStream(),
                            8192);

                    String dirPath = Environment.getExternalStorageDirectory().toString();
                    // delete if it exists already
                    File file = new File(dirPath + "/download.kmz");
                    file.delete();

                    // Output stream
                    OutputStream output = new FileOutputStream(
                            getExternalStorageDirectory().toString()
                                    + "/download.kmz");

                    byte data[] = new byte[1024];

                    while ((count = input.read(data)) != -1) {
                        // writing data to file
                        output.write(data, 0, count);
                    }

                    // flushing output
                    output.flush();

                    // closing streams
                    output.close();
                    input.close();
                }

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        /**
         * Updating progress bar
         * */
        protected void onProgressUpdate(String... progress) {
            // don't need this
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(String file_url) {
            // unzip the file
            String directoryPath = Environment.getExternalStorageDirectory().toString();
            try {
                Helper.unzip(new File(directoryPath + "/download.kmz"),
                        new File(directoryPath + "/unzip"));
            } catch (IOException e) {
                Log.i(LOG_TAG, e.getLocalizedMessage());
            }

            // convert to filestream
            FileInputStream fileInputStream = null;
            String filePath = directoryPath + "/unzip/doc.kml";
            File file = new File(filePath);
            try {
                fileInputStream = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                Log.i(LOG_TAG, e.getLocalizedMessage());
            }

            if (fileInputStream != null) {
                try {
                    KmlLayer layer = new KmlLayer(mMap, fileInputStream, getApplicationContext());
                    layer.addLayerToMap();
                    // Set a listener for geometry clicked events.
                    layer.setOnFeatureClickListener(new KmlLayer.OnFeatureClickListener() {
                        @Override
                        public void onFeatureClick(Feature feature) {
                            Log.i("KmlClick", "Feature clicked: " + feature.getProperties().toString());
                        }
                    });
                } catch (XmlPullParserException | IOException e) {
                    // do something
                }
            }
            progressBar.setVisibility(View.INVISIBLE);
            canMakeServerRequest = true;
        }
    }

    public static Bitmap getBitmapFromDrawable(Context context, @DrawableRes int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof VectorDrawable || drawable instanceof VectorDrawableCompat) {
            Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);

            return bitmap;
        } else {
            throw new IllegalArgumentException("unsupported drawable type");
        }
    }

    // location stuff
    @Override
    protected void onPause() {
        super.onPause();
        // stopLocationUpdates();
    }

    private void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
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
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MapsActivity.this,
                                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
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

    // storage permissions
    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

}
