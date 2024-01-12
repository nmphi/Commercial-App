package com.example.dacs3.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.dacs3.MainActivity
import com.example.dacs3.Message
import com.example.dacs3.Retrofit.Api
import com.example.dacs3.databinding.ActivityRegisterBinding
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnRegister.setOnClickListener{
            register()
        }
        binding.btnCancel.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun register() {
        val name = binding.edName.text.toString()
        val email = binding.edTaiKhoan.text.toString()
        val password = binding.edPassWord.text.toString()
        val password_confirm = binding.edPassWordConfirm.text.toString()

        if (name.isEmpty()){
            binding.edName.error = "Không được để trống"
            return
        }
        if (email.isEmpty()){
            binding.edTaiKhoan.error = "Không được để trống"
            return
        }
        if (password.isEmpty()){
            binding.edPassWord.error = "Không được để trống"
            return
        }
        if (isValidEmail(email) == false){
            binding.edTaiKhoan.error = "email không hơp lệ"
            return
        }
        if (password_confirm.equals(password) == false ){
            Toast.makeText( this, "Mật khẩu không trùng nhau", Toast.LENGTH_SHORT).show();
            return
        }
        Api.retrofitService.register(name, email, password).enqueue(
            object: Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    val gson = Gson()
                    val message = gson.fromJson(response.body(), Message::class.java)
                    if (message.message.equals("Register Success")){
                        Toast.makeText( this@RegisterActivity, message.message, Toast.LENGTH_SHORT).show();

                    }else if (message.message.equals("Email is used")){
                        binding.edTaiKhoan.error = "Email này đã tồn tại"

                    }
                    else {
                        Toast.makeText( this@RegisterActivity, message.message, Toast.LENGTH_SHORT).show();
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    //binding.textView.text = "Failure: " + t.message
                }
            })


    }
    fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
        return emailRegex.matches(email)
    }
}