package com.example.starwarsapp1.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.starwarsapp1.MainViewModel
import com.example.starwarsapp1.adapters.TransportAdapter
import com.example.starwarsapp1.databinding.FragmentTransportBinding

class TransportFragment : Fragment() {
    private lateinit var binding : FragmentTransportBinding
    private lateinit var adapter: TransportAdapter
    private val model : MainViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTransportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRcView()
        model.liveDataList.observe(viewLifecycleOwner){ transportList ->
            adapter.submitList(transportList)
        }
    }

    private fun initRcView() = with(binding){
        rcView.layoutManager = LinearLayoutManager(activity)
        adapter = TransportAdapter()
        rcView.adapter = adapter
    }


    companion object {
        @JvmStatic
        fun newInstance() = TransportFragment()
    }
}