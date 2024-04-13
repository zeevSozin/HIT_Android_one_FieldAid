package hit.androidonecourse.fieldaid.ui.views.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.security.cert.PKIXRevocationChecker;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import hit.androidonecourse.fieldaid.R;
import hit.androidonecourse.fieldaid.databinding.FragmentEditSiteViewBinding;
import hit.androidonecourse.fieldaid.domain.RepositoryMediator;
import hit.androidonecourse.fieldaid.domain.models.Contact;
import hit.androidonecourse.fieldaid.domain.models.CustomLatLng;
import hit.androidonecourse.fieldaid.domain.models.EntityBase;
import hit.androidonecourse.fieldaid.domain.models.Project;
import hit.androidonecourse.fieldaid.domain.models.Site;
import hit.androidonecourse.fieldaid.ui.adapters.ContactsEditAdapter;
import hit.androidonecourse.fieldaid.ui.handlers.SiteListItemHandler;
import hit.androidonecourse.fieldaid.ui.viewmodels.FragmentEditSiteViewModel;
import hit.androidonecourse.fieldaid.ui.views.activities.ActivityMainView;
import hit.androidonecourse.fieldaid.util.ListUtil;
import hit.androidonecourse.fieldaid.util.TimeStamp;


public class FragmentEditSiteView extends Fragment{
    private RepositoryMediator repositoryMediator;
    private Site currentSite;
    private MutableLiveData<Site> currentLiveSite;
    private List<Project> projects;
    private List<Project> orderedProjects;
    private List<String> projectNames;
    private List<Contact> contacts;
    private Project selectedProject;
    private Contact selectedContact;
    private Spinner spinnerProjects;

    private EditText editTextSiteName;
    private EditText editTextSiteDescription;
    private ImageButton imageButtonAddContact;
    private ListView listViewContacts;
    private ContactsEditAdapter contactsEditAdapter;
    private Fragment mapFragment;
    private MapView mapView;
    private GoogleMap map;
    private LocationManager locationManager;
    private Location location;
    private CustomLatLng sitesLocation;
    private Button buttonSubmit;
    private ImageButton imageButtonDeleteSite;

    // dialog

    private Dialog addContactDialog;
    private EditText editTextContactName;
    private EditText editTextContactPhoneNumber;
    private Button btnAddContact;
    private Button btnCancel;



    @SuppressLint("ServiceCast")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_edit_site_view, container, false);

        repositoryMediator = RepositoryMediator.getInstance(this.getContext());
        currentSite = repositoryMediator.getCurrentSite();
        currentLiveSite = repositoryMediator.getCurrentSiteLiveData();
        Log.d("FieldAid", "onCreateView: site edit view current site is "+ currentSite.getName());
        contacts = repositoryMediator.getContactsByIds(currentSite.getContactIds());
        projects = repositoryMediator.getProjectLiveData().getValue();
        projectNames = projects.stream().map(EntityBase::getName).collect(Collectors.toList());
        Optional<Project> optionalProject = projects.stream().filter( p -> p.getId() == currentSite.getProjectId()).findFirst();
        selectedProject = optionalProject.orElse(null);


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

        // fragment

        spinnerProjects = view.findViewById(R.id.spinner_site_edit_fragment);
        editTextSiteName = view.findViewById(R.id.editText_name_site_edit_fragment);
        editTextSiteDescription = view.findViewById(R.id.editText_description_site_edit_fragment);
        imageButtonAddContact = view.findViewById(R.id.imageBtn_add_contact_site_edit_fragment);
        listViewContacts = view.findViewById(R.id.listView_contacts_site_edit_fragment);

        mapFragment = new FragmentMapView();
        this.getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout_map_site_edit_fragment, mapFragment)
                .commit();

        buttonSubmit = view.findViewById(R.id.btn_Site_edit_submit);
        imageButtonDeleteSite = view.findViewById(R.id.imageBtn_delete_site_edit_site_fragment);

        editTextSiteName.setText(currentSite.getName());
        editTextSiteDescription.setText(currentSite.getDescription());


        //spinner
        ListUtil<Project> projectListUtil = new ListUtil<>();
        orderedProjects = projectListUtil.setFirst(projects, selectedProject);
        List<String> orderedProjectName = orderedProjects.stream().map(EntityBase::getName).collect(Collectors.toList());

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                orderedProjectName
        );
        spinnerProjects.setAdapter(spinnerAdapter);

        currentLiveSite.observe(this.getViewLifecycleOwner(), new Observer<Site>() {
            @Override
            public void onChanged(Site site) {
                List<Long> contactIds = repositoryMediator.getCurrentSite().getContactIds();
                contacts = repositoryMediator.getContactsByIds(contactIds);
                contactsEditAdapter = new ContactsEditAdapter(getContext(),R.layout.contact_list_item_edit,contacts);
                listViewContacts.setAdapter(contactsEditAdapter);
                listViewContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        repositoryMediator.setCurrentContact(contacts.get(position));
                    }
                });


            }
        });


        imageButtonAddContact.setOnClickListener(v -> addContactDialog.show());
        imageButtonDeleteSite.setOnClickListener(v -> deleteSite());
        buttonSubmit.setOnClickListener(v -> saveChanges());
        return view;
    }



    private void saveChanges() {
        currentSite.setProjectId(projects.get((int)spinnerProjects.getSelectedItemPosition()).getId());
        currentSite.setName(editTextSiteName.getText().toString());
        currentSite.setDescription(editTextSiteDescription.getText().toString());
        if(repositoryMediator.getSiteUpdateLocation()!= null){
            currentSite.setLatLongMapString(repositoryMediator.getSiteUpdateLocation());
        }
        repositoryMediator.updateSite(currentSite);

        navigateToSites();
    }

    private void navigateToSites(){
        Navigation.findNavController(this.getView()).navigate(R.id.action_fragmentEditSiteView_to_fragmentSitesView);
    }

    private void addContact(String name, String phoneNumber) {
        Contact contact = new Contact(0,name,"", TimeStamp.getTimeStamp(), TimeStamp.getTimeStamp(),phoneNumber);
        repositoryMediator.insertContactToSite(contact, currentSite);
    }
    private void deleteSite() {
        repositoryMediator.deleteSite();
        navigateToSites();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("FieldAid", "onDestroy: fragment edit site destroy");
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("FieldAid", "onDestroyView: edit site destroy");
        mapFragment.onDestroy();
    }
}