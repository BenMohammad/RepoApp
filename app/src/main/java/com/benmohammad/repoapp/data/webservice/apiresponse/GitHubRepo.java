package com.benmohammad.repoapp.data.webservice.apiresponse;

import androidx.annotation.NonNull;

import com.benmohammad.repoapp.data.webservice.apiresponse.items.Item;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GitHubRepo {

    @SerializedName("items")
    @Expose
    private ArrayList<Item> items;

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public String toString() {
        return "ModelBaseGitHubProject{" + "items=" + items + '}';
    }
}
