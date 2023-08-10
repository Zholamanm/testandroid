package com.example.starwarsapp1.adapters

import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.starwarsapp1.R
import com.example.starwarsapp1.databinding.ListFilmsBinding
import androidx.recyclerview.widget.ListAdapter

class FilmAdapter : ListAdapter<FilmModel, FilmAdapter.Holder>(Comparator()) {

    class Holder(view : View) : RecyclerView.ViewHolder(view) {
        val binding = ListFilmsBinding.bind(view)

        fun bind(item : FilmModel) = with(binding) {
            tvTitle.text = item.title
            tvDirector.text = item.director
            tvProducer.text = item.producer
        }
    }

    class Comparator : DiffUtil.ItemCallback<FilmModel>() {
        override fun areItemsTheSame(oldItem: FilmModel, newItem: FilmModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: FilmModel, newItem: FilmModel): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_films, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
    }
}