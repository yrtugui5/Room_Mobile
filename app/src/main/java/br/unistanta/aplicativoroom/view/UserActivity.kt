package br.unistanta.aplicativoroom.view

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import br.unistanta.aplicativoroom.dao.UserDao
import br.unistanta.aplicativoroom.database.AppDatabase
import br.unistanta.aplicativoroom.databinding.ActivityUserBinding
import br.unistanta.aplicativoroom.model.User
import coil.load
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserBinding
    private lateinit var db:AppDatabase
    private lateinit var userDao: UserDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "database-unisanta-tp"
        ).fallbackToDestructiveMigration()
            .build()
        userDao = db.userDao()
        var loggedUser: User = intent.getSerializableExtra("loggedUser") as User
        setUserOnScreen(loggedUser)

        binding.btnUpdate.setOnClickListener {
            val newNome = binding.edtvNome.text.toString()
            val newEmail = binding.edtvEmail.text.trim().toString()
            val newUrl = binding.edtvUrl.text.trim().toString()
            val newLogin = binding.edtvLogin.text.trim().toString()
            val newPW = binding.edtvPw.text.trim().toString()
            val updatedUser = User(loggedUser.uid, newLogin, newPW, newNome, newEmail, newUrl)

            lifecycleScope.launch(Dispatchers.IO) {
                if (updatedUser.isAnyPropertyChange(loggedUser)) {
                    userDao.updateUser(updatedUser)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@UserActivity, "Usuário Alterado com Sucesso.", Toast.LENGTH_SHORT).show()
                        setUserOnScreen(updatedUser)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@UserActivity, "Nenhum dado do usuário foi alterado.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        binding.btnDelete.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                userDao.deleteUser(loggedUser)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@UserActivity, "Usuário deletado com sucesso.", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }




        binding.fabBack.setOnClickListener { finish() }

    }
    fun setUserOnScreen(user: User){
        binding.edtvNome.setText(user.name)
        binding.edtvEmail.setText(user.email)
        binding.edtvLogin.setText(user.login)
        binding.edtvPw.setText(user.senha)
        binding.edtvUrl.setText(user.profilePicURL)
        binding.userImg.load(user.profilePicURL)
    }
}