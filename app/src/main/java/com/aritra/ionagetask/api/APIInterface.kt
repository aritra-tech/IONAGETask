package com.aritra.ionagetask.api

import com.aritra.ionagetask.utils.Constant
import com.aritra.ionagetask.models.MovieDetails
import com.aritra.ionagetask.models.ResultWrapper
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface APIInterface {

    @GET(" ")
    fun searchMovies(
        @Query("s") searchQuery: String,
        @Query("page") pageNumber: Int = 1,
        @Query("apikey") apiKey: String = Constant.API_KEY,
    ): Call<ResultWrapper>

    @GET(" ")
    fun getDetailsOfMovie(
        @Query("i") imdbId: String,
        @Query("apikey") apiKey: String = Constant.API_KEY,
    ): Call<MovieDetails>

}