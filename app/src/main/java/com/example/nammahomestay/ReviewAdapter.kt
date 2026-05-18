package com.example.nammahomestay

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ReviewAdapter(private val reviewList: List<Review>) :
    RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userName: TextView = itemView.findViewById(R.id.reviewUserName)
        val ratingBar: RatingBar = itemView.findViewById(R.id.reviewRating)
        val comment: TextView = itemView.findViewById(R.id.reviewComment)
        val date: TextView = itemView.findViewById(R.id.reviewDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_review, parent, false)
        return ReviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviewList[position]
        holder.userName.text = review.userName
        holder.ratingBar.rating = review.rating
        holder.comment.text = review.comment
        holder.date.text = review.date
    }

    override fun getItemCount(): Int = reviewList.size
}