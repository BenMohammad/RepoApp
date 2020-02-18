package com.benmohammad.repoapp.data.webservice.apiresponse;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Owner {

    @SerializedName("login")
    @Expose
    private String login;

    @SerializedName("avatar_url")
    @Expose
    private String avatar_url;

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @NonNull
    @Override
    public String toString() {
        return "Owner{" +
                "login='" + login + '\'' + ", avatar_url='" + avatar_url + '\'' + '}';
    }
}
