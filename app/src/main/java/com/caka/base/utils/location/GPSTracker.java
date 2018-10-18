package com.caka.base.utils.location;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;

import com.caka.base.utils.log.Log;

public class GPSTracker extends Service implements LocationListener {
    static final String TAG = GPSTracker.class.getName();

    public static final int DEFAULT_MIN_DISTANCE_UPDATE_LOCATION = 10;// meters
    public static final long DEFAULT_MIN_TIME_UPDATE_LOCATION = 1000 * 60 * 1;// 1 minutes


    static GPSTracker instance;
    private final Context mContext;
    // flag for GPS status
    boolean isGPSEnabled = false;
    // flag for network status
    boolean isNetworkEnabled = false;
    // flag for GPS status
    boolean canGetLocation = false;
    Location location; // location
    double latitude; // latitude
    double longitude; // longitude
    String addressesLocation;
    ChangedLocationListener locationListener;

    // The minimum distance to change Updates in meters
    private long minDistanceUpdate = DEFAULT_MIN_DISTANCE_UPDATE_LOCATION; // 10 meters
    // The minimum time between updates in milliseconds
    private long minTimeUpdate = DEFAULT_MIN_TIME_UPDATE_LOCATION; // 1 minute

    // Declaring a Location Manager
    protected LocationManager locationManager;


    public static synchronized GPSTracker getInstance(Context context) {
        if (null == instance) {
            instance = new GPSTracker(context);
        }
        return instance;
    }

    public static synchronized GPSTracker getInstance(Context context, ChangedLocationListener locationListener) {
        if (null == instance) {
            instance = new GPSTracker(context, locationListener);
        }
        return instance;
    }


    public static synchronized GPSTracker getInstance(Context context, ChangedLocationListener locationListener, long minTimeUpdate, int minDistanceUpdate) {
        if (null == instance) {
            instance = new GPSTracker(context, locationListener, minTimeUpdate, minDistanceUpdate);
        }
        return instance;
    }

    public static synchronized GPSTracker getInstance(Context context, int minDistanceUpdate) {
        if (null == instance) {
            instance = new GPSTracker(context, minDistanceUpdate);
        }
        return instance;
    }


    public GPSTracker(Context context) {
        this(context, null);
    }

    public GPSTracker(Context context, ChangedLocationListener locationListener) {
        this(context, locationListener, DEFAULT_MIN_TIME_UPDATE_LOCATION, DEFAULT_MIN_DISTANCE_UPDATE_LOCATION);
    }

    public GPSTracker(Context context, long minTimeUpdate) {
        this(context, null, minTimeUpdate);
    }

    public GPSTracker(Context context, ChangedLocationListener locationListener, long minTimeUpdate) {
        this(context, locationListener, minTimeUpdate, DEFAULT_MIN_DISTANCE_UPDATE_LOCATION);
    }

    public GPSTracker(Context context, int minDistanceUpdate) {
        this(context, null, minDistanceUpdate);
    }

    public GPSTracker(Context context, ChangedLocationListener locationListener, int minDistanceUpdate) {
        this(context, locationListener, DEFAULT_MIN_TIME_UPDATE_LOCATION, minDistanceUpdate);
    }

    public GPSTracker(Context context, long minTimeUpdate, int minDistanceUpdate) {
        this(context, null, minTimeUpdate, minDistanceUpdate);
    }

    public GPSTracker(Context context, ChangedLocationListener locationListener, long minTimeUpdate, int minDistanceUpdate) {
        this.mContext = context;
        this.locationListener = locationListener;
        this.minTimeUpdate = minTimeUpdate;
        this.minDistanceUpdate = minDistanceUpdate;
        getLocation();
    }


    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
            // getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            // getting network status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
                showSettingsAlert();
            } else {
                this.canGetLocation = true;
                if (isNetworkEnabled) {
                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return null;
                    }
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTimeUpdate, minDistanceUpdate, this);
                    location = locationManager
                            .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                    }
                }

                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return null;
                        }
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                                minTimeUpdate, minDistanceUpdate, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
            }

        } catch (Exception e) {
            Log.e(GPSTracker.class.getName()+ " - Function: getLocation ", "error: " +  e.getMessage());
            throw new RuntimeException(e);
        }

//        if (null != locationListener) {
//            locationListener.onChangedLocation(location);
//        }
        return location;
    }

    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app
     */
    public void stopUsingGPS() {
        if (locationManager != null) {
            locationManager.removeUpdates(GPSTracker.this);
        }
    }

    /**
     * Function to get latitude
     */
    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }

        // return latitude
        return latitude;
    }

    /**
     * Function to get longitude
     */
    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }
        // return longitude
        return longitude;
    }

    public String getAddressesLocation() {
        return addressesLocation;
    }

    /**
     * Function to check GPS/wifi enabled
     *
     * @return boolean
     */
    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    /**
     * Function to show settings alert dialog
     * On pressing Settings button will lauch Settings Options
     */
    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (null != locationListener) {
            locationListener.onChangedLocation(location);
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    private boolean running = false;

//    public class AddressLocationByLatLngTask extends AsyncTask<Double, Void, Void> {
//        @Override
//        protected Void doInBackground(Double... params) {
//            if (params == null || params.length != 2) {
//                throw new IllegalArgumentException("Parameter of WeatherQueryByLatLonTask is illegal."
//                        + "No Lat Lon exists.");
//            }
//            final Double lat = params[0];
//            final Double lng = params[1];
//            if (mContext != null) {
//                if (Geocoder.isPresent()) {
//                    try {
//                        final Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
//                        List<Address> addresses = geocoder.getFromLocation(lat.doubleValue(), lng.doubleValue(), 1);
//                        if (addresses != null && addresses.size() > 0) {
//                            Address addres = addresses.get(0);
//                            String locality = UtilsCakaICT.isNotNullOrEmpty(addresses.get(0).getLocality()) ?
//                                    addresses.get(0).getLocality() : addresses.get(0).getAdminArea();
//                            if (locality != null) {
//                                addressesLocation = addres.getAddressLine(0);
//                                EventBus.getDefault().post(new ChangeEvent(ChangeEvent.GET_SUCCESS_ADDRESS_SETTLEMENTS, addres));
//                            }
//                        }
//                    } catch (IOException e) {
//                        Log.e(GPSTracker.class.getName()+ " - Function: rotateBitmap ", "error: " +  e.getMessage());
//            throw new RuntimeException(e);
//                    }
//                }
//            }
//            return null;
//        }
//    }
}
