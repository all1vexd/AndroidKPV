package ru.itis.hw6.data

import com.google.gson.annotations.SerializedName
import ru.itis.hw6.domain.SongDetails

data class SearchResponseByQuery(
    @SerializedName("meta")
    val meta: MetaModel,
    @SerializedName("response")
    val response: HitsResponse
)

data class MetaModel(
    @SerializedName("status")
    val status: Int
)

data class HitsResponse(
    @SerializedName("hits")
    val hits: List<HitsResponseModel>
)

data class HitsResponseModel(
    @SerializedName("result")
    val result: SongResponseModel
)

data class SongResponseModel(
    @SerializedName("id")
    val id: Long,
    @SerializedName("title")
    val title: String,
    @SerializedName("artist_names")
    val artistName: String,
    @SerializedName("header_image_thumbnail_url")
    val headerImageThumbnailUrl: String
)

data class SearchResponseById(
    @SerializedName("meta")
    val meta: MetaModel,
    @SerializedName("response")
    val response: SongDetailsResponseWrapper
)

data class SongDetailsResponseWrapper(
    @SerializedName("song")
    val song: SongDetailsResponseModel
)

data class SongDetailsResponseModel(
    @SerializedName("id")
    val id: Long,
    @SerializedName("title")
    val title: String,
    @SerializedName("artist_names")
    val artistName: String,
    @SerializedName("album")
    val album: Album,
    @SerializedName("release_date_for_display")
    val releaseDate: String,
    @SerializedName("header_image_thumbnail_url")
    val headerImageThumbnailUrl: String,
    @SerializedName("url")
    val lyricsPageUrl: String
)

data class Album(
    @SerializedName("name")
    val name: String
)