package hit.androidonecourse.fieldaid.ui.handlers;



import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.activity.result.ActivityResultCallerLauncher;
import androidx.activity.result.ActivityResultLauncher;
import androidx.navigation.Navigation;

import hit.androidonecourse.fieldaid.R;
import hit.androidonecourse.fieldaid.domain.RepositoryMediator;
import hit.androidonecourse.fieldaid.domain.models.Project;

public class ProjectListItemHandler {
    private Context context;
    private RepositoryMediator repositoryMediator;


    public ProjectListItemHandler(Context context ) {
        this.context = context;
        repositoryMediator = RepositoryMediator.getInstance(context);



    }
    public void onEditButtonClicked(View view){
        Log.d("FieldAid", "onEditButtonClicked: Project edit button clicked");
        Log.d("FieldAid", "view is" + view);
        Navigation.findNavController(view).navigate(R.id.action_fragmentProjectsView_to_fragmentEditProjectView);

    }
    public void onDetailsButtonClicked(View view){
        Log.d("FieldAid", "onCardClicked: Project details button clicked");
        Log.d("FieldAid", "view is" + view);
        Navigation.findNavController(view).navigate(R.id.action_fragmentProjectsView_to_fragmentDetailsProjectView);
    }


}
