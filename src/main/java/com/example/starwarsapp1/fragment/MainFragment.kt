package com.example.starwarsapp1.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ImageButton
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.starwarsapp1.MainViewModel
import com.example.starwarsapp1.R
import com.example.starwarsapp1.adapters.DbModel
import com.example.starwarsapp1.adapters.FilmModel
import com.example.starwarsapp1.adapters.PeopleModel
import com.example.starwarsapp1.adapters.TransportModel
import com.example.starwarsapp1.adapters.VpAdapter
import com.example.starwarsapp1.databinding.FragmentMainBinding
import com.example.starwarsapp1.db.MyDbManager
import com.google.android.material.tabs.TabLayoutMediator
import org.json.JSONObject

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private val model: MainViewModel by activityViewModels()
    private var currentRequest: JsonObjectRequest? = null
    private var checkPeople : Boolean = true
    private var checkTransport : Boolean = true
    private var checkFilm : Boolean = true
    private var fList = listOf(
        PeopleFragment.newInstance(),
        TransportFragment.newInstance(),
        FilmsFragment.newInstance()
    )
    private var tList = listOf(
        "People",
        "Transport",
        "Films"
    )
    private lateinit var name: String
    private lateinit var gender: String
    private lateinit var count: String
    private lateinit var isFav: String
    private lateinit var myDbManager: MyDbManager
    private var myDataItemList: List<DbModel> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        myDbManager = MyDbManager(requireContext())
        myDbManager.openDb() // Open the database here
        init()
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tvName: TextView = view.findViewById(R.id.tvPerson)
        val tvGender: TextView = view.findViewById(R.id.tvGender)
        val tvCount: TextView = view.findViewById(R.id.tvCount)
        val ibFav: ImageButton = view.findViewById(R.id.ibFav)
        binding.bSearch.setOnClickListener {
            name = tvName.text.toString()
            gender = tvGender.text.toString()
            count = tvCount.text.toString()
            isFav = if (ibFav.isSelected) "1" else "0"
            checkPeople = false
            checkTransport = false
            checkFilm = false
            searchForData(1)
            myDataItemList = myDbManager.fromDb
            displayDataFromDb()
        }
        ibFav.setOnClickListener {
            isFav = if (ibFav.isSelected) "1" else "0"
            if (isFav == "1") {
                binding.ibFav.setImageResource(R.drawable.ic_action_name)
            } else {
                binding.ibFav.setImageResource(R.drawable.ic_action_name_1)
            }
            myDbManager.insertToDb(name, gender, count, isFav)
            val nameFromApi = tvName.text.toString()
            val isFavFromDb = myDbManager.getIsFavFromDb(nameFromApi)
            Log.d("FAV_DB", "isFav from DB: " + isFavFromDb + nameFromApi + isFav)
            ibFav.isSelected = !ibFav.isSelected
        }
    }

    private fun displayDataFromDb() {
        val nameFromApi = view?.findViewById<TextView>(R.id.tvPerson)?.text.toString()
        val isFavFromDb = myDbManager.getIsFavFromDb(nameFromApi)
        Log.d("FAV_DB", "isFav from DB: " + isFavFromDb)

        if (isFavFromDb == "1") {
            binding.ibFav.setImageResource(R.drawable.ic_action_name)
        } else {
            binding.ibFav.setImageResource(R.drawable.ic_action_name_1)
        }
    }

    private fun init() = with(binding){
        val currentActivity = activity
        if (currentActivity != null) {
            val adapter = VpAdapter(currentActivity, fList)
            vp.adapter = adapter
            TabLayoutMediator(tabLayout, vp) { tab, pos -> tab.text = tList[pos] }.attach()
        }
    }

    private fun updateTabVisibility() {
        tList = if(checkPeople) {
            fList = listOf(
                TransportFragment.newInstance(),
                FilmsFragment.newInstance()
            )
            listOf("Transport", "Films")
        } else if(checkTransport) {
            fList = listOf(
                PeopleFragment.newInstance(),
                FilmsFragment.newInstance()
            )
            listOf("People", "Films")
        } else if(checkFilm) {
            fList = listOf(
                PeopleFragment.newInstance(),
                TransportFragment.newInstance()
            )
            listOf("People", "Transport")
        } else {
            fList = listOf(
                PeopleFragment.newInstance()
            )
            listOf("People")
        }
        init()
    }


    private fun searchForData(number: Int) {
        val nameToSearch = binding.txInput.text?.toString()?.trim() ?: ""
        if (nameToSearch.isNotEmpty()) {
            val url = "https://swapi.dev/api/people/$number/"
            currentRequest?.cancel()
            searchForDataInJsonObject(url, nameToSearch, number)
        }
    }

    private fun searchForTransport(number: Int) {
        val transportToSearch = binding.txInput.text?.toString()?.trim() ?: ""
        if (transportToSearch.isNotEmpty()) {
            val url = "https://swapi.dev/api/starships/$number/"
            currentRequest?.cancel()
            searchForTransportInJsonObject(url, transportToSearch, number)
        }
    }

    private fun searchForFilm(number: Int) {
        val filmToSearch = binding.txInput.text?.toString()?.trim() ?: ""
        if (filmToSearch.isNotEmpty()) {
            val url = "https://swapi.dev/api/films/$number/"
            currentRequest?.cancel()
            searchForFilmInJsonObject(url, filmToSearch, number)
        }
    }


    private fun searchForDataInJsonObject(url: String, dataToSearch: String, number : Int) {
        if (!isAdded) {
            return // Fragment is not attached to a context, return early
        }
        val queue = Volley.newRequestQueue(requireContext())
        val request = JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            { response ->
                val nextNumber = number + 1
                searchForData(nextNumber)
                val name = response.optString("name", "")
                if (name.equals(dataToSearch, ignoreCase = true)) {
                    val gender = response.optString("gender", "")
                    val starshipsCount = response.getJSONArray("starships").length()
                    checkPeople = true
                    updateUI(name, gender, starshipsCount.toString())
                    parsePeopleData(response)
                } else {
                    searchForData(number + 1)
                }
            },
            {
                if (number <= 82){
                    searchForData(number + 1)
                }
                else {
                    checkPeople = false
                    searchForTransport(1)
                }
            }
        )
        currentRequest = request
        queue.add(request)
    }
    private fun searchForTransportInJsonObject(url: String, dataToSearch: String, number : Int) {
        if (!isAdded) {
            return // Fragment is not attached to a context, return early
        }
        val queue = Volley.newRequestQueue(requireContext())
        val request = JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            { response ->
                val transport = response.optString("name", "")
                if (transport.equals(dataToSearch, ignoreCase = true)) {
                    val model = response.optString("model", "")
                    val manufacturer = response.optString("manufacturer", "")
                    checkTransport = true
                    updateUI(transport, model, manufacturer)
                    parseTransportDataForTransport(response)
                } else {
                    searchForTransport(number + 1)
                }
            },
            {
                if (number <= 36){
                    searchForTransport(number + 1)
                }
                else {
                    checkTransport = false
                    searchForFilm(1)
                }
            }
        )
        currentRequest = request
        queue.add(request)
    }
    private fun searchForFilmInJsonObject(url: String, dataToSearch: String, number : Int) {
        if (!isAdded) {
            return // Fragment is not attached to a context, return early
        }
        val queue = Volley.newRequestQueue(requireContext())
        val request = JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            { response ->
                val film = response.optString("title", "")
                if (film.equals(dataToSearch, ignoreCase = true)) {
                    val director = response.optString("director", "")
                    val producer = response.optString("producer", "")
                    checkFilm = true
                    updateUI(film, director, producer)
                    parseFilmDataForFilm(response)
                } else {
                    val nextNumber = number + 1
                    searchForFilm(nextNumber)
                }
            },
            { error ->
                if (number <= 6){
                    val nextNumber = number + 1
                    searchForFilm(nextNumber)
                }
                else {
                    showError3("Error: ${error.message}")
                }
            }
        )
        currentRequest = request
        queue.add(request)
    }

    private fun showError3(message: String) {
        Log.d("MyLog", message)
    }

    private fun updateUI(name: String, gender: String, starshipsCount: String) {
        binding.tvPerson.text = name
        binding.tvGender.text = gender
        binding.tvCount.text = starshipsCount
    }

    private fun parsePeopleData(result: JSONObject) {
        parsePeopleTransport(result)
        parsePeopleFilm(result)
    }

    private fun requestTransportData(url : String): LiveData<TransportModel> {
        val queue = Volley.newRequestQueue(context)
        val transportDataLiveData = MutableLiveData<TransportModel>()
        val request = StringRequest(
            Request.Method.GET,
            url,
            { response ->
                val transportData = parseTransportData(response)
                transportDataLiveData.value = transportData
            },
            { error ->
                Log.d("MyLog", "Error: $error")
            }
        )
        queue.add(request)
        return transportDataLiveData
    }

    private fun requestFilmData(url : String): LiveData<FilmModel> {
        val queue = Volley.newRequestQueue(context)
        val filmDataLiveData = MutableLiveData<FilmModel>()
        val request = StringRequest(
            Request.Method.GET,
            url,
            { response ->
                val filmData = parseFilmData(response)
                filmDataLiveData.value = filmData
            },
            { error ->
                Log.d("MyLog", "Error: $error")
            }
        )
        queue.add(request)
        return filmDataLiveData
    }

    private fun requestPeopleData(url : String): LiveData<PeopleModel> {
        val queue = Volley.newRequestQueue(context)
        val peopleDataLiveData = MutableLiveData<PeopleModel>()
        val request = StringRequest(
            Request.Method.GET,
            url,
            { response ->
                val peopleData = parsePeopleDataForTransport(response)
                peopleDataLiveData.value = peopleData
            },
            { error ->
                Log.d("MyLog", "Error: $error")
            }
        )
        queue.add(request)
        return peopleDataLiveData
    }

    private fun parseTransportData(result : String): TransportModel {
        val mainObject = JSONObject(result)
        return parseTransport(mainObject)
    }

    private fun parseTransportDataForTransport(result : JSONObject) {
        parseTransportUser(result)
        parseTransportFilm(result)
    }

    private fun parseFilmDataForFilm(result : JSONObject) {
        parseFilmUser(result)
        parseFilmTransport(result)
    }

    private fun parsePeopleDataForTransport(result: String): PeopleModel {
        val mainObject = JSONObject(result)
        return parsePeople(mainObject)
    }

    private fun parseFilmData(result : String): FilmModel {
        val mainObject = JSONObject(result)
        return parseFilm(mainObject)
    }

    private fun parsePeopleTransport(mainObject: JSONObject) {
        val transportArray = mainObject.getJSONArray("starships")
        val transportList = ArrayList<TransportModel>()
        var observationCount = 0
        for (i in 0 until transportArray.length()) {
            val url = transportArray.getString(i)
            requestTransportData(url).observe(viewLifecycleOwner) { transportData ->
                transportList.add(transportData)
                observationCount++
                if (observationCount == transportArray.length()) {
                    model.liveDataList.value = transportList
                }
            }
        }
    }

    private fun parseFilmTransport(mainObject: JSONObject) {
        val transportArray = mainObject.getJSONArray("starships")
        val transportList = ArrayList<TransportModel>()
        var observationCount = 0
        for (i in 0 until transportArray.length()) {
            val url = transportArray.getString(i)
            requestTransportData(url).observe(viewLifecycleOwner) { transportData ->
                transportList.add(transportData)
                observationCount++
                if (observationCount == transportArray.length()) {
                    model.liveDataList.value = transportList
                }
            }
        }
    }

    private fun parsePeopleFilm(mainObject: JSONObject) {
        val filmArray = mainObject.getJSONArray("films")
        val filmList = ArrayList<FilmModel>()
        var observationCount = 0
        for (i in 0 until filmArray.length()) {
            val url = filmArray.getString(i)
            requestFilmData(url).observe(viewLifecycleOwner) { filmData ->
                filmList.add(filmData)
                observationCount++
                if (observationCount == filmArray.length()) {
                    model.liveDataListFilms.value = filmList
                }
            }
        }
    }

    private fun parseTransportFilm(mainObject: JSONObject) {
        val filmArray = mainObject.getJSONArray("films")
        val filmList = ArrayList<FilmModel>()
        var observationCount = 0
        for (i in 0 until filmArray.length()) {
            val url = filmArray.getString(i)
            requestFilmData(url).observe(viewLifecycleOwner) { filmData ->
                filmList.add(filmData)
                observationCount++
                if (observationCount == filmArray.length()) {
                    model.liveDataListFilms.value = filmList
                }
            }
        }
    }

    private fun parseTransportUser(mainObject: JSONObject) {
        val userArray = mainObject.getJSONArray("pilots")
        val userList = ArrayList<PeopleModel>()
        var observationCount = 0
        for(i in 0 until userArray.length()) {
            val url = userArray.getString(i)
            requestPeopleData(url).observe(viewLifecycleOwner) { peopleData ->
                userList.add(peopleData)
                observationCount++
                if (observationCount == userArray.length()) {
                    model.liveDataListPeople.value = userList
                }
            }
        }
    }

    private fun parseFilmUser(mainObject: JSONObject) {
        val userArray = mainObject.getJSONArray("characters")
        val userList = ArrayList<PeopleModel>()
        var observationCount = 0
        for(i in 0 until userArray.length()) {
            val url = userArray.getString(i)
            requestPeopleData(url).observe(viewLifecycleOwner) { peopleData ->
                userList.add(peopleData)
                observationCount++
                if (observationCount == userArray.length()) {
                    model.liveDataListPeople.value = userList
                }
            }
        }
    }

    private fun parseTransport(mainObject: JSONObject): TransportModel {
        val name = mainObject.getString("name")
        val model = mainObject.optString("model", "")
        val manufacturer = mainObject.getString("manufacturer")
        updateTabVisibility()
        return TransportModel(name, model, manufacturer)
    }

    private fun parseFilm(mainObject: JSONObject): FilmModel {
        val title = mainObject.getString("title")
        val director = mainObject.optString("director", "")
        val producer = mainObject.getString("producer")
        updateTabVisibility()
        return FilmModel(title, director, producer)
    }
    private fun parsePeople(mainObject: JSONObject): PeopleModel {
        val name = mainObject.getString("name")
        val gender = mainObject.optString("gender", "")
        val starshipsCount = mainObject.getJSONArray("starships").length()
        updateTabVisibility()
        return PeopleModel(name, gender, starshipsCount)
    }

    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }
}
