package com.benmohammad.repoapp.data.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DaoCachedProjects {

    @Insert
    void insert(ModelCachedGitHubProject baseGitHubRepo);

    @Insert
    void saveToCache(List<ModelCachedGitHubProject> listOfGitHubProjects);

    @Update
    void update(ModelCachedGitHubProject baseGitHubRepo);

    @Delete
    void delete(ModelCachedGitHubProject baseGitHubRepo);

    @Query("DELETE FROM cached_gh_projects")
    void deleteAllCachedRepos();

    @Query("SELECT * FROM cached_gh_projects")
    LiveData<List<ModelCachedGitHubProject>> getAllCachedFromDb();

    @Query("DELETE FROM cached_gh_projects WHERE id NOT IN (SELECT id FROM cached_gh_projects ORDER BY id LIMIT :cacheLimit)")
    void trimCacheTable(int cacheLimit);
}
