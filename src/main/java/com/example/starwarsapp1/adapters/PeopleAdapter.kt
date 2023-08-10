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
import com.example.starwarsapp1.databinding.ListPeopleBinding

class PeopleAdapter : ListAdapter<PeopleModel, PeopleAdapter.Holder>(Comparator()) {

    class Holder(view : View) : RecyclerView.ViewHolder(view) {
        val binding = ListPeopleBinding.bind(view)

        fun bind(item : PeopleModel) = with(binding) {
            tvName.text = item.name
            tvGender1.text = item.gender
            tvCounts.text = item.starshipscount.toString()
        }
    }

    class Comparator : DiffUtil.ItemCallback<PeopleModel>() {
        override fun areItemsTheSame(oldItem: PeopleModel, newItem: PeopleModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: PeopleModel, newItem: PeopleModel): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_people, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
    }
}