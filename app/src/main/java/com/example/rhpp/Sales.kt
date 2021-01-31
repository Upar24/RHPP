package com.example.rhpp

import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rhpp.ViewHolder.OvkViewHolder
import com.example.rhpp.ViewHolder.SalesViewHolder
import com.example.rhpp.databinding.FragmentSalesBinding
import com.example.rhpp.model.ObatVK
import com.example.rhpp.model.Penjualan
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import java.lang.Math.abs
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class Sales: Fragment(R.layout.fragment_sales) {
    private var _binding : FragmentSalesBinding? = null
    private val binding get() = _binding!!
    private val args : SalesArgs by navArgs()
    private val viewModel :  PlasmaViewModel by viewModels()
    private val db = FirebaseFirestore.getInstance()
    private var adapter: FirestoreRecyclerAdapter<Penjualan, SalesViewHolder>? = null
    private var firestoreListener: ListenerRegistration? = null
    private var salesList = mutableListOf<Penjualan>()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentSalesBinding.inflate(inflater,container,false)
        binding.viewmodel = viewModel
        val view = binding.root
        return view}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.etDate.transformIntoDatePicker(requireContext(), "MM-dd-yyyy", Date())
        viewModel.username = args.username
        viewModel.idDocc = args.chickIn
        val mLayoutManager = LinearLayoutManager(activity)
        binding.rvListSales.layoutManager = mLayoutManager
        binding.rvListSales.itemAnimator = DefaultItemAnimator()

        loadPenjualanList()
        hideRv()
        binding.fabaddsales.setOnClickListener{
            hideRv()
        }

        firestoreListener= db!!.collection("/users/pl/Plasma").document(args.username).collection("doc").document(args.chickIn).collection("sales")
                .addSnapshotListener { documentSnapshots, e->
                    if(e != null){
                        Log.e(ContentValues.TAG,"Listen Failed",e)
                        return@addSnapshotListener
                    }
                    if(documentSnapshots != null){
                        salesList = mutableListOf()
                        for(doc in documentSnapshots){
                            val sales = doc.toObject(Penjualan::class.java)
                            sales.id= doc.id
                            salesList.add(sales)
                        }
                    }
                    adapter!!.notifyDataSetChanged()
                    binding.rvListSales.adapter=adapter
                }
        binding.btnListSales.setOnClickListener {
            hideEntry()
        }

        binding.etDate.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                if(!binding.etDate.text.toString().equals("")){
                    val dates = SimpleDateFormat("MM-dd-yy")
                    val date1 : Date = dates.parse(viewModel.idDocc)
                    val date2 : Date = dates.parse(binding.etDate.text.toString())
                    val difference : Long = abs(date1.time - date2.time)
                    val diffDate = difference/(24*60*60*1000)
                    val dayDiff = diffDate.toString()
                    binding.tvUmur.text = dayDiff
                }
            }
        })

        binding.etKg.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int){
                if(!binding.etEkor.text.toString().equals("")&&!binding.etKg.text.toString().equals("")){
                    var b = binding.etEkor.text.toString().toDouble()
                    var c = binding.etKg.text.toString().toDouble()
                    var abw : Double = Math.round((c/b)*100.0)/100.0
                    binding.tvAbw.text = abw.toString()
                    if(abw in 0.000..0.400){binding.tvHargaGrns.text=0.toString()}
                    if(abw in 0.401..0.900){binding.tvHargaGrns.text=19300.toString()}
                    if(abw in 0.901..1.000){binding.tvHargaGrns.text=18800.toString()}
                    if(abw in 1.001..1.100){binding.tvHargaGrns.text=18400.toString()}
                    if(abw in 1.101..1.200){binding.tvHargaGrns.text=18050.toString()}
                    if(abw in 1.201..1.300){binding.tvHargaGrns.text=17850.toString()}
                    if(abw in 1.301..1.400){binding.tvHargaGrns.text=17650.toString()}
                    if(abw in 1.401..1.500){binding.tvHargaGrns.text=17450.toString()}
                    if(abw in 1.501..1.600){binding.tvHargaGrns.text=17250.toString()}
                    if(abw in 1.601..1.700){binding.tvHargaGrns.text=17050.toString()}
                    if(abw in 1.701..1.800){binding.tvHargaGrns.text=16800.toString()}
                    if(abw in 1.801..1.900){binding.tvHargaGrns.text=16650.toString()}
                    if(abw in 1.901..2.000){binding.tvHargaGrns.text=16550.toString()}
                    if(abw in 2.001..2.100){binding.tvHargaGrns.text=16500.toString()}
                    if(abw in 2.101..3.100){binding.tvHargaGrns.text=16450.toString()}
                    var x = binding.tvHargaGrns.text.toString().toInt()*c
                    binding.tvTotalSales.setText(Math.round(x).toString())

                }}
            override fun afterTextChanged(p0: Editable?) {}
        })
        binding.fabSales.setOnClickListener{
            viewModel.saveSale(binding.etDate.text.toString(),
                binding.etInvoice.text.toString(),
                binding.etBuyer.text.toString(),
                binding.etEkor.text.toString(),
                binding.etKg.text.toString(),
                    binding.tvUmur.text.toString(),
                    binding.tvAbw.text.toString(),
            binding.tvHargaGrns.text.toString(),
            binding.tvTotalSales.text.toString())
            hideEntry()
            Toast.makeText(activity, "Data telah disimpan!", Toast.LENGTH_LONG).show()
        }




    }

    private fun loadPenjualanList() {val query = db!!.collection("/users/pl/Plasma").document(args.username).collection("doc").document(args.chickIn).collection("sales")
        val response = FirestoreRecyclerOptions.Builder<Penjualan>()
                .setQuery(query,Penjualan::class.java).build()
        adapter = object : FirestoreRecyclerAdapter<Penjualan, SalesViewHolder>(response) {
            override fun onBindViewHolder(holder: SalesViewHolder, position: Int, model: Penjualan) {
                val pnjln = salesList[position]
                holder.id.text = pnjln.id
                holder.tgl.text = pnjln.tgl
                holder.ekor.text = pnjln.ekor
                holder.kg.text = pnjln.kg!!.format("%.2f",pnjln.kg)
//                holder.total.text = String.format("%,d",pnjln.total,(Locale.getDefault()))
                holder.total.text = pnjln.total
//                holder.total.text = (pnjln.kg?.toDouble().let {
//                    if (it != null) {
//                        pnjln.hgaransi?.toInt()?.times(it)
//                    }
//                }).toString()
//                holder.total.text = (obat.jumlah?.let { obat.harga?.times(it) }).toString()
//                holder.total.text = {totalsales(pnjln.id!!)}
                holder.edit.setOnClickListener{editSales(pnjln.id!!)}
                holder.delete.setOnClickListener{deleteSales(pnjln.id!!)}
            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SalesViewHolder {
                val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_sales,parent,false)
                return SalesViewHolder(view)
            }
        }
        adapter!!.notifyDataSetChanged()
        binding.rvListSales.adapter=adapter
    }
private fun editSales(id:String){
    hideRv()
    db!!.collection("/users/pl/Plasma").document(args.username).collection("doc").document(args.chickIn).collection("sales")
            .document(id)
            .get()
            .addOnSuccessListener{doc->
                if(doc != null){
                    binding.etDate.setText(doc.get("tgl").toString())
                    binding.etInvoice.setText(doc.get("id").toString())
                    binding.etBuyer.setText(doc.get("pembeli").toString())
                    binding.etEkor.setText(doc.get("ekor").toString())
                    binding.etKg.setText(doc.get("kg").toString())
                }
            }
            .addOnCompleteListener {
                Toast.makeText(activity, "Silahkan edit penjualan!", Toast.LENGTH_SHORT).show()
            }
            }
    private fun deleteSales(id:String){
        db!!.collection("/users/pl/Plasma").document(args.username).collection("doc").document(args.chickIn).collection("sales")
                .document(id)
                .delete()
                .addOnCompleteListener{
                    Toast.makeText(activity, "Sales has been deleted!", Toast.LENGTH_SHORT).show()
                }
    }
    private fun hideRv(){
        binding.etDate.visibility = View.VISIBLE
        binding.etInvoice.visibility = View.VISIBLE
        binding.etBuyer.visibility = View.VISIBLE
        binding.etEkor.visibility = View.VISIBLE
        binding.etKg.visibility = View.VISIBLE
        binding.tvTotalSales.visibility = View.VISIBLE
        binding.tvTitle.visibility = View.VISIBLE
        binding.tvDate.visibility = View.VISIBLE
        binding.tvKg.visibility = View.VISIBLE
        binding.tvEkor.visibility = View.VISIBLE
        binding.tvInvoice.visibility = View.VISIBLE
        binding.tvBuyer.visibility = View.VISIBLE
        binding.lUmur.visibility = View.VISIBLE
        binding.tvUmur.visibility = View.VISIBLE
        binding.lHargaGaransi.visibility = View.VISIBLE
        binding.lAbw.visibility = View.VISIBLE
        binding.tvAbw.visibility = View.VISIBLE
        binding.tvHargaGrns.visibility = View.VISIBLE
        binding.tvLsales.visibility = View.VISIBLE
        binding.fabSales.visibility = View.VISIBLE
        binding.btnListSales.visibility = View.VISIBLE

        binding.rvListSales.visibility = View.GONE
        binding.idLinear.visibility = View.GONE
        binding.tvLekor.visibility = View.GONE
        binding.tvLkg.visibility = View.GONE
        binding.tvLRp.visibility = View.GONE
        binding.tvEkorj.visibility = View.GONE
        binding.tvKgj.visibility = View.GONE
        binding.tvTotalj.visibility = View.GONE
        binding.fabaddsales.visibility = View.GONE
    }
    private fun hideEntry() {
        db!!.collection("/users/pl/Plasma").document(args.username).collection("doc")
            .document(args.chickIn).collection("sales").get()
            .addOnSuccessListener { document ->
                var totalEkor = 0
                var totalKg = 0.00
                var totalRp : Long = 0
                for (doc in document) {
                    var b = doc.get("ekor").toString().toInt()
                    var c = doc.get("kg").toString().toFloat()
                    var d = doc.get("total").toString().toLong()
                    totalEkor += b
                    totalKg += c
//                    String.format("%.2f",totalKg)
                    totalKg = totalKg * 100
                    totalKg = totalKg / 100
                    totalRp += d
                    binding.tvEkorj.text = totalEkor.toString()
                    binding.tvKgj.text = totalKg.toString().format("%2.f")
                    binding.tvTotalj.text = totalRp.toString()
                }
            }
        binding.etDate.visibility = View.GONE
        binding.etInvoice.visibility = View.GONE
        binding.etBuyer.visibility = View.GONE
        binding.etEkor.visibility = View.GONE
        binding.etKg.visibility = View.GONE
        binding.tvTotalSales.visibility = View.GONE
        binding.tvTitle.visibility = View.GONE
        binding.tvDate.visibility = View.GONE
        binding.tvKg.visibility = View.GONE
        binding.tvEkor.visibility = View.GONE
        binding.tvInvoice.visibility = View.GONE
        binding.tvBuyer.visibility = View.GONE
        binding.lUmur.visibility = View.GONE
        binding.tvUmur.visibility = View.GONE
        binding.lHargaGaransi.visibility = View.GONE
        binding.lAbw.visibility = View.GONE
        binding.tvAbw.visibility = View.GONE
        binding.tvHargaGrns.visibility = View.GONE
        binding.tvLsales.visibility = View.GONE
        binding.fabSales.visibility = View.GONE
        binding.btnListSales.visibility = View.GONE

        binding.rvListSales.visibility = View.VISIBLE
        binding.idLinear.visibility = View.VISIBLE
        binding.tvLekor.visibility = View.VISIBLE
        binding.tvLkg.visibility = View.VISIBLE
        binding.tvLRp.visibility = View.VISIBLE
        binding.tvEkorj.visibility = View.VISIBLE
        binding.tvKgj.visibility = View.VISIBLE
        binding.tvTotalj.visibility = View.VISIBLE
        binding.fabaddsales.visibility = View.VISIBLE
    }


    override fun onDestroy() {
        super.onDestroy()
        firestoreListener!!.remove()
        _binding = null
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