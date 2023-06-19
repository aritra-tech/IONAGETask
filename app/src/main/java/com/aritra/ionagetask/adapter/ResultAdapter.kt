package com.aritra.ionagetask.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aritra.ionagetask.databinding.ItemMovieResultBinding
import com.aritra.ionagetask.models.SearchResult
import com.aritra.ionagetask.ui.MovieDetailsActivity
import com.bumptech.glide.Glide

class ResultAdapter(private val results: ArrayList<SearchResult>) : RecyclerView.Adapter<ResultAdapter.SearchResultViewHolder>() {

    // View Holder for Search Item
    inner class SearchResultViewHolder(private val binding: ItemMovieResultBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: SearchResult) {
            Glide.with(binding.root).load(data.posterLink).into(binding.imgResult)
            binding.txResult.text = data.title
            binding.root.setOnClickListener {
                // start details activity using view context
                val ctx = binding.root.context
                ctx.startActivity(
                    Intent(
                        ctx,
                        MovieDetailsActivity::class.java
                    ).apply {
                        // send imdb_id to activity
                        putExtra(MovieDetailsActivity.IMDB_ID, data.imdbId)
                    }
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        return SearchResultViewHolder(ItemMovieResultBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        holder.bind(results[position])
    }

    override fun getItemCount(): Int {
        return results.size
    }
}