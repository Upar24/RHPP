package com.example.rhpp

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rhpp.databinding.FragmentDailyBinding
import com.example.rhpp.model.Harian
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.util.*

class Daily: Fragment(R.layout.fragment_daily) {
    private var _binding : FragmentDailyBinding? = null
    private val binding get() = _binding!!
    private val args : DailyArgs by navArgs()
    private val viewModel :  PlasmaViewModel by viewModels()
    private val db = FirebaseFirestore.getInstance()
    private var idDoc = ""
    private var adapter: DailyFirestoreRecyclerAdapter? = null


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentDailyBinding.inflate(inflater,container,false)
        binding.viewmodel = viewModel
        val view = binding.root
        return view}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.edDate.transformIntoDatePicker(requireContext(),"MM-dd-yyyy",Date())
        viewModel.username = args.username


        db.collection("users").document(args.username).collection("doc")
                .whereEqualTo("siap", true)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        Log.d(ContentValues.TAG, "${document.id} => ${document.data}")
                        idDoc = document.id
                        viewModel.idDocc= idDoc
                    }
                }



        binding.fabSaveDaily.setOnClickListener{
            viewModel.saveDaily(binding.edDate.text.toString(),
                    binding.etDeath.text.toString().toInt(),
                    binding.etSick.text.toString().toInt(),
                    binding.etFeed.text.toString().toInt())
        }
     binding.rvDaily.layoutManager = LinearLayoutManager(activity)
        var dailyRef = db.collection("users").document(args.username).collection("doc").document(idDoc).collection("daily")
        var query = dailyRef!!.orderBy("id", Query.Direction.ASCENDING)
        val options = FirestoreRecyclerOptions.Builder<Harian>().setQuery(query, Harian::class.java).build()


    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

class DailyFirestoreRecyclerAdapter {
    private inner class DailyFirestoreRecyclerAdapter internal constructor(options: FirestoreRecyclerOptions<Harian>) : FirestoreRecyclerAdapter<Harian, DailyViewHolder>(options) {
        override fun onBindViewHolder(dailyViewHolder: DailyViewHolder, position: Int, harianModel: Harian) {
            dailyViewHolder.setDaily(harianModel.id,
                harianModel.afkir,
                harianModel.mati,
                harianModel.konsumsi)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_daily, parent, false)
            return DailyViewHolder(view)
        }
    }
}

    class DailyViewHolder internal constructor(private val view: View) : RecyclerView.ViewHolder(view) {
        fun setDaily(id : String,
                              afkir : Int,
                              mati : Int,
                              konsumsi: Int) {

//            val textView = view.findViewById<TextView>(R.id.text_view)
//            textView.text = productName
        }
    }


