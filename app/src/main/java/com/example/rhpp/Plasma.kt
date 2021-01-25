package com.example.rhpp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.rhpp.databinding.FragmentLoginBinding
import com.example.rhpp.databinding.FragmentPlasmaBinding
import com.google.android.material.snackbar.Snackbar
import java.util.*

class Plasma : Fragment(R.layout.fragment_plasma) {
    private var _binding: FragmentPlasmaBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PlasmaViewModel by viewModels()
    private val args: PlasmaArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentPlasmaBinding.inflate(inflater, container, false)
        binding.viewmodel = viewModel
        val view = binding.root
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.edChickIn.transformIntoDatePicker(requireContext(), "MM-dd-yyyy", Date())
        viewModel.username = args.username
        viewModel.idDocc = binding.edChickIn.text.toString()

        var chickIn = binding.edChickIn.text.toString()
        Snackbar.make(view, "LOL", Snackbar.LENGTH_LONG).show()
        super.onViewCreated(view, savedInstanceState)
        binding.btnDaily.setOnClickListener {
            var chickIn = binding.edChickIn.text.toString()
            findNavController().navigate(PlasmaDirections.actionPlasmaToDaily(args.username,chickIn,args.jbtn))
        }
        binding.btnDoc.setOnClickListener {
            var chickIn = binding.edChickIn.text.toString()
            findNavController().navigate(PlasmaDirections.actionPlasmaToDoc(args.username,chickIn))
        }
        binding.btnFeed.setOnClickListener {
            var chickIn = binding.edChickIn.text.toString()
            findNavController().navigate(PlasmaDirections.actionPlasmaToFeed(args.username,chickIn,args.jbtn))
        }
        binding.btnOvk.setOnClickListener {
            var chickIn = binding.edChickIn.text.toString()
            findNavController().navigate(PlasmaDirections.actionPlasmaToOvk(args.username,chickIn,args.jbtn))
        }
        binding.btnPenjualan.setOnClickListener {
            var chickIn = binding.edChickIn.text.toString()
            findNavController().navigate(PlasmaDirections.actionPlasmaToSales(args.username,chickIn,args.jbtn))
        }
        binding.btnIp.setOnClickListener {
            var chickIn = binding.edChickIn.text.toString()
            findNavController().navigate(PlasmaDirections.actionPlasmaToIP(args.username,args.jbtn,chickIn))
        }
        binding.btnRhpp.setOnClickListener {
            var chickIn = binding.edChickIn.text.toString()
            findNavController().navigate(PlasmaDirections.actionPlasmaToRhpp(args.username,chickIn,args.jbtn))
        }



    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}