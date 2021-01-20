package com.example.rhpp

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.rhpp.databinding.FragmentDocBinding
import java.text.SimpleDateFormat
import java.util.*

class Doc: Fragment(R.layout.fragment_doc) {
    private var _binding : FragmentDocBinding? = null
    private val binding get() = _binding!!
    private val args : DocArgs by navArgs()
    private val viewModel :  PlasmaViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentDocBinding.inflate(inflater,container,false)
        binding.viewmodel = viewModel
        val view = binding.root
        return view}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.username = args.username
        viewModel.idDocc = args.chickIn
        binding.etDate.transformIntoDatePicker(requireContext(),"MM-dd-yyyy", Date())
        binding.fabDoc.setOnClickListener{
            viewModel.saveDoc(binding.etDate.text.toString(),
                    binding.etStrain.text.toString(),
                    binding.etQty.text.toString().toInt(),
                    binding.etBobot.text.toString().toInt(),
                    binding.checkBox.isChecked)
        } }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}