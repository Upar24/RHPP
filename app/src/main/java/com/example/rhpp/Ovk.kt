package com.example.rhpp

import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.rhpp.databinding.FragmentOvkBinding
import com.example.rhpp.databinding.FragmentPlasmaBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class Ovk: Fragment(R.layout.fragment_ovk) {
    private var _binding : FragmentOvkBinding? = null
    private val binding get() = _binding!!
    private val args : OvkArgs by navArgs()
    private val viewModel :  PlasmaViewModel by viewModels()
    private val db = FirebaseFirestore.getInstance()
    private var idDoc = ""

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
        binding.fabOvk.setOnClickListener{
            viewModel.saveOvk(binding.etInvoice.text.toString(),
                binding.etDate.text.toString(),
                binding.etOvk.text.toString(),
                binding.etPrice.text.toString().toInt(),
                binding.etAmount.text.toString().toInt())
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}