package ru.itis.hw5.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ru.itis.hw5.data.database.entities.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(user: User)

    // для проверки на существование
    @Query("SELECT * FROM users WHERE email = :email")
    suspend fun getUserByEmail(email: String): User?

    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: Int): User?

    // Для логина (проверяет email + пароль)
    @Query("SELECT * FROM users WHERE email = :email AND password_hash = :passwordHash")
    suspend fun authenticate(email: String, passwordHash: String): User?

    // Для регистрации (проверка что email свободен)
    @Query("SELECT COUNT(*) FROM users WHERE email = :email")
    suspend fun checkEmailExists(email: String): Int

    // Для редактирования профиля
    @Update
    suspend fun update(user: User)


    // 1. SOFT DELETE (пользователь нажал "Удалить аккаунт")
    @Query("UPDATE users SET is_deleted = 1, deleted_date = :deletionTime WHERE id = :id")
    suspend fun markAsDeleted(id: Int, deletionTime: Long = System.currentTimeMillis())

    // 2. ВОССТАНОВЛЕНИЕ (выбрал "Восстановить аккаунт")
    @Query("UPDATE users SET is_deleted = 0, deleted_date = NULL WHERE email = :email")
    suspend fun restoreUser(email: String)

    // 3. ПОЛУЧЕНИЕ УДАЛЕННОГО ПОЛЬЗОВАТЕЛЯ (для проверки при логине)
    @Query("SELECT * FROM users WHERE email = :email and is_deleted = 1")
    suspend fun getDeletedUserByEmail(email: String): User?

    // 4. HARD DELETE ОКОНЧАТЕЛЬНОЕ УДАЛЕНИЕ (выбрал "Удалить окончательно" ИЛИ прошло 7 дней)
    @Query("DELETE FROM users WHERE email = :email")
    suspend fun permanentlyDeleteUser(email: String)

    // 5. ОЧИСТКА СТАРЫХ УДАЛЕННЫХ (запускаем раз в день, удаляем тех, кому прошло 7+ дней)
    @Query("DELETE FROM users WHERE is_deleted = 1 AND deleted_date < :cutoffTime")
    suspend fun cleanupOldDeletedUsers(cutoffTime: Long)


    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<List<User>>
}