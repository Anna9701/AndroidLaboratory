package com.anna.wyrwal.musicplaceslocator.MusicBrainz

data class LifeSpan(
    val begin: String?,
    val ended: Any
) {
    fun getOpeningYear(): Int = (begin?.split('-')?.get(0) ?: "0").toInt()
}