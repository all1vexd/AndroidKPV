package ru.itis.hw5.data.database.entities

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import ru.itis.hw5.constants.StatusConstant

@Entity(
    tableName = "movies",
    indices = [
        Index(value = ["user_id", "title"], unique = true),
        Index(value = ["user_id", "rating"]),
        Index(value = ["user_id", "added_date"]),
        Index(value = ["user_id", "status"])
    ]
)
class Movie {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    var id: Int = 0

    @ColumnInfo(name = "user_id")
    var userId: Int = 0

    @NonNull
    var title: String = ""

    var director: String? = null

    var year: Int? = null

    var rating: Float? = null

    var genre: String? = null

    @NonNull
    var status: String = StatusConstant.STATUS_PLANNED

    @ColumnInfo(name = "poster_url")
    var posterUrl: String? = null

    var notes: String? = null

    @ColumnInfo(name = "added_date")
    var addedDate: Long = System.currentTimeMillis()

    @ColumnInfo(name = "is_favorite")
    var isFavorite: Boolean = false
}