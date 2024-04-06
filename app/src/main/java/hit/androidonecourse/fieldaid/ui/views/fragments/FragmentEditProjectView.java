package hit.androidonecourse.fieldaid.ui.views.fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.List;

import hit.androidonecourse.fieldaid.R;
import hit.androidonecourse.fieldaid.domain.RepositoryMediator;
import hit.androidonecourse.fieldaid.domain.models.Contact;
import hit.androidonecourse.fieldaid.domain.models.Project;
import hit.androidonecourse.fieldaid.ui.adapters.ContactsEditAdapter;
import hit.androidonecourse.fieldaid.util.TimeStamp;


public class FragmentEditProjectView extends Fragment {
    private RepositoryMediator repositoryMediator;
    private Project currentProject;
    private MutableLiveData<Project> currentLiveProject;
    private List<Contact> projectContacts;

    private Button buttonSubmit;
    private ImageButton imageButtonAddContacts;
    private ListView contactsListView;

    private EditText editTextProjectName;
    private EditText editTextProjectDescription;
    ContactsEditAdapter contactsEditAdapter;

    // dialog
    private Dialog addContactDialog;
    private EditText editTextContactName;
    private EditText editTextContactPhoneNumber;
    private Button btnAddContact;
    private Button btnCancel;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_edit_project_view, container, false);


        repositoryMediator = RepositoryMediator.getInstance(this.getContext());
        currentProject = repositoryMediator.getCurrentProject();
        currentLiveProject = repositoryMediator.getCurrentProjectLiveData();
        List<Long> contactIds = repositoryMediator.getCurrentProject().getContactIds();
        projectContacts = repositoryMediator.getContactsByIds(contactIds);



        // dialog

        addContactDialog = new Dialog(this.getContext());
        addContactDialog.setContentView(R.layout.dialog_add_contact);
        addContactDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addContactDialog.getWindow().setBackgroundDrawableResource(com.google.firebase.database.R.drawable.common_google_signin_btn_icon_dark_normal_background);
        addContactDialog.setCancelable(true);
        editTextContactName = addContactDialog.findViewById(R.id.dialog_addContact_editText_contactName);
        editTextContactPhoneNumber = addContactDialog.findViewById(R.id.dialog_addContact_editText_ContactPhoneNumber);
        btnAddContact = addContactDialog.findViewById(R.id.dialog_addContact_btn_add);
        btnCancel = addContactDialog.findViewById(R.id.dialog_addContact_btn_cancel);

        btnAddContact.setOnClickListener(v -> {
            if(!editTextContactName.getText().toString().isEmpty() || !editTextContactPhoneNumber.getText().toString().isEmpty()){
                addContact(editTextContactName.getText().toString(), editTextContactPhoneNumber.getText().toString());
                addContactDialog.dismiss();

            }
        });

        btnCancel.setOnClickListener(v -> addContactDialog.dismiss());

        //Fragment

        buttonSubmit = view.findViewById(R.id.btn_Project_edit_submit);
        imageButtonAddContacts = view.findViewById(R.id.imageBtn_add_contact_project_edit_fragment);
        contactsListView = view.findViewById(R.id.listView_contacts_project_edit_fragment);
        contactsEditAdapter = new ContactsEditAdapter(this.getContext(),R.layout.contact_list_item_edit, projectContacts);
        contactsListView.setAdapter(contactsEditAdapter);

        editTextProjectName = view.findViewById(R.id.editText_name_project_edit_fragment);
        editTextProjectDescription = view.findViewById(R.id.editText_description_project_edit_fragment);

        editTextProjectName.setText(currentProject.getName());

        editTextProjectDescription.setText(currentProject.getDescription());
        
        buttonSubmit.setOnClickListener(v -> {
            currentProject.setName(editTextProjectName.getText().toString());
            currentProject.setDescription(editTextProjectDescription.getText().toString());
            repositoryMediator.updateProject(currentProject);
            navigateToProjects();
        });
        imageButtonAddContacts.setOnClickListener(v -> addContactDialog.show());

        currentLiveProject.observe(this.getViewLifecycleOwner(), new Observer<Project>() {
            @Override
            public void onChanged(Project project) {
                List<Long> contactIds = repositoryMediator.getCurrentProject().getContactIds();
                Log.d("FieldAid", "onChanged: contact count:" + contactIds.size() + "contacts:" + contactIds);
                projectContacts = repositoryMediator.getContactsByIds(contactIds);
                contactsEditAdapter = new ContactsEditAdapter(getContext(),R.layout.contact_list_item_edit, projectContacts);
                contactsListView.setAdapter(contactsEditAdapter);
                contactsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        repositoryMediator.setCurrentContact(projectContacts.get(position));
                    }
                });
            }
        });

        

        return view;
    }

    private void addContact(String name, String phoneNumber) {
        Contact contact = new Contact(0,name,"", TimeStamp.getTimeStamp(), TimeStamp.getTimeStamp(),phoneNumber);
        repositoryMediator.insertContactToProject(contact, currentProject);
    }

    private void navigateToProjects() {
        Navigation.findNavController(this.getView()).navigate(R.id.action_fragmentEditProjectView_to_fragmentProjectsView);
    }


}