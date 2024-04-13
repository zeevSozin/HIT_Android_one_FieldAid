package hit.androidonecourse.fieldaid.ui.views.fragments;

import static android.app.Activity.RESULT_OK;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
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
    private Button buttonUploadPhoto;
    private ImageButton imageButtonAddContacts;
    private ListView contactsListView;

    private EditText editTextProjectName;
    private EditText editTextProjectDescription;
    ContactsEditAdapter contactsEditAdapter;
    private ImageButton imageButtonDeleteProject;
    private ImageView imageViewProjectProfilePicture;
    private Uri imagePath;

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
        buttonUploadPhoto = view.findViewById(R.id.btn_project_edit_upload_photo);
        imageButtonAddContacts = view.findViewById(R.id.imageBtn_add_contact_project_edit_fragment);
        contactsListView = view.findViewById(R.id.listView_contacts_project_edit_fragment);
        imageButtonDeleteProject = view.findViewById(R.id.imageBtn_delete_project_edit_project_fragment);
        imageViewProjectProfilePicture = view.findViewById(R.id.imageView_project_profile_picture);
        contactsEditAdapter = new ContactsEditAdapter(this.getContext(),R.layout.contact_list_item_edit, projectContacts);
        contactsListView.setAdapter(contactsEditAdapter);

        editTextProjectName = view.findViewById(R.id.editText_name_project_edit_fragment);
        editTextProjectDescription = view.findViewById(R.id.editText_description_project_edit_fragment);

        editTextProjectName.setText(currentProject.getName());

        editTextProjectDescription.setText(currentProject.getDescription());

        Glide.with(this).load(currentProject.getPictureUri()).
                error(R.drawable.ic_add_phot).placeholder(R.drawable.ic_add_phot).
                into(imageViewProjectProfilePicture);

        buttonSubmit.setOnClickListener(v -> {
            currentProject.setName(editTextProjectName.getText().toString());
            currentProject.setDescription(editTextProjectDescription.getText().toString());
            repositoryMediator.updateProject(currentProject);
            navigateToProjects();
        });
        imageButtonAddContacts.setOnClickListener(v -> addContactDialog.show());
        imageButtonDeleteProject.setOnClickListener(v -> deleteProject());
        imageViewProjectProfilePicture.setOnClickListener(v -> selectAndUpUoadPicture());
        buttonUploadPhoto.setOnClickListener(v -> uploadImage());

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




    private void deleteProject() {
        repositoryMediator.deleteProject();
        navigateToProjects();
    }

    private void addContact(String name, String phoneNumber) {
        Contact contact = new Contact(0,name,"", TimeStamp.getTimeStamp(), TimeStamp.getTimeStamp(),phoneNumber);
        repositoryMediator.insertContactToProject(contact, currentProject);
    }

    private void navigateToProjects() {
        Navigation.findNavController(this.getView()).navigate(R.id.action_fragmentEditProjectView_to_fragmentProjectsView);
    }

    private void selectAndUpUoadPicture() {
        Intent photoIntent = new Intent(Intent.ACTION_PICK);
        photoIntent.setType("image/*");
        startActivityForResult(photoIntent, 1);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && data!=null){
            imagePath = data.getData();
            getImageInImageView();
        }
    }

    private void getImageInImageView() {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContext().getContentResolver(),imagePath);
            buttonUploadPhoto.setEnabled(true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        imageViewProjectProfilePicture.setImageBitmap(bitmap);
    }

    private void uploadImage() {
        ProgressDialog progressDialog = new ProgressDialog(this.getContext());
        progressDialog.setTitle("Uploading...");
        progressDialog.show();


        FirebaseStorage.getInstance().getReference("images/" + currentProject.getId()).putFile(imagePath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()){
                    task.getResult().getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if(task.isSuccessful()){
                                currentProject.setPictureUri(task.getResult().toString());
                            }
                        }
                    });
                    Toast.makeText(FragmentEditProjectView.this.getContext(), "Image Uploaded!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(FragmentEditProjectView.this.getContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }
        });

    }
}