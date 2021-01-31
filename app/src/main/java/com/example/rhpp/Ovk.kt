package com.example.rhpp

import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rhpp.ViewHolder.HarianViewHolder
import com.example.rhpp.ViewHolder.OvkViewHolder
import com.example.rhpp.databinding.FragmentOvkBinding
import com.example.rhpp.databinding.FragmentPlasmaBinding
import com.example.rhpp.model.Harian
import com.example.rhpp.model.ObatVK
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import java.text.SimpleDateFormat
import java.util.*

class Ovk: Fragment(R.layout.fragment_ovk) {
    private var _binding : FragmentOvkBinding? = null
    private val binding get() = _binding!!
    private val args : OvkArgs by navArgs()
    private val viewModel :  PlasmaViewModel by viewModels()
    private val db = FirebaseFirestore.getInstance()
    private var adapter : FirestoreRecyclerAdapter<ObatVK,OvkViewHolder>? = null
    private var firestoreListener : ListenerRegistration? = null
    private var OvkList = mutableListOf<ObatVK>()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentOvkBinding.inflate(inflater,container,false)
        binding.viewmodel = viewModel
        val view = binding.root
        return view}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.etDate.transformIntoDatePicker(requireContext(),"MM-dd-yyyy", Date())
        viewModel.username = args.username
        viewModel.idDocc = args.chickIn

        hideRv()
        val mLayoutManager = LinearLayoutManager(activity)
        binding.rvOvk.layoutManager = mLayoutManager
        binding.rvOvk.itemAnimator = DefaultItemAnimator()
        binding.spovk.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                var item = p0?.getItemAtPosition(p2).toString()
                var index = 0
                when(item){
                    "anovid" -> index = 0
                    "acid" -> index = 1
                    "ceva" -> index = 2
                    "frmln" -> index = 3
                    "neobro" -> index = 4
                    "medi" -> index = 5
                    "meniv" -> index = 6
                    "moxtin" -> index = 7
                    "toltra" -> index = 8
                    "vit b" -> index = 9
                    else -> index = 10
            }
                if(index==0){binding.etOvk.text = item
                binding.etPrice.text = "24000"}
                if(index==1){binding.etOvk.text = item
                binding.etPrice.text = "10000"}
                if(index==2){binding.etOvk.text = item
                binding.etPrice.text = "15000"}
                if(index==3){binding.etOvk.text = item
                binding.etPrice.text = "3200"}
                if(index==4){binding.etOvk.text = item
                binding.etPrice.text = "30000"}
                if(index==5){binding.etOvk.text = item
                binding.etPrice.text = "18000"}
                if(index==6){binding.etOvk.text = item
                binding.etPrice.text = "19800"}
                if(index==7){binding.etOvk.text = item
                binding.etPrice.text = "9900"}
                if(index==8){binding.etOvk.text = item
                binding.etPrice.text = "15300"}
                if(index==9){binding.etOvk.text = item
                binding.etPrice.text = "8900"}
                if(index==10){binding.etOvk.text = item
                binding.etPrice.text = "2100"}


                }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
        binding.fabAddOvk.setOnClickListener{
            hideRv()
        }
        loadOvkList()
        firestoreListener = db!!.collection("/users/pl/Plasma").document(args.username).collection("doc").document(args.chickIn).collection("ovk")
                .addSnapshotListener { documentSnapshots, e->
                    if(e != null){
                        Log.e(TAG,"Listen Failed",e)
                        return@addSnapshotListener
                    }
                    if(documentSnapshots != null){
                        OvkList = mutableListOf()
                        for(doc in documentSnapshots){
                            val ovk = doc.toObject(ObatVK::class.java)
                            ovk.id= doc.id
                            OvkList.add(ovk)
                        }
                    }
                    adapter!!.notifyDataSetChanged()
                    binding.rvOvk.adapter=adapter
                }
        binding.btnList.setOnClickListener{
            hideEntry()
        }

        binding.etAmount.addTextChangedListener(object  : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(!binding.etAmount.text.toString().equals("")&&!binding.etPrice.text.toString().equals("")){
                    var b = binding.etAmount.text.toString().toInt()
                    var c = binding.etPrice.text.toString().toInt()
                    var x = b*c
                    binding.etTotalRp.setText(x.toString()) }
                }
            })
        binding.etPrice.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(!binding.etAmount.text.toString().equals("")&&!binding.etPrice.text.toString().equals("")){
                    var b = binding.etAmount.text.toString().toInt()
                    var c = binding.etPrice.text.toString().toInt()
                    var x = b*c
                    binding.etTotalRp.setText(x.toString()) }
                }
            override fun afterTextChanged(p0: Editable?) {
                }

        })


        binding.fabOvk.setOnClickListener{
            viewModel.saveOvk(binding.etInvoice.text.toString(),
                binding.etDate.text.toString(),
                binding.etOvk.text.toString(),
                binding.etPrice.text.toString().toInt(),
                binding.etAmount.text.toString().toInt(),
                binding.etTotalRp.text.toString().toInt())
            hideEntry()
            Toast.makeText(activity, "Data telah disimpan!", Toast.LENGTH_LONG).show()
        }
    }




    private fun loadOvkList() {
        val query = db!!.collection("/users/pl/Plasma").document(args.username).collection("doc").document(args.chickIn).collection("ovk")
        val response = FirestoreRecyclerOptions.Builder<ObatVK>()
                .setQuery(query,ObatVK::class.java).build()
        adapter = object : FirestoreRecyclerAdapter<ObatVK, OvkViewHolder>(response) {
            override fun onBindViewHolder(holder: OvkViewHolder, position: Int, model: ObatVK) {
                val obat = OvkList[position]
                holder.id.text = obat.id
                holder.tgl.text = obat.tgl
                holder.namaOvk.text = obat.namaOvk
                holder.harga.text = obat.harga.toString()
                holder.jumlah.text = obat.jumlah.toString()
//                holder.total.text = (obat.jumlah?.let { obat.harga?.times(it) }).toString()
                holder.total.text = obat.total.toString()
                holder.edit.setOnClickListener{editOvk(obat.id!!)}
                holder.delete.setOnClickListener{deleteOvk(obat.id!!)}
            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OvkViewHolder {
                val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_ovk,parent,false)
                return OvkViewHolder(view)
            }
        }
        adapter!!.notifyDataSetChanged()
        binding.rvOvk.adapter=adapter

    }

    override fun onStart() {
        super.onStart()
        adapter!!.startListening()
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        firestoreListener!!.remove()
    }

    override fun onStop() {
        super.onStop()
        adapter!!.stopListening()
    }
    private fun deleteOvk(id:String){
        db!!.collection("/users/pl/Plasma").document(args.username).collection("doc").document(args.chickIn).collection("ovk")
                .document(id)
                .delete()
                .addOnCompleteListener{
                    Toast.makeText(activity, "OVK has been deleted!", Toast.LENGTH_SHORT).show()
                }
    }
    private fun editOvk(id: String){
        hideRv()
        db!!.collection("/users/pl/Plasma").document(args.username).collection("doc").document(args.chickIn).collection("ovk")
                .document(id)
                .get()
                .addOnSuccessListener{doc->
                    if(doc != null){
                        binding.etDate.setText(doc.get("tgl").toString())
                        binding.etInvoice.setText(doc.get("id").toString())
                        binding.etOvk.setText(doc.get("namaOvk").toString())
                        binding.etPrice.setText(doc.get("harga").toString())
                        binding.etAmount.setText(doc.get("jumlah").toString())
                        binding.etTotalRp.setText(doc.get("total").toString())
                    }
                }
                .addOnCompleteListener {
                    Toast.makeText(activity, "Silahkan Edit OVK!", Toast.LENGTH_SHORT).show()
                }
                }
    private fun hideRv(){
        binding.tvTitle.visibility = View.VISIBLE
        binding.tvInvoice.visibility = View.VISIBLE
        binding.tvDate.visibility = View.VISIBLE
        binding.tvOvk.visibility = View.VISIBLE
        binding.tvPrice.visibility = View.VISIBLE
        binding.tvTotalOvk.visibility = View.VISIBLE
        binding.tvTotalRp.visibility = View.VISIBLE
        binding.etInvoice.visibility = View.VISIBLE
        binding.etDate.visibility = View.VISIBLE
        binding.etOvk.visibility = View.VISIBLE
        binding.etPrice.visibility = View.VISIBLE
        binding.etAmount.visibility = View.VISIBLE
        binding.etTotalRp.visibility = View.VISIBLE
        binding.btnList.visibility = View.VISIBLE
        binding.fabOvk.visibility = View.VISIBLE
        binding.spovk.visibility = View.VISIBLE

        binding.idLayout.visibility=View.GONE
        binding.rvOvk.visibility=View.GONE
        binding.tvTotalb.visibility=View.GONE
        binding.tvTotalB.visibility=View.GONE
        binding.fabAddOvk.visibility = View.GONE
    }
    private fun hideEntry(){
        db!!.collection("/users/pl/Plasma").document(args.username).collection("doc")
                .document(args.chickIn).collection("ovk").get()
                .addOnSuccessListener { document ->
                    var totalRp = 0
                     for (doc in document) {
                        var b = doc.get("total").toString().toInt()
                         totalRp= totalRp + b
                         binding.tvTotalB.text = totalRp.toString()
                       }
                }
//        db!!.collection("users").document(args.username).collection("doc")
//                .document(args.chickIn).collection("ovk").get()
//                .addOnSuccessListener { document ->
//                    var totalRp = 0
//                     for (doc in document) {
//                        var b = doc.get("jumlah").toString().toInt()
//                         var c = doc.get("harga").toString().toInt()
//                         var d = b*c
//                         totalRp= totalRp + d
//                         binding.tvTotalB.text = totalRp.toString()
//                       }
//                }
        binding.tvTitle.visibility = View.GONE
        binding.tvInvoice.visibility = View.GONE
        binding.tvDate.visibility = View.GONE
        binding.tvOvk.visibility = View.GONE
        binding.tvPrice.visibility = View.GONE
        binding.tvTotalOvk.visibility = View.GONE
        binding.tvTotalRp.visibility = View.GONE
        binding.etInvoice.visibility = View.GONE
        binding.etDate.visibility = View.GONE
        binding.etOvk.visibility = View.GONE
        binding.etPrice.visibility = View.GONE
        binding.etAmount.visibility = View.GONE
        binding.etTotalRp.visibility = View.GONE
        binding.btnList.visibility = View.GONE
        binding.fabOvk.visibility = View.GONE
        binding.spovk.visibility = View.GONE

        binding.idLayout.visibility=View.VISIBLE
        binding.rvOvk.visibility=View.VISIBLE
        binding.tvTotalb.visibility=View.VISIBLE
        binding.tvTotalB.visibility=View.VISIBLE
        binding.fabAddOvk.visibility = View.VISIBLE

    }

}