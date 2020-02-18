package com.benmohammad.repoapp.data.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {ModelCachedGitHubProject.class, ModelSavedGitHubProject.class}, version = 1)
public abstract class GitHubProjectsDatabase extends RoomDatabase {
    private static GitHubProjectsDatabase instance;

    public abstract DaoSavedProjects savedProjectsDao();

    public abstract  DaoCachedProjects cachedProjectsDao();

    public static synchronized GitHubProjectsDatabase getInstance(Context context) {
        if(instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    GitHubProjectsDatabase.class, "projects_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }
    };
}
