package ru.itis.hw5.constants

object StringResources {

    // Login
    const val EMAIL_REQUIRED = "Введите email"
    const val PASSWORD_REQUIRED = "Введите пароль"
    const val INVALID_EMAIL_FORMAT = "Неверный формат email"
    const val INVALID_CREDENTIALS = "Неверный email или пароль"
    const val USER_NOT_FOUND = "Пользователь с таким email не найден"
    const val ACCOUNT_DELETED_PERMANENTLY = "Срок восстановления (7 дней) истек. Аккаунт удален окончательно."
    const val ERROR = "Ошибка"

    // Registration
    const val ERROR_NAME_EMPTY = "Имя не может быть пустым"
    const val ERROR_NAME_TOO_SHORT = "Имя должно быть длиннее 5 символов"
    const val ERROR_EMAIL_EMPTY = "Email не может быть пустым"
    const val ERROR_PASSWORD_EMPTY = "Пароль не может быть пустым"
    const val ERROR_PASSWORD_TOO_SHORT = "Пароль должен быть длиннее 6 символов"
    const val ERROR_EMAIL_INVALID = "Неверный формат email"
    const val ERROR_PASSWORDS_NOT_MATCH = "Пароли не совпадают"

    // Addendum
    const val ERROR_TITLE_EMPTY = "Введите название фильма"
    const val ERROR_DIRECTOR_EMPTY = "Введите имя режиссера"
    const val ERROR_RATING_RANGE = "Рейтинг должен быть от 0.0 до 10.0"
    const val ERROR_SAVING_DB = "Ошибка при сохранении в базу данных"

    // MovieItem
    const val DATE_FORMAT_PATTERN = "dd.MM.yyyy HH:mm"

    // Restore
    const val RESTORE_ERROR = "Не удалось восстановить аккаунт"
    const val DELETE_ERROR = "Ошибка при удалении"

    // UserRepo
    const val PASSWORDS_DONT_MATCH = "Пароли не совпадают"
    const val EXISTING_USER_ERROR = "Пользователь с таким email уже существует"
    const val REGISTRATION_ERROR = "Ошибка регистрации: "

}