package com.aritra.ionagetask.models

import com.google.gson.annotations.SerializedName

data class ResultWrapper(
    @SerializedName("Search")
    val searchResults: List<SearchResult>?
)
