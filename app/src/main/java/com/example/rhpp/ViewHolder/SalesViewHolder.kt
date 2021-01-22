package com.example.rhpp.ViewHolder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rhpp.R

class SalesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var id: TextView
    var tgl: TextView
    var ekor: TextView
    var kg: TextView
    var total: TextView
    var edit: ImageView
    var delete : ImageView



    init {
        id = view.findViewById(R.id.tvInvoiceS)
        tgl = view.findViewById(R.id.tvTglS)
        ekor = view.findViewById(R.id.tvEkorS)
        kg = view.findViewById(R.id.tvKgS)
        total = view.findViewById(R.id.tvTotalS)
        edit = view.findViewById(R.id.imEditS)
        delete = view.findViewById(R.id.imDeleteS)

    }
}