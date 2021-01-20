package com.example.rhpp.ViewHolder

import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rhpp.R


class OvkViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var id: TextView
    var tgl: TextView
    var namaOvk: TextView
    var harga: TextView
    var jumlah: TextView
    var total: TextView
    var edit: ImageView
    var delete : ImageView



    init {
        id = view.findViewById(R.id.tvInvoiceI)
        tgl = view.findViewById(R.id.tvTgl)
        namaOvk = view.findViewById(R.id.tvOvkI)
        harga = view.findViewById(R.id.tvHarga)
        jumlah = view.findViewById(R.id.tvQtyI)
        total = view.findViewById(R.id.tvTotalI)
        edit = view.findViewById(R.id.imEditI)
        delete = view.findViewById(R.id.imDeleteI)

    }
}