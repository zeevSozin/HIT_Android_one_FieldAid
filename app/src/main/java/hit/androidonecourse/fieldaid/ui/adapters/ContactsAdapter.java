package hit.androidonecourse.fieldaid.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import java.util.List;

import hit.androidonecourse.fieldaid.databinding.ContactListItemBinding;
import hit.androidonecourse.fieldaid.domain.RepositoryMediator;
import hit.androidonecourse.fieldaid.domain.models.Contact;
import hit.androidonecourse.fieldaid.ui.handlers.ContactListItemHandler;

public class ContactsAdapter extends ArrayAdapter<Contact> {
    private List<Contact> contacts;
    private Context context;
    private int resourceLayoutId;
    RepositoryMediator repositoryMediator;


    public ContactsAdapter(@NonNull Context context, int resource, @NonNull List<Contact> objects) {
        super(context, resource, objects);
        this.context = context;
        resourceLayoutId = resource;
        contacts = objects;
        repositoryMediator = RepositoryMediator.getInstance(context);

    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ContactListItemBinding binding = DataBindingUtil.inflate(layoutInflater, resourceLayoutId,parent,false);
        binding.setContact(contacts.get(position));
        binding.setHandler(new ContactListItemHandler(context));
        repositoryMediator.setCurrentContact(contacts.get(position));
        return  binding.getRoot();
    }




}
