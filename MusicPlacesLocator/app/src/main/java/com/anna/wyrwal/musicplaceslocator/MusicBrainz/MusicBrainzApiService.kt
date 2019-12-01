package com.anna.wyrwal.musicplaceslocator.MusicBrainz

import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MusicBrainzApiService {
    private val apiService by lazy {
        IMusicBrainzApiService.create()
    }
    private var disposable: Disposable? = null

    fun searchPlace(place: String, successCallback: (result: PlaceQueryResponse) -> Unit,
                            errorCallback: (errorMsg: String?) -> Unit, limit: Int = 25, offset: Int = 0) {
        Log.d("Place", place)
        disposable =
            apiService.searchMusicVenues(place, limit, offset)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result -> successCallback(result) },
                    { error -> errorCallback(error.message) }
                )
    }
}