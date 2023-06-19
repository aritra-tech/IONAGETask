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

    private val client = APIClient.getApiClient()
    private val results = arrayListOf<SearchResult>()
    private val adapter = ResultAdapter(results)


    private lateinit var binding: ActivityMainBinding


    private var currentPage = 0
    private var currentQuery = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialise binding variable
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.rvSearchResult.apply {
            adapter = this@MainActivity.adapter
            layoutManager = GridLayoutManager(applicationContext, 2) // Displays search result manager in 2 columns
        }


        binding.searchBtn.setOnClickListener {

            val query = binding.etQuery.text.toString().trim()
            if (currentQuery != query) {
                currentPage = 0
                val count = results.size
                results.clear()
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
            binding.animationSearch.visibility = View.GONE
            binding.searchDemoText.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE
            loadResults(query, currentPage)
        }
    }

    private fun loadResults(query: String, page: Int) {
        // search movies
        client.searchMovies(query, page).enqueue(object : Callback<ResultWrapper> {
            override fun onResponse(call: Call<ResultWrapper>, response: Response<ResultWrapper>) {
                response.body()?.let { body ->
                    if (body.searchResults == null) {
                        Toast.makeText(applicationContext, getString(R.string.no_result, query), Toast.LENGTH_LONG).show()
                    }
                    else {
                        binding.consSearchResult.isVisible = true
                        binding.consWaitingSearch.isVisible = false
                        results.addAll(body.searchResults)
                        adapter.notifyItemRangeInserted(results.size - body.searchResults.size, body.searchResults.size)
                    }
                    binding.progressBar.visibility = View.GONE
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