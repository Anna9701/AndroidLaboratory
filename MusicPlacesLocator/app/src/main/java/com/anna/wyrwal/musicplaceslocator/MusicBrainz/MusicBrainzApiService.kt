package com.anna.wyrwal.musicplaceslocator.MusicBrainz

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MusicBrainzApiService {
    private val apiService by lazy {
        IMusicBrainzApiService.create()
    }
    private var disposable: Disposable? = null

    private val limit: Int = 25

    fun searchPlace(
        place: String, successCallback: (result: PlaceQueryResponse) -> Unit,
        errorCallback: (errorMsg: String?) -> Unit, offset: Int = 0
    ) {
        disposable =
            apiService.searchMusicVenues(place, limit, offset)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result -> handleResponse(successCallback, result, place, errorCallback) },
                    { error -> errorCallback(error.message) }
                )
    }

    private fun handleResponse(
        successCallback: (result: PlaceQueryResponse) -> Unit,
        result: PlaceQueryResponse,
        place: String,
        errorCallback: (errorMsg: String?) -> Unit
    ) {
        successCallback(result)
        if (result.count > result.offset * limit + result.places.size) {
            searchPlace(place, successCallback, errorCallback, result.offset + 1)
        }
    }
}