package hit.androidonecourse.fieldaid.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import hit.androidonecourse.fieldaid.domain.models.CustomLatLng;

public class CustomLocationManager {
    private Context context;

    private Location currentLocation;
    FusedLocationProviderClient fusedLocationClient;


    private CustomLatLng customLatLngCurrentLocation;

    public CustomLocationManager(Context context) {
        this.context = context;

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {

                fusedLocationClient.getLastLocation().addOnCompleteListener(task -> {
                    currentLocation = task.getResult();
                    Log.d("FieldAid", "CustomLocationManager: Got current location - lat:" + currentLocation.getLatitude() + " lng:" + currentLocation.getLongitude());
                });

            }

    }


    public Location getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            askForPermissions();
        }
        else {
            fusedLocationClient.getLastLocation().addOnCompleteListener(task -> {
                currentLocation = task.getResult();
                Log.d("FieldAid", "CustomLocationManager: Got current location - lat:" + currentLocation.getLatitude() + " lng:" + currentLocation.getLongitude());
            });
        }
        return currentLocation;
    }

    public CustomLatLng getCustomLatLngCurrentLocation() {
        customLatLngCurrentLocation = new CustomLatLng(31.7683, 35.2137);
        if(currentLocation != null){
            customLatLngCurrentLocation = new CustomLatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        }
        Log.d("FieldAid", "getCustomLatLngCurrentLocation: location is lat: " + customLatLngCurrentLocation.getLat()+ "lng: "+ customLatLngCurrentLocation.getLng());
        return customLatLngCurrentLocation;
    }

    private void askForPermissions(){
        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
    }


}
