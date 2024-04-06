package hit.androidonecourse.fieldaid.ui.handlers;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import hit.androidonecourse.fieldaid.ui.views.activities.ActivityMapView;

public class SiteListItemHandler {

    private Context context;

    public SiteListItemHandler(Context context) {
        this.context = context;
    }

    public void onEditButtonClicked(View view){
        //TODO : open edit site activity
        Log.d("FieldAid", "onEditButtonClicked:SiteListItemListener ");

    }
    public void onLocationButtonClicked(View view){
        //TODO: open google maps activity
        Log.d("FieldAid", "onLocationButtonClicked: SiteListItemListener ");
        Log.d("FieldAid", "onLocationButtonClicked:" + view.toString());
        Log.d("FieldAid", "onLocationButtonClicked:" + view.getRootView().toString());

        Intent intent = new Intent(view.getContext(), ActivityMapView.class);
        context.startActivity(intent);
    }
}
