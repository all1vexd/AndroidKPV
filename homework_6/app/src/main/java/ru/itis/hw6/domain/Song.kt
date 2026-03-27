package ru.itis.hw6.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Song(
    val id: Long,
    val title: String,
    val author: String,
    val thumbnailUrl: String
) : Parcelable