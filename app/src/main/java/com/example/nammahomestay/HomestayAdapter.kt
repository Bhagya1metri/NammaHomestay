package com.example.nammahomestay

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class HomestayAdapter(private var homestayList: List<Homestay>) :
    RecyclerView.Adapter<HomestayAdapter.HomestayViewHolder>() {

    class HomestayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val image: ImageView = itemView.findViewById(R.id.homeImage)
        val name: TextView = itemView.findViewById(R.id.homeName)
        val price: TextView = itemView.findViewById(R.id.homePrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomestayViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_homestay, parent, false)

        return HomestayViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomestayViewHolder, position: Int) {

        val homestay = homestayList[position]

        if (homestay.imageUrl.isNotEmpty()) {
            Glide.with(holder.itemView.context)
                .load(homestay.imageUrl)
                .placeholder(R.drawable.homestay)
                .into(holder.image)
        } else {
            holder.image.setImageResource(homestay.image)
        }

        holder.name.text = homestay.name
        holder.price.text = homestay.price

        holder.itemView.setOnClickListener {

            val intent = Intent(holder.itemView.context, DetailsActivity::class.java)

            intent.putExtra("name", homestay.name)
            intent.putExtra("price", homestay.price)
            intent.putExtra("image", homestay.image)
            intent.putExtra("imageUrl", homestay.imageUrl)
            intent.putExtra("description", homestay.description)
            intent.putExtra("facilities", homestay.facilities)
            intent.putExtra("menu", homestay.menu)
            intent.putExtra("nearby", homestay.nearbyPlaces)
            intent.putExtra("secret", homestay.secretSpots)
            intent.putExtra("verified", homestay.isVerified)

            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {

        return homestayList.size
    }
}