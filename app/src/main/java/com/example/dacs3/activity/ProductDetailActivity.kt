package com.example.dacs3.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.dacs3.R
import com.example.dacs3.databinding.ActivityProductDetailBinding
import com.example.dacs3.helper.SharedPref

import com.example.dacs3.model.Product
import com.google.firebase.database.*
import com.google.gson.Gson

class ProductDetailActivity : AppCompatActivity() {
    private lateinit var dbRef : DatabaseReference
    private lateinit var product: Product
    private lateinit var binding: ActivityProductDetailBinding
    private lateinit var s: SharedPref
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mainButton()
        getInfo()
    }
    fun mainButton(){
        s = SharedPref(this)
        dbRef = FirebaseDatabase.getInstance().getReference(s.getUser()!!.id)
        val data = intent.getStringExtra("extra")
        product= Gson().fromJson<Product>(data, Product::class.java)
        binding.btnCart.setOnClickListener{
            val itemRef = dbRef.child(product.id!!)
            itemRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val quantity = snapshot.child("qty").getValue(Int::class.java)
                        val newQuantity = quantity?.plus(1)
                        itemRef.child("qty").setValue(newQuantity)
                        Toast.makeText(this@ProductDetailActivity, "Thêm sản phẩm " + product.name +" vào giỏ hàng thành công", Toast.LENGTH_SHORT).show()
                    } else {
                        product.qty = 1
                        itemRef.setValue(product)
                        Toast.makeText(this@ProductDetailActivity, "Thêm sản phẩm " + product.name +" vào giỏ hàng thành công", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })

            }





        }


    private fun getInfo() {
        val data = intent.getStringExtra("extra")
        product= Gson().fromJson<Product>(data, Product::class.java)
        // set Value
        val price = product.price!!.toFloat()
        binding.tvName.text = product.name
        binding.tvPrice.text = price.toString()+"$"
        binding.tvDescription.text = product.description
        binding.tvQty.text = product.qty.toString()
        val image = "http://10.0.2.2:8000/front/images/product/"+product.path
        Glide.with(this)
            .load(image)
            .into(binding.image)



    }
}