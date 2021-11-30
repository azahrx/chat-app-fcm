package com.zahra.chatapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdapterChat() : RecyclerView.Adapter<AdapterChat.ViewHolder>() {

    var data = listOf<String>()
        set(value) {
            data = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.chat_view_holder, parent))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val d = data[position]
        holder.textView.text = d
    }

    override fun getItemCount(): Int = data.size

    open inner class ViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView = itemView.findViewById<TextView>(R.id.messageHolder)
    }

}