package com.example.nammahomestay

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class FavoritesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var favoriteList: ArrayList<Homestay>
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        recyclerView = findViewById(R.id.favoritesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        favoriteList = arrayListOf()

        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            database = FirebaseDatabase.getInstance().getReference("favorites").child(userId)
            database.addValueEventListener(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        favoriteList.clear()
                        for (data in snapshot.children) {
                            val homestay = data.getValue(Homestay::class.java)
                            if (homestay != null) {
                                favoriteList.add(homestay)
                            }
                        }
                        recyclerView.adapter = HomestayAdapter(favoriteList)
                    }

                    override fun onCancelled(error: DatabaseError) {}
                },
            )
        }
    }
}