package hit.androidonecourse.fieldaid.ui.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import java.util.List;

public class ProjectNamesSpinnerAdapter extends ArrayAdapter<String> {
    public ProjectNamesSpinnerAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
    }
}
