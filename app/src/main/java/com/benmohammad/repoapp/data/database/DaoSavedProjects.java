package com.benmohammad.repoapp.data.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DaoSavedProjects {

    @Insert
    void insert(ModelSavedGitHubProject baseGitHubRepo);

    @Update
    void update(ModelSavedGitHubProject baseGitHubRepo);

    @Delete
    void delete(ModelSavedGitHubProject baseGitHubRepo);

    @Query("DELETE FROM saved_gh_projects")
    void deleteAllSavedRepos();

    @Query("SELECT * FROM saved_gh_projects ORDER BY id DESC")
    LiveData<List<ModelSavedGitHubProject>> getAllSavedProjects();
}
