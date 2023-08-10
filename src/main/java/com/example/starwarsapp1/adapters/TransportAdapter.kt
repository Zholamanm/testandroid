package com.example.starwarsapp1.adapters

import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.starwarsapp1.R
import com.example.starwarsapp1.databinding.ListItemBinding
import androidx.recyclerview.widget.ListAdapter

class TransportAdapter : ListAdapter<TransportModel, TransportAdapter.Holder>(Comparator()) {

    class Holder(view : View) : RecyclerView.ViewHolder(view) {
        val binding = ListItemBinding.bind(view)

        fun bind(item : TransportModel) = with(binding) {
            tvStarship.text = item.name
            tvModel.text = item.model
            tvMaster.text = item.manufacturer
        }
    }

    class Comparator : DiffUtil.ItemCallback<TransportModel>() {
        override fun areItemsTheSame(oldItem: TransportModel, newItem: TransportModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: TransportModel, newItem: TransportModel): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
    }
}