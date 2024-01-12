package com.example.dacs3.model

data class Product (
   val description: String?="",
   val id:String? = "",
   val name:String?="",
   val path:String?="",
   val price: String?="",
   var qty:Int? = 0,
   var selected: Boolean = true
)

