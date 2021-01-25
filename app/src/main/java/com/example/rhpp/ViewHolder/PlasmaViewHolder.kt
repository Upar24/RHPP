package com.example.rhpp.ViewHolder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rhpp.R

class PlasmaViewHolder(view: View) : RecyclerView.ViewHolder(view)  {
    var id: TextView
    var nama: TextView
    var ip: ImageView
    var rhpp: ImageView

    init {
        id = view.findViewById(R.id.tvPlasmaId)
        nama = view.findViewById(R.id.tvPlasmaname)
        ip = view.findViewById(R.id.imIP)
        rhpp = view.findViewById(R.id.imRHPP)

    }
}