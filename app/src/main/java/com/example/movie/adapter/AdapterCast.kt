package com.example.movie.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.movie.R
import com.example.movie.data.cast.Cast
import com.example.moviesapp.util.Constants
import kotlinx.android.synthetic.main.top_rated_and_upcoming_movie_item.view.*
import javax.inject.Inject

class AdapterCast @Inject constructor(
    private val glide: RequestManager
) : RecyclerView.Adapter<AdapterCast.CastViewHolder>() {


    inner class CastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Cast>() {
            override fun areItemsTheSame(oldItem: Cast, newItem: Cast) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Cast, newItem: Cast) =
                oldItem.id == newItem.id
        }
    }

    val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder {
        return CastViewHolder(
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

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        val cast = differ.currentList[position]
        holder.itemView.apply {
            glide.load(Constants.IMAGE_URL + cast.profile_path).into(imageView)

            textViewTitle.text = cast?.character ?: "Spider man"
            textViewDate.text = cast?.name ?: "Tobey Maguire"

            setOnClickListener {
                onItemClickListener?.let { it(cast) }
            }

        }


    }


    private var onItemClickListener: ((Cast) -> Unit)? = null

    fun setOnItemClickListener(listener: (Cast) -> Unit) {
        onItemClickListener = listener
    }

}
