package hit.androidonecourse.fieldaid.ui.handlers;

import android.content.Context;
import android.view.View;

import androidx.navigation.Navigation;

import hit.androidonecourse.fieldaid.R;
import hit.androidonecourse.fieldaid.domain.RepositoryMediator;

public class JobListItemHandler extends ListItemHandlerBase {


    public JobListItemHandler(Context context) {
        super(context);
    }

    public void onEditButtonClicked(View view){
        Navigation.findNavController(view).navigate(R.id.action_fragmentJobsView_to_fragmentEditJobView);

    }

    public void onTasksButtonClicked(View view){
        repositoryMediator.setNavigationOriginToTasks("jobs");
        Navigation.findNavController(view).navigate(R.id.action_fragmentJobsView_to_fragmentTasksView);
    }
}
