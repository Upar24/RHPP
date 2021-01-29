package com.example.rhpp

import android.content.ContentValues
import android.os.Bundle
import android.text.Editable
import android.text.Layout
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
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
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirestoreRegistrar
import com.google.firebase.firestore.ListenerRegistration
import java.text.FieldPosition
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
        binding.spJenisPakan.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            var item = p0?.getItemAtPosition(p2).toString()
                when(item){
                    "starter" -> binding.spFeed.text = "7600"
                    "finisher" -> binding.spFeed.text = "7500"
                    else -> binding.spFeed.text ="0"

               }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}

        }
        binding.fabAddPakan.setOnClickListener{hideRV()}
        binding.button.setOnClickListener { hideEntry()}
        val mlayoutManager = LinearLayoutManager(activity)
        binding.rvListFeed.layoutManager = mlayoutManager
        binding.rvListFeed.itemAnimator = DefaultItemAnimator()
        loadPakanList()
        firestoreListener = db.collection("/users/pl/Plasma").document(args.username).collection("doc").document(args.chickIn).collection("feed")
                .addSnapshotListener { documentSnapshots, e->
                    if(e != null){
                        Log.e(ContentValues.TAG,"Listen Failed",e)
                        return@addSnapshotListener
                    }
                    if(documentSnapshots != null){
                        pakanList = mutableListOf()
                        for(doc in documentSnapshots){
                            val pakan = doc.toObject(Pakan::class.java)
                            pakan.id= doc.id
                            pakanList.add(pakan)
                        }
                    }
                    adapter!!.notifyDataSetChanged()
                    binding.rvListFeed.adapter=adapter
                }

        Snackbar.make(view, viewModel.username, Snackbar.LENGTH_LONG).show()



        binding.edTotalFeed.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                if(!binding.spFeed.text.toString().equals("")&&!binding.edTotalFeed.text.toString().equals("")){
                var b = binding.spFeed.text.toString().toInt()
                var c = binding.edTotalFeed.text.toString().toInt()
                var x = b*c
                binding.etAmount.setText(x.toString()) }}

        })

        binding.fabFeed.setOnClickListener{
            viewModel.saveFeed(binding.etInvoice.text.toString(),
                binding.edDate.text.toString(),
                binding.etFeed.text.toString(),
                binding.spFeed.text.toString(),
                binding.edTotalFeed.text.toString(),
                binding.etAmount.text.toString())
            hideEntry()
        }
    }

    private fun loadPakanList() {
        val query = db.collection("/users/pl/Plasma").document(args.username).collection("doc").document(args.chickIn).collection("feed")
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
    hideRV()
    db.collection("/users/pl/Plasma").document(args.username).collection("doc").document(args.chickIn).collection("feed")
            .document(id)
            .get()
            .addOnSuccessListener{doc->
                if(doc != null){
                    binding.etInvoice.setText(doc.get("id").toString())
                    binding.edDate.setText(doc.get("tgl").toString())
                    binding.etFeed.setText(doc.get("namaPakan").toString())
                    binding.spFeed.setText(doc.get("jenis").toString())
                    binding.edTotalFeed.setText(doc.get("jumlah").toString())
                }
            }

}
    private fun deleteFeed(id:String){
        db.collection("/users/pl/Plasma").document(args.username).collection("doc").document(args.chickIn).collection("feed")
                .document(id)
                .delete()
                .addOnCompleteListener{
                    Toast.makeText(activity, "Pakan has been deleted!", Toast.LENGTH_SHORT).show()
                }

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
        binding.fabAddPakan.visibility = View.GONE
    }
    private fun hideEntry() {
        db.collection("/users/pl/Plasma").document(args.username).collection("doc")
                .document(args.chickIn).collection("feed").get()
                .addOnSuccessListener { document ->
                    var totalKg = 0
                    var totalRp = 0
                    for (doc in document) {
                        var b = doc.get("jumlah").toString().toInt()
                        var c = doc.get("total").toString().toInt()
                        totalKg= totalKg + b
                        totalRp= totalRp + c
                        binding.tvJmlh.text = totalKg.toString()
                        binding.tvTtl.text = totalRp.toString()
                    }
                }
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
        binding.fabAddPakan.visibility = View.VISIBLE
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