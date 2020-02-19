package com.benmohammad.repoapp.ui.mainscreen;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.benmohammad.repoapp.data.DataRepository;
import com.benmohammad.repoapp.data.database.ModelBaseGitHubProject;
import com.benmohammad.repoapp.data.database.ModelCachedGitHubProject;
import com.benmohammad.repoapp.data.webservice.apiresponse.GitHubClientService;
import com.benmohammad.repoapp.utils.WebServiceMessage;

import java.util.Date;
import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {

    private DataRepository repository;
    private LiveData<List<ModelCachedGitHubProject>> allCachedProjects;

    private int pageCounter = 1;
    private String searchTerm;
    private boolean isFirstRun;
    private MutableLiveData<WebServiceMessage> webServiceStatus;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        repository = DataRepository.getInstance(application);
        allCachedProjects = repository.getAllCachedProjects();
        searchTerm = repository.getLastSavedSearchTerm();
        isFirstRun = repository.isFirstRun();
        if(!repository.isCacheFreshEnough(new Date())) {
            repository.updateFromWebservice(this.searchTerm, pageCounter, GitHubClientService.RESULTS_PER_PAGE);
        }
        webServiceStatus = (MutableLiveData<WebServiceMessage>) Transformations.switchMap(repository.getWebServiceMessage(),
                (Function<WebServiceMessage, LiveData<WebServiceMessage>>) input -> {
                    switch (input) {
                        case UPDATING_STATUS:
                            webServiceStatus.postValue(WebServiceMessage.UPDATING_STATUS);
                            break;
                        case ON_FAILURE:
                            if(pageCounter > 1) pageCounter--;
                                webServiceStatus.postValue(WebServiceMessage.ON_FAILURE);
                            break;
                        case ON_RESPONSE_SUCCESS:
                            webServiceStatus.postValue(WebServiceMessage.ON_RESPONSE_SUCCESS);
                            break;
                        case ON_RESPONSE_NOTHING_FOUND:
                            webServiceStatus.postValue(WebServiceMessage.ON_RESPONSE_NOTHING_FOUND);
                            break;
                        case ON_RESPONSE_NO_MORE_RESULTS:
                            webServiceStatus.postValue(WebServiceMessage.ON_RESPONSE_NO_MORE_RESULTS);
                            break;
                            default:
                                break;
                    }
                    return null;
                });
            }


    @Override
    protected void onCleared() {
        repository.trimCacheTable();
        repository.clearWebServiceMessage();
        super.onCleared();

    }

    void setFirstRunFlagOff()  {
        isFirstRun = false;
        repository.setFirstRunOff();
    }

    boolean isFirstRun() {
        return isFirstRun;
    }

    void bookmarkProject(ModelBaseGitHubProject baseGitHubProject) {
        repository.bookmarkProject(baseGitHubProject);
    }

    LiveData<List<ModelCachedGitHubProject>>  getAllCachedProjects() {
        return allCachedProjects;
    }

    String getSearchTerm() {
        return searchTerm;
    }

    void searchGitHub(String searchTerm) {
        pageCounter = 1;
        this.searchTerm = searchTerm;
        repository.updateFromWebservice(searchTerm, pageCounter, GitHubClientService.RESULTS_PER_PAGE);
    }

    void loadMore() {
        pageCounter++;
        repository.updateFromWebservice(this.searchTerm, pageCounter, GitHubClientService.RESULTS_PER_PAGE);
    }

    MutableLiveData<WebServiceMessage> getWebServiceStatus() {
        return webServiceStatus;
    }
}
