package com.example.rhpp

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rhpp.ViewHolder.HarianViewHolder
import com.example.rhpp.databinding.FragmentDailyBinding
import com.example.rhpp.model.Harian
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.*
import java.util.*

class Daily: Fragment(R.layout.fragment_daily) {
    private var _binding: FragmentDailyBinding? = null
    private val binding get() = _binding!!
    private val args: DailyArgs by navArgs()
    private val viewModel: PlasmaViewModel by viewModels()
    private val db = FirebaseFirestore.getInstance()
    private var adapter: FirestoreRecyclerAdapter<Harian, HarianViewHolder>? = null
    private var firestoreListener: ListenerRegistration? = null
    private var harianList = mutableListOf<Harian>()


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentDailyBinding.inflate(inflater, container, false)
        binding.viewmodel = viewModel
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.edDate.transformIntoDatePicker(requireContext(), "MM-dd-yyyy", Date())
        viewModel.username = args.username
        viewModel.idDocc = args.chickIn
        Snackbar.make(view, viewModel.idDocc, Snackbar.LENGTH_LONG).show()



        hideRv()
        binding.fabSaveDaily.setOnClickListener {
            viewModel.saveDaily(binding.edDate.text.toString(),
                    binding.etDeath.text.toString().toInt(),
                    binding.etSick.text.toString().toInt(),
                    binding.etFeed.text.toString().toInt())
            hideEntry()
            Toast.makeText(activity, "Data telah disimpan!", Toast.LENGTH_LONG).show()
        }
        binding.fabAdd.setOnClickListener{
            hideRv()
        }
        binding.btnHarian.setOnClickListener{
            hideEntry()
        }
        val mLayoutManager = LinearLayoutManager(activity)
        binding.rvDaily.layoutManager = mLayoutManager
        binding.rvDaily.itemAnimator = DefaultItemAnimator()

        loadHarianList()
        firestoreListener = db!!.collection("/users/pl/Plasma").document(args.username).collection("doc").document(args.chickIn).collection("daily")
                .addSnapshotListener { documentSnapshots, e ->
                    if (e != null) {
                        Log.e(TAG, "Listen failed!", e)
                        return@addSnapshotListener
                    }
                    if (documentSnapshots != null) {
                        harianList = mutableListOf()

                        for (doc in documentSnapshots) {
                            val harian = doc.toObject(Harian::class.java)
                            harian.id = doc.id
                            harianList.add(harian)
                        }
                    }

                    adapter!!.notifyDataSetChanged()
                    binding.rvDaily.adapter = adapter
                }
    }

    private fun hideRv() {
        binding.tvTitle.visibility = View.VISIBLE
        binding.tvDate.visibility = View.VISIBLE
        binding.tvDeath.visibility = View.VISIBLE
        binding.tvFeed.visibility = View.VISIBLE
        binding.tvSick.visibility = View.VISIBLE
        binding.etFeed.visibility = View.VISIBLE
        binding.etDeath.visibility = View.VISIBLE
        binding.edDate.visibility = View.VISIBLE
        binding.etSick.visibility = View.VISIBLE
        binding.fabSaveDaily.visibility = View.VISIBLE
        binding.btnHarian.visibility = View.VISIBLE


        binding.idLayout.visibility =View.GONE
        binding.rvDaily.visibility = View.GONE
        binding.tvTA.visibility = View.GONE
        binding.tvTM.visibility =View.GONE
        binding.tvTF.visibility =View.GONE
        binding.tvTotal.visibility = View.GONE
        binding.tvTotalAfkir.visibility = View.GONE
        binding.tvTotalFeed.visibility = View.GONE
        binding.fabAdd.visibility = View.GONE
    }

    private fun hideEntry() {
        db!!.collection("/users/pl/Plasma").document(args.username).collection("doc")
                .document(args.chickIn).collection("daily").get()
                .addOnSuccessListener { document ->
                    var totalAfkir = 0
                    var totalMati =0
                    var totalFeed = 0
                    for (doc in document) {
                        var b = doc.get("afkir").toString().toInt()
                        var c = doc.get("mati").toString().toInt()
                        var d = doc.get("konsumsi").toString().toInt()
                        totalAfkir = totalAfkir + b
                        totalMati = totalMati + c
                        totalFeed = totalFeed + d
                        binding.tvTotal.text = totalAfkir.toString()
                        binding.tvTotalAfkir.text = totalMati.toString()
                        binding.tvTotalFeed.text = totalFeed.toString()
                    }
                }

                    binding.tvTitle.visibility = View.GONE
        binding.tvDate.visibility = View.GONE
        binding.tvDeath.visibility = View.GONE
        binding.tvFeed.visibility = View.GONE
        binding.tvSick.visibility = View.GONE
        binding.etFeed.visibility = View.GONE
        binding.etDeath.visibility = View.GONE
        binding.edDate.visibility = View.GONE
        binding.etSick.visibility = View.GONE
        binding.fabSaveDaily.visibility = View.GONE
        binding.btnHarian.visibility = View.GONE


        binding.idLayout.visibility =View.VISIBLE
        binding.rvDaily.visibility = View.VISIBLE
        binding.tvTA.visibility = View.VISIBLE
        binding.tvTM.visibility =View.VISIBLE
        binding.tvTF.visibility =View.VISIBLE
        binding.tvTotal.visibility = View.VISIBLE
        binding.tvTotalAfkir.visibility = View.VISIBLE
        binding.tvTotalFeed.visibility = View.VISIBLE
        binding.fabAdd.visibility = View.VISIBLE



    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        firestoreListener!!.remove()}


    private fun loadHarianList(){

        val query = db!!.collection("/users/pl/Plasma").document(args.username).collection("doc").document(args.chickIn).collection("daily")

        val response = FirestoreRecyclerOptions.Builder<Harian>()
                .setQuery(query, Harian::class.java)
                .build()

        adapter = object : FirestoreRecyclerAdapter<Harian, HarianViewHolder>(response) {
            override fun onBindViewHolder(holder: HarianViewHolder, position: Int, model: Harian) {
                val harian = harianList[position]

                holder.id.text = harian.id
                holder.afkir.text = harian.afkir.toString()
                holder.mati.text = harian.mati.toString()
                holder.konsumsi.text = harian.konsumsi.toString()
                holder.check.isChecked = harian.check
                holder.edit.setOnClickListener { updateHarian(harian.id!!) }
                holder.delete.setOnClickListener { deleteHarian(harian.id!!)}
                holder.check.setOnCheckedChangeListener{buttomView,isChecked ->
                    if(isChecked){checkHarian(harian.id!!)}}
            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HarianViewHolder {
                val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_daily, parent, false)

                return HarianViewHolder(view)
            }

        }
        adapter!!.notifyDataSetChanged()
        binding.rvDaily.adapter = adapter
    }

    public override fun onStart() {
        super.onStart()

        adapter!!.startListening()
    }

    public override fun onStop() {
        super.onStop()

        adapter!!.stopListening()
    }



    private fun deleteHarian(id: String) {
        db!!.collection("/users/pl/Plasma").document(args.username).collection("doc").document(args.chickIn).collection("daily")
                .document(id)
                .delete()
                .addOnCompleteListener {
                    Toast.makeText(activity, "Harian has been deleted!", Toast.LENGTH_SHORT).show()
                }
    }
    private fun checkHarian(id:String) {
        if (args.jbtn.equals("Technical Service")) {
            db!!.collection("/users/pl/Plasma").document(args.username).collection("doc").document(args.chickIn).collection("daily")
                    .document(id)
                    .update("check", true)
                    .addOnCompleteListener {
                        Toast.makeText(activity, "Harian sudah valid!", Toast.LENGTH_SHORT).show()
                    }
        }
    }
    private fun updateHarian(id: String) {
        hideRv()
        db!!.collection("/users/pl/Plasma").document(args.username).collection("doc").document(args.chickIn).collection("daily")
                .document(id)
                .get()
                .addOnSuccessListener { doc ->
                    if (doc != null) {
                        binding.edDate.setText(doc.get("id").toString())
                        binding.etDeath.setText(doc.get("mati").toString())
                        binding.etSick.setText(doc.get("afkir").toString())
                        binding.etFeed.setText(doc.get("konsumsi").toString())
                    }
                }
                .addOnCompleteListener {
                    Toast.makeText(activity, "Silahkan Update Harian!", Toast.LENGTH_SHORT).show()

                }
    }


}






