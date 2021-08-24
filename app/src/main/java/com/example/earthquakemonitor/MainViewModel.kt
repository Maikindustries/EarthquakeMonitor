package com.example.earthquakemonitor

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import org.json.JSONObject

class MainViewModel: ViewModel() {
    /*private val job = Job() Esto se sustituye con viewModelScope en la linea 17 y se borra el metodo onCleared()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + job)
    viewModelScope solo funciona en viewModel Main, sin se quiere utilizar en otro lado se usan las lineas comentadas y se cambia la linea 17*/

    private var _EqList = MutableLiveData<MutableList<Earthquake>>()
    val eqList: LiveData<MutableList<Earthquake>>
        get() = _EqList

    init {//Nunca acatualizar elementos live data adentro de IO, solo se puede hacer en Dispatchers.Main
        viewModelScope.launch {
            _EqList.value = fetchEarthquakes()
        }
    }

    //Si utilizamos withContext afuera de una coroutina debemos hacer la función suspend
    //Ya que que withContext debe ser llamado en una coroutina
    //Y tiene que tener un return
    private suspend fun fetchEarthquakes(): MutableList<Earthquake> {
        return withContext(Dispatchers.IO) {
            val eqListString = service.getLastHourEarthquakes()
            val eqList =  parseEqResult(eqListString)

            eqList
            //el ultimo valor que hay en una coroutina es lo que devuelve, en este caso devuelve la lista eqList
        }
    }

    private fun parseEqResult(eqListString: String): MutableList<Earthquake> {
        val eqJsonObject = JSONObject(eqListString)
        val featuresJsonArray = eqJsonObject.getJSONArray("features")

        val eqList = mutableListOf<Earthquake>()

        for (i in 0 until featuresJsonArray.length()) {
            val featuresJsonObject = featuresJsonArray[i] as JSONObject
            val id = featuresJsonObject.getString("id")

            val propertiesJsonObject = featuresJsonObject.getJSONObject("properties")
            val magnitude = propertiesJsonObject.getDouble("mag")
            val place = propertiesJsonObject.getString("place")
            val time = propertiesJsonObject.getLong("time")

            val geometryJsonObject = featuresJsonObject.getJSONObject("geometry")
            val coordinatesJsonArray = geometryJsonObject.getJSONArray("coordinates")
            val longitude = coordinatesJsonArray.getDouble(0)
            val latitude = coordinatesJsonArray.getDouble(1)

            val earthquake = Earthquake(id, place, magnitude, time,longitude,latitude)
            eqList.add(earthquake)
        }
        return eqList
    }

    /*override fun onCleared() {
        super.onCleared()
        job.cancel() //Cuando la app cierre todos los procesos que se lleven en la corutina se van a cerrar
    }*/
}