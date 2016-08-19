package com.bignerdranch.android.animals.web;

import android.text.TextUtils;
import android.util.Log;

import com.bignerdranch.android.animals.listener.PhotoSearchListener;
import com.bignerdranch.android.animals.model.GalleryItem;
import com.bignerdranch.android.animals.model.GalleryItemList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

public class PhotoFetcher {
    private static final String TAG = "PhotoFetcher";
    private static final String API_KEY = "d4049e2da567934e35d1e734469cdde0";
    private static final String ENDPOINT = "https://api.flickr.com";

    private static PhotoFetcher sPhotoFetcher;

    private RestAdapter mRestAdapter;
    private List<PhotoSearchListener> mPhotoSearchListeners;
    private List<GalleryItem> mGalleryItems;

    public static PhotoFetcher get() {
        if (sPhotoFetcher == null) {
            sPhotoFetcher = new PhotoFetcher();
        }
        return sPhotoFetcher;
    }

    private PhotoFetcher() {
        mPhotoSearchListeners = new ArrayList<>();
        mGalleryItems = new ArrayList<>();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(GalleryItemList.class,
                        new GalleryItemDeserializer())
                .create();
        mRestAdapter = new RestAdapter.Builder()
                .setEndpoint(ENDPOINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setConverter(new GsonConverter(gson))
                .build();
    }

    public List<GalleryItem> getGalleryItems() {
        return mGalleryItems;
    }

    public void searchCats() {
        FlickrInterface flickrInterface = mRestAdapter.create(FlickrInterface.class);
        flickrInterface.photoSearch(API_KEY, "cat", new Callback<GalleryItemList>() {
            @Override
            public void success(GalleryItemList galleryItemList, Response response) {
                Log.d(TAG, "Downloaded gallery item list");
                setupGalleryItems(galleryItemList);
                notifyPhotoSearchListeners();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "Failed to download cats", error);
            }
        });
    }

    public void searchDogs() {
        FlickrInterface flickrInterface = mRestAdapter.create(FlickrInterface.class);
        flickrInterface.photoSearch(API_KEY, "dog", new Callback<GalleryItemList>() {
            @Override
            public void success(GalleryItemList galleryItemList, Response response) {
                Log.d(TAG, "Downloaded gallery item list");
                setupGalleryItems(galleryItemList);
                notifyPhotoSearchListeners();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "Failed to download cats", error);
            }
        });
    }

    private void setupGalleryItems(GalleryItemList galleryItemList) {
        mGalleryItems.clear();
        for (GalleryItem item : galleryItemList.getGalleryItems()) {
            if (!TextUtils.isEmpty(item.getUrl())) {
                mGalleryItems.add(item);
            }
        }
    }

    public void registerPhotoSearchListener(PhotoSearchListener photoSearchListener) {
        mPhotoSearchListeners.add(photoSearchListener);
    }

    public void unregisterPhotoSearchListener(
            PhotoSearchListener photoSearchListener) {
        mPhotoSearchListeners.remove(photoSearchListener);
    }

    private void notifyPhotoSearchListeners() {
        for (PhotoSearchListener listener : mPhotoSearchListeners) {
            listener.onPhotoSearchFinished();
        }
    }
}
