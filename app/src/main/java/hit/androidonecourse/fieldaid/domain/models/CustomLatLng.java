package hit.androidonecourse.fieldaid.domain.models;

import com.google.android.gms.maps.model.LatLng;

public class CustomLatLng  {
    private double lat;
    private double lng;

    public CustomLatLng() {
    }

    public CustomLatLng(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
