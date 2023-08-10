package com.example.starwarsapp1

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.starwarsapp1.adapters.DbModel
import com.example.starwarsapp1.adapters.FilmModel
import com.example.starwarsapp1.adapters.PeopleModel
import com.example.starwarsapp1.adapters.TransportModel

class MainViewModel : ViewModel() {
    val liveDataCurrent = MutableLiveData<PeopleModel>()
    val liveDataTransport = MutableLiveData<TransportModel>()
    val liveDataList = MutableLiveData<List<TransportModel>>()
    val liveDataListFilms = MutableLiveData<List<FilmModel>>()
    val liveDataListPeople = MutableLiveData<List<PeopleModel>>()
    val liveDataListFav = MutableLiveData<List<DbModel>>()
    fun setLiveDataListFav(dbList: List<DbModel>) {
        liveDataListFav.value = dbList
    }
}
