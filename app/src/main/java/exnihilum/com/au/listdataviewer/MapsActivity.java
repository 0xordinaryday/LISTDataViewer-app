package exnihilum.com.au.listdataviewer;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
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
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.SphericalUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import Utilities.ColorMappingHelper;
import Utilities.CoordinateConversion;
import Utilities.ParametersHelper;
import Utilities.TileProviderFactory;

import static exnihilum.com.au.listdataviewer.R.id.map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    /**
     * Tag for the log messages
     */
    public static final String LOG_TAG = MapsActivity.class.getSimpleName();
    // refactor
    private static final String TAG = MapsActivity.class.getSimpleName();
    // Code used in requesting runtime permissions.
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    // Constant used in the location settings dialog.
    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    // The desired interval for location updates. Inexact. Updates may be more or less frequent.
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    // Keys for storing activity state in the Bundle.
    private final static String KEY_REQUESTING_LOCATION_UPDATES = "requesting-location-updates";
    private final static String KEY_LOCATION = "location";
    private final static String KEY_LAST_UPDATED_TIME_STRING = "last-updated-time-string";
    private static GoogleMap mMap;
    private static String geometryType;
    private static String finalRequestString;
    private static boolean canMakeServerRequest = true;
    private static LatLng currentPosition;
    private static String layerName;
    private static int alphaValue;
    // end refactor
    private static Drawable drawable;
    // polygon list, needs global access
    private static ArrayList<Polygon> polygonList = null;
    // arraylist for markers
    private static ArrayList<Marker> markers = new ArrayList<>();
    LayerType selectedType = null;
    // Provides access to the Fused Location Provider API.
    private FusedLocationProviderClient mFusedLocationClient;
    // Provides access to the Location Settings API.
    private SettingsClient mSettingsClient;
    // Stores parameters for requests to the FusedLocationProviderApi.
    private LocationRequest mLocationRequest;
    /**
     * Stores the types of location services the client is interested in using. Used for checking
     * settings to determine if the device has optimal location settings.
     */
    private LocationSettingsRequest mLocationSettingsRequest;
    // Callback for Location events.
    private LocationCallback mLocationCallback;
    /**
     * Represents a geographical location.
     */
    private Location mCurrentLocation;
    private Boolean mRequestingLocationUpdates;
    // Time when the location was updated represented as a String.
    private String mLastUpdateTime;
    private boolean isGeologyRequest = false;
    private boolean isCOLRequest = false;
    private boolean isHCCRequest = false;
    private ArrayList<LayerType> layers = ParametersHelper.layerTypes();
    private String PARAM1;
    private String PARAM2;
    private String PARAM3;
    private String PARAM4;
    private ProgressBar progressBar;
    private LatLng initialPosition;
    private float ZOOM_LEVEL = 17;
    private TextView callout;
    private boolean hasSecondLayer = false;
    private String mapValue = "";
    private SeekBar opacitySlider;
    private TextView opacityText;
    private TextView locationText;
    private TextView coordinatesText;
    private ImageView menuButton;
    private View sliderBackground;
    private View locationBackground;
    private View spacer;
    private RadioGroup radioGroup;
    private boolean isMenuShowing = false;
    private static CoordinateConversion mCoordinateConversion;

    // your location
    private static Marker mMarker;
    // search window
    private Polygon searchWindow;

    // code to get bitmap from SVG file
    // http://stackoverflow.com/questions/33696488/getting-bitmap-from-vector-drawable
    private static Bitmap getBitmapFromDrawable() {
        if (drawable != null) {
            Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        }
        return null;
    }

    /**
     * URL to query
     */
    // query parameters
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        drawable = VectorDrawableCompat.create(getResources(), R.drawable.ic_my_location_black_24px, null);

        // find and hide views
        callout = (TextView) findViewById(R.id.text_callout);
        callout.setMovementMethod(LinkMovementMethod.getInstance());
        callout.setVisibility(View.INVISIBLE);

        opacityText = (TextView) findViewById(R.id.opacityText);
        locationText = (TextView) findViewById(R.id.locationText);
        menuButton = (ImageView) findViewById(R.id.mapMenuButton);
        opacitySlider = (SeekBar) findViewById(R.id.opacitySlider);
        sliderBackground = findViewById(R.id.sliderBackground);
        spacer = findViewById(R.id.spacer);
        locationBackground = findViewById(R.id.locationBackground);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        coordinatesText = (TextView) findViewById(R.id.coords_callout);

        opacitySlider.setVisibility(View.INVISIBLE);
        opacityText.setVisibility(View.INVISIBLE);
        sliderBackground.setVisibility(View.INVISIBLE);
        locationBackground.setVisibility(View.INVISIBLE);
        locationText.setVisibility(View.INVISIBLE);
        radioGroup.setVisibility(View.INVISIBLE);
        spacer.setVisibility(View.INVISIBLE);
        coordinatesText.setVisibility(View.INVISIBLE);
        menuButton.setColorFilter(Color.BLACK);

        // set onClick for menu button
        menuButton.setOnClickListener(view -> {
            if (!isMenuShowing) {
                opacitySlider.setVisibility(View.VISIBLE);
                opacityText.setVisibility(View.VISIBLE);
                sliderBackground.setVisibility(View.VISIBLE);
                locationText.setVisibility(View.VISIBLE);
                locationBackground.setVisibility(View.VISIBLE);
                radioGroup.setVisibility(View.VISIBLE);
                spacer.setVisibility(View.VISIBLE);
                menuButton.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
                RadioButton locationOK = (RadioButton) findViewById(R.id.locationOK);
                RadioButton locationNO = (RadioButton) findViewById(R.id.locationNO);
                if (mRequestingLocationUpdates) {
                    locationOK.setChecked(true);
                } else {
                    locationNO.setChecked(true);
                }
                isMenuShowing = true;
            } else {
                opacitySlider.setVisibility(View.INVISIBLE);
                opacityText.setVisibility(View.INVISIBLE);
                sliderBackground.setVisibility(View.INVISIBLE);
                locationText.setVisibility(View.INVISIBLE);
                locationBackground.setVisibility(View.INVISIBLE);
                radioGroup.setVisibility(View.INVISIBLE);
                spacer.setVisibility(View.INVISIBLE);
                menuButton.setImageResource(android.R.drawable.ic_menu_add);
                isMenuShowing = false;
            }
        });

        // get initial position from shared preferences, and check if navigation allowed
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String latString = prefs.getString("lat", "-41.436033");
        String lonString = prefs.getString("lon", "147.138470");
        alphaValue = prefs.getInt("alpha", 100);
        ZOOM_LEVEL = prefs.getFloat("zoom", 17);
        initialPosition = new LatLng(Double.valueOf(latString), Double.valueOf(lonString));

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);

        String[] envelopeArray = generateEnvelope();

        // set progress bar
        progressBar = (ProgressBar) findViewById(R.id.indeterminateBar);

        // set opacity slider initial value
        opacitySlider.setProgress(alphaValue);
        // set listener for sliding
        opacitySlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int positionValue = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int position, boolean fromUser) {
                positionValue = position;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                alphaValue = positionValue;
                SharedPreferences.Editor prefEditor =
                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                prefEditor.putInt("alpha", positionValue);
                prefEditor.apply();
                if (polygonList != null) {
                    // style polygons
                    for (Polygon polygon : polygonList) {
                        int fillColor = polygon.getFillColor();
                        int r = Color.red(fillColor);
                        int g = Color.green(fillColor);
                        int b = Color.blue(fillColor);
                        polygon.setFillColor(Color.argb(alphaValue, r, g, b));
                    }
                }
            }
        });

        Intent createIntent = getIntent();
        layerName = createIntent.getStringExtra("layerName");
        String server = createIntent.getStringExtra("server");
        mapValue = createIntent.getStringExtra("mapValue");

        switch (server) {
            case "MRT":
                String geologyString = makeGeologyString(layerName, generateEnvelope());
                isGeologyRequest = true;
                finalRequestString = geologyString;
                break;
            default:
                for (LayerType type : layers) {
                    if (type.isNameEqualTo(layerName)) {
                        selectedType = type;
                    }
                }
                break;
        }

        if (server.equals("COL")) {
            isCOLRequest = true;
        } else if (server.equals("HCC")) {
            isHCCRequest = true;
        }

        // create a list to contain the MRT point datasets - the others are all polygons
        ArrayList<String> geologyPointRequests = new ArrayList<>(Arrays.asList("mrtwfs:Boreholes", "mrtwfs:MineralOccurences"));

        if (!isGeologyRequest) {
            finalRequestString = generateString(selectedType, envelopeArray);
            Log.i(LOG_TAG, finalRequestString);
            geometryType = selectedType.getGeometryType(); // rings, paths or none
        } else if (isGeologyRequest && geologyPointRequests.contains(layerName)) {
            geometryType = "none";
        } else {
            geometryType = "rings";
        }

        mRequestingLocationUpdates = false;
        mLastUpdateTime = "";

        // Update values using data stored in the Bundle.
        updateValuesFromBundle(savedInstanceState);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);

        // Kick off the process of building the LocationCallback, LocationRequest, and
        // LocationSettingsRequest objects, and create an instance of coordinate conversion
        createLocationCallback();
        createLocationRequest();
        buildLocationSettingsRequest();
        mCoordinateConversion = new CoordinateConversion();

        chooseTaskAndExecute();
        progressBar.setVisibility(View.VISIBLE);
    }

    // onclick method for radio button to turn location updating on or off
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.locationOK:
                if (checked)
                    startLocationUpdates();
                    Toast.makeText(this, "Location services will be started", Toast.LENGTH_SHORT).show();
                break;
            case R.id.locationNO:
                if (checked)
                    stopLocationUpdates();
                    Toast.makeText(this, "Location will not be used", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * Updates fields based on data stored in the bundle.
     *
     * @param savedInstanceState The activity state saved in the Bundle.
     */
    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            // Update the value of mRequestingLocationUpdates from the Bundle, and make sure that
            // the Start Updates and Stop Updates buttons are correctly enabled or disabled.
            if (savedInstanceState.keySet().contains(KEY_REQUESTING_LOCATION_UPDATES)) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean(
                        KEY_REQUESTING_LOCATION_UPDATES);
            }

            // Update the value of mCurrentLocation from the Bundle and update the UI to show the
            // correct latitude and longitude.
            if (savedInstanceState.keySet().contains(KEY_LOCATION)) {
                // Since KEY_LOCATION was found in the Bundle, we can be sure that mCurrentLocation
                // is not null.
                mCurrentLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            }

            // Update the value of mLastUpdateTime from the Bundle and update the UI.
            if (savedInstanceState.keySet().contains(KEY_LAST_UPDATED_TIME_STRING)) {
                mLastUpdateTime = savedInstanceState.getString(KEY_LAST_UPDATED_TIME_STRING);
            }
        }
    }

    /**
     * Sets up the location request. Android has two location request settings:
     * {@code ACCESS_COARSE_LOCATION} and {@code ACCESS_FINE_LOCATION}. These settings control
     * the accuracy of the current location. This sample uses ACCESS_FINE_LOCATION, as defined in
     * the AndroidManifest.xml.
     * <p/>
     * When the ACCESS_FINE_LOCATION setting is specified, combined with a fast update
     * interval (5 seconds), the Fused Location Provider API returns location updates that are
     * accurate to within a few feet.
     * <p/>
     * These settings are appropriate for mapping applications that show real-time location
     * updates.
     */
    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /**
     * Creates a callback for receiving location events.
     */
    private void createLocationCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                mCurrentLocation = locationResult.getLastLocation();
                // add coordinates to screen
                String UTMcoords = "Position is: " + mCoordinateConversion.latLon2UTM(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                coordinatesText.setText(UTMcoords);
                coordinatesText.setVisibility(View.VISIBLE);
                updateLocationUI();
            }
        };
    }

    /**
     * Uses a {@link com.google.android.gms.location.LocationSettingsRequest.Builder} to build
     * a {@link com.google.android.gms.location.LocationSettingsRequest} that is used for checking
     * if a device has the needed location settings.
     */
    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.i(TAG, "User agreed to make required location settings changes.");
                        // Nothing to do. startLocationupdates() gets called in onResume again.
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.i(TAG, "User chose not to make required location settings changes.");
                        mRequestingLocationUpdates = false;
                        break;
                }
                break;
        }
    }

    /**
     * Requests location updates from the FusedLocationApi. Note: we don't call this unless location
     * runtime permission has been granted.
     */
    @SuppressWarnings({"MissingPermission"})
    private void startLocationUpdates() {
        if (!mRequestingLocationUpdates) {
            mRequestingLocationUpdates = true;
        }
        // Begin by checking if the device has the necessary location settings.
        mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(this, locationSettingsResponse -> {
                    Log.i(TAG, "All location settings are satisfied.");

                    //noinspection MissingPermission
                    mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                            mLocationCallback, Looper.myLooper());

                })
                .addOnFailureListener(this, e -> {
                    int statusCode = ((ApiException) e).getStatusCode();
                    switch (statusCode) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
                                    "location settings ");
                            try {
                                // Show the dialog by calling startResolutionForResult(), and check the
                                // result in onActivityResult().
                                ResolvableApiException rae = (ResolvableApiException) e;
                                rae.startResolutionForResult(MapsActivity.this, REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException sie) {
                                Log.i(TAG, "PendingIntent unable to execute request.");
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            String errorMessage = "Location settings are inadequate, and cannot be " +
                                    "fixed here. Fix in Settings.";
                            Log.e(TAG, errorMessage);
                            Toast.makeText(MapsActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                            mRequestingLocationUpdates = false;
                    }

                });
    }

    /**
     * Sets the value of the UI fields for the location latitude, longitude and last update time.
     */
    private void updateLocationUI() {
        if (mCurrentLocation != null) {
            // set progress bar invisible - it will get called visible again shortly if necessary
            progressBar.setVisibility(View.INVISIBLE);
            currentPosition = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
            // get the current zoom level, don't rezoom
            float currentZoom = mMap.getCameraPosition().zoom;
            float[] results = new float[3];
            Location.distanceBetween(
                    currentPosition.latitude,
                    currentPosition.longitude,
                    initialPosition.latitude,
                    initialPosition.longitude,
                    results);
            if (results[0] > 10.0 && canMakeServerRequest) {
                initialPosition = currentPosition;
                // save to shared preferences
                SharedPreferences.Editor prefEditor =
                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                prefEditor.putString("lat", String.valueOf(currentPosition.latitude));
                prefEditor.putString("lon", String.valueOf(currentPosition.longitude));
                prefEditor.apply();
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, currentZoom));
                mMap.clear();
                if (!isGeologyRequest) {
                    finalRequestString = generateString(selectedType, generateEnvelope());
                } else {
                    finalRequestString = makeGeologyString(layerName, generateEnvelope());
                }
                addSearchPolygon();
                // make new server request
                chooseTaskAndExecute();
            } else if (results[0] <= 10.0 && canMakeServerRequest) {
                initialPosition = currentPosition;
                // save to shared preferences
                SharedPreferences.Editor prefEditor =
                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                prefEditor.putString("lat", String.valueOf(currentPosition.latitude));
                prefEditor.putString("lon", String.valueOf(currentPosition.longitude));
                prefEditor.apply();
                if (mMarker != null) {
                    mMarker.setPosition(currentPosition);
                }
                addSearchPolygon();
            }
        }
    }

    /**
     * Removes location updates from the FusedLocationApi.
     */
    private void stopLocationUpdates() {
        if (!mRequestingLocationUpdates) {
            Log.d(TAG, "stopLocationUpdates: updates never requested, no-op.");
            return;
        }

        // Remove location requests if paused or stopped
        mFusedLocationClient.removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        coordinatesText.setVisibility(View.INVISIBLE);
                        mRequestingLocationUpdates = false;
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        // Within {@code onPause()}, we remove location updates. Here, we resume receiving
        // location updates if the user has requested them.
        if (mRequestingLocationUpdates && checkPermissions()) {
            startLocationUpdates();
        } else if (!checkPermissions()) {
            requestPermissions();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Remove location updates to save battery.
        stopLocationUpdates();
    }

    /**
     * Stores activity data in the Bundle.
     */
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(KEY_REQUESTING_LOCATION_UPDATES, mRequestingLocationUpdates);
        savedInstanceState.putParcelable(KEY_LOCATION, mCurrentLocation);
        savedInstanceState.putString(KEY_LAST_UPDATED_TIME_STRING, mLastUpdateTime);
        super.onSaveInstanceState(savedInstanceState);
    }

    /**
     * Shows a {@link Snackbar}.
     *
     * @param mainTextStringId The id for the string resource for the Snackbar text.
     * @param actionStringId   The text of the action item.
     * @param listener         The listener associated with the Snackbar action.
     */
    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(
                findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }

    /**
     * Return the current state of the permissions needed.
     */
    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
            showSnackbar(R.string.permission_rationale,
                    android.R.string.ok, view -> {
                        // Request permission
                        ActivityCompat.requestPermissions(MapsActivity.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                REQUEST_PERMISSIONS_REQUEST_CODE);
                    });
        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(MapsActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (mRequestingLocationUpdates) {
                    Log.i(TAG, "Permission granted, updates requested, starting location updates");
                    startLocationUpdates();
                }
            } else {
                // Permission denied.

                // Notify the user via a SnackBar that they have rejected a core permission for the
                // app, which makes the Activity useless. In a real app, core permissions would
                // typically be best requested during a welcome-screen flow.

                // Additionally, it is important to remember that a permission might have been
                // rejected without asking the user for permission (device policy or "Never ask
                // again" prompts). Therefore, a user interface affordance is typically implemented
                // when permissions are denied. Otherwise, your app could appear unresponsive to
                // touches or interactions which have required permissions.
                showSnackbar(R.string.permission_denied_explanation,
                        R.string.settings, view -> {
                            // Build intent that displays the App settings screen.
                            Intent intent = new Intent();
                            intent.setAction(
                                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package",
                                    BuildConfig.APPLICATION_ID, null);
                            intent.setData(uri);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        });
            }
        }
    }

    private void chooseTaskAndExecute() {
        LISTMapAsyncTask task = new LISTMapAsyncTask(this);
        task.execute();
    }

    private String[] generateEnvelope() {
        // build envelope from initialPosition
        double delta;
        if (isGeologyRequest) {
            delta = 0.01;
        } else {
            delta = 0.003;
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
        String LIST_REQUEST_URL_PART1;
        String LIST_REQUEST_URL_PART5;
        String LIST_REQUEST_URL_PART2;
        if (isCOLRequest) {
            LIST_REQUEST_URL_PART1 = "http://mapping.launceston.tas.gov.au/arcgis/rest/services/Public/";
            LIST_REQUEST_URL_PART5 = "&returnGeometry=true&outSR=4326&f=pjson";
            LIST_REQUEST_URL_PART2 = "/MapServer/";
        } else if (isHCCRequest) {
            LIST_REQUEST_URL_PART1 = "https://services1.arcgis.com/";
            LIST_REQUEST_URL_PART5 = "&returnGeometry=true&returnTrueCurves=false&maxAllowableOffset=&geometryPrecision=&outSR=4326&returnIdsOnly=false&returnCountOnly=false&orderByFields=&groupByFieldsForStatistics=&outStatistics=&returnZ=false&returnM=false&gdbVersion=&returnDistinctValues=false&resultOffset=&resultRecordCount=&f=pjson";
            LIST_REQUEST_URL_PART2 = "/arcgis/rest/services/" + mapValue + "/FeatureServer/";
        } else {
            LIST_REQUEST_URL_PART1 = "http://services.thelist.tas.gov.au/arcgis/rest/services/Public/";
            LIST_REQUEST_URL_PART5 = "&returnGeometry=true&returnTrueCurves=false&maxAllowableOffset=&geometryPrecision=&outSR=4326&returnIdsOnly=false&returnCountOnly=false&orderByFields=&groupByFieldsForStatistics=&outStatistics=&returnZ=false&returnM=false&gdbVersion=&returnDistinctValues=false&resultOffset=&resultRecordCount=&f=pjson";
            LIST_REQUEST_URL_PART2 = "/MapServer/";
        }

        String LIST_REQUEST_URL_PART3 = "/query?where=&text=&objectIds=&time=&geometry=" +
                envelope[1] +
                separator +
                envelope[0] +
                separator +
                envelope[3] +
                separator +
                envelope[2] +
                "&geometryType=esriGeometryEnvelope&inSR=4326&spatialRel=esriSpatialRelIntersects&relationParam=&outFields=";


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

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialPosition, ZOOM_LEVEL));

        // Set a listener for marker click.
        mMap.setOnMarkerClickListener(this);

        // show search polygon
        addSearchPolygon();

        // add tile overlay
        // test lol
        // TODO - implement this properly
        TileProvider wmsTileProvider = TileProviderFactory.getOsgeoWmsTileProvider();
        TileOverlayOptions tileOverlay = new TileOverlayOptions()
                .tileProvider(wmsTileProvider);
        mMap.addTileOverlay(tileOverlay);

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
                if (mRequestingLocationUpdates) {
                    stopLocationUpdates(); // stop moving, since we're dragging
                    Toast.makeText(this, "Stopping location services!", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.VISIBLE);
                initialPosition = newLocation;
                mMap.clear();
                addSearchPolygon();
                // redraw tiles after clear
                mMap.addTileOverlay(tileOverlay);
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

    private void addSearchPolygon() {
        // if it's added already, get rid of it
        if (searchWindow != null) {
            searchWindow.remove();
        }
        String[] points = generateEnvelope();
        Double lowerLeftLat = Double.valueOf(points[0]);
        Double lowerLeftLon = Double.valueOf(points[1]);
        Double upperRightLat = Double.valueOf(points[2]);
        Double upperRightLon = Double.valueOf(points[3]);

        PolygonOptions rectOptions = new PolygonOptions()
                .add(new LatLng(lowerLeftLat, lowerLeftLon),
                        new LatLng(lowerLeftLat, upperRightLon),
                        new LatLng(upperRightLat, upperRightLon),
                        new LatLng(upperRightLat, lowerLeftLon),
                        new LatLng(lowerLeftLat, lowerLeftLon));
        // Get back the mutable Polygon
        searchWindow = mMap.addPolygon(rectOptions);
        searchWindow.setStrokeWidth(3);
        searchWindow.setStrokeColor(Color.argb(255, 255, 109, 0));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        for (Marker eachMarker : markers) {
            eachMarker.setIcon(BitmapDescriptorFactory.defaultMarker());
        }
        if (marker.getTag() != null && !marker.getTag().equals("Added location")) {
            String textToSet = marker.getTag().toString();
            callout.setText(textToSet);
            callout.setVisibility(View.VISIBLE);
            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        }
        return true;
    }

    // new version using JSONPolyfeature object
    private ArrayList<JSONPolyfeature> extractPolyFromJson(String listMapJSON) {
        if (TextUtils.isEmpty(listMapJSON)) { // checks for null and empty string
            return null;
        }
        try { // check if there was an error
            JSONObject baseJsonResponse = new JSONObject(listMapJSON);
            if (baseJsonResponse.has("error")) {
                return null;
            }

            // setup object to hold data
            ArrayList<JSONPolyfeature> featureList = new ArrayList<>();

            JSONArray featureArray = baseJsonResponse.getJSONArray("features");
            int length = featureArray.length();
            for (int i = 0; i < length; i++) {
                JSONObject attributes = featureArray.getJSONObject(i);
                JSONObject geometry = attributes.getJSONObject("geometry");
                // differentiate between MRT and The LIST
                String tagToSet;
                JSONArray geometryArray;
                if (!isGeologyRequest) {
                    tagToSet = setTags(attributes.getJSONObject("attributes"));
                } else {
                    tagToSet = geoTags(attributes.getJSONObject("properties"));
                }

                if (geometryType.equals("paths") || geometryType.equals("rings")) {
                    // differentiate between MRT and The LIST
                    if (!isGeologyRequest) {
                        geometryArray = geometry.getJSONArray(geometryType);
                    } else {
                        geometryArray = geometry.getJSONArray("coordinates");
                    }
                    // If there are results in the features array
                    if (geometryArray.length() > 0) {
                        for (int counter = 0; counter < geometryArray.length(); counter++) {
                            // make a new poly feature - can be polygon or polyline
                            JSONPolyfeature newPolyFeature = new JSONPolyfeature();
                            // make a List<LatLng> for the object geometry
                            ArrayList<LatLng> newGeometryList = new ArrayList<>();
                            JSONArray workingArray;
                            if (!isGeologyRequest) { // The LIST
                                workingArray = geometryArray.getJSONArray(counter);
                            } else { // MRT request has extra level of nexting
                                workingArray = geometryArray.getJSONArray(counter).getJSONArray(0);
                            }

                            int featureLength = workingArray.length();
                            for (int j = 0; j < featureLength; j++) {
                                try {
                                    double Lon = (workingArray.getJSONArray(j).getDouble(0));
                                    double Lat = (workingArray.getJSONArray(j).getDouble(1));
                                    LatLng toAppend = new LatLng(Lat, Lon);
                                    newGeometryList.add(toAppend);
                                } catch (NullPointerException e) {
                                    Log.i(LOG_TAG, "value was null");
                                }
                            }
                            // set the geometry list to the object
                            newPolyFeature.setGeometry(newGeometryList);
                            if (counter == 0) {
                                // if this is the FIRST object from the array, set the name
                                newPolyFeature.setName(tagToSet);
                                featureList.add(newPolyFeature);
                            } else {
                                JSONPolyfeature firstFeature = featureList.get(featureList.size() - 1); // get the most recent entry
                                double firstArea = SphericalUtil.computeSignedArea(firstFeature.getGeometry());
                                double thisArea = SphericalUtil.computeSignedArea(newPolyFeature.getGeometry());
                                // boolean thisSmaller = Math.abs(thisArea) < Math.abs(firstArea);
                                boolean oppositeSigns = (firstArea > 0 && thisArea < 0) || (firstArea < 0 && thisArea > 0);
                                if (geometryType.equals("rings") && !isGeologyRequest && oppositeSigns) { // assume any further parts are holes.
                                    // if (geometryType.equals("rings") && thisSmaller && oppositeSigns) {

                                    // has holes is NOT applicable to paths/polylines
                                    // has holes - if not not setup previously, do now:
                                    if (!firstFeature.hasHoles()) {
                                        firstFeature.setHoles(new ArrayList<>());
                                        firstFeature.setHasHoles(true);
                                    }
                                    // this is a hole, so it doesn't get a name, and it gets added to the
                                    // hole list, not the main list
                                    firstFeature.addHoleToList(newGeometryList);
                                } else {
                                    // not a hole, but a second/third/nth part to the shape
                                    newPolyFeature.setName(tagToSet);
                                    featureList.add(newPolyFeature);
                                }
                            }
                        }
                    }
                } else if (geometryType.equals("none")) { // for point features
                    Double x;
                    Double y;
                    if (isGeologyRequest) {
                        geometryArray = geometry.getJSONArray("coordinates");
                        x = (Double) geometryArray.get(0);
                        y = (Double) geometryArray.get(1);
                    } else {
                        x = geometry.getDouble("x");
                        y = geometry.getDouble("y");
                    }
                    LatLng newLatLng = new LatLng(y, x);
                    JSONPolyfeature newPointFeature = new JSONPolyfeature(tagToSet, newLatLng);
                    featureList.add(newPointFeature);
                }
            }
            return featureList;
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the JSON results", e);
        }
        return null;
    }

    private String geoTags(JSONObject json) {
        // string tags for feature
        String[] stringArray;
        String[] landslip = {"NAME", "FEATURE_TYPE", "ACTIVITY_STATE", "CLASSIFICATION_MATERIAL_TYPE", "DETAILS"};
        String[] licence = {"NAME", "OWNER", "STATUS", "EXPIREDATE", "DETAILS"};
        String[] proclaimed = {"GID", "AREA_CLASSIFICATION", "STATUTORY_RULE"};
        String[] borehole = {"NAME", "PURPOSE", "OPERATOR", "DRILLDATE", "DETAILS"};
        String[] minerals = {"DEPOSIT_NAME", "TYPE", "GEOLOGICALUNIT", "COMMODITY", "DETAILS"};
        boolean isProclaimedRequest = false;

        switch (layerName) {
            case "mrtwfs:LandSlidePoly":
                stringArray = landslip;
                break;
            case "mrtwfs:ProclaimedAreasPoly":
                stringArray = proclaimed;
                isProclaimedRequest = true;
                break;
            case "mrtwfs:Boreholes":
                stringArray = borehole;
                break;
            case "mrtwfs:MineralOccurences":
                stringArray = minerals;
                break;
            default:
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
                String tempTwo = strTwo.replace("(<a target=\"_blank\" href=\"", "");
                String trimTwo = tempTwo.substring(0, tempTwo.indexOf("\""));
                return stringArray[0] + ": " + strOne + "\n" +
                        stringArray[1] + ": " + trimTwo + "\n" +
                        stringArray[2] + ": " + strThree;
            }

        } catch (JSONException e) {
            Log.i("JSONException", e.getLocalizedMessage());
            return "error";
        }
    }

    private String setTags(JSONObject paramAttributes) {
        try {
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
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getLocalizedMessage());
        }
        return null;
    }

    private void addSecondLayer() {
        hasSecondLayer = true;
        SharedPreferences.Editor prefEditor =
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
        prefEditor.putString("firstLayer", layerName);
        prefEditor.apply();
    }

    private void setLastLocation() {
        LatLng newLocation = mMap.getCameraPosition().target;
        float currentZoom = mMap.getCameraPosition().zoom;
        SharedPreferences.Editor prefEditor =
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
        prefEditor.putString("lat", String.valueOf(newLocation.latitude));
        prefEditor.putString("lon", String.valueOf(newLocation.longitude));
        prefEditor.putFloat("zoom", currentZoom);
        prefEditor.apply();
    }

    private void blankSecondLayer() {
        SharedPreferences.Editor prefEditor =
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
        prefEditor.remove("firstLayer");
        prefEditor.apply();
        hasSecondLayer = false;
    }


    @Override
    protected void onStop() {
        super.onStop();
        setLastLocation();
        if (!hasSecondLayer) {
            blankSecondLayer();
        }
        stopLocationUpdates();
    }

    /**
     * {@link AsyncTask} to perform the network request on a background thread, and then
     * DO THE THINGS
     */
    private static class LISTMapAsyncTask extends AsyncTask<URL, Void, ArrayList<JSONPolyfeature>> {

        private WeakReference<MapsActivity> activityReference;

        // only retain a weak reference to the activity
        LISTMapAsyncTask(MapsActivity context) {
            activityReference = new WeakReference<>(context);
        }

        @Override
        protected ArrayList<JSONPolyfeature> doInBackground(URL... urls) {
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

            return activityReference.get().extractPolyFromJson(jsonResponse);
        }

        @Override
        protected void onPostExecute(ArrayList<JSONPolyfeature> featureList) {

            // get a reference to the activity if it is still there
            MapsActivity activity = activityReference.get();
            if (activity == null || activity.isFinishing()) return;

            if (featureList == null) {
                Toast.makeText(activityReference.get().getBaseContext(), "There was a problem with the server request",
                        Toast.LENGTH_SHORT).show();
                activityReference.get().progressBar.setVisibility(View.INVISIBLE);
                canMakeServerRequest = true;
                return;
            }
            int collectionCount = featureList.size();
            if (collectionCount == 0) {
                Toast.makeText(activityReference.get().getBaseContext(), "No results for this view", Toast.LENGTH_SHORT).show();
            }

            switch (geometryType) {
                case "rings": // for rings or MRT data
                    final ArrayList<Polygon> polyFeatures = new ArrayList<>();
                    for (JSONPolyfeature jsonPolygon : featureList) {
                        PolygonOptions rectOptions = new PolygonOptions()
                                .add(new LatLng(37.35, -122.0),
                                        new LatLng(37.45, -122.0),
                                        new LatLng(37.35, -122.2),
                                        new LatLng(37.35, -122.0));
                        // Get back the mutable Polygon
                        Polygon polygon = mMap.addPolygon(rectOptions);
                        final String tag = jsonPolygon.getName();
                        polygon.setPoints(jsonPolygon.getGeometry());
                        // set z index
                        polygon.setZIndex(1000);
                        if (jsonPolygon.hasHoles()) {
                            polygon.setHoles(jsonPolygon.getHoles());
                        }
                        doPolygonStyling(tag, polygon);
                        polygon.setTag(tag);
                        if (layerName.equals("Cadastral Parcels") && tag.contains("PID: 0")) {
                            polygon.setClickable(false);
                        } else {
                            polygon.setClickable(true);
                        }
                        polygon.setStrokeWidth(3);
                        polyFeatures.add(polygon);
                        mMap.setOnPolygonClickListener(polygon1 -> {
                            for (Polygon feature : polyFeatures) {
                                feature.setStrokeWidth(3);
                            }
                            polygon1.setStrokeWidth(7);
                            if (polygon1.getTag() != null) {
                                activityReference.get().callout.setText(polygon1.getTag().toString());
                            }
                            activityReference.get().callout.setVisibility(View.VISIBLE);
                        });
                    }
                    polygonList = polyFeatures; // keep a copy of it
                    break;
                case "paths":
                    final ArrayList<Polyline> lineFeatures = new ArrayList<>();
                    for (JSONPolyfeature jsonPolyline : featureList) {
                        PolylineOptions polyOptions = new PolylineOptions()
                                .add(new LatLng(37.35, -122.0),
                                        new LatLng(37.45, -122.0));
                        // Get back the mutable Polyline
                        Polyline polyline = mMap.addPolyline(polyOptions);
                        // set z index so it sits above the tile overlay
                        polyline.setZIndex(1000);
                        final String tag = jsonPolyline.getName();
                        polyline.setPoints(jsonPolyline.getGeometry());
                        polyline.setTag(tag);
                        polyline.setClickable(true);
                        polyline.setWidth(3);
                        lineFeatures.add(polyline);
                        mMap.setOnPolylineClickListener(polyline1 -> {
                            for (Polyline feature : lineFeatures) {
                                feature.setWidth(3);
                            }
                            polyline1.setWidth(7);
                            if (polyline1.getTag() != null) {
                                activityReference.get().callout.setText(polyline1.getTag().toString());
                            }
                            activityReference.get().callout.setVisibility(View.VISIBLE);
                        });
                    }
                    break;
                case "none":
                    markers.clear();
                    for (int i = 0; i < collectionCount; i++) {
                        final String keyValue = featureList.get(i).getName();
                        // Instantiates a new marker
                        MarkerOptions markerOptions = new MarkerOptions()
                                .position(featureList.get(i).getPointCoords())
                                .title(keyValue);
                        Marker mMarker = mMap.addMarker(markerOptions);
                        mMarker.setTag(keyValue);
                        markers.add(mMarker);
                    }
                    break;
            }

            // add current location to map - if known
            if (currentPosition != null) {
                Bitmap icon = getBitmapFromDrawable();
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(currentPosition)
                        .icon(BitmapDescriptorFactory.fromBitmap(icon))
                        .title("Current Position");
                mMarker = mMap.addMarker(markerOptions);
            }

            // make links clickable, hopefully
            activityReference.get().callout.setAutoLinkMask(Linkify.WEB_URLS);
            canMakeServerRequest = true;
            activityReference.get().progressBar.setVisibility(View.INVISIBLE);
        }

        private void doPolygonStyling(String tag, Polygon polygon) {
            switch (layerName) { //
                case "TasWater Water Serviced Land":
                    if (tag.contains("Full Service")) {
                        polygon.setFillColor(Color.argb(alphaValue, 0, 191, 214));
                    } else {
                        polygon.setFillColor(Color.argb(alphaValue, 255, 198, 39));
                    }
                    break;
                case "Tasmanian Planning Zones":
                    HashMap<String, Integer> planningColorMap = ColorMappingHelper.makePlanningOnlineColorMap(alphaValue);
                    for (String key : planningColorMap.keySet()) {
                        if (tag.contains(key)) {
                            polygon.setFillColor(planningColorMap.get(key));
                        }
                    }
                    break;
                case "Soil Types":
                    HashMap<String, Integer> soilColorMap = ColorMappingHelper.makeSoilsColorMap(alphaValue);
                    for (String key : soilColorMap.keySet()) {
                        if (tag.contains(key)) {
                            polygon.setFillColor(soilColorMap.get(key));
                        }
                    }
                    break;
                case "Authority Land":
                    HashMap<String, Integer> authorityColorMap = ColorMappingHelper.makeAuthorityColorMap(alphaValue);
                    for (String key : authorityColorMap.keySet()) {
                        if (tag.contains(key)) {
                            polygon.setFillColor(authorityColorMap.get(key));
                        }
                    }
                    break;
                case "Landslide Planning Map – Hazard Bands 20131022":
                    HashMap<String, Integer> landslideColorMap = ColorMappingHelper.makeLandslideHazardColorMap(alphaValue);
                    for (String key : landslideColorMap.keySet()) {
                        if (tag.contains(key)) {
                            polygon.setFillColor(landslideColorMap.get(key));
                        }
                    }
                    break;
                default:
                    polygon.setFillColor(Color.argb(alphaValue, 150, 50, 50));
            }
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
                    Toast.makeText(activityReference.get().getBaseContext(),
                            "A problem has occurred with the server, please try again later",
                            Toast.LENGTH_SHORT).show();
                    return jsonResponse;
                }
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } catch (SocketTimeoutException e) {
                Log.e(LOG_TAG, e.getLocalizedMessage());
                Toast.makeText(activityReference.get().getBaseContext(),
                        "The server timed out, please try again later", Toast.LENGTH_SHORT).show();
                return jsonResponse;
            } catch (IOException e) {
                Log.e(LOG_TAG, e.getLocalizedMessage());
                Toast.makeText(activityReference.get().getBaseContext(),
                        "An error occurred, please try again later", Toast.LENGTH_SHORT).show();
                return jsonResponse;
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
    }

}
