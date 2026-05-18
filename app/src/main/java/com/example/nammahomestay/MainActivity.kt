package com.example.nammahomestay

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var homestayList: ArrayList<Homestay>
    lateinit var adapter: HomestayAdapter

    lateinit var database: DatabaseReference

    val images = listOf(
        R.drawable.homestay,
        R.drawable.homestay1,
        R.drawable.homestay2,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)

        val logoutBtn = findViewById<Button>(R.id.logoutBtn)
        val aiBtn = findViewById<Button>(R.id.aiBtn)
        val favoritesBtn = findViewById<Button>(R.id.favoritesBtn)
        val historyBtn = findViewById<Button>(R.id.historyBtn)

        // LOGOUT BUTTON
        logoutBtn.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        // AI BUTTON
        aiBtn.setOnClickListener {
            startActivity(Intent(this, AiActivity::class.java))
        }

        // FAVORITES BUTTON
        favoritesBtn.setOnClickListener {
            startActivity(Intent(this, FavoritesActivity::class.java))
        }

        // HISTORY BUTTON
        historyBtn.setOnClickListener {
            startActivity(Intent(this, BookingHistoryActivity::class.java))
        }

        homestayList = arrayListOf()

        adapter = HomestayAdapter(homestayList)

        recyclerView.layoutManager = LinearLayoutManager(this)

        recyclerView.adapter = adapter

        database = FirebaseDatabase.getInstance().getReference("homestays")

        database.addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    homestayList.clear()

                    var index = 0

                    for (data in snapshot.children) {
                        val homestay = data.getValue(Homestay::class.java)

                        if (homestay != null) {
                            homestay.image = images[index % images.size]

                            homestayList.add(homestay)

                            index++
                        }
                    }

                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {

                }
            },
        )
    }
}