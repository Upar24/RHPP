package com.example.rhpp

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.os.Bundle
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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import java.util.*

class Daily: Fragment(R.layout.fragment_daily) {
    private var _binding: FragmentDailyBinding? = null
    private val binding get() = _binding!!
    private val args: DailyArgs by navArgs()
    private val viewModel: PlasmaViewModel by viewModels()
    private val db = FirebaseFirestore.getInstance()
    private var idDoc = ""
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


        db.collection("users").document(args.username).collection("doc")
                .whereEqualTo("siap", true)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        Log.d(ContentValues.TAG, "${document.id} => ${document.data}")
                        idDoc = document.id
                        viewModel.idDocc = idDoc
                    }
                }



        binding.fabSaveDaily.setOnClickListener {
            viewModel.saveDaily(binding.edDate.text.toString(),
                    binding.etDeath.text.toString().toInt(),
                    binding.etSick.text.toString().toInt(),
                    binding.etFeed.text.toString().toInt())
        }
        val mLayoutManager = LinearLayoutManager(activity)
        binding.rvDaily.layoutManager = mLayoutManager
        binding.rvDaily.itemAnimator = DefaultItemAnimator()

        loadHarianList()
        firestoreListener = db!!.collection("users").document(args.username).collection("doc").document(idDoc).collection("daily")
                .addSnapshotListener{ documentSnapshots, e ->
                    if (e != null) {
                        Log.e(TAG, "Listen failed!", e)
                        return@addSnapshotListener
                    }
                    if ( documentSnapshots != null){
                    harianList = mutableListOf()

                    for (doc in documentSnapshots) {
                        val harian = doc.toObject(Harian::class.java)
                        harian.id = doc.id
                        harianList.add(harian)
                    }}

                    adapter!!.notifyDataSetChanged()
                    binding.rvDaily.adapter = adapter
                })
    }
//     binding.rvDaily.layoutManager = LinearLayoutManager(activity)
//        var dailyRef = db.collection("users").document(args.username).collection("doc").document(idDoc).collection("daily")
//        var query = dailyRef!!.orderBy("id", Query.Direction.ASCENDING)
//        val options = FirestoreRecyclerOptions.Builder<Harian>().setQuery(query, Harian::class.java).build()


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        firestoreListener!!.remove()
    }

    private fun loadHarianList() {

        val query = db!!.collection("users").document(args.username).collection("doc").document(idDoc).collection("daily")

        val response = FirestoreRecyclerOptions.Builder<Harian>()
                .setQuery(query, Harian::class.java)
                .build()

        adapter = object : FirestoreRecyclerAdapter<Harian, HarianViewHolder>(response) {
            override fun onBindViewHolder(holder: HarianViewHolder, position: Int, model: Harian) {
                val harian = harianList[position]

                holder.id.text = harian.id
                holder.afkir.text = harian.afkir
                holder.mati.text = harian.mati
                holder.konsumsi.text = harian.konsumsi

                holder.edit.setOnClickListener { updateNote(harian) }

                holder.delete.setOnClickListener { deleteNote(harian.id!!) }
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

    private fun updateNote(harian: Harian) {
//        val intent = Intent(this, NoteActivity::class.java)
//        intent.putExtra("UpdateNoteId", note.id)
//        intent.putExtra("UpdateNoteTitle", note.title)
//        intent.putExtra("UpdateNoteContent", note.content)
//        startActivity(intent)
    }

    private fun deleteNote(id: String) {
        db!!.collection("users").document(args.username).collection("doc").document(idDoc).collection("daily")
                .document(id)
                .delete()
                .addOnCompleteListener {
                    Toast.makeText(activity, "Note has been deleted!", Toast.LENGTH_SHORT).show()
                }
    }
}






