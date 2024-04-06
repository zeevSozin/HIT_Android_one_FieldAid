package hit.androidonecourse.fieldaid.ui.views.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import hit.androidonecourse.fieldaid.R;
import hit.androidonecourse.fieldaid.domain.RepositoryMediator;
import hit.androidonecourse.fieldaid.domain.models.Contact;
import hit.androidonecourse.fieldaid.domain.models.Project;
import hit.androidonecourse.fieldaid.ui.adapters.ContactsAdapter;
import hit.androidonecourse.fieldaid.ui.adapters.ContactsEditAdapter;


public class FragmentDetailsProjectView extends Fragment {
    private RepositoryMediator repositoryMediator;
    private Project currentProject;
    private List<Contact> contacts;
    private TextView textViewProjectName;
    private TextView textViewProjectDetails;
    private ListView listViewContacts;
    private Button btnBack;
    private Button btnSites;
    private ContactsAdapter contactsAdapter;



    public FragmentDetailsProjectView() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("FielAid", "onCreate: details fragment project");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_details_project_view, container, false);
        repositoryMediator = RepositoryMediator.getInstance(this.getContext());
        currentProject = repositoryMediator.getCurrentProject();
        contacts = repositoryMediator.getContactsByIds(currentProject.getContactIds());

        textViewProjectName = view.findViewById(R.id.textView_name_project_detail_fragment);
        textViewProjectDetails = view.findViewById(R.id.textView_description_project_detail_fragment);
        listViewContacts = view.findViewById(R.id.listView_contacts_project_details_fragment);
        btnBack = view.findViewById(R.id.btn_Project_details_nav_back);
        btnSites = view.findViewById(R.id.btn_Project_details_nav_sites);

        textViewProjectName.setText(currentProject.getName());
        textViewProjectDetails.setText(currentProject.getDescription());

        contactsAdapter = new ContactsAdapter(this.getContext(), R.layout.contact_list_item, contacts);
        listViewContacts.setAdapter(contactsAdapter);
        listViewContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                repositoryMediator.setCurrentContact(contacts.get(position));
            }
        });

        btnBack.setOnClickListener(v -> {navigateToProjects();});

        btnSites.setOnClickListener(v -> {
            repositoryMediator.setSiteFilterByProject(currentProject);
            navigateToSites();
        });



        return view;
    }

    private void navigateToProjects() {


        Navigation.findNavController(this.getView()).navigate(R.id.action_fragmentDetailsProjectView_to_fragmentProjectsView);

    }

    private void navigateToSites(){
        Navigation.findNavController(this.getView()).navigate(R.id.action_fragmentDetailsProjectView_to_fragmentSitesView2);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("FielAid", "onPause: details fragment project");


    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("FielAid", "onDetach: details fragment project");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("FielAid", "onDestroyView: details fragment project");


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("FielAid", "onDestroy: details fragment project");


    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("FielAid", "onStop: details fragment project");
//        Navigation.findNavController(this.getView()).popBackStack();
       // Navigation.findNavController(this.getView()).navigateUp();



    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("FielAid", "onResume: details fragment project");
//
    }
}