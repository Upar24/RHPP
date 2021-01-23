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
        viewModel.username = args.username
        viewModel.idDocc = args.chickIn
        var jabatan = args.jbtn



        Snackbar.make(view, viewModel.idDocc, Snackbar.LENGTH_LONG).show()
        super.onViewCreated(view, savedInstanceState)
        binding.btnDaily.setOnClickListener {
            findNavController().navigate(PlasmaDirections.actionPlasmaToDaily(args.username,args.chickIn,args.jbtn))
        }
        binding.btnDoc.setOnClickListener {
            findNavController().navigate(PlasmaDirections.actionPlasmaToDoc(args.username,args.chickIn))
        }
        binding.btnFeed.setOnClickListener {
            findNavController().navigate(PlasmaDirections.actionPlasmaToFeed(args.username,args.chickIn,args.jbtn))
        }
        binding.btnOvk.setOnClickListener {
            findNavController().navigate(PlasmaDirections.actionPlasmaToOvk(args.username,args.chickIn,args.jbtn))
        }
        binding.btnPenjualan.setOnClickListener {
            findNavController().navigate(PlasmaDirections.actionPlasmaToSales(args.username,args.chickIn,args.jbtn))
        }
        binding.btnIp.setOnClickListener {
            findNavController().navigate(PlasmaDirections.actionPlasmaToIP(args.username,args.jbtn,args.chickIn))
        }



    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}