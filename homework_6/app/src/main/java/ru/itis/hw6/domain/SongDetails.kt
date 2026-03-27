package ru.itis.hw6.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SongDetails(
    val id: Long,
    val title: String,
    val author: String,
    val album: String,
    val releaseDate: String,
    val imageUrl: String,
    val lyrics: String
) : Parcelable