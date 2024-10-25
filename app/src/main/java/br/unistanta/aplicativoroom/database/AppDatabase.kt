package br.unistanta.aplicativoroom.database

import androidx.room.Database
import androidx.room.RoomDatabase
import br.unistanta.aplicativoroom.dao.UserDao
import br.unistanta.aplicativoroom.model.User

@Database(entities = [User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
