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
    var namaPakan: TextView
    var jenis: TextView
    var jumlah: TextView
    var total: TextView
    var edit : ImageView
    var delete : ImageView

    init {
        id = view.findViewById(R.id.etInvoice)
        tgl = view.findViewById(R.id.edDate)
        namaPakan = view.findViewById(R.id.etFeed)
        jenis = view.findViewById(R.id.spFeed)
        jumlah = view.findViewById(R.id.edTotalFeed)
        total = view.findViewById(R.id.etAmount)
        edit = view.findViewById(R.id.imEditF)
        delete = view.findViewById(R.id.imDeleteF)

    }
}