package com.benmohammad.repoapp.ui.savedscreen;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.benmohammad.repoapp.data.DataRepository;
import com.benmohammad.repoapp.data.database.ModelSavedGitHubProject;

import java.util.List;

public class SavedProjectsActivityViewModel extends AndroidViewModel {

    private DataRepository dataRepository;
    private LiveData<List<ModelSavedGitHubProject>> allSavedRepos;


    public SavedProjectsActivityViewModel(@NonNull Application application) {
        super(application);
        dataRepository = DataRepository.getInstance(application);
        allSavedRepos = dataRepository.getAllSavedProjects();
    }

    void delete(ModelSavedGitHubProject savedGitHubProject) {
        dataRepository.deleteSavedRepo(savedGitHubProject);
    }

    void deleteAllSavedRepos() {
        dataRepository.deleteAllSavedRepos();
    }

    LiveData<List<ModelSavedGitHubProject>> getAllSavedRepos() {
        return allSavedRepos;
    }
}
