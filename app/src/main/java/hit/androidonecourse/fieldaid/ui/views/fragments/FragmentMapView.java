package hit.androidonecourse.fieldaid.ui.views.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Objects;

import hit.androidonecourse.fieldaid.R;
import hit.androidonecourse.fieldaid.domain.RepositoryMediator;
import hit.androidonecourse.fieldaid.domain.models.CustomLatLng;
import hit.androidonecourse.fieldaid.domain.models.Site;
import hit.androidonecourse.fieldaid.util.CustomLocationManager;


public class FragmentMapView extends Fragment {
    RepositoryMediator repositoryMediator;
    Site currentSite;
    CustomLocationManager customLocationManager;
    Location location;
    GoogleMap map;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_map_view, container, false);

        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.google_map);

        repositoryMediator = RepositoryMediator.getInstance(this.getContext());
        currentSite = repositoryMediator.getCurrentSite();


        customLocationManager = repositoryMediator.getCustomLocationManager();
        location = customLocationManager.getCurrentLocation();
        Log.d("FieldAid", "onCreateView: location asked from custom location manage: "+ location);


        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                map = googleMap;
                LatLng siteLocation = null;
                MarkerOptions siteMarker = new MarkerOptions();
                if(currentSite.getLatLongMapString()!= null) {
                    Log.d("FieldAid", "onMapReady: current site is " + currentSite.getName() + ":" + currentSite.getLatLongMapString());
                    siteLocation = new LatLng(currentSite.getLatLongMapString().getLat(), currentSite.getLatLongMapString().getLng());
                    siteMarker.icon(BitmapFromVector(getContext(),R.drawable.ic_coutage_32));
                } else if (currentSite.getLatLongMapString()== null) {
                    if(location!= null){
                        siteLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    }
                    else {
                        siteLocation = new LatLng(31.7683, 35.2137);
                    }
                    siteMarker.icon(BitmapFromVector(getContext(),R.drawable.ic__not_listed_location_32));
                }
                siteMarker.position(siteLocation);
                siteMarker.title(currentSite.getName());

                siteMarker.draggable(true);
                map.addMarker(siteMarker);



                LatLng currentLocation;
                if(location!=null){
                    Log.d("FieldAid", "onMapReady: location is" + location);
                    currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                }else{
                    currentLocation = new LatLng(31.7683, 35.2137);
                }


                map.addMarker(new MarkerOptions().position(currentLocation).title("your location").draggable(true));

                if(siteLocation != null){
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(siteLocation, 14.0f));
                }
                else {
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 14.0f));
                }


                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(@NonNull LatLng latLng) {
//                        MarkerOptions markerOptions = new MarkerOptions();
//                        markerOptions.position(latLng);

                    }
                });
                googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                    @Override
                    public void onMarkerDrag(@NonNull Marker marker) {
                        if(!Objects.equals(marker.getTitle(), "your location")){
                            CustomLatLng customLatLng = new CustomLatLng(marker.getPosition().latitude, marker.getPosition().longitude);
                            repositoryMediator.setSiteUpdateLocation(customLatLng);
                        }

                        Log.d("FieldAid", "onMarkerDrag: ");

                    }

                    @Override
                    public void onMarkerDragEnd(@NonNull Marker marker) {
                        if(!Objects.equals(marker.getTitle(), "your location")){
                            CustomLatLng customLatLng = new CustomLatLng(marker.getPosition().latitude, marker.getPosition().longitude);
                            repositoryMediator.setSiteUpdateLocation(customLatLng);
                            Log.d("FieldAid", "onMarkerDragEnd: update site location " + repositoryMediator.getSiteUpdateLocation().getLat());
                        }

                    }

                    @Override
                    public void onMarkerDragStart(@NonNull Marker marker) {
                        Log.d("FieldAid", "onMarkerDragStart: ");

                    }
                });
            }
        });


        return view;
    }

    private BitmapDescriptor BitmapFromVector(Context context, int vectorResId)
    {
        // below line is use to generate a drawable.
        Drawable vectorDrawable = ContextCompat.getDrawable(
                context, vectorResId);

        // below line is use to set bounds to our vector
        // drawable.
        vectorDrawable.setBounds(
                0, 0, vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight());

        // below line is use to create a bitmap for our
        // drawable which we have added.
        Bitmap bitmap = Bitmap.createBitmap(
                vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888);

        // below line is use to add bitmap in our canvas.
        Canvas canvas = new Canvas(bitmap);

        // below line is use to draw our
        // vector drawable in canvas.
        vectorDrawable.draw(canvas);

        // after generating our bitmap we are returning our
        // bitmap.
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("FiledAid", "onDetach: Map Fragment");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("FiledAid", "onPause:  Map Fragment");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("FieldAid", "onDestroyView: Map fragment ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("FieldAid", "onDestroy: Map fragment is distroyed");
    }
}