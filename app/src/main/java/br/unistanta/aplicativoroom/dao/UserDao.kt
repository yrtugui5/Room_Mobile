package br.unistanta.aplicativoroom.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import br.unistanta.aplicativoroom.model.User

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): List<User>

    @Query("SELECT * FROM user WHERE login = :login AND senha = :senha")
    fun getUserByLoginAndSenha(login: String, senha: String): User?

    @Insert
    suspend fun insertAll(vararg users: User)

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)
}
