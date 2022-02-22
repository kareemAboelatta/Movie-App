package com.example.movie.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.movie.R
import com.example.movie.data.Movie
import com.example.moviesapp.util.Constants
import kotlinx.android.synthetic.main.top_rated_and_upcoming_movie_item.view.*
import javax.inject.Inject

class AdapterTopRatedAndUpComing @Inject constructor(
    private val glide: RequestManager
) : RecyclerView.Adapter<AdapterTopRatedAndUpComing.MovieViewHolder>() {


    inner class MovieViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie) =
                oldItem.id == newItem.id
        }
    }

    val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):   MovieViewHolder {
        return   MovieViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.top_rated_and_upcoming_movie_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder:   MovieViewHolder, position: Int) {
        val   movie = differ.currentList[position]
        holder.itemView.apply {
            glide.load(Constants.IMAGE_URL+movie.poster_path).into(imageView)
            textViewTitle.text = movie?.title ?: "venom"
            textViewDate.text = movie?.release_date ?: "10/17"

            setOnClickListener {
                onItemClickListener?.let { it(movie) }
            }
        }

    }

    private var onItemClickListener: ((Movie) -> Unit)? = null
    fun setOnItemClickListener(listener: (Movie) -> Unit) {
        onItemClickListener = listener
    }


}