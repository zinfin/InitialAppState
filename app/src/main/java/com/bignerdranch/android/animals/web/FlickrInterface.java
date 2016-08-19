package com.bignerdranch.android.animals.web;

import com.bignerdranch.android.animals.model.GalleryItemList;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

public interface FlickrInterface {

    @GET("/services/rest?method=flickr.photos.search&format=json&nojsoncallback=1&extras=url_s")
    void photoSearch(@Query("api_key") String apiKey, @Query("text") String query,
                     Callback<GalleryItemList> callback);
}