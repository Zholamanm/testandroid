package com.example.starwarsapp1.fragment

import android.content.pm.LauncherActivityInfo
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.activityViewModels
import com.example.starwarsapp1.MainViewModel
import com.example.starwarsapp1.R
import com.example.starwarsapp1.adapters.DbModel
import com.example.starwarsapp1.adapters.VpAdapter
import com.example.starwarsapp1.databinding.FragmentDbBinding
import com.example.starwarsapp1.db.MyDbManager
import com.google.android.material.tabs.TabLayoutMediator

class DbFragment : Fragment() {
    private lateinit var binding: FragmentDbBinding
    private val model: MainViewModel by activityViewModels()
    private lateinit var myDbManager : MyDbManager
    private var fList = listOf(
        FavFragment.newInstance()
    )
    private var tList = listOf(
        "Favourite"
    )
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDbBinding.inflate(inflater, container, false)
        myDbManager = MyDbManager(requireContext())
        myDbManager.openDb()
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        getDataFormDb()
    }

    private fun init() = with(binding){
        val currentActivity = activity
        if (currentActivity != null) {
            val adapter = VpAdapter(currentActivity, fList)
            vp2.adapter = adapter
            TabLayoutMediator(tb, vp2) { tab, pos -> tab.text = tList[pos] }.attach()
        }
    }

    private fun getDataFormDb() {
        val dbList = myDbManager.getFromDb()
        model.setLiveDataListFav(dbList)
    }
    companion object {
        @JvmStatic
        fun newInstance() = DbFragment()
    }
}
