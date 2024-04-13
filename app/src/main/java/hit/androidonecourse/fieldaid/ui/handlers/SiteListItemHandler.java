package hit.androidonecourse.fieldaid.ui.handlers;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import androidx.navigation.Navigation;

import hit.androidonecourse.fieldaid.R;
import hit.androidonecourse.fieldaid.domain.RepositoryMediator;
import hit.androidonecourse.fieldaid.domain.models.CustomLatLng;
import hit.androidonecourse.fieldaid.ui.views.activities.ActivityMapView;

public class SiteListItemHandler {

    private Context context;
    private RepositoryMediator repositoryMediator;

    public SiteListItemHandler(Context context) {
        this.context = context;
        repositoryMediator = RepositoryMediator.getInstance(context);
    }

    public void onEditButtonClicked(View view){

        Log.d("FieldAid", "onEditButtonClicked:SiteListItemListener ");
        Navigation.findNavController(view).navigate(R.id.action_fragmentSitesView_to_fragmentEditSiteView);
        Log.d("FieldAid", "onEditButtonClicked:view is " + view.toString());

    }
    public void onLocationButtonClicked(View view){

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

    public void onSubmitButtonEditFragmentClicked(View view){
        Log.d("FieldAid", "onSubmitButtonEditFragmentClicked: view is" + view);
        Navigation.findNavController(view).navigate(R.id.action_fragmentEditSiteView_to_fragmentSitesView);
    }

    public void onDetailsButtonClicked(View view){
        Log.d("FielAid", "onDetailsButtonClicked: SiteListItemListener");
        Navigation.findNavController(view).navigate(R.id.action_fragmentSitesView_to_fragmentDetailsSiteView);

    }
}
