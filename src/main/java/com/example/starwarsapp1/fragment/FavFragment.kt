package com.example.starwarsapp1.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.starwarsapp1.MainViewModel
import com.example.starwarsapp1.R
import com.example.starwarsapp1.adapters.FavAdapter
import com.example.starwarsapp1.databinding.FragmentDbBinding
import com.example.starwarsapp1.databinding.FragmentFavBinding
import com.example.starwarsapp1.db.MyDbManager

class FavFragment : Fragment() {
    private lateinit var binding : FragmentFavBinding
    private lateinit var adapter: FavAdapter
    private lateinit var myDbManager : MyDbManager
    private val model : MainViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRcView()
        model.liveDataListFav.observe(viewLifecycleOwner) { dbList ->
            adapter.submitList(dbList)
        }
    }

    private fun initRcView() = with(binding){
        rcViewFav.layoutManager = LinearLayoutManager(activity)
        adapter = FavAdapter()
        rcViewFav.adapter = adapter
    }

    companion object {
        @JvmStatic
        fun newInstance() = FavFragment()
    }
}