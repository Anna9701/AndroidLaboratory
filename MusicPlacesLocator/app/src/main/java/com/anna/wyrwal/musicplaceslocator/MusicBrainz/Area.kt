package com.anna.wyrwal.musicplaceslocator.MusicBrainz

import com.beust.klaxon.Json

data class Area(
    val id: String,
    @Json(name = "life-span")val life_span: LifeSpan,
    val name: String,
    val sort_name: String,
    val type: String,
    @Json(name = "type-id")val type_id: String
)