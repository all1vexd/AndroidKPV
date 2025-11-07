package ru.itis.hw_3.domain.model

object ValidationConstants {

    const val TITLE_EMPTY_ERROR = "Заголовок не может быть пустым"

    const val ID_NUMBER_ERROR = "Ошибка: ID должен быть числом"

    fun notificationNotFound(id: Int): String {
        return "Ошибка: уведомление с ID $id не найдено"
    }

}