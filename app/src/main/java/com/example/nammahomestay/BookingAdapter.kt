package com.example.nammahomestay

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BookingAdapter(private val bookingList: ArrayList<Booking>) :
    RecyclerView.Adapter<BookingAdapter.BookingViewHolder>() {

    class BookingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTv: TextView = itemView.findViewById(R.id.bookingName)
        val priceTv: TextView = itemView.findViewById(R.id.bookingPrice)
        val dateTv: TextView = itemView.findViewById(R.id.bookingDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_booking, parent, false)
        return BookingViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        val currentBooking = bookingList[position]
        val context = holder.itemView.context
        holder.nameTv.text = currentBooking.homestayName
        holder.priceTv.text = context.getString(R.string.price_format, currentBooking.price)
        holder.dateTv.text = context.getString(R.string.date_format, currentBooking.date)
    }

    override fun getItemCount(): Int = bookingList.size
}