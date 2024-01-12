package com.example.dacs3.model

class History {
    var id = 0
    var user_id = ""
    var name = ""
    var phone = ""
    var street_address = ""
    var status = ""
    var updated_at = ""
    var created_at = ""
    val order_detail = ArrayList<HistoryDetail>()
}