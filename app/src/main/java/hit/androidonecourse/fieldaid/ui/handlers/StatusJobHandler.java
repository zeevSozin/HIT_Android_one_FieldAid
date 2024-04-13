package hit.androidonecourse.fieldaid.ui.handlers;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import androidx.navigation.Navigation;

import hit.androidonecourse.fieldaid.R;
import hit.androidonecourse.fieldaid.domain.models.CustomLatLng;

public class StatusJobHandler extends ListItemHandlerBase{

    public StatusJobHandler(Context context) {
        super(context);
    }

    public void onNavigateToSiteClicked(View view){
        CustomLatLng sourcescustomLatLng = repositoryMediator.getCustomLocationManager().getCustomLatLngCurrentLocation();
        CustomLatLng destinationCustomLatLng = repositoryMediator.getCurrentSite().getLatLongMapString();
        String source = sourcescustomLatLng.getLat() + "," + sourcescustomLatLng.getLng();
        String destination = destinationCustomLatLng.getLat() + "," + destinationCustomLatLng.getLng();
        Uri uri = Uri.parse("https://www.google.com/maps/dir/"+ source + "/" + destination);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setPackage("com.google.android.apps.maps");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        view.getContext().startActivity(intent);

    }

    public void onNavigateToTasksClicked(View view){
        Log.d("FieldAid", "onNavigateToTasksClicked: status view");
        repositoryMediator.setNavigationOriginToTasks("status");
        Navigation.findNavController(view).navigate(R.id.action_fragmentStatusView_to_fragmentTasksView2);

    }

}
