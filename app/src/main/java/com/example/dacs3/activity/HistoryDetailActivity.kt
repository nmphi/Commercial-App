package com.example.dacs3.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dacs3.R
import com.example.dacs3.adapter.HistoryDetailAdapter
import com.example.dacs3.databinding.ActivityHistoryDetailBinding
import com.example.dacs3.model.History
import com.example.dacs3.model.HistoryDetail
import com.google.gson.Gson
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class HistoryDetailActivity : AppCompatActivity() {
    var history = History()
    private lateinit var binding:ActivityHistoryDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val json = intent.getStringExtra("history")
        history = Gson().fromJson(json, History::class.java)

        setData(history)
        displayProduct(history.order_detail)
    }
    fun setData(history: History) {
        binding.tvStatus.text = history.status
        binding.tvTgl.text =convertDay(history.created_at)

        binding.tvPenerima.text = history

            .name + " - " + history.phone
        binding.tvAlamat.text = history.street_address
        var total_item = 0
        var total_price =0.0
        for(dt in history.order_detail){
            total_item+=dt.qty
            total_price+=dt.qty!!.toDouble() * dt.product.price!!.toDouble()

        }
        binding.tvTotal.text = total_price.toString()
        binding.tvTotalBelanja.text = total_price.toString()


//        if (t.status != "WAITING") div_footer.visibility = View.GONE

        var color = getColor(R.color.menungu)
        if (history.status == "1"){
            color = getColor(R.color.selesai)
            binding.tvStatus.text = "DONE"
        }
        else {binding.tvStatus.text = "NOT DONE"
                color = getColor(R.color.batal)}

        binding.tvStatus.setTextColor(color)
    }
    fun displayProduct(historyDetails: ArrayList<HistoryDetail>) {
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvProduk.adapter = HistoryDetailAdapter(historyDetails)
        binding.rvProduk.layoutManager = layoutManager
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