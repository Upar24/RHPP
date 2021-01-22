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
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.rhpp.databinding.FragmentSalesBinding
import com.google.firebase.firestore.FirebaseFirestore
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
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int){}
            override fun afterTextChanged(p0: Editable?) {
                if(!binding.etEkor.text.toString().equals("")&&!binding.etKg.text.toString().equals("")){
                    var b = binding.etEkor.text.toString().toDouble()
                    var c = binding.etKg.text.toString().toDouble()
                    var abw = (c / b).toString()
                    binding.tvAbw.text = abw
                    if(abw.toDouble() in 0.001..0.400){binding.tvHargaGrns.text=0.toString()}
                    if(abw.toDouble() in 0.401..0.900){binding.tvHargaGrns.text=19300.toString()}
                    if(abw.toDouble() in 0.901..1.000){binding.tvHargaGrns.text=18800.toString()}
                    if(abw.toDouble() in 1.001..1.100){binding.tvHargaGrns.text=18400.toString()}
                    if(abw.toDouble() in 1.101..1.200){binding.tvHargaGrns.text=18050.toString()}
                    if(abw.toDouble() in 1.201..1.300){binding.tvHargaGrns.text=17850.toString()}
                    if(abw.toDouble() in 1.301..1.400){binding.tvHargaGrns.text=17650.toString()}
                    if(abw.toDouble() in 1.401..1.500){binding.tvHargaGrns.text=17450.toString()}
                    if(abw.toDouble() in 1.501..1.600){binding.tvHargaGrns.text=17250.toString()}
                    if(abw.toDouble() in 1.601..1.700){binding.tvHargaGrns.text=17050.toString()}
                    if(abw.toDouble() in 1.701..1.800){binding.tvHargaGrns.text=16800.toString()}
                    if(abw.toDouble() in 1.801..1.900){binding.tvHargaGrns.text=16650.toString()}
                    if(abw.toDouble() in 1.901..2.000){binding.tvHargaGrns.text=16550.toString()}
                    if(abw.toDouble() in 2.001..2.100){binding.tvHargaGrns.text=16500.toString()}
                    if(abw.toDouble() in 2.101..4.100){binding.tvHargaGrns.text=10000.toString()}
                    binding.tvTotalSales.setText((binding.tvHargaGrns.text.toString().toDouble()*c).toString())
            }
        }})
        binding.fabSales.setOnClickListener{
            viewModel.saveSale(binding.etDate.text.toString(),
                binding.etInvoice.text.toString(),
                binding.etBuyer.text.toString(),
                binding.etEkor.text.toString().toInt(),
                binding.etKg.text.toString().toDouble())
        }




    }

    private fun hideEntry() {
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
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}