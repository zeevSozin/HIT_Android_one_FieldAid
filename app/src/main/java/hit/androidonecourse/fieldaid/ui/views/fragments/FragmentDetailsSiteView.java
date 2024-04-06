package hit.androidonecourse.fieldaid.ui.views.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

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
import hit.androidonecourse.fieldaid.domain.models.Site;
import hit.androidonecourse.fieldaid.ui.adapters.ContactsAdapter;


public class FragmentDetailsSiteView extends Fragment {
    private RepositoryMediator repositoryMediator;
    private Project currentProject;
    private Site currenrSite;
    private List<Contact> contacts;
    private TextView textViewProjectName;
    private TextView textViewSiteName;
    private TextView textViewSiteDescription;
    private ListView listViewContacts;
    private Button buttonBack;
    private Button buttonJobs;
    private ContactsAdapter contactsAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details_site_view, container, false);
        repositoryMediator = RepositoryMediator.getInstance(this.getContext());
        currenrSite = repositoryMediator.getCurrentSite();
        currentProject = repositoryMediator.getProjectById(currenrSite.getProjectId());
        contacts = repositoryMediator.getContactsByIds(currenrSite.getContactIds());

        textViewProjectName = view.findViewById(R.id.textView_project_name_site_detail_fragment);
        textViewSiteName = view.findViewById(R.id.textView_name_site_detail_fragment);
        textViewSiteDescription = view.findViewById(R.id.textView_description_site_detail_fragment);
        listViewContacts = view.findViewById(R.id.listView_contacts_site_detail_fragment);
        buttonBack = view.findViewById(R.id.btn_Site_detail_back);
        buttonJobs = view.findViewById(R.id.btn_Site_detail_navToJobs);

        textViewProjectName.setText(currentProject != null? currentProject.getName(): "Undefined");
        textViewSiteName.setText(currenrSite.getName());
        textViewSiteDescription.setText(currenrSite.getDescription());

        contactsAdapter = new ContactsAdapter(this.getContext(), R.layout.contact_list_item, contacts);
        listViewContacts.setAdapter(contactsAdapter);

        listViewContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                repositoryMediator.setCurrentContact(contacts.get(position));
            }
        });

        buttonBack.setOnClickListener(v -> navigateToSites());

        buttonJobs.setOnClickListener(v -> navigateToJobs());

        return view;
    }

    private void navigateToJobs() {
        repositoryMediator.setJobFilterBySite(currenrSite);
        Navigation.findNavController(this.getView()).navigate(R.id.action_fragmentDetailsSiteView_to_fragmentJobsView2);


    }

    private void navigateToSites() {
        Navigation.findNavController(this.getView()).navigate(R.id.action_fragmentDetailsSiteView_to_fragmentSitesView);
    }
}