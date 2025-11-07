package ru.itis.hw_2.navigation

object NavParams {

    const val EMAIL_PARAM = "email"
    var FIRST_SCREEN = "first_screen"

    private var SECOND_SCREEN = "second_screen"
    var secondScreenWithEmail = "$SECOND_SCREEN/{$EMAIL_PARAM}"
    fun secondScreenBuilder(email: String): String {
        return "$SECOND_SCREEN/$email"
    }

    var THIRD_SCREEN = "third_screen"

    const val NEW_POST_TITLE_KEY = "new_title"
    const val NEW_POST_CONTENT_KEY = "new_content"
}
