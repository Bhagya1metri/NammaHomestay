package com.example.nammahomestay

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class DetailsActivity : AppCompatActivity() {

    private var homeName: String? = null
    private var homePrice: String? = null
    private lateinit var reviewList: ArrayList<Review>
    private lateinit var reviewAdapter: ReviewAdapter
    
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val image = findViewById<ImageView>(R.id.detailImage)
        val name = findViewById<TextView>(R.id.detailName)
        val price = findViewById<TextView>(R.id.detailPrice)
        val description = findViewById<TextView>(R.id.detailDescription)
        val facilities = findViewById<TextView>(R.id.detailFacilities)
        val menu = findViewById<TextView>(R.id.detailMenu)
        val favoriteCb = findViewById<CheckBox>(R.id.favoriteCb)
        
        val detailNearby = findViewById<TextView>(R.id.detailNearby)
        val detailSecret = findViewById<TextView>(R.id.detailSecret)
        val verifiedBadge = findViewById<LinearLayout>(R.id.verifiedBadge)

        val contactBtn = findViewById<Button>(R.id.contactBtn)
        val callBtn = findViewById<Button>(R.id.callBtn)
        val addToFavoritesBtn = findViewById<Button>(R.id.addToFavoritesBtn)

        val submitRating = findViewById<RatingBar>(R.id.submitRating)
        val reviewCommentEt = findViewById<EditText>(R.id.reviewCommentEt)
        val submitReviewBtn = findViewById<Button>(R.id.submitReviewBtn)
        val reviewsRecyclerView = findViewById<RecyclerView>(R.id.reviewsRecyclerView)
        
        val inquiryEt = findViewById<EditText>(R.id.inquiryEt)
        val sendInquiryBtn = findViewById<Button>(R.id.sendInquiryBtn)

        homeName = intent.getStringExtra("name")
        homePrice = intent.getStringExtra("price")
        val homeImage = intent.getIntExtra("image", 0)
        val homeImageUrl = intent.getStringExtra("imageUrl") ?: ""
        val homeDescription = intent.getStringExtra("description")
        val homeFacilities = intent.getStringExtra("facilities")
        val homeMenu = intent.getStringExtra("menu")
        
        val homeNearby = intent.getStringExtra("nearby") ?: ""
        val homeSecret = intent.getStringExtra("secret") ?: ""
        val isHomeVerified = intent.getBooleanExtra("verified", false)

        name.text = homeName
        price.text = homePrice
        
        if (homeImageUrl.isNotEmpty()) {
            Glide.with(this).load(homeImageUrl).placeholder(R.drawable.homestay).into(image)
        } else {
            image.setImageResource(homeImage)
        }

        description.text = homeDescription
        facilities.text = getString(R.string.facilities_format, homeFacilities)
        menu.text = getString(R.string.menu_format, homeMenu)
        
        detailNearby.text = getString(R.string.nearby_places, if (homeNearby.isBlank()) "Information coming soon" else homeNearby)
        detailSecret.text = getString(R.string.secret_spots, if (homeSecret.isBlank()) "Ask host for local hidden gems" else homeSecret)
        
        if (isHomeVerified) verifiedBadge.visibility = View.VISIBLE

        checkIfFavorite(favoriteCb)

        // Setup Reviews
        reviewList = arrayListOf()
        reviewAdapter = ReviewAdapter(reviewList)
        reviewsRecyclerView.layoutManager = LinearLayoutManager(this)
        reviewsRecyclerView.adapter = reviewAdapter
        fetchReviews()

        // Listeners
        favoriteCb.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) saveToFavorites() else removeFromFavorites()
        }

        contactBtn.setOnClickListener {
            val intent = Intent(this, PaymentActivity::class.java)
            intent.putExtra("homestayName", homeName)
            intent.putExtra("price", homePrice)
            startActivity(intent)
        }

        callBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = "tel:9876543210".toUri()
            startActivity(intent)
        }

        addToFavoritesBtn.setOnClickListener {
            saveToFavorites()
            favoriteCb.isChecked = true
        }

        submitReviewBtn.setOnClickListener {
            val rating = submitRating.rating
            val comment = reviewCommentEt.text.toString().trim()
            if (rating != 0f && comment.isNotEmpty()) {
                submitReview(rating, comment)
                submitRating.rating = 0f
                reviewCommentEt.text.clear()
            }
        }
        
        sendInquiryBtn.setOnClickListener {
            val inquiryText = inquiryEt.text.toString().trim()
            if (inquiryText.isNotEmpty()) {
                sendInquiryToFirestore(inquiryText)
                inquiryEt.text.clear()
            }
        }
    }

    private fun sendInquiryToFirestore(text: String) {
        val user = FirebaseAuth.getInstance().currentUser
        val email = user?.email ?: "anonymous@namma.com"
        val inquiry = hashMapOf(
            "homestayName" to homeName,
            "userEmail" to email,
            "message" to text,
            "timestamp" to FieldValue.serverTimestamp()
        )
        firestore.collection("inquiries").add(inquiry).addOnSuccessListener {
            Toast.makeText(this, "Inquiry sent!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchReviews() {
        val noReviewsTv = findViewById<TextView>(R.id.noReviewsTv)
        val database = FirebaseDatabase.getInstance().getReference("reviews").child(homeName ?: "unknown")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                reviewList.clear()
                for (data in snapshot.children) {
                    val review = data.getValue(Review::class.java)
                    if (review != null) reviewList.add(review)
                }
                noReviewsTv.visibility = if (reviewList.isEmpty()) View.VISIBLE else View.GONE
                reviewAdapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun submitReview(rating: Float, comment: String) {
        val user = FirebaseAuth.getInstance().currentUser
        val userName = user?.displayName ?: user?.email ?: "Anonymous User"
        val database = FirebaseDatabase.getInstance().getReference("reviews").child(homeName ?: "unknown")
        val reviewId = database.push().key ?: return
        val date = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        val review = Review(reviewId, userName, rating, comment, date)
        database.child(reviewId).setValue(review).addOnSuccessListener {
            Toast.makeText(this, "Review submitted!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkIfFavorite(favoriteCb: CheckBox) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val database = FirebaseDatabase.getInstance().getReference("favorites").child(userId)
        database.child(homeName ?: "unknown").get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) favoriteCb.isChecked = true
        }
    }

    private fun saveToFavorites() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val database = FirebaseDatabase.getInstance().getReference("favorites").child(userId)
        val homestay = Homestay(
            name = homeName ?: "",
            price = homePrice ?: "",
            description = intent.getStringExtra("description") ?: "",
            facilities = intent.getStringExtra("facilities") ?: "",
            menu = intent.getStringExtra("menu") ?: "",
            image = intent.getIntExtra("image", 0),
            imageUrl = intent.getStringExtra("imageUrl") ?: ""
        )
        database.child(homeName ?: "unknown").setValue(homestay)
        Toast.makeText(this, "Added to Favorites", Toast.LENGTH_SHORT).show()
    }

    private fun removeFromFavorites() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val database = FirebaseDatabase.getInstance().getReference("favorites").child(userId)
        database.child(homeName ?: "unknown").removeValue()
        Toast.makeText(this, "Removed from Favorites", Toast.LENGTH_SHORT).show()
    }
}