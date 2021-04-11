package com.example.cyberguide2.rests;

import com.example.cyberguide2.model.ResponseModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
//This interface is using special retrofit annotations to encode the parameters and request method.
// The return value is always parameterized like call<T> object such as call<ResponseModel>
public interface APIInterface {
    //news api --- https://newsapi.org/v2/everything
    @GET("everything")
    //query appended
//    https://newsapi.org/v2/everything?language=en&q=cyberbullying
    Call<ResponseModel> getLatestNews( @Query("apiKey") String apiKey,@Query("q") String keyword,@Query("language") String language,@Query("page") String page,@Query("sortBy")String sortBy);
}
