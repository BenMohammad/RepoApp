package com.benmohammad.repoapp.utils;

import android.content.Intent;

import com.benmohammad.repoapp.data.database.ModelSavedGitHubProject;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

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

    public static ModelSavedGitHubProject projectFromIntent(Intent parentIntent) {
        return null;
    }
}
