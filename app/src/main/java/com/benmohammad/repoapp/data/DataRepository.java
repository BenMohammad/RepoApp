package com.benmohammad.repoapp.data;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.MutableLiveData;

import com.benmohammad.repoapp.data.database.DaoCachedProjects;
import com.benmohammad.repoapp.data.database.DaoSavedProjects;
import com.benmohammad.repoapp.data.database.GitHubProjectsDatabase;
import com.benmohammad.repoapp.data.webservice.apiresponse.GitHubClientService;
import com.benmohammad.repoapp.utils.Configuration;
import com.benmohammad.repoapp.utils.WebServiceMessage;

import java.util.Calendar;
import java.util.Date;

import retrofit2.Retrofit;

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

    private void setLastSearchTerm(String searchTerm) {
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

    }

}
