package com.example.starwarsapp1.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.starwarsapp1.MainViewModel
import com.example.starwarsapp1.adapters.PeopleAdapter
import com.example.starwarsapp1.databinding.FragmentPeopleBinding

class PeopleFragment : Fragment() {
    private lateinit var binding : FragmentPeopleBinding
    private lateinit var adapter: PeopleAdapter
    private val model : MainViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPeopleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRcView()
        model.liveDataListPeople.observe(viewLifecycleOwner){ peopleList ->
            adapter.submitList(peopleList)
        }
    }

    private fun initRcView() = with(binding){
        rcViewPeople.layoutManager = LinearLayoutManager(activity)
        adapter = PeopleAdapter()
        rcViewPeople.adapter = adapter
    }

    companion object {
        @JvmStatic
        fun newInstance() = PeopleFragment()
    }
}