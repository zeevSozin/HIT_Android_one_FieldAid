package hit.androidonecourse.fieldaid.ui.handlers;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import hit.androidonecourse.fieldaid.domain.RepositoryMediator;
import hit.androidonecourse.fieldaid.domain.models.Contact;

public class ContactListItemHandler extends ListItemHandlerBase {

    public ContactListItemHandler(Context context) {
        super(context);

    }
    public void onBtnRemoveClicked(View view){
        Log.d("FieldAid", "onBtnRemoveClicked: Contact list item button remove clicked");
        repositoryMediator.deleteContactFromProject();


    }
    public void  onBtnCallClicked(View view){
        Log.d("FieldAid", "onBtnCallClicked: Contact list item button call clicked");
        Toast.makeText(context, "Calling " + repositoryMediator.getCurrentContact().getName(), Toast.LENGTH_SHORT).show();
    }

}
