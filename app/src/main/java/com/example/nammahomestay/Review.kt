package com.example.nammahomestay

data class Review(
    var reviewId: String = "",
    var userName: String = "",
    var rating: Float = 0f,
    var comment: String = "",
    var date: String = ""
)