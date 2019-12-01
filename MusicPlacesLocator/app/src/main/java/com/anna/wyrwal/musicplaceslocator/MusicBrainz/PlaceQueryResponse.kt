package com.anna.wyrwal.musicplaceslocator.MusicBrainz

data class PlaceQueryResponse(
    val count: Int,
    val created: String,
    val offset: Int,
    val places: List<Place>
)


