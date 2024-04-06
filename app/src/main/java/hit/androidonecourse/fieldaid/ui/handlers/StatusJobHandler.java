package hit.androidonecourse.fieldaid.ui.handlers;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.navigation.Navigation;

import hit.androidonecourse.fieldaid.R;

public class StatusJobHandler extends ListItemHandlerBase{

    public StatusJobHandler(Context context) {
        super(context);
    }

    public void onNavigateToSiteClicked(View view){

    }

    public void onNavigateToTasksClicked(View view){
        Log.d("FieldAid", "onNavigateToTasksClicked: status view");
        repositoryMediator.setNavigationOriginToTasks("status");
        Navigation.findNavController(view).navigate(R.id.action_fragmentStatusView_to_fragmentTasksView2);

    }

}
