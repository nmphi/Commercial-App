package com.example.dacs3.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.request.RequestOptions
import com.example.dacs3.BuildConfig
import com.example.dacs3.R
import com.example.dacs3.activity.ProductDetailActivity
import com.example.dacs3.helper.SharedPref
import com.example.dacs3.model.Product
import com.google.gson.Gson
import java.lang.Math.round
import kotlin.math.roundToInt


class HomeAdapter(var activity: Activity, var data:ArrayList<Product>): RecyclerView.Adapter<HomeAdapter.Holder>() {



    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName = view.findViewById<TextView>(R.id.tv_name)
        val tvPrice = view.findViewById<TextView>(R.id.tv_price)
        val tvStock = view.findViewById<TextView>(R.id.tv_stock)
        val imgProduct = view.findViewById<ImageView>(R.id.img_product)
        val layout = view.findViewById<CardView>(R.id.layout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val price =  data[position].price!!.toFloat()

        holder.tvName.text=data[position].name
        holder.tvPrice.text=price.toString() + "$"
        holder.tvStock.text ="Stock: "+data[position].qty.toString()
        val image = "http://10.0.2.2:8000/front/images/product/" + data[position].path
        Glide.with(holder.imgProduct)
            .load(image)
            .error(R.drawable.logo)
            .into(holder.imgProduct)

        holder.layout.setOnClickListener {
            val activiti = Intent(activity, ProductDetailActivity::class.java)
            val str = Gson().toJson(data[position], Product::class.java)
            activiti.putExtra("extra", str)
            activity.startActivity(activiti)
        }

    }

    override fun getItemCount(): Int {
        return data.size
    }
}