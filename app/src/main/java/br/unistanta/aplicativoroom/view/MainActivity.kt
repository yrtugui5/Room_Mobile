package br.unistanta.aplicativoroom.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import br.unistanta.aplicativoroom.dao.UserDao
import br.unistanta.aplicativoroom.database.AppDatabase
import br.unistanta.aplicativoroom.databinding.ActivityMainBinding
import br.unistanta.aplicativoroom.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var db:AppDatabase
    private lateinit var userDao: UserDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "database-unisanta-tp"
        ).fallbackToDestructiveMigration()
            .build()
        userDao = db.userDao()

        binding.btnCadastrar.setOnClickListener{
            val login = binding.edtLogin.text.toString()
            val senha = binding.edtPw.text.toString()
            val name = binding.edtNome.text.toString()
            val email = binding.edtEmail.text.toString()
            val URL = binding.edtUrl.text.toString()
            val user = User(0,login,senha,name,email,URL)

            lifecycleScope.launch(Dispatchers.IO) {
                if(user.isAnyFieldNull()){
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@MainActivity, "Há campos vazios a serem preenchidos.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    userDao.insertAll(user)
                    Log.i("INSERT", "Usuário inserido: $name $email")
                    withContext(Dispatchers.Main) {
                        clearFields()
                        Toast.makeText(this@MainActivity, "Usuario cadastrado com sucesso.", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }
        binding.btnEntrar.setOnClickListener {
            val login = binding.edtLogin.text.toString()
            val senha = binding.edtPw.text.toString()
            lifecycleScope.launch(Dispatchers.IO) {
                val userLogged = userDao.getUserByLoginAndSenha(login,senha)
                withContext(Dispatchers.Main) {
                    if(userLogged != null) {
                        Log.d("LogIn", "${userLogged.uid} - ${userLogged.name} - ${userLogged.email}")
                        val intentUser = Intent(this@MainActivity, UserActivity::class.java)
                        intentUser.putExtra("loggedUser", userLogged)
                        startActivity(intentUser)
                        clearFields()

                    }else{
                        Toast.makeText(this@MainActivity, "Usuario não encontrado.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        binding.fabDebug.setOnClickListener {

            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    val listaUsers: List<User> = userDao.getAll()
                    withContext(Dispatchers.Main) {
                        listaUsers.forEach {
                            Log.i("BD", "User: ${it.login} - ${it.senha} - ${it.name} - ${it.email}")
                        }
                    }
                }catch (e: Exception) {
                    Log.e("BD", "Erro ao obter os usuários: ${e.message}")
                }
            }
        }
    }
    private fun clearFields(){
        binding.edtLogin.text.clear()
        binding.edtPw.text.clear()
        binding.edtNome.text.clear()
        binding.edtEmail.text.clear()
        binding.edtUrl.text.clear()
    }
}