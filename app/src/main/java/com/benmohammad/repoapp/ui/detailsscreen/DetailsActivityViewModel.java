package com.benmohammad.repoapp.ui.detailsscreen;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.benmohammad.repoapp.data.DataRepository;
import com.benmohammad.repoapp.data.database.ModelBaseGitHubProject;
import com.benmohammad.repoapp.data.database.ModelSavedGitHubProject;

public class DetailsActivityViewModel extends AndroidViewModel {

    private DataRepository dataRepository;

    public DetailsActivityViewModel(@NonNull Application application, DataRepository dataRepository) {
        super(application);
        this.dataRepository = dataRepository;
    }

    void bookmarkProject(ModelBaseGitHubProject baseGitHubProject) {
        dataRepository.bookmarkProject(baseGitHubProject);
    }

    void deleteBookmark(ModelSavedGitHubProject savedGitHubProject) {
        dataRepository.deleteSavedRepo(savedGitHubProject);
    }
}
