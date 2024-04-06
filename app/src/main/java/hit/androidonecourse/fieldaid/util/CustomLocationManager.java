package hit.androidonecourse.fieldaid.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.model.LatLng;

import hit.androidonecourse.fieldaid.domain.models.CustomLatLng;

public class CustomLocationManager {
    private Context context;
    private LocationManager locationManager;
    private Location currentLocation;



    private CustomLatLng customLatLngCurrentLocation;

    public CustomLocationManager(Context context) {
        this.context = context;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                }

            }

    }

    public Location getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            askForPermissions();
        }
        else {
            currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        Log.d("FieldAid", "getCurrentLocation: " + currentLocation);
        return currentLocation;
    }

    public CustomLatLng getCustomLatLngCurrentLocation() {
        customLatLngCurrentLocation = new CustomLatLng(31.7683, 35.2137);
        if(currentLocation != null){
            customLatLngCurrentLocation = new CustomLatLng(currentLocation.getAltitude(), currentLocation.getLongitude());
        }
        return customLatLngCurrentLocation;
    }

    private void askForPermissions(){
        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
    }


}
