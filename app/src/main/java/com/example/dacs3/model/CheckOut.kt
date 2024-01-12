package com.example.dacs3.model

class CheckOut {
    lateinit var country: String
    lateinit var user_id: String
    lateinit var name: String
    lateinit var company_name: String
    lateinit var street_address: String
    lateinit var town_city: String
    lateinit var postcode_zip: String
    lateinit var email: String
    lateinit var phone: String
    var products = ArrayList<Item>()
    class Item {
        lateinit var product_id: String
        lateinit var qty: String
        lateinit var amount: String
        lateinit var total: String
    }
}