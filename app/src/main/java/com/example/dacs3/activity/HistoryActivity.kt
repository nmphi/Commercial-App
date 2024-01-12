package com.example.dacs3.activity


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dacs3.Retrofit.Api
import com.example.dacs3.adapter.HistoryAdapter
import com.example.dacs3.databinding.ActivityHistoryBinding
import com.example.dacs3.helper.SharedPref
import com.example.dacs3.model.History

import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun getHistory() {
        val id = SharedPref(this).getUser()!!.id
        Api.retrofitService.getHistory(id).enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {

            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                val res = response.body()
                val gson = Gson()
                val data = gson.fromJson(res, Array<History>::class.java).toCollection(ArrayList())
                displayHistory(data)

            }
        })
    }
    fun displayHistory(data: ArrayList<History>) {
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        binding.rvHistory.adapter = HistoryAdapter(data, object : HistoryAdapter.Listeners {
            override fun onClicked(data:History ) {
                val json = Gson().toJson(data, History::class.java)
                val intent = Intent(this@HistoryActivity, HistoryDetailActivity::class.java)
                intent.putExtra("history", json)
                startActivity(intent)
            }
        })
        binding.rvHistory.layoutManager = layoutManager
    }
    override fun onResume() {
        getHistory()
        super.onResume()
    }
}