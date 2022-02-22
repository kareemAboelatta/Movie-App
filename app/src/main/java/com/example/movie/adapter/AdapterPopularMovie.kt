package com.example.movie.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.movie.R
import com.example.movie.data.Movie
import com.example.moviesapp.util.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.android.synthetic.main.popular_movie_list_item.view.*
import javax.inject.Inject

class AdapterPopularMovie @Inject constructor (
    @ApplicationContext var context: Context,
    private val glide: RequestManager
) : RecyclerView.Adapter<AdapterPopularMovie.MovieViewHolder>() {


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
                R.layout.popular_movie_list_item,
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
        val animation=AnimationUtils.loadAnimation(context,R.anim.slide_in_top)
        holder.itemView.apply {
           glide.load(Constants.IMAGE_URL+movie.poster_path).into(imageViewPopular)

            rating_popular.rating= ((movie.vote_average/ 2.00.toDouble() ).toFloat())
            rating_popular_ratting.text= movie.vote_average.toString()


            setOnClickListener {
                onItemClickListener?.let { it(movie) }
            }

            startAnimation(animation)

        }

/*        holder.itemView.setOnClickListener {
            //start   Movie acticty
            val intent = Intent(context,MovieActivity::class.java)
            intent.putExtra("url",   Movie.url)
            intent.putExtra("title",   Movie.title)
            context.startActivity(intent)
        }*/
    }
    private var onItemClickListener: ((Movie) -> Unit)? = null

    fun setOnItemClickListener(listener: (Movie) -> Unit) {
        onItemClickListener = listener
    }

}