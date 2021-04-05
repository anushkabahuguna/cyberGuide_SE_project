package com.example.cyberguide2.rests;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
/*

1) created Retrofit instance. Retrofit turns HTTP API into Java interface.
2) to send network request to an API, used Retrofit Builder class.
3) Base URL common for all requests

 */

public class ApiClient {
    public static final String BASE_URL = "https://newsapi.org/v2/";
    private static Retrofit retrofit = null;
    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    //JSON RESPONSE FROM API
         //Converter takes care of mapping the JSON data to Java objects

                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}