package com.anna.wyrwal.musicplaceslocator.MusicBrainz

import com.google.gson.annotations.SerializedName

data class Place(
    val address: String,
    val area: Area,
    val coordinates: Coordinates?,
    val disambiguation: String,
    val id: String,
    @SerializedName(value = "life-span") val life_span: LifeSpan,
    val name: String,
    val score: Int,
    val type: String,
    @SerializedName(value = "type-id") val type_id: String
)