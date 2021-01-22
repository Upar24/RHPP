package com.example.rhpp

import android.os.Bundle
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rhpp.ViewHolder.PakanViewHolder
import com.example.rhpp.databinding.FragmentFeedBinding
import com.example.rhpp.model.ObatVK
import com.example.rhpp.model.Pakan
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirestoreRegistrar
import com.google.firebase.firestore.ListenerRegistration
import java.util.*

class Feed: Fragment(R.layout.fragment_feed) {
    private var _binding : FragmentFeedBinding? = null
    private val binding get() = _binding!!
    private val args : FeedArgs by navArgs()
    private val viewModel :  PlasmaViewModel by viewModels()
    private val db = FirebaseFirestore.getInstance()
    private var adapter : FirestoreRecyclerAdapter<Pakan,PakanViewHolder>? = null
    private var firestoreListener: ListenerRegistration?= null
    private var pakanList = mutableListOf<Pakan>()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentFeedBinding.inflate(inflater,container,false)
        binding.viewmodel = viewModel
        val view = binding.root
        return view}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.edDate.transformIntoDatePicker(requireContext(),"MM-dd-yyyy", Date())
        viewModel.username = args.username
        viewModel.idDocc = args.chickIn
        hideRV()
        binding.button.setOnClickListener {
            hideEntry()
        }
        val mlayoutManager = LinearLayoutManager(activity)
        binding.rvListFeed.layoutManager = mlayoutManager
        binding.rvListFeed.itemAnimator = DefaultItemAnimator()
        loadPakanList()


        binding.fabFeed.setOnClickListener{
            viewModel.saveFeed(binding.etInvoice.text.toString(),
                binding.edDate.text.toString(),
                binding.etFeed.text.toString(),
                binding.spFeed.text.toString().toInt(),
                binding.edTotalFeed.text.toString().toInt(),
                binding.etAmount.text.toString().toInt())
            hideEntry()
        }
    }

    private fun loadPakanList() {
        val query = db!!.collection("users").document(args.username).collection("doc").document(args.chickIn).collection("feed")
        val response = FirestoreRecyclerOptions.Builder<Pakan>()
                .setQuery(query, Pakan::class.java).build()
        adapter = object : FirestoreRecyclerAdapter<Pakan,PakanViewHolder>(response){
            override fun onBindViewHolder(holder: PakanViewHolder, position: Int, model: Pakan) {
                val pakan = pakanList[position]
                holder.id.text = pakan.id
                holder.tgl.text = pakan.tgl
                holder.namaPakan.text = pakan.namaPakan
                holder.jumlah.text = pakan.jumlah.toString()
                holder.total.text = pakan.total.toString()
                holder.edit.setOnClickListener{editFeed(pakan.id!!)}
                holder.delete.setOnClickListener{deleteFeed(pakan.id!!)}
            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PakanViewHolder {
                val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_feed,parent,false)
                return PakanViewHolder(view)
            }
        }
        adapter!!.notifyDataSetChanged()
        binding.rvListFeed.adapter=adapter
    }
private fun editFeed(id : String){

}
    private fun deleteFeed(id:String){

    }
    private fun hideRV() {
        binding.tvTitle.visibility = View.VISIBLE
        binding.tvInvoice.visibility = View.VISIBLE
        binding.tvDate.visibility =View.VISIBLE
        binding.etFeed.visibility =View.VISIBLE
        binding.tvTotalFeed.visibility = View.VISIBLE
        binding.tvTotal.visibility = View.VISIBLE
        binding.etInvoice.visibility = View.VISIBLE
        binding.edDate.visibility = View.VISIBLE
        binding.etFeed.visibility = View.VISIBLE
        binding.spFeed.visibility = View.VISIBLE
        binding.edTotalFeed.visibility = View.VISIBLE
        binding.etAmount.visibility = View.VISIBLE
        binding.button.visibility = View.VISIBLE
        binding.fabFeed.visibility = View.VISIBLE
        binding.tvFeed.visibility = View.VISIBLE
        binding.tvFeedType.visibility = View.VISIBLE


        binding.idLayout.visibility = View.GONE
        binding.rvListFeed.visibility = View.GONE
        binding.tvJumlah.visibility = View.GONE
        binding.tvTOTALLL.visibility = View.GONE
        binding.tvJmlh.visibility = View.GONE
        binding.tvTtl.visibility = View.GONE
    }
    private fun hideEntry() {
        binding.tvTitle.visibility = View.GONE
        binding.tvInvoice.visibility = View.GONE
        binding.tvDate.visibility =View.GONE
        binding.tvTotalFeed.visibility = View.GONE
        binding.tvTotal.visibility = View.GONE
        binding.etInvoice.visibility = View.GONE
        binding.edDate.visibility = View.GONE
        binding.etFeed.visibility = View.GONE
        binding.spFeed.visibility = View.GONE
        binding.edTotalFeed.visibility = View.GONE
        binding.etAmount.visibility = View.GONE
        binding.button.visibility = View.GONE
        binding.fabFeed.visibility = View.GONE
        binding.tvFeed.visibility= View.GONE
        binding.tvFeedType.visibility=View.GONE


        binding.idLayout.visibility = View.VISIBLE
        binding.rvListFeed.visibility = View.VISIBLE
        binding.tvJumlah.visibility = View.VISIBLE
        binding.tvTOTALLL.visibility = View.VISIBLE
        binding.tvJmlh.visibility = View.VISIBLE
        binding.tvTtl.visibility = View.VISIBLE
    }
    override fun onStart() {
        super.onStart()
        adapter!!.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter!!.stopListening()
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        firestoreListener!!.remove()
    }
}