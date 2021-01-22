package com.example.rhpp.ViewHolder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rhpp.R
import org.w3c.dom.Text

class PakanViewHolder(view: View) : RecyclerView.ViewHolder(view){
    var id: TextView
    var tgl: TextView
    var namaPakan : TextView
    var jumlah: TextView
    var total: TextView
    var edit : ImageView
    var delete : ImageView

    init {
        id = view.findViewById(R.id.tvInvoiceF)
        tgl = view.findViewById(R.id.tvDateF)
        namaPakan= view.findViewById(R.id.tvNameFeed)
        jumlah = view.findViewById(R.id.tvJumlahF)
        total = view.findViewById(R.id.tvTotalFe)
        edit = view.findViewById(R.id.imEditF)
        delete = view.findViewById(R.id.imDeleteF)

    }
}