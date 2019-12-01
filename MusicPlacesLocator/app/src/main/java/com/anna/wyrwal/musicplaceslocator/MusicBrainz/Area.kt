package com.anna.wyrwal.musicplaceslocator.MusicBrainz

import com.google.gson.annotations.SerializedName

data class Area(
    val id: String,
    @SerializedName(value = "life-span") val life_span: LifeSpan,
    val name: String,
    val sort_name: String,
    val type: String,
    @SerializedName(value = "type-id") val type_id: String
)