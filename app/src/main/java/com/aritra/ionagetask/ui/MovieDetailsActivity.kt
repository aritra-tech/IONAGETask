package com.aritra.ionagetask.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aritra.ionagetask.R
import com.aritra.ionagetask.api.APIClient
import com.aritra.ionagetask.databinding.ActivityMovieDetailsBinding
import com.aritra.ionagetask.models.MovieDetails
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieDetailsActivity : AppCompatActivity() {

    private val client = APIClient.getApiClient()
    private lateinit var binding: ActivityMovieDetailsBinding

    companion object {
        const val IMDB_ID = "imdb_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMovieDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imdbID = intent?.extras?.getString(IMDB_ID) ?: ""

        if (imdbID.isNotEmpty()) {
            client.getDetailsOfMovie(imdbID).enqueue(object : Callback<MovieDetails> {
                override fun onResponse(call: Call<MovieDetails>, response: Response<MovieDetails>) {
                    response.body()?.let { details ->
                        binding.apply {
                            txMovieTitle.text = details.title
                            txMovieActors.text = details.actors
                            txMovieDirector.text = details.director
                            txMovieGenre.text = details.genre
                            txMoviePlot.text = details.plot
                            txMovieRated.text = details.rated
                            txMovieRating.text = details.imdbRating
                            txMovieRelease.text = details.released
                            Glide.with(applicationContext).load(details.poster).into(binding.imgMovie)
                        }
                    }
                }

                override fun onFailure(call: Call<MovieDetails>, t: Throwable) {
                    t.printStackTrace()
                }

            })
        }
    }
}