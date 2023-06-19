package com.aritra.ionagetask.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.aritra.ionagetask.R
import com.aritra.ionagetask.adapter.ResultAdapter
import com.aritra.ionagetask.api.APIClient
import com.aritra.ionagetask.databinding.ActivityMainBinding
import com.aritra.ionagetask.models.ResultWrapper
import com.aritra.ionagetask.models.SearchResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    // Api Client for our app
    private val client = APIClient.getApiClient()
    // Array list which holds search results
    private val results = arrayListOf<SearchResult>()
    // Adapter for the search recycler view
    private val adapter = ResultAdapter(results)

    // Binding variable for MainActivity
    private lateinit var binding: ActivityMainBinding

    // Holds current page number
    private var currentPage = 0
    // Current search query
    private var currentQuery = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialise binding variable
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Apply and setup Recycler View
        binding.rvSearchResult.apply {
            adapter = this@MainActivity.adapter
            layoutManager = GridLayoutManager(applicationContext, 2) // Displays search result manager in 2 columns
        }

        // Set action for the Floating Action Button
        binding.searchBtn.setOnClickListener {
            // current search query
            val query = binding.etQuery.text.toString().trim()
            if (currentQuery != query) {
                // If user entered new query reload results from start
                currentPage = 0
                // Count new items to notify adapter
                val count = results.size
                // clear results
                results.clear()
                // notify adapter that new items are added
                adapter.notifyItemRangeRemoved(0, count)
            }
            // Check for empty query
            if (query.isEmpty()) {
                Toast.makeText(applicationContext, "Enter the movie name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // Load results with the query
            currentQuery = query
            currentPage += 1
            loadResults(query, currentPage)
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun loadResults(query: String, page: Int) {
        // search movies
        client.searchMovies(query, page).enqueue(object : Callback<ResultWrapper> {
            override fun onResponse(call: Call<ResultWrapper>, response: Response<ResultWrapper>) {
                response.body()?.let { body -> // body not null
                    if (body.searchResults == null) {
                        // search results null, so no results loaded
                        Toast.makeText(applicationContext, getString(R.string.no_result, query), Toast.LENGTH_LONG).show()
                    }
                    else {
                        // search results present, showcase them
                        binding.consSearchResult.isVisible = true
                        binding.consWaitingSearch.isVisible = false
                        results.addAll(body.searchResults)
                        adapter.notifyItemRangeInserted(results.size - body.searchResults.size, body.searchResults.size)
                    }
                }
            }

            // Api call failed
            override fun onFailure(call: Call<ResultWrapper>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(applicationContext, getString(R.string.page_end), Toast.LENGTH_LONG).show()
            }
        })
    }
}