package com.example.nammahomestay

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class PaymentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        val homestayName = intent.getStringExtra("homestayName") ?: ""
        val price = intent.getStringExtra("price") ?: ""

        val payNowBtn = findViewById<Button>(R.id.payNowBtn)

        payNowBtn.setOnClickListener {
            saveBookingToFirebase(homestayName, price)
        }
    }

    private fun saveBookingToFirebase(name: String, price: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val database = FirebaseDatabase.getInstance().getReference("bookings").child(userId)
        
        val bookingId = database.push().key ?: return
        val date = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        
        val booking = Booking(bookingId, name, price, date, "Confirmed")
        
        database.child(bookingId).setValue(booking).addOnSuccessListener {
            Toast.makeText(this, "Booking Successful!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
    }
}