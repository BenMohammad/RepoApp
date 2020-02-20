package com.benmohammad.repoapp.data.webservice.apiresponse;

import com.benmohammad.repoapp.utils.Configuration;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface GitHubClientService {

    String BASE_URL = "http://api.github.com/";
    String DEFAULT_SEARCH_TERM = Configuration.DEFAULT_SEARCH_ITEM;
    int RESULTS_PER_PAGE = Configuration.RESULTS_PER_PAGE;


    //@Headers("User-Agent: giant2turtle@gmail.com")
    @GET("search/repositories")
    Call<GitHubRepo> getRepos(@Query("q") String searchParam, @Query("page") int page, @Query("per_page") int perPage);


}
