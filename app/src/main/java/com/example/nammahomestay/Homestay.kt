package com.example.nammahomestay

data class Homestay(
    var image: Int = 0,
    var imageUrl: String = "",
    var name: String = "",
    var price: String = "",
    var description: String = "",
    var facilities: String = "",
    var menu: String = "",
    var isVerified: Boolean = false,
    var nearbyPlaces: String = "",
    var secretSpots: String = ""
)