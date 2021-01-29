package com.example.rhpp

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rhpp.ViewHolder.PlasmaViewHolder
import com.example.rhpp.databinding.FragmentInternalBinding
import com.example.rhpp.model.User
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import java.util.*


class Internal : Fragment(R.layout.fragment_internal) {
    private var _binding: FragmentInternalBinding? = null
    private val binding get() = _binding!!
    private val viewModel: InternalViewModel by viewModels()
    private val db = FirebaseFirestore.getInstance()
    private var adapter: FirestoreRecyclerAdapter<User, PlasmaViewHolder>? = null
    private var firestoreListener: ListenerRegistration? = null
    private var plasmaList = mutableListOf<User>()
    private val args: InternalArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentInternalBinding.inflate(inflater, container, false)

        binding.viewmodel = viewModel
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mLayoutManager = LinearLayoutManager(activity)
        binding.rvListPlasma.layoutManager = mLayoutManager
        binding.rvListPlasma.itemAnimator = DefaultItemAnimator()
        binding.edTglAll.transformIntoDatePicker(requireContext(), "MM-dd-yyyy", Date())

        loadPlasma()
        firestoreListener = db!!.collection("/users/pl/Plasma")
                .addSnapshotListener { documentSnapshots, e ->
                    if (e != null) {
                        Log.e(ContentValues.TAG, "Listen failed!", e)
                        return@addSnapshotListener
                    }
                    if (documentSnapshots != null) {
                        plasmaList = mutableListOf()

                        for (doc in documentSnapshots) {
                            val plasma = doc.toObject(User::class.java)
                            plasma.id = doc.id
                            plasmaList.add(plasma)
                        }
                    }

                    adapter!!.notifyDataSetChanged()
                    binding.rvListPlasma.adapter = adapter
                }
        binding.btnPlasmaList.setOnClickListener{
            binding.imageView.visibility = View.GONE
            binding.btnPlasmaList.visibility = View.GONE
            binding.rvListPlasma.visibility = View.VISIBLE
            binding.edTglAll.visibility = View.VISIBLE
            binding.idLayout.visibility = View.VISIBLE
        }


    }

    private fun loadPlasma() {
        val query = db!!.collection("/users/pl/Plasma")

        val response = FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query, User::class.java)
                .build()

        adapter = object : FirestoreRecyclerAdapter<User, PlasmaViewHolder>(response) {
            override fun onBindViewHolder(holder: PlasmaViewHolder, position: Int, model: User) {
                val plasma = plasmaList[position]

                holder.id.text = plasma.id
                holder.nama.text = plasma.nama
                holder.ip.setOnClickListener { showip(plasma.id!!) }
                holder.rhpp.setOnClickListener { showrhpp(plasma.id!!) }
            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlasmaViewHolder {
                val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_plasma, parent, false)
                return PlasmaViewHolder(view)
            }
        }
        adapter!!.notifyDataSetChanged()
        binding.rvListPlasma.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        firestoreListener!!.remove()
        _binding = null
    }


    private fun showrhpp(id: String) {
        var chickIn = binding.edTglAll.text.toString()
    findNavController().navigate(InternalDirections.actionInternalToRhpp(id,chickIn,args.jbtn))

    }

    private fun showip(id: String) {
        var chickIn = binding.edTglAll.text.toString()
        findNavController().navigate(InternalDirections.actionInternalToIP(id,args.jbtn,chickIn))
    }
    public override fun onStart() {
        super.onStart()

        adapter!!.startListening()
    }

    public override fun onStop() {
        super.onStop()

        adapter!!.stopListening()
    }
}

