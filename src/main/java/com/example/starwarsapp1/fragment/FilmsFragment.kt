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
import com.example.starwarsapp1.adapters.FilmAdapter
import com.example.starwarsapp1.adapters.TransportAdapter
import com.example.starwarsapp1.databinding.FragmentFilmsBinding
import com.example.starwarsapp1.databinding.FragmentTransportBinding

class FilmsFragment : Fragment() {
    private lateinit var binding : FragmentFilmsBinding
    private lateinit var adapter: FilmAdapter
    private val model : MainViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFilmsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRcView()
        model.liveDataListFilms.observe(viewLifecycleOwner){ filmList ->
            adapter.submitList(filmList)
        }
    }

    private fun initRcView() = with(binding){
        rcViewFilms.layoutManager = LinearLayoutManager(activity)
        adapter = FilmAdapter()
        rcViewFilms.adapter = adapter
    }

    companion object {
        @JvmStatic
        fun newInstance() = FilmsFragment()
    }
}