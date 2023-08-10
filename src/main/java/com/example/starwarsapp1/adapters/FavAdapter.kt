package com.example.starwarsapp1.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.starwarsapp1.databinding.ListFavBinding
import com.example.starwarsapp1.R
import androidx.recyclerview.widget.ListAdapter

class FavAdapter() : ListAdapter<DbModel, FavAdapter.Holder>(Comparator()) {

    class Holder(view : View) : RecyclerView.ViewHolder(view) {
        val binding = ListFavBinding.bind(view)

        fun bind(item: DbModel) = with(binding) {
            tvNameDb.text = item.name
            tvDescDb.text = item.description
            tvInfoDb.text = item.additionalInfo
            ibFavDb.isSelected.equals(item.isFav)
        }
    }

    class Comparator : DiffUtil.ItemCallback<DbModel>() {
        override fun areItemsTheSame(oldItem: DbModel, newItem: DbModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DbModel, newItem: DbModel): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_fav, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
    }
}