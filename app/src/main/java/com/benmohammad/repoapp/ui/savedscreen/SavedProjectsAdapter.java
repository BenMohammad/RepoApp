package com.benmohammad.repoapp.ui.savedscreen;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.benmohammad.repoapp.R;
import com.benmohammad.repoapp.data.database.ModelSavedGitHubProject;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class SavedProjectsAdapter extends RecyclerView.Adapter<SavedProjectsAdapter.SavedViewHolder> {

    private List<ModelSavedGitHubProject> savedGitHubProjectList = new ArrayList<>();
    private IClickCallback iClickCallback;
    private Resources resources;

    SavedProjectsAdapter(IClickCallback iClickCallback) {
        this.iClickCallback = iClickCallback;
    }


    @NonNull
    @Override
    public SavedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.repo_item, parent, false);
        if(resources == null) {
            resources = itemView.getContext().getApplicationContext().getResources();

        }

        return new SavedViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedViewHolder holder, int position) {
        final ModelSavedGitHubProject currentSavedProject = savedGitHubProjectList.get(position);
        Glide.with(holder.itemView.getContext()).load(currentSavedProject.getAvatarUrl()).into(holder.avatar);
        holder.ownerName.setText(currentSavedProject.getOwnerName());
        holder.repoName.setText(currentSavedProject.getRepoName());
        holder.createdAtTv.setText(resources.getString(R.string.created_at, currentSavedProject.getPrettyCreatedAt()));
        holder.updatedAtTv.setText(resources.getString(R.string.updated_at, currentSavedProject.getPrettyUpdatedAt()));
        holder.language.setText(resources.getString(R.string.programming_language, currentSavedProject.getLanguage()));
        holder.forksCount.setText(resources.getString(R.string.forks_count, currentSavedProject.getForksCount()));
        holder.score.setText(resources.getString(R.string.score, currentSavedProject.getScore()));

    }

    private ModelSavedGitHubProject getCurrentRowInfo(int position) {
        return savedGitHubProjectList.get(position);
    }


    public void setList(List<ModelSavedGitHubProject> savedGitHubProjects) {
        if(this.savedGitHubProjectList == null) {
            this.savedGitHubProjectList = savedGitHubProjects;
            notifyDataSetChanged();
        } else {
            final List<ModelSavedGitHubProject> oldTemList = this.savedGitHubProjectList;
            final List<ModelSavedGitHubProject> newTempList = savedGitHubProjectList;

            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return oldTemList.size();
                }

                @Override
                public int getNewListSize() {
                    return newTempList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return oldTemList.get(oldItemPosition).getId() == newTempList.get(newItemPosition).getId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    ModelSavedGitHubProject oldSavedProject = oldTemList.get(oldItemPosition);
                    ModelSavedGitHubProject newSavedProject = newTempList.get(newItemPosition);
                    return oldSavedProject.equals(newSavedProject);
                }
            });
            result.dispatchUpdatesTo(this);
            this.savedGitHubProjectList = savedGitHubProjects;
        }
    }


    @Override
    public int getItemCount() {
        return savedGitHubProjectList.size();
    }

    class SavedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView ownerName, repoName, createdAtTv, updatedAtTv, language, forksCount, score;
        Button showMoreButton, actionButton;
        ImageView avatar;
        CardView entireRowCardView;

        public SavedViewHolder(@NonNull View itemView) {
            super(itemView);
            repoName = itemView.findViewById(R.id.repo_name);
            ownerName = itemView.findViewById(R.id.repo_owner);
            createdAtTv = itemView.findViewById(R.id.created_at_tv);
            updatedAtTv = itemView.findViewById(R.id.updated_at_tv);
            language = itemView.findViewById(R.id.language);
            forksCount = itemView.findViewById(R.id.forks_count);
            score = itemView.findViewById(R.id.score);
            avatar = itemView.findViewById(R.id.avatar);
            actionButton = itemView.findViewById(R.id.action_button);
            showMoreButton = itemView.findViewById(R.id.show_more_button);
            entireRowCardView = itemView.findViewById(R.id.card_view_entire_row);
            entireRowCardView.setOnClickListener(this);
            actionButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.delete_24dp, 0, 0, 0);
            actionButton.setText(resources.getString(R.string.delete_button_text));
            actionButton.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            ModelSavedGitHubProject currentProject = getCurrentRowInfo(getAdapterPosition());

            switch (v.getId()) {
                case R.id.card_view_entire_row:
                    iClickCallback.onRowClicked(currentProject, ClickActionSaved.GO_TO_DETAILS);
                    break;
                case R.id.action_button:
                    iClickCallback.onRowClicked(currentProject, ClickActionSaved.DELETE_SAVED);
                    break;
                    default:
                        break;
            }
        }
    }

    public interface IClickCallback {
        void onRowClicked(ModelSavedGitHubProject savedGitHubProject, ClickActionSaved clickActionSaved);
    }

    public enum ClickActionSaved{
        DELETE_SAVED,
        GO_TO_DETAILS
    }


}
