package com.example.rhpp

import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.rhpp.databinding.FragmentDocBinding
import com.example.rhpp.databinding.FragmentIpBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore

class IP : Fragment(R.layout.fragment_ip){
    private var _binding : FragmentIpBinding? = null
    private val binding get() = _binding!!
    private val viewModel :  PlasmaViewModel by viewModels()
    private val args : IPArgs by navArgs()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentIpBinding.inflate(inflater,container,false)
        binding.viewmodel = viewModel
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.username = args.username
        viewModel.idDocc = args.chickIn
        loadIP()
        binding.tvDateChickIn.text = args.chickIn
        binding.tvKonsumsiIp.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                if (!binding.tvKapasitasIp.text.toString().equals("")
                        && !binding.tvEkorIp.text.toString().equals("")
                        && !binding.tvKgIp.text.toString().equals("")
                        && !binding.tvKonsumsiIp.text.toString().equals("")) {
                    var kapasitas = binding.tvKapasitasIp.text.toString().toInt()
                    var ekor = binding.tvEkorIp.text.toString().toInt()
                    var kg = binding.tvKgIp.text.toString().toFloat()
                    var konsumsi = binding.tvKonsumsiIp.text.toString().toInt()
                    var umurp = binding.tvUmurIp.text.toString().toFloat()
                    var fcr = Math.round((konsumsi / kg) * 100.0) / 100.0
                    var abw = Math.round((kg / ekor) * 100.0) / 100.0
                    var live = Math.round(((ekor * 100) / kapasitas).toDouble())
                    var umur = Math.round(umurp)
                    var ip = Math.round((live * 100 * abw) / (umur * fcr))
                    binding.tvFcrIp.text = fcr.toString()
                    binding.tvAbwIp.text = abw.toString()
                    binding.tvLiveIp.text = live.toString()
                    binding.tvIp.text = ip.toString()
                }
            }
        })
        Handler().postDelayed( {db!!.collection("users").document(args.username).collection("doc").document(args.chickIn).collection("ip")
                .document(args.chickIn)
                .get()
                .addOnSuccessListener { doc ->
                    if (doc != null)
                    {if(doc.get("validAdmin") == true){
                        binding.checkAdminIp.isChecked = true }}
                }},500)


        binding.checkAdminIp.setOnCheckedChangeListener{buttomView,isChecked ->
            if(binding.checkAdminIp.isChecked)
            viewModel.saveIP(binding.tvFcrIp.text.toString(),binding.tvAbwIp.text.toString(),
            binding.tvLiveIp.text.toString(),binding.tvUmurIp.text.toString(),binding.tvIp.text.toString(),
            binding.tvDateChickIn.text.toString())
        }
    }

    private fun loadIP(){
        db!!.collection("users").document(args.username)
                .get()
                .addOnSuccessListener { doc ->
                    if (doc != null) {binding.tvPlasma.text = doc.get("nama").toString()}
                }
        var dataRef = db!!.collection("users").document(args.username).collection("doc")
        dataRef.document(args.chickIn)
                .get()
                .addOnSuccessListener { doc ->
                    if (doc != null) {binding.tvKapasitasIp.setText(doc.get("ekor").toString())
                    }
                }
        dataRef.document(args.chickIn).collection("sales")
                .get()
                .addOnSuccessListener { document ->
                    var totalEkor = 0
                    var totalKg = 0.00
                    var umurp: Long = 0
                    for (doc in document) {
                        var b = doc.get("ekor").toString().toInt()
                        var c = doc.get("kg").toString().toFloat()
                        var d = doc.get("umur").toString().toInt()
                        umurp += (b * d).toLong()
                        totalEkor += b
                        totalKg += c
                        binding.tvEkorIp.text = totalEkor.toString()
                        binding.tvKgIp.text = totalKg.toString()
                        binding.tvUmurIp.text = (umurp / totalEkor).toString()
                    }
                }
        dataRef.document(args.chickIn).collection("daily")
                .get()
                .addOnSuccessListener { document ->
                    var totalKonsumsi: Long = 0
                    for (doc in document) {
                        var b = doc.get("konsumsi").toString().toLong()
                        totalKonsumsi += b
                        binding.tvKonsumsiIp.text = totalKonsumsi.toString()
                    }
                }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}