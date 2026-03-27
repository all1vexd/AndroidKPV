package ru.itis.hw5.data.database.entities

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "users",
    indices = [Index(value = ["email"], unique = true)]
)
class User {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    var id: Int = 0

    @ColumnInfo(name = "user_name")
    @NonNull
    var userName: String = ""

    @NonNull
    var email: String = ""

    var salt: String = ""

    @ColumnInfo(name = "password_hash")
    var passwordHash: String = ""

    @ColumnInfo(name = "registration_date")
    var registrationDate : Long = 0

    @ColumnInfo(name = "is_deleted")
    var isDeleted: Boolean = false
    @ColumnInfo(name = "deleted_date")
    var deletedDate: Long? = null

    @ColumnInfo(name = "avatar_url")
    var avatarUrl: String? = null

    var bio: String? = null

}