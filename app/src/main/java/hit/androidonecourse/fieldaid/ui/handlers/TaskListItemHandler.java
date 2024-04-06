package hit.androidonecourse.fieldaid.ui.handlers;

import android.content.Context;
import android.util.Log;
import android.view.View;

public class TaskListItemHandler extends ListItemHandlerBase{
    public TaskListItemHandler(Context context) {
        super(context);
    }

    public void onButtonCompleteTaskClicked(View view){
        Log.d("FieldAid", "onButtonCompleteTaskClicked: task ticked");
    }
}
