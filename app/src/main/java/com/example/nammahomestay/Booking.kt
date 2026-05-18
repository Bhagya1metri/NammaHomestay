package com.example.nammahomestay

data class Booking(
    var bookingId: String = "",
    var homestayName: String = "",
    var price: String = "",
    var date: String = "",
    var status: String = "Confirmed"
)