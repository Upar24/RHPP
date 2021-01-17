//package com.example.rhpp
//
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView
//import com.example.rhpp.model.Harian
//import com.firebase.ui.firestore.FirestoreRecyclerAdapter
//import com.firebase.ui.firestore.FirestoreRecyclerOptions
//
//class AdapterDaily {
//    private inner class DailyFirestoreRecyclerAdapter internal constructor(options: FirestoreRecyclerOptions<Harian>) : FirestoreRecyclerAdapter<Harian, DailyViewHolder>(options) {
//        override fun onBindViewHolder(dailyViewHolder: DailyViewHolder, position: Int, harianModel: Harian) {
//            dailyViewHolder.setDaily(harianModel.id,
//                                        harianModel.afkir,
//                                        harianModel.mati,
//                                        harianModel.konsumsi)
//        }
//
//        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyViewHolder {
//            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_daily, parent, false)
//            return DailyViewHolder(view)
//        }
//    }
//}