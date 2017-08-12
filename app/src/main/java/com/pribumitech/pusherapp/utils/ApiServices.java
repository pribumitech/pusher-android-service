package com.pribumitech.pusherapp.utils;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Seluruh keperluan Response ke SERVER berada disini
 */
public interface ApiServices {

    /**
     * Post User Settings
     *
     * @return Observable
     */
    @FormUrlEncoded
    @POST("pusherevent")
    public Observable<String> postTrigger(
            @Field("channel_name") String channel_name,
            @Field("event_name") String event_name,
            @Field("data") String data
    );
}

