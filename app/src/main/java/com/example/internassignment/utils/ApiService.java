package com.example.internassignment.utils;

import com.example.internassignment.model.Country;
import com.example.internassignment.model.State;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface ApiService {
    @GET("countries/")
    Call<List<Country>> getCountries(@Header("X-CSCAPI-KEY") String apiKey);

    @GET("countries/{ciso}/states")
    Call<List<State>> getState(
            @Header("X-CSCAPI-KEY") String apiKey,
            @Path("ciso") String countryCode
    );
}
