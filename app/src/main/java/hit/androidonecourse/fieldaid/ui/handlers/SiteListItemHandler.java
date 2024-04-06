package hit.androidonecourse.fieldaid.ui.handlers;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.navigation.Navigation;

import hit.androidonecourse.fieldaid.R;
import hit.androidonecourse.fieldaid.domain.RepositoryMediator;
import hit.androidonecourse.fieldaid.ui.views.activities.ActivityMapView;

public class SiteListItemHandler {

    private Context context;
    private RepositoryMediator repositoryMediator;

    public SiteListItemHandler(Context context) {
        this.context = context;
        repositoryMediator = RepositoryMediator.getInstance(context);
    }

    public void onEditButtonClicked(View view){
        //TODO : open edit site activity
        Log.d("FieldAid", "onEditButtonClicked:SiteListItemListener ");
        Navigation.findNavController(view).navigate(R.id.action_fragmentSitesView_to_fragmentEditSiteView);
        Log.d("FieldAid", "onEditButtonClicked:view is " + view.toString());

    }
    public void onLocationButtonClicked(View view){
        //TODO: open google maps activity
        Log.d("FieldAid", "onLocationButtonClicked: SiteListItemListener ");
        Intent intent = new Intent(view.getContext(), ActivityMapView.class);
        context.startActivity(intent);
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
