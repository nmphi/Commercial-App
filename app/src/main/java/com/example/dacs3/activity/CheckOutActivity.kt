package com.example.dacs3.activity


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.dacs3.Retrofit.Api

import com.example.dacs3.databinding.ActivityCheckOutBinding

import com.example.dacs3.helper.SharedPref
import com.example.dacs3.model.CheckOut
import com.example.dacs3.model.Product

import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
data class ResponseM(val message: String)

class CheckOutActivity : AppCompatActivity() {
    private lateinit var dbRef: DatabaseReference
    private lateinit var binding: ActivityCheckOutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckOutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnRegister.setOnClickListener {
            if (binding.edName.text.toString().isEmpty()){
                binding.edName.error = "Không được để trống"
                return@setOnClickListener
            }
            if (binding.edStreetAddress.text.toString().isEmpty()){
                binding.edStreetAddress.error = "Không được để trống"
                return@setOnClickListener
            }
            if (binding.edPhone.text.toString().isEmpty()){
                binding.edPhone.error = "Không được để trống"
                return@setOnClickListener
            }
            checkout()
        }
    }
    private fun checkout() {
        val user = SharedPref(this).getUser()!!
        var listProduct = ArrayList<Product>()
        dbRef = FirebaseDatabase.getInstance().getReference(user.id)
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    listProduct.clear()
                    for (prSnap in snapshot.children){
                        val product = prSnap.getValue(Product::class.java)
                        listProduct.add(product!!)
                    }
                }
                var products = ArrayList<CheckOut.Item>()
                for (p in listProduct) {
                    if (p.selected) {
                        val product = CheckOut.Item()
                        product.product_id = "" + p.id
                        product.qty = "" + p.qty
                        product.amount = "" + p.price
                        val total = p.qty!!.toDouble()  * p.price!!.toDouble()
                        product.total = ""+total.toString()
                        products.add(product)

                    }
                }
                var checkout = CheckOut()
                checkout.country = "Viet Nam"
                checkout.user_id = "" + user.id
                checkout.name = ""+user.name
                checkout.company_name = ""+binding.edName.text
                checkout.street_address =""+binding.edStreetAddress.text
                checkout.town_city = "Đà Nẵng"
                checkout.postcode_zip = "100000"
                checkout.email = user.email
                checkout.phone = ""+ binding.edPhone.text
                checkout.products = products
                val json = Gson().toJson(checkout, CheckOut::class.java)
                Log.d("Respon:", "jseon:" + json)
                Api.retrofitService.checkout(checkout).enqueue(
                    object: Callback<String> {
                        override fun onResponse(call: Call<String>, response: Response<String>) {
                            val gson = Gson()
                            val message = gson.fromJson(response.body(), ResponseM::class.java)
                                Toast.makeText( this@CheckOutActivity, message.message, Toast.LENGTH_SHORT).show()


                        }

                        override fun onFailure(call: Call<String>, t: Throwable) {
                            Toast.makeText( this@CheckOutActivity, t.message.toString(), Toast.LENGTH_SHORT).show()
                        }
                    })

            }


            override fun onCancelled(error: DatabaseError) {
            }
        })

    }
}