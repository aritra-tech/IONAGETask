package com.aritra.ionagetask.models

import com.google.gson.annotations.SerializedName

data class MovieDetails (
    @SerializedName("Title")
    val title: String,
    @SerializedName("Year")
    val year: String,
    @SerializedName("Rated")
    val rated: String,
    @SerializedName("Released")
    val released: String,
    @SerializedName("Genre")
    val genre: String,
    @SerializedName("Director")
    val director: String,
    @SerializedName("Actors")
    val actors: String,
    @SerializedName("Plot")
    val plot: String,
    @SerializedName("imdbRating")
    val imdbRating: String,
    @SerializedName("Poster")
    val poster: String,
)