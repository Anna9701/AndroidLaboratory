package com.anna.wyrwal.musicplaceslocator.MusicBrainz

import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface IMusicBrainzApiService {

    @GET("https://musicbrainz.org/ws/2/place/")
    fun searchMusicVenues(@Query("query") query: String,
                      @Query("fmt") format: String = "json"):
            Observable<PlaceQueryResponse>

    companion object {


        fun create(): IMusicBrainzApiService {

            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(
                    RxJava2CallAdapterFactory.create())
                .addConverterFactory(
                    GsonConverterFactory.create())
                .baseUrl("https://musicbrainz.org/ws/2/place/")
                .build()

            return retrofit.create(IMusicBrainzApiService::class.java)
        }
    }

}

//https://musicbrainz.org/ws/2/place/?query=AA&fmt=json