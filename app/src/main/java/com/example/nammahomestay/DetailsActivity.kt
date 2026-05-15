package com.example.nammahomestay

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

class DetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val image = findViewById<ImageView>(R.id.detailImage)
        val name = findViewById<TextView>(R.id.detailName)
        val price = findViewById<TextView>(R.id.detailPrice)
        val description = findViewById<TextView>(R.id.detailDescription)
        val facilities = findViewById<TextView>(R.id.detailFacilities)
        val menu = findViewById<TextView>(R.id.detailMenu)

        val contactBtn = findViewById<Button>(R.id.contactBtn)
        val callBtn = findViewById<Button>(R.id.callBtn)

        val homeName = intent.getStringExtra("name")
        val homePrice = intent.getStringExtra("price")
        val homeImage = intent.getIntExtra("image", 0)
        val homeDescription = intent.getStringExtra("description")
        val homeFacilities = intent.getStringExtra("facilities")
        val homeMenu = intent.getStringExtra("menu")

        name.text = homeName
        price.text = homePrice
        image.setImageResource(homeImage)

        description.text = homeDescription
        facilities.text = "Facilities:\n$homeFacilities"
        menu.text = "Today's Menu:\n$homeMenu"

        // BOOK NOW BUTTON
        contactBtn.setOnClickListener {

            val bookingData = HashMap<String, String>()

            bookingData["hotelName"] = homeName ?: ""
            bookingData["price"] = homePrice ?: ""

            FirebaseDatabase.getInstance()
                .getReference("bookings")
                .push()
                .setValue(bookingData)
                .addOnSuccessListener {

                    Toast.makeText(
                        this,
                        "Booking Successful",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }

        // CALL HOST BUTTON
        callBtn.setOnClickListener {

            val intent = Intent(Intent.ACTION_DIAL)

            intent.data = Uri.parse("tel:9876543210")

            startActivity(intent)
        }
    }
}