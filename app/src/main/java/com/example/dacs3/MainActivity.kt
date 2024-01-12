package com.example.dacs3

import android.content.Intent import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.dacs3.Retrofit.Api
import com.example.dacs3.activity.HomePageActivity
import com.example.dacs3.activity.RegisterActivity
import com.example.dacs3.databinding.ActivityMainBinding
import com.example.dacs3.helper.SharedPref
import com.example.dacs3.model.User
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


data class Message(val message: String,
                   val user: User)
class MainActivity : AppCompatActivity() {
    lateinit var s: SharedPref
    private lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnSignIn.setOnClickListener{

            login()
        }
        binding.txtSignUp.setOnClickListener {
            startActivity(Intent(this@MainActivity, RegisterActivity::class.java))
            finish()
        }
    }
    fun login(){
        s = SharedPref(this)
        val email = binding.txtEmailSignIn.text.toString()
        val password = binding.txtPasswordSignIn.text.toString()
        Api.retrofitService.login(email, password).enqueue(
            object: Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    val gson = Gson()
                    val message = gson.fromJson(response.body(), Message::class.java)
                    if (message.message.equals("Login Success")){
                        s.setUser(message.user)
                        Toast.makeText( this@MainActivity, message.message, Toast.LENGTH_SHORT).show();
                        val intent = Intent(this@MainActivity, HomePageActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()

                    }
                    else {
                        Toast.makeText( this@MainActivity, message.message, Toast.LENGTH_SHORT).show();


                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Toast.makeText( this@MainActivity, t.message.toString(), Toast.LENGTH_SHORT).show();
                }
            })



    }
}