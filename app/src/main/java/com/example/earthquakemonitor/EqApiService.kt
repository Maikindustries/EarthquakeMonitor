package com.example.earthquakemonitor

import retrofit2.http.GET
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory


interface EqApiservice {
    @GET("all_hour.geojson")
    suspend fun getLastHourEarthquakes(): String //como lo utilizamos en una coroutina la hacemos suspend
}

private var retrofit = Retrofit.Builder()
    .baseUrl("https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/")
    .addConverterFactory(ScalarsConverterFactory.create())
    .build()

var service: EqApiservice = retrofit.create(EqApiservice::class.java)