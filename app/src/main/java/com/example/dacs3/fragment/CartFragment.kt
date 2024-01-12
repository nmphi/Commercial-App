package com.example.dacs3.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dacs3.R
import com.example.dacs3.activity.CheckOutActivity
import com.example.dacs3.adapter.CartAdapter
import com.example.dacs3.helper.SharedPref
import com.example.dacs3.model.CheckOut
import com.example.dacs3.model.Product
import com.google.firebase.database.*


class CartFragment : Fragment() {
    private lateinit var dbRef: DatabaseReference
    lateinit var s: SharedPref


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_cart,container,false)
        init(view)
        s = SharedPref(requireActivity())
        btnBuy.setOnClickListener {
            val intent = Intent(requireActivity(), CheckOutActivity::class.java)
//            intent.putExtra("extra", "" + totalPrice)
            startActivity(intent)

        }
        return view

    }
    lateinit var rvProduct: RecyclerView
    lateinit var tvTotal: TextView
    lateinit var btnBuy: TextView


    private fun init(view: View) {
        rvProduct = view.findViewById(R.id.rv_product)
        tvTotal = view.findViewById(R.id.tv_total)
        btnBuy = view.findViewById(R.id.btn_bayar)

    }
    lateinit var adapter: CartAdapter
    var listProduct= ArrayList<Product>()
    private fun displayProduct() {
        dbRef = FirebaseDatabase.getInstance().getReference(s.getUser()!!.id)
        dbRef.addValueEventListener(object : ValueEventListener{


            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    listProduct.clear()
                    for (prSnap in snapshot.children){
                        val product = prSnap.getValue(Product::class.java)
                        listProduct.add(product!!)
                    }
                }
                countTotal()
                val layoutManager = LinearLayoutManager(activity)
                layoutManager.orientation = LinearLayoutManager.VERTICAL
                adapter = CartAdapter(requireActivity(), listProduct, object : CartAdapter.Listeners {
                    override fun onUpdate() {
                        countTotal()

                    }
                    override fun onDelete(position: Int) {
                        listProduct.removeAt(position)
                        countTotal()
                    }
                })
                rvProduct.adapter = adapter
                rvProduct.layoutManager = layoutManager
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
    var totalPrice: Double = 0.0
    private fun countTotal() {
//        dbRef = FirebaseDatabase.getInstance().getReference(s.getUser()!!.id)
//        dbRef.addValueEventListener(object : ValueEventListener{
//            override fun onDataChange(snapshot: DataSnapshot) {
//                if (snapshot.exists()){
//                    listProduct.clear()
//                    for (prSnap in snapshot.children){
//                        val product = prSnap.getValue(Product::class.java)
//                        listProduct.add(product!!)
//                    }
//                }
//
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//            }
//        })

        totalPrice = 0.0
        for (product in listProduct) {
            if (product.selected) {
                val price = product.price?.toDouble()
                totalPrice += (price?.times(product.qty!!)!!)
            }
        }

        tvTotal.text = totalPrice.toString()

    }



    override fun onResume() {
        displayProduct()
        countTotal()
        super.onResume()
    }

}




