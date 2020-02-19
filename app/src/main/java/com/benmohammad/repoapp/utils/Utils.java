package com.benmohammad.repoapp.utils;

import android.app.Activity;
import android.content.Intent;

import com.benmohammad.repoapp.data.database.ModelBaseGitHubProject;
import com.benmohammad.repoapp.data.database.ModelSavedGitHubProject;
import com.benmohammad.repoapp.ui.detailsscreen.DetailsActivity;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {


    public static Intent createDetailsIntent(Activity parentActivity, ModelBaseGitHubProject clickedProject) {
        Intent myIntent = new Intent(parentActivity, DetailsActivity.class);
        myIntent.putExtra("parentActivity", parentActivity.getClass().getSimpleName());
        myIntent.putExtra("avatarUrl", clickedProject.getAvatarUrl());
        myIntent.putExtra("createdAt", clickedProject.getCreatedAt());
        myIntent.putExtra("pushedAt", clickedProject.getPushedAt());
        myIntent.putExtra("updatedAt", clickedProject.getUpdatedAt());
        myIntent.putExtra("description", clickedProject.getDescription());
        myIntent.putExtra("htmlUrl", clickedProject.getHtmlUrl());
        myIntent.putExtra("forksCount", Integer.toString(clickedProject.getForksCount()));
        myIntent.putExtra("ownersName", clickedProject.getOwnerName());
        myIntent.putExtra("language", clickedProject.getLanguage());
        myIntent.putExtra("score", Float.toString(clickedProject.getScore()));
        myIntent.putExtra("repoName", clickedProject.getRepoName());
        myIntent.putExtra("repoSize", Double.toString(clickedProject.getRepoSize()));
        myIntent.putExtra("id", Integer.toString(clickedProject.getId()));
        myIntent.putExtra("hasWiki", Boolean.toString(clickedProject.hasWiki()));
        myIntent.putExtra("prettyCreatedAt", clickedProject.getPrettyCreatedAt());
        myIntent.putExtra("prettyUpdatedAt", clickedProject.getPrettyUpdatedAt());
        myIntent.putExtra("prettyPushedAt", clickedProject.getPrettyPushedAt());
        return myIntent;

    }


    public static String convertToPrettyTime(String dateStr) {
        if(dateStr != null) {
            try {
                PrettyTime prettyTime = new PrettyTime();
                SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                Date convertedDate = sourceFormat.parse(dateStr);
                return prettyTime.format(convertedDate);
            } catch (ParseException e) {
                return dateStr;
            }
        }

        else {
            return "N/A";
        }
    }

    public static ModelSavedGitHubProject changeProjectType(ModelBaseGitHubProject baseGitHubProject) {
        ModelSavedGitHubProject savedGitHubProject = new ModelSavedGitHubProject();
        savedGitHubProject.setAvatarUrl(baseGitHubProject.getAvatarUrl());
        savedGitHubProject.setCreatedAt(baseGitHubProject.getCreatedAt());
        savedGitHubProject.setPushedAt(baseGitHubProject.getPushedAt());
        savedGitHubProject.setUpdatedAt(baseGitHubProject.getUpdatedAt());
        savedGitHubProject.setHtmlUrl(baseGitHubProject.getHtmlUrl());
        savedGitHubProject.setDescription(baseGitHubProject.getDescription());
        savedGitHubProject.setForksCount(baseGitHubProject.getForksCount());
        savedGitHubProject.setHasWiki(baseGitHubProject.hasWiki());
        savedGitHubProject.setLanguage(baseGitHubProject.getLanguage());
        savedGitHubProject.setOwnerName(baseGitHubProject.getOwnerName());
        savedGitHubProject.setRepoName(baseGitHubProject.getRepoName());
        savedGitHubProject.setRepoSize(baseGitHubProject.getRepoSize());
        savedGitHubProject.setScore(baseGitHubProject.getScore());
        return savedGitHubProject;

    }

    public static ModelSavedGitHubProject projectFromIntent(Intent intent) {
        ModelSavedGitHubProject savedGitHubProject =  new ModelSavedGitHubProject();
        savedGitHubProject.setAvatarUrl(intent.getStringExtra("avatarUrl"));
        savedGitHubProject.setCreatedAt(intent.getStringExtra("createdAt"));
        savedGitHubProject.setPushedAt(intent.getStringExtra("pushedAt"));
        savedGitHubProject.setUpdatedAt(intent.getStringExtra("updatedAt"));
        savedGitHubProject.setDescription(intent.getStringExtra("description"));
        savedGitHubProject.setHtmlUrl(intent.getStringExtra("htmlUrl"));
        savedGitHubProject.setForksCount(Integer.parseInt(intent.getStringExtra("forksCount")));
        savedGitHubProject.setOwnerName(intent.getStringExtra(intent.getStringExtra("ownersName")));
        savedGitHubProject.setLanguage(intent.getStringExtra("language"));
        savedGitHubProject.setScore(Float.parseFloat(intent.getStringExtra("score")));
        savedGitHubProject.setRepoName(intent.getStringExtra("repoName"));
        savedGitHubProject.setRepoSize(Double.parseDouble(intent.getStringExtra("repoSize")));
        savedGitHubProject.setId(Integer.parseInt(intent.getStringExtra("id")));
        savedGitHubProject.setHasWiki(Boolean.parseBoolean(intent.getStringExtra("hasWiki")));
        return savedGitHubProject;

    }
}
