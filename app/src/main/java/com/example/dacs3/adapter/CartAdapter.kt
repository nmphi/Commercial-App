package com.example.dacs3.adapter
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dacs3.R
import com.example.dacs3.helper.SharedPref
import com.example.dacs3.model.Product
import com.google.firebase.database.*


class CartAdapter(var activity: Activity,
                  var data: ArrayList<Product>,
                  var listener: Listeners) : RecyclerView.Adapter<CartAdapter.Holder>(){
    class Holder (view: View) : RecyclerView.ViewHolder(view) {
        val tvName = view.findViewById<TextView>(R.id.tv_name)
        val tvPrice = view.findViewById<TextView>(R.id.tv_price)
        val imgProduct = view.findViewById<ImageView>(R.id.img_product)
        val layout = view.findViewById<CardView>(R.id.layout)

        val btnPlus = view.findViewById<ImageView>(R.id.btn_plus)
        val btnMinus = view.findViewById<ImageView>(R.id.btn_minus)
        val btnDelete = view.findViewById<ImageView>(R.id.btn_delete)

        val checkBox = view.findViewById<CheckBox>(R.id.checkBox)
        val tvQty = view.findViewById<TextView>(R.id.tv_qty)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return Holder(view)
    }
    override fun onBindViewHolder(holder: Holder, position: Int) {
        var QTY = data[position].qty
        val product = data[position]

        var price = product.price!!.toFloat()*QTY!!

        holder.tvName.text = product.name
        holder.tvPrice.text = price.toString() + "$"
        holder.tvQty.text = QTY.toString()
        holder.checkBox.isChecked = product.selected
        holder.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            product.selected = isChecked
            update(product)
        }

        val image = "http://10.0.2.2:8000/front/images/product/" + data[position].path
        Glide.with(holder.imgProduct)
            .load(image)
            .error(R.drawable.logo)
            .into(holder.imgProduct)
        holder.btnPlus.setOnClickListener {
            QTY = product.qty?.plus(1)
            product.qty = QTY
            update(product)
            holder.tvQty.text = QTY.toString()
            holder.tvPrice.text = price.toString()
        }
        holder.btnMinus.setOnClickListener {
            if (QTY!! <= 1) return@setOnClickListener
            QTY = product.qty?.minus(1)
            product.qty = QTY
            update(product)
            holder.tvQty.text = QTY.toString()
            holder.tvPrice.text = price.toString()
        }
        holder.btnDelete.setOnClickListener {
            delete(product)
            listener.onDelete(position)
        }

    }
    override fun getItemCount(): Int {
        return data.size
    }
    interface Listeners {
        fun onUpdate()
        fun onDelete(position: Int)


    }
    private lateinit var s: SharedPref
    private fun update(data: Product) {
        s = SharedPref(activity)
        val dbRef : DatabaseReference = FirebaseDatabase.getInstance().getReference(s.getUser()!!.id)
        val itemRef = dbRef.child(data.id!!)
        itemRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
               itemRef.setValue(data)
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
        listener.onUpdate()

    }
    private fun delete(data: Product){
        s = SharedPref(activity)
        val dbRef = FirebaseDatabase.getInstance().getReference(s.getUser()!!.id).child(data.id!!)
        val mTask = dbRef.removeValue()
        mTask.addOnSuccessListener {
            Toast.makeText(activity, "đã xóa thành công", Toast.LENGTH_SHORT).show()

            }
            .addOnFailureListener {
                Toast.makeText(activity, "xóa thất bại", Toast.LENGTH_SHORT).show()
            }
    }

}
