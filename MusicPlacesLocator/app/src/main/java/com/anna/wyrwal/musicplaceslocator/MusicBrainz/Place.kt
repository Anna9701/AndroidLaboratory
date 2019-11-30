package com.anna.wyrwal.musicplaceslocator.MusicBrainz

data class Place(
    val address: String,
    val area: Area,
    val coordinates: Coordinates,
    val disambiguation: String,
    val id: String,
    val life-span: LifeSpanX,
    val name: String,
    val score: Int,
    val type: String,
    val type-id: String
)