package com.benmohammad.repoapp.ui.savedscreen;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.benmohammad.repoapp.R;
import com.benmohammad.repoapp.data.database.ModelSavedGitHubProject;
import com.benmohammad.repoapp.utils.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;


public class SavedProjectsActivity extends AppCompatActivity implements SavedProjectsAdapter.IClickCallback {

    private SavedProjectsActivityViewModel savedProjectsActivityViewModel;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_projects);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab_delete_all_saved_projects);
        fab.setOnClickListener(v -> {
            Snackbar.make(v, R.string.delete_all_snack, Snackbar.LENGTH_SHORT)
                    .setAction("YES", v1 -> {
                        savedProjectsActivityViewModel.deleteAllSavedRepos();
                    }).show();
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recycler_view_save);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final SavedProjectsAdapter adapter = new SavedProjectsAdapter(this);
        recyclerView.setAdapter(adapter);

        savedProjectsActivityViewModel  = ViewModelProviders.of(this).get(SavedProjectsActivityViewModel.class);
        savedProjectsActivityViewModel.getAllSavedRepos().observe(this, modelSavedGitHubProjects -> adapter.setList(modelSavedGitHubProjects));
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return false;
    }


    @Override
    protected void onDestroy() {
        recyclerView.setAdapter(null);
        super.onDestroy();
    }



    @Override
    public void onRowClicked(ModelSavedGitHubProject savedGitHubProject, SavedProjectsAdapter.ClickActionSaved clickActionSaved) {
        switch(clickActionSaved) {
            case DELETE_SAVED:
                Toast.makeText(this, getString(R.string.project_deleted_toast), Toast.LENGTH_SHORT).show();
                savedProjectsActivityViewModel.delete(savedGitHubProject);
                break;
            case GO_TO_DETAILS:
                startActivity(Utils.createDetailsIntent(SavedProjectsActivity.this, savedGitHubProject));
                break;

            default:
                break;
        }
    }
}
