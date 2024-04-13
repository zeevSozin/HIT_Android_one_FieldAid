package hit.androidonecourse.fieldaid.domain;

import android.app.Activity;
import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import hit.androidonecourse.fieldaid.R;
import hit.androidonecourse.fieldaid.data.repositories.ContacRepo;
import hit.androidonecourse.fieldaid.data.repositories.JobRepo;
import hit.androidonecourse.fieldaid.data.repositories.ProjectRepo;
import hit.androidonecourse.fieldaid.data.repositories.SiteRepo;
import hit.androidonecourse.fieldaid.data.repositories.TaskRepo;
import hit.androidonecourse.fieldaid.data.repositories.UserAccountRepo;
import hit.androidonecourse.fieldaid.data.repositories.UserRepo;
import hit.androidonecourse.fieldaid.domain.models.Contact;
import hit.androidonecourse.fieldaid.domain.models.CustomLatLng;
import hit.androidonecourse.fieldaid.domain.models.Job;
import hit.androidonecourse.fieldaid.domain.models.Project;
import hit.androidonecourse.fieldaid.domain.models.Site;
import hit.androidonecourse.fieldaid.domain.models.Task;
import hit.androidonecourse.fieldaid.domain.models.User;
import hit.androidonecourse.fieldaid.domain.models.UserAccount;
import hit.androidonecourse.fieldaid.util.CustomLocationManager;
import hit.androidonecourse.fieldaid.util.DbUtils;

public class RepositoryMediator  {
    private static volatile RepositoryMediator instance = null;

    // repos
    private UserAccountRepo userAccountRepo;
    private UserRepo userRepo;
    private ContacRepo contacRepo;
    private ProjectRepo projectRepo;
    private SiteRepo siteRepo;
    private JobRepo jobRepo;
    private TaskRepo taskRepo;



    private CustomLocationManager customLocationManager;

    //collections
    private MutableLiveData<List<UserAccount>> userAccountLiveData = new MutableLiveData<>();
    private MutableLiveData<List<User>> userLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Contact>> contactLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Project>> projectLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Site>> siteLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Job>> jobLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Task>> taskLiveData = new MutableLiveData<>();

    //live Data status

    private MutableLiveData<Boolean> isUserAccountCollectionReady = new MutableLiveData<>();
    private MutableLiveData<Boolean> isUserCollectionReady = new MutableLiveData<>();
    private MutableLiveData<Boolean> isContactCollectionReady = new MutableLiveData<>();
    private MutableLiveData<Boolean> isProjectCollectionReady = new MutableLiveData<>();
    private MutableLiveData<Boolean> isSiteCollectionReady = new MutableLiveData<>();
    private MutableLiveData<Boolean> isJobCollectionReady = new MutableLiveData<>();
    private MutableLiveData<Boolean> isTaskCollectionReady = new MutableLiveData<>();

    private MutableLiveData<Boolean> isAllCollectionsLoaded = new MutableLiveData<>();

    //current states
    private String currentUserAccountId;
    private UserAccount currentUserAccount = new UserAccount();
    private User currentUser = new User();
    private Contact currentContact = new Contact();
    private Project currentProject = new Project();
    private Site currentSite = new Site();
    private Job currentJob = new Job();



    private Job currentStatusJob = new Job();
    private Task currentTask = new Task();



    private CustomLatLng siteUpdateLocation;

    // filters
    private Project sitesFilterProject = null;
    private Site JobFilterBySite = null;
    private Job taskFilterByJob = null;
    private String statusFilter =null;

    //current mutable states


    private MutableLiveData<Project> currentProjectLiveData = new MutableLiveData<>();
    private MutableLiveData<Site> currentSiteLiveData = new MutableLiveData<>();

    // navigation flags
    private String navigationOriginToTasks = "jobs";



    private RepositoryMediator(Context context){
        customLocationManager = new CustomLocationManager(context);
        //set flags
        isAllCollectionsLoaded.setValue(false);
        isUserCollectionReady.setValue(false);
        isContactCollectionReady.setValue(false);
        isProjectCollectionReady.setValue(false);
        isSiteCollectionReady.setValue(false);
        isJobCollectionReady.setValue(false);
        isTaskCollectionReady.setValue(false);

        // init repositories
        userAccountRepo = new UserAccountRepo(context.getString(R.string.FireBase_UserAccount));
        userRepo = new UserRepo(context.getString(R.string.FireBase_User));
        contacRepo = new ContacRepo(context.getString(R.string.FireBase_Contact));
        projectRepo = new ProjectRepo(context.getString(R.string.FireBase_Project));
        siteRepo = new SiteRepo(context.getString(R.string.FireBase_Site));
        jobRepo = new JobRepo(context.getString(R.string.FireBase_Job));
        taskRepo = new TaskRepo(context.getString(R.string.FireBase_Task));

        //Register live Data collections
 //       userAccountLiveData = userAccountRepo.getCollection();
        userLiveData = userRepo.getCollection();
        contactLiveData =contacRepo.getCollection();
        projectLiveData = projectRepo.getCollection();
        siteLiveData = siteRepo.getCollection();
        jobLiveData = jobRepo.getCollection();
        taskLiveData = taskRepo.getCollection();


        //Observer

        Activity activity = (Activity) context;
//        userAccountLiveData.observe((LifecycleOwner) activity, new Observer<List<UserAccount>>() {
//            @Override
//            public void onChanged(List<UserAccount> userAccounts) {
//                if(userAccounts.size() > 0){
//                    isUserAccountCollectionReady.setValue(true);
//                }
//            }
//        });
        userLiveData.observe((LifecycleOwner) activity, users -> {
            if(!users.isEmpty()){
                isUserCollectionReady.setValue(true);
                checkAllCollectionsAvailability();
            }
        });
        contactLiveData.observe((LifecycleOwner) activity, contacts -> {
            if(!contacts.isEmpty()){
                isContactCollectionReady.setValue(true);
                checkAllCollectionsAvailability();
            }
        });

        projectLiveData.observe((LifecycleOwner) activity, projects -> {
            if(!projects.isEmpty()){
                isProjectCollectionReady.setValue(true);
                checkAllCollectionsAvailability();
                if(currentProjectLiveData.hasActiveObservers()){
                    updateCurrentProject();
                }
            }
        });
        siteLiveData.observe((LifecycleOwner) activity, sites -> {
            if(!sites.isEmpty()){
                isSiteCollectionReady.setValue(true);
                checkAllCollectionsAvailability();
            }
        });
        jobLiveData.observe((LifecycleOwner) activity, jobs -> {
            if(!jobs.isEmpty()){
                isJobCollectionReady.setValue(true);
                checkAllCollectionsAvailability();
            }
        });
        taskLiveData.observe((LifecycleOwner) activity, tasks -> {
            if(!tasks.isEmpty()){
                isTaskCollectionReady.setValue(true);
                checkAllCollectionsAvailability();
            }
        });

    }

    public static synchronized RepositoryMediator getInstance(Context context) {
        if(instance == null){
            instance = new RepositoryMediator(context);
        }
        return instance;
    }

    private void checkAllCollectionsAvailability(){
        if(isUserCollectionReady.getValue() &&
                isProjectCollectionReady.getValue() &&
                isSiteCollectionReady.getValue() &&
                isJobCollectionReady.getValue() &&
                isContactCollectionReady.getValue() &&
                isTaskCollectionReady.getValue()){

            isAllCollectionsLoaded.setValue(true);
        }
    }
    public void setCurrentUserAccountId(String accountId){
        setLoggedInUserByAccountId(accountId, Objects.requireNonNull(userLiveData.getValue()));

    }

    private void setLoggedInUserByAccountId(String accountId, List<User> users){
        this.currentUser =  users.stream().filter(u -> Objects.equals(u.getUserAccountId(), accountId)).findFirst().orElse(null);

    }


    // events
    public LiveData<Boolean> getIaAllCollectionsLoaded(){
        return isAllCollectionsLoaded;
    }
    public LiveData<Boolean> getIsUserAccountCollectionReady(){
        return isUserAccountCollectionReady;
    }

    public LiveData<Boolean> getIsUserCollectionReady(){
        return isUserCollectionReady;
    }
    public String getCurrentUserName(){
        return currentUser.getFirstName();
    }

    public LiveData<Boolean> getIsAllCollectionsLoaded() {
        return isAllCollectionsLoaded;
    }

    // CRUD

    //Project
    public void insertProject(Project project){
        this.projectRepo.insert(project);
    }

    public void updateProject(Project project) {
        projectRepo.update(project);
    }
    public void setCurrentProject(Project currentProject) {
        this.currentProject = currentProject;
        this.currentProjectLiveData.setValue(currentProject);
    }

    public Project getCurrentProject() {
        return this.currentProject;
    }

    public MutableLiveData<List<Project>> getProjectLiveData(){
        return projectLiveData;
    }

    public MutableLiveData<Project> getCurrentProjectLiveData(){
        return currentProjectLiveData;
    }

    private void updateCurrentProject(){
        Project tempProject = projectLiveData.getValue().stream().filter(p -> p.getId() == currentProject.getId()).findFirst().get();
        if(tempProject != null){
           setCurrentProject(tempProject);
        }
    }

    public void deleteProject() {
        currentProject.setDeleted(true);
        updateProject(currentProject);
    }

    //Sites

    public void insertSite(Site site){
        siteRepo.insert(site);
    }

    public MutableLiveData<List<Site>> getSiteLiveData(){
        return siteLiveData;
    }

    public void setCurrentSite(Site site){
        this.currentSite = site;
        this.currentSiteLiveData.setValue(site);
    }

    public MutableLiveData<Site> getCurrentSiteLiveData() {
        return currentSiteLiveData;
    }


    public Site getCurrentSite() {
        return currentSite;
    }

    public void updateSite(Site currentSite) {
        siteRepo.update(currentSite);
    }

    public void deleteSite() {
        currentSite.setDeleted(true);
        updateSite(currentSite);
    }


// contacts
    public void setCurrentContact(Contact contact) {
        this.currentContact = contact;
    }

    public Contact getCurrentContact(){
        return currentContact;
    }

    public List<Contact> getContactsByIds(List<Long> ids) {
        List<Contact> result = new ArrayList<>();
        if(!contactLiveData.getValue().isEmpty() && ids != null){
            for (Long id : ids) {
//                result.add(contactLiveData.getValue().stream().filter(c -> c.getId() == id).findFirst().orElse(null));
                Contact tempContact =  contactLiveData.getValue().stream().filter(c -> c.getId() == id).findFirst().orElse(null);
                if(tempContact !=null){
                    result.add(tempContact);
                }
            }
        }
        return result;
    }


    public void insertContactToProject(Contact contact, Project currentProject) {
        currentProject.getContactIds().add(contacRepo.insertAndGetId(contact));
        projectRepo.update(currentProject);
    }

    public void insertContactToSite(Contact contact, Site currentSite) {
        currentSite.getContactIds().add(contacRepo.insertAndGetId(contact));
        siteRepo.update(currentSite);

    }


    public void deleteContactFromProject() {
        long currentContactId = currentContact.getId();
//        contacRepo.delete(currentContact);
        currentContact = null;
        DbUtils<Long> dbUtils = new DbUtils<>();
        List<Long> updatedContactIds = dbUtils.removeItemFromList(currentProject.getContactIds(), currentContactId);
        currentProject.setContactIds(updatedContactIds);
        projectRepo.update(currentProject);

    }


    public void setSiteFilterByProject(Project currentProject) {
        this.sitesFilterProject = currentProject;
    }

    public Project getSitesFilterProject() {
        return sitesFilterProject;
    }

    public Project getProjectById(long projectId) {
        Project result = null;
        if (!projectLiveData.getValue().isEmpty()){
            result = projectLiveData.getValue().stream().filter(p -> p.getId() == projectId).findFirst().orElse(null);
        }
        return result;
    }


    public CustomLocationManager getCustomLocationManager() {
        return customLocationManager;
    }
    public CustomLatLng getSiteUpdateLocation() {
        return siteUpdateLocation;
    }

    public void setSiteUpdateLocation(CustomLatLng siteUpdateLocation) {
        this.siteUpdateLocation = siteUpdateLocation;
    }

    // Jobs

    public void setJobFilterBySite(Site currenrSite) {
        this.JobFilterBySite = currenrSite;
    }
    public Site getJobFilterBySite() {
        return JobFilterBySite;
    }

    public Job getCurrentJob() {
        return this.currentJob;
    }
    public void setCurrentJob(Job job){
        this.currentJob = job;
        setTaskFilterByJob(job);

    }

    public MutableLiveData<List<Job>> getJobLiveData() {
        return jobLiveData;
    }

    public void insertJob(Job job) {
        jobRepo.insert(job);
    }

    public void deleteJob() {
        currentJob.setDeleted(true);
        jobRepo.update(currentJob);
    }

    // Tasks

    public Task getCurrentTask() {
        return currentTask;
    }

    public void setCurrentTask(Task task) {
        this.currentTask = task;
    }

    public MutableLiveData<List<Task>> getTaskLiveData() {
        return this.taskLiveData;
    }

    public void insertTask(Task task) {
        long id = taskRepo.insertAndGetId(task);
        currentJob.getTaskIds().add(id);
        jobRepo.update(currentJob);
    }
    public void deleteTaskFromJob() {
        long taskId = currentTask.getId();
        currentJob.getTaskIds().remove(taskId);
        updateJob(currentJob);
        currentTask.setJobId(-1);
        taskRepo.update(currentTask);
    }



    public void updateJob(Job job) {
        jobRepo.update(job);

    }

    // status
    public void setTaskFilterByJob(Job currentJob) {
        this.taskFilterByJob = currentJob;
        this.currentJob = currentJob;
    }

    public Job getTaskFiltetByJob() {
        return taskFilterByJob;
    }

    public Job getCurrentStatusJob() {
        return this.currentStatusJob;
    }
    public void setCurrentStatusJob(Job currentStatusJob) {
        this.currentStatusJob = currentStatusJob;
    }

    public String getStatusFilter() {
        return statusFilter;
    }
    public void setStatusFilter(String statusFilter) {
        this.statusFilter = statusFilter;
    }

    public void setNavigationOriginToTasks(String origin) {
        this.navigationOriginToTasks = origin;
    }
    public String getNavigationOriginToTasks(){
        return this.navigationOriginToTasks;
    }



}
