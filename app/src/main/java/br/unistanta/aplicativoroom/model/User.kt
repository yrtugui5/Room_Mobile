package br.unistanta.aplicativoroom.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class User(
    @PrimaryKey(autoGenerate = true) val uid:Int,
    @ColumnInfo(name = "login") val login: String?,
    @ColumnInfo(name = "senha") val senha: String?,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "email") val email: String?,
    @ColumnInfo(name = "profile_pic") val profilePicURL: String?
):Serializable{
    fun isAnyFieldNull(): Boolean {
        return login == "" ||
                senha == "" ||
                name == "" ||
                email == "" ||
                profilePicURL == ""
    }
    fun isAnyPropertyChange(original: User): Boolean {
        return login != original.login ||
                senha != original.senha ||
                name != original.name ||
                email != original.email ||
                profilePicURL != original.profilePicURL
    }
}

