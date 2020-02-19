package com.benmohammad.repoapp.data.webservice.apiresponse.items;

import androidx.annotation.NonNull;

import com.benmohammad.repoapp.data.webservice.apiresponse.Owner;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item {

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("has_wiki")
    @Expose
    private boolean has_wiki;

    @SerializedName("size")
    @Expose
    private double size;

    @SerializedName("owner")
    @Expose
    private Owner owner;

    @SerializedName("html_url")
    @Expose
    private String html_url;

    @SerializedName("created_at")
    @Expose
    private String created_at;

    @SerializedName("updated_at")
    @Expose
    private String updated_at;

    @SerializedName("pushed_at")
    @Expose
    private String pushed_at;

    @SerializedName("language")
    @Expose
    private String language;

    @SerializedName("forks_count")
    @Expose
    private int forks_count;

    @SerializedName("score")
    @Expose
    private float score;

    @SerializedName("description")
    @Expose
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public int getForks_count() {
        return forks_count;
    }

    public void setForks_count(int forks_count) {
        this.forks_count = forks_count;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getPushed_at() {
        return pushed_at;
    }

    public void setPushed_at(String pushed_at) {
        this.pushed_at = pushed_at;
    }

    public String getHtml_url() {
        return html_url;
    }

    public void setHtml_url(String html_url) {
        this.html_url = html_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isHas_wiki() {
        return has_wiki;
    }

    public void setHas_wiki(boolean has_wiki) {
        this.has_wiki = has_wiki;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    @NonNull
    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                ", has_wiki=" + has_wiki +
                ", size=" + size +
                ", owner=" + owner +
                ", html_url=" + html_url + '\'' +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                ", pushed_at='" + pushed_at + '\'' +
                ", language='" + language + '\'' +
                ", forks_count=" + forks_count +
                ", score=" + score +
                ", description='" + description + '\'' +
                '}';

    }
}
