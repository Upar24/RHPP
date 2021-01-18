package com.example.rhpp.ViewHolder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rhpp.R

class HarianViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var id: TextView
    var afkir: TextView
    var mati: TextView
    var konsumsi: TextView
    var edit: ImageView
    var delete: ImageView

    init {
        id = view.findViewById(R.id.tvId)
        afkir = view.findViewById(R.id.tvAfkir)
        mati = view.findViewById(R.id.tvMati)
        konsumsi = view.findViewById(R.id.tvKonsumsi)

        edit = view.findViewById(R.id.imEdit)
        delete = view.findViewById(R.id.imDelete)
    }
}