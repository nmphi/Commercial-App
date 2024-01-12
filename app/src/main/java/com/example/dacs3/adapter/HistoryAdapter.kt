package com.example.dacs3.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.dacs3.R
import com.example.dacs3.model.History

import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class HistoryAdapter(var data: ArrayList<History>, var listener: Listeners) : RecyclerView.Adapter<HistoryAdapter.Holder>() {
    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNama = view.findViewById<TextView>(R.id.tv_nama)
        val tvHarga = view.findViewById<TextView>(R.id.tv_harga)
        val tvTangal = view.findViewById<TextView>(R.id.tv_tgl)
        val tvJumlah = view.findViewById<TextView>(R.id.tv_jumlah)
        val tvStatus = view.findViewById<TextView>(R.id.tv_status)
        val btnDetail = view.findViewById<TextView>(R.id.btn_detail)
        val layout = view.findViewById<CardView>(R.id.layout)
    }

    lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        context = parent.context
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return Holder(view)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val a = data[position]

        val name = a.order_detail[0].product.name
        holder.tvNama.text = name.toString()

        var total_item = 0
        var total_price =0.0
        for(dt in a.order_detail){
            total_item+=dt.qty
            total_price+=dt.qty!!.toDouble() * dt.product.price!!.toDouble()

        }
        holder.tvHarga.text = total_price.toString()+"$"
        holder.tvJumlah.text = total_item.toString()+" items"
        holder.tvStatus.text = a.status

        // 2021-04-30 18:30:20 //24
        // jam 1   k || 01  kk
        // 09:20:20 am 12/pm/am
        val formatBaru = "d MMM yyyy"
        holder.tvTangal.text = convertDay(a.created_at)
        var color = context.getColor(R.color.menungu)
        if (a.status == "1") {color = context.getColor(R.color.selesai)
            holder.tvStatus.text = "DONE"}
        else {color = context.getColor(R.color.batal)
            holder.tvStatus.text = "NOT DONE"
        }

        holder.tvStatus.setTextColor(color)

        holder.layout.setOnClickListener {
            listener.onClicked(a)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    interface Listeners {
        fun onClicked(data: History)
    }
    fun convertDay(input: String) :String{
        val inputFormat = DateTimeFormatter.ISO_DATE_TIME
        val outputFormat = DateTimeFormatter.ofPattern("d MMM yyyy")
        val date = LocalDateTime.parse(input, inputFormat)
        val zonedDateTime = ZonedDateTime.of(date, ZoneOffset.UTC)
        val formattedDate = outputFormat.format(zonedDateTime)
        return formattedDate
    }
}