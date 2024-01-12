package com.example.dacs3.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.dacs3.R
import com.example.dacs3.model.HistoryDetail

class HistoryDetailAdapter (var data: ArrayList<HistoryDetail>) : RecyclerView.Adapter<HistoryDetailAdapter.Holder>(){
    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNama = view.findViewById<TextView>(R.id.tv_nama)
        val tvHarga = view.findViewById<TextView>(R.id.tv_harga)
        val tvTotalHarga = view.findViewById<TextView>(R.id.tv_totalHarga)
        val tvJumlah = view.findViewById<TextView>(R.id.tv_jumlah)
        val layout = view.findViewById<CardView>(R.id.layout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_history_detail, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val a = data[position]

        val name = a.product.name
        val p = a.product
        holder.tvNama.text = name
        holder.tvHarga.text = p.price
        holder.tvTotalHarga.text = a.total.toString()
        holder.tvJumlah.text = a.qty.toString() + " Items"

        holder.layout.setOnClickListener {
//            listener.onClicked(a)
        }
    }


    override fun getItemCount(): Int {
        return data.size
    }

    interface Listeners {
        fun onClicked(data: HistoryDetail)
    }

}