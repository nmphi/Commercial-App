package com.example.dacs3.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView

import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager

import androidx.recyclerview.widget.RecyclerView
import com.example.dacs3.R
import com.example.dacs3.Retrofit.Api
import com.example.dacs3.adapter.HomeAdapter
import com.example.dacs3.model.Product
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment() {


    lateinit var adapter: HomeAdapter
    lateinit var rvProduct: RecyclerView
    private lateinit var searchView: SearchView
    private var listProduct: ArrayList<Product> = ArrayList()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view:View=inflater.inflate(R.layout.fragment_home,container,false)
        rvProduct = view.findViewById(R.id.rv_product)
        searchView = view.findViewById(R.id.search)
        Api.retrofitService.getProduct().enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d("RetrofitTest", t.toString())

            }
            override fun onResponse(call: Call<String>, response: Response<String>) {
                val res = response.body()
                val gson = Gson()
                val products = gson.fromJson(res, Array<Product>::class.java).toCollection(ArrayList())
                listProduct = products
                val manager = GridLayoutManager(context, 2)
                rvProduct.setLayoutManager(manager)
                rvProduct.adapter = HomeAdapter(requireActivity(), listProduct)

            }

        })
        val resultsList = arrayListOf<Product>()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                resultsList.clear()
                if (newText == "") {
                    resultsList.addAll(listProduct)
//                    Toast.makeText(activity, resultsList.toString(), Toast.LENGTH_SHORT).show()
                }
                else{
                    listProduct.forEach { item ->
                        if (item.name!!.contains(newText, ignoreCase = true)){
                            resultsList.add(item)
                        }
                    }
                }
                if (resultsList.size == 0){
                    Toast.makeText(activity, "kh có sản phẩm này", Toast.LENGTH_SHORT).show()

                }

                val manager = GridLayoutManager(context, 2)
                rvProduct.setLayoutManager(manager)
                rvProduct.adapter = HomeAdapter(requireActivity(), resultsList)
//                Toast.makeText(activity, resultsList.toString(), Toast.LENGTH_SHORT).show()

                return false

            }
        })

        return view
    }










}