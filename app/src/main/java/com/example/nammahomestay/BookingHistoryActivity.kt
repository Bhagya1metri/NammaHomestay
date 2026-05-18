package com.example.nammahomestay

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class BookingHistoryActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var bookingList: ArrayList<Booking>
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_history)

        recyclerView = findViewById(R.id.historyRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        bookingList = arrayListOf()

        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            database = FirebaseDatabase.getInstance().getReference("bookings").child(userId)
            database.addValueEventListener(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        bookingList.clear()
                        for (data in snapshot.children) {
                            val booking = data.getValue(Booking::class.java)
                            if (booking != null) {
                                bookingList.add(booking)
                            }
                        }
                        recyclerView.adapter = BookingAdapter(bookingList)
                    }

                    override fun onCancelled(error: DatabaseError) {}
                },
            )
        }
    }
}