package com.benmohammad.repoapp.ui.mainscreen;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.benmohammad.repoapp.R;
import com.benmohammad.repoapp.data.database.ModelCachedGitHubProject;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.CardViewHolder> {

    private List<ModelCachedGitHubProject> cachedGitHubReposList = new ArrayList<>();
    private ItemClickCallBack itemClickCallBack;
    private Resources resources;

    MainAdapter(ItemClickCallBack itemClickCallBack) {
        this.itemClickCallBack = itemClickCallBack;
    }



    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.repo_item, parent, false);
        if(resources == null) {
            resources = itemView.getContext().getApplicationContext().getResources();
        }

        return new CardViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        ModelCachedGitHubProject currentCachedGitHubProject = cachedGitHubReposList.get(position);
        Glide.with(holder.itemView.getContext()).load(currentCachedGitHubProject.getAvatarUrl()).into(holder.avatar);
        holder.ownerName.setText(currentCachedGitHubProject.getOwnerName());
        holder.repoName.setText(currentCachedGitHubProject.getRepoName());
        holder.createdAtTv.setText(resources.getString(R.string.created_at, currentCachedGitHubProject.getPrettyCreatedAt()));
        holder.updatedAtTv.setText(resources.getString(R.string.updated_at, currentCachedGitHubProject.getPrettyUpdatedAt()));
        holder.language.setText(resources.getString(R.string.programming_language, currentCachedGitHubProject.getLanguage()));
        holder.forksCount.setText(resources.getString(R.string.forks_count, currentCachedGitHubProject.getForksCount()));
        holder.score.setText(resources.getString(R.string.score, currentCachedGitHubProject.getScore()));

        if(position == cachedGitHubReposList.size() - 1) {
            holder.showMoreButton.setVisibility(View.VISIBLE);
        } else {
            holder.showMoreButton.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return cachedGitHubReposList.size();
    }

    private ModelCachedGitHubProject getCurrentRowInfo(int position) {
        return cachedGitHubReposList.get(position);
    }

    public void setList(List<ModelCachedGitHubProject> cachedGitHubReposList) {
        this.cachedGitHubReposList = cachedGitHubReposList;
        notifyDataSetChanged();
    }

    class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView ownerName, repoName, createdAtTv, updatedAtTv, language, forksCount, score;
        private Button showMoreButton, actionButton;
        private ImageView avatar;
        private CardView entireRowCardView;

        public CardViewHolder(@NonNull View itemView) {
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
            showMoreButton.setOnClickListener(this);
            entireRowCardView = itemView.findViewById(R.id.card_view_entire_row);
            entireRowCardView.setOnClickListener(this);
            actionButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bookmarks_24dp, 0, 0, 0);
            if(resources == null) {
                resources = itemView.getResources();
            }
            actionButton.setText(resources.getString(R.string.bookmark_button_text));
            actionButton.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            ModelCachedGitHubProject currentProject = getCurrentRowInfo(getAdapterPosition());

            switch (v.getId()) {
                case R.id.card_view_entire_row:
                    itemClickCallBack.onRowClicked(currentProject, MainClickType.GO_TO_DETAILS);
                    break;
                case R.id.action_button:
                    itemClickCallBack.onRowClicked(currentProject, MainClickType.BOOKMARK_PROJECT);
                    break;
                case R.id.show_more_button:
                    itemClickCallBack.onRowClicked(currentProject, MainClickType.SHOW_MORE);
                    break;
                    default:
                        break;
            }
        }
    }

    public interface ItemClickCallBack {
        void onRowClicked(ModelCachedGitHubProject cachedGitHubProject, MainClickType mainClickType);
    }

    public enum MainClickType {
        BOOKMARK_PROJECT,
        SHOW_MORE,
        GO_TO_DETAILS
    }

}
