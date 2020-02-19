package com.benmohammad.repoapp.data;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.ColorSpace;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.benmohammad.repoapp.data.database.DaoCachedProjects;
import com.benmohammad.repoapp.data.database.DaoSavedProjects;
import com.benmohammad.repoapp.data.database.GitHubProjectsDatabase;
import com.benmohammad.repoapp.data.database.ModelBaseGitHubProject;
import com.benmohammad.repoapp.data.database.ModelCachedGitHubProject;
import com.benmohammad.repoapp.data.database.ModelSavedGitHubProject;
import com.benmohammad.repoapp.data.webservice.apiresponse.GitHubClientService;
import com.benmohammad.repoapp.data.webservice.apiresponse.GitHubRepo;
import com.benmohammad.repoapp.data.webservice.apiresponse.items.Item;
import com.benmohammad.repoapp.utils.Configuration;
import com.benmohammad.repoapp.utils.Utils;
import com.benmohammad.repoapp.utils.WebServiceMessage;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DataRepository {

    private static DataRepository dataRepositoryInstance;
    private DaoSavedProjects savedProjectsDao;
    private DaoCachedProjects cachedProjectsDao;
    private Retrofit retrofit;
    private GitHubClientService gitHubClient;
    private SharedPreferences sharedPreferences;
    private static int FRESH_TIMEOUT_IN_MINUTES = Configuration.FRESH_TIMEOUT_IN_MINUTES;
    private MutableLiveData<WebServiceMessage> webServiceMessageCallStatus = new MutableLiveData<>();

    public static synchronized DataRepository getInstance(Application application) {
        if(dataRepositoryInstance == null) {
            dataRepositoryInstance = new DataRepository(application);
        }
        return dataRepositoryInstance;

    }

    public DataRepository (Application application) {
        GitHubProjectsDatabase database = GitHubProjectsDatabase.getInstance(application);
        cachedProjectsDao = database.cachedProjectsDao();
        savedProjectsDao = database.savedProjectsDao();
        sharedPreferences = application.getSharedPreferences("repoExplorerSharedPref", Context.MODE_PRIVATE);
    }

    public boolean isFirstRun() {
        return sharedPreferences.getBoolean("ïsFirstRun", true);
    }

    public void setFirstRunOff() {
        SharedPreferences.Editor myEditor = sharedPreferences.edit();
        myEditor.putBoolean("isFirstRun", false);
        myEditor.apply();
    }

    public String getLastSavedSearchTerm() {
        return sharedPreferences.getString("lastSearchTerm", GitHubClientService.DEFAULT_SEARCH_TERM);
    }

    private void saveLastSearchTerm(String searchTerm) {
        SharedPreferences.Editor myEditor = sharedPreferences.edit();
        myEditor.putString("lastSearchTerm", searchTerm);
        myEditor.apply();
    }

    private void setLastRefreshDate(Date date) {
        SharedPreferences.Editor myEditor = sharedPreferences.edit();
        myEditor.putString("lastRefreshDate", date.toString());
        myEditor.apply();
    }

    private Date getMaxRefreshTime(Date currentDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        cal.add(Calendar.MINUTE, -FRESH_TIMEOUT_IN_MINUTES);
        return cal.getTime();
    }

    public boolean isCacheFreshEnough(Date date) {
        String lastRefreshDate = sharedPreferences.getString("lastRefreshDate", null);
        if(lastRefreshDate == null) {
            return false;
        } else {
            return new Date(lastRefreshDate).compareTo(getMaxRefreshTime(date))>0;
        }
    }

    public void updateFromWebservice(final String searchTerm, final int pageNumber, int resultsPerPage) {
        webServiceMessageCallStatus.setValue(WebServiceMessage.UPDATING_STATUS);
        final List<ModelCachedGitHubProject> gitHubProjectsList =  new ArrayList<>();
        if(retrofit == null || gitHubClient == null) {
            retrofit = new Retrofit.Builder().baseUrl(GitHubClientService.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            gitHubClient = retrofit.create(GitHubClientService.class);
        }
        Call<GitHubRepo> call = gitHubClient.getRepos(searchTerm, pageNumber, resultsPerPage);
        call.enqueue(new Callback<GitHubRepo>() {
            @Override
            public void onResponse(Call<GitHubRepo> call, Response<GitHubRepo> response) {
                ArrayList<Item> itemsList = response.body().getItems();
                for(int i = 0; i < itemsList.size(); i++) {
                    ModelCachedGitHubProject currentProject = new ModelCachedGitHubProject();
                    Item responseItem = itemsList.get(i);
                    currentProject.setOwnerName(responseItem.getOwner().getLogin());
                    currentProject.setRepoName(responseItem.getName());
                    currentProject.setRepoSize(responseItem.getSize());
                    currentProject.setHasWiki(responseItem.isHas_wiki());
                    currentProject.setCreatedAt(responseItem.getCreated_at());
                    currentProject.setPushedAt(responseItem.getPushed_at());
                    currentProject.setUpdatedAt(responseItem.getUpdated_at());
                    currentProject.setHtmlUrl(responseItem.getHtml_url());
                    currentProject.setAvatarUrl(responseItem.getOwner().getAvatar_url());
                    currentProject.setLanguage(responseItem.getLanguage());
                    currentProject.setForksCount(responseItem.getForks_count());
                    currentProject.setScore(responseItem.getScore());
                    currentProject.setDescription(responseItem.getDescription());
                    gitHubProjectsList.add(currentProject);
                }

                if(!gitHubProjectsList.isEmpty()) {
                    boolean clearPreviousCache;
                    if(pageNumber == 1) {
                        clearPreviousCache = true;
                        saveLastSearchTerm(searchTerm);

                    } else clearPreviousCache = false;
                    cacheProjectsList(gitHubProjectsList, clearPreviousCache);
                    setLastRefreshDate(new Date());
                    webServiceMessageCallStatus.postValue(WebServiceMessage.ON_RESPONSE_SUCCESS);
                } else {
                    if(pageNumber == 1)
                        webServiceMessageCallStatus.postValue(WebServiceMessage.ON_RESPONSE_NOTHING_FOUND);
                    else {
                        webServiceMessageCallStatus.postValue(WebServiceMessage.ON_RESPONSE_NO_MORE_RESULTS);
                    }
                }
            }

            @Override
            public void onFailure(Call<GitHubRepo> call, Throwable t) {
                webServiceMessageCallStatus.postValue(WebServiceMessage.ON_FAILURE);
            }
        });
    }


    public LiveData<List<ModelCachedGitHubProject>> getAllCachedProjects() {
        return cachedProjectsDao.getAllCachedFromDb();
    }

    public LiveData<List<ModelSavedGitHubProject>> getAllSavedProjects() {
        return savedProjectsDao.getAllSavedProjects();
    }

    public MutableLiveData<WebServiceMessage> getWebServiceMessage() {
        return webServiceMessageCallStatus;
    }

    public void clearWebServiceMessage() {
        webServiceMessageCallStatus = new MutableLiveData<>();
    }

    private void cacheProjectsList(List<ModelCachedGitHubProject> cachedGitHubProjects, boolean clearCache) {
        new SaveListToCacheAsyncTask(cachedProjectsDao, clearCache).execute(cachedGitHubProjects);
    }

    public void trimCacheTable() {
        new TrimCacheAsyncTask(cachedProjectsDao).execute();
    }

    private static class SaveListToCacheAsyncTask extends AsyncTask<List<ModelCachedGitHubProject>, Void, Void> {
        private DaoCachedProjects cachedProjectsDao;
        boolean clearCacheBeforeAdding;

        private SaveListToCacheAsyncTask(DaoCachedProjects cachedProjectsDao, boolean clearCacheBeforeAdding) {
            this.cachedProjectsDao = cachedProjectsDao;
            this.clearCacheBeforeAdding = clearCacheBeforeAdding;
        }

        @Override
        protected Void doInBackground(List<ModelCachedGitHubProject>... lists) {
            if(clearCacheBeforeAdding) {
                cachedProjectsDao.deleteAllCachedRepos();
            }
            cachedProjectsDao.saveToCache(lists[0]);
            return null;
        }
    }

    private static class TrimCacheAsyncTask extends AsyncTask<ModelCachedGitHubProject, Void, Void> {
        private DaoCachedProjects  cachedProjectsDao;

        public TrimCacheAsyncTask(DaoCachedProjects cachedProjectsDao) {
            this.cachedProjectsDao = cachedProjectsDao;
        }

        @Override
        protected Void doInBackground(ModelCachedGitHubProject... modelCachedGitHubProjects) {
            cachedProjectsDao.trimCacheTable(GitHubClientService.RESULTS_PER_PAGE);
            return null;
        }
    }

    private static class SavedTableAsyncTask extends AsyncTask<ModelSavedGitHubProject, Void, Void> {
        private DaoSavedProjects savedProjectsDao;
        private ActionTypeSaved actionTypeSaved;

        public SavedTableAsyncTask(DaoSavedProjects savedProjectsDao, ActionTypeSaved actionTypeSaved) {
            this.savedProjectsDao = savedProjectsDao;
            this.actionTypeSaved = actionTypeSaved;
        }

        @Override
        protected Void doInBackground(ModelSavedGitHubProject... savedGitHubProjects) {
            switch(actionTypeSaved) {
                case DELETE_SAVED:
                    savedProjectsDao.delete(savedGitHubProjects[0]);
                    break;
                case INSERT_BOOKMARK:
                    savedProjectsDao.insert(savedGitHubProjects[0]);
                    break;
                case DELETE_ALL_SAVED:
                    savedProjectsDao.deleteAllSavedRepos();
                    break;
                case UPDATE_SAVED:
                    savedProjectsDao.update(savedGitHubProjects[0]);
                    break;
                    default:
                        break;
            }

            return null;
        }
    }


    public void bookmarkProject(ModelBaseGitHubProject baseGitHubProject) {
        new SavedTableAsyncTask(savedProjectsDao, ActionTypeSaved.INSERT_BOOKMARK)
                .execute();
    }

    public void deleteSavedRepo(ModelSavedGitHubProject savedGitHubProject) {
        new SavedTableAsyncTask(savedProjectsDao, ActionTypeSaved.DELETE_SAVED)
                .execute();
    }

    public void deleteAllSavedRepos(){
        new SavedTableAsyncTask(savedProjectsDao, ActionTypeSaved.DELETE_ALL_SAVED).execute();
    }



    private enum ActionTypeSaved {
        DELETE_SAVED,
        DELETE_ALL_SAVED,
        UPDATE_SAVED,
        INSERT_BOOKMARK
    }

}
