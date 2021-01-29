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
import com.example.rhpp.databinding.FragmentRhppBinding
import com.google.firebase.firestore.FirebaseFirestore

class Rhpp : Fragment(R.layout.fragment_rhpp) {
    private var _binding : FragmentRhppBinding? = null
    private val binding get() = _binding!!
    private val viewModel :  PlasmaViewModel by viewModels()
    private val args : RhppArgs by navArgs()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
            _binding = FragmentRhppBinding.inflate(inflater,container,false)
            binding.viewmodel = viewModel
            val view = binding.root
            return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.username = args.username
        viewModel.idDocc = args.chickin
        binding.tvChickInRh.text = args.chickin
        loadRHPP()
        binding.adminCheck.setOnCheckedChangeListener{bottomView,isChecked ->
            if(args.jbtn.equals("Administrasi")){
            if(binding.adminCheck.isChecked){
                viewModel.saveRHPP(args.chickin,binding.tvPenjRpRh.text.toString(),binding.tvBopRh.text.toString(),
                binding.tvBonusIpRh.text.toString(),binding.tvBibitRh.text.toString(),binding.tvPakanRh.text.toString(),
                binding.tvOvkRh.text.toString())
            }
            }
        }
        binding.checkManager.setOnCheckedChangeListener{bottomView,isChecked ->
            if(args.jbtn.equals("Pimpinan")){
            db!!.collection("/users/pl/Plasma").document(args.username).collection("doc").document(args.chickin).collection("rhpp")
                    .document(args.chickin)
                    .update(mapOf("validManager" to true))
            }
        }
        Handler().postDelayed( {db!!.collection("/users/pl/Plasma").document(args.username).collection("doc").document(args.chickin).collection("rhpp")
                .document(args.chickin)
                .get()
                .addOnSuccessListener { doc ->
                    if (doc != null)
                    if(doc.get("validAdmin") == true){
                        binding.adminCheck.isChecked = true }
                    if(doc.get("validManager")==true){
                        binding.checkManager.isChecked=true
                    }
                }},500)




        binding.tvOvkRh.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun afterTextChanged(p0: Editable?) {
                var qty = binding.tvQtyRh.text.toString()
                var panenKg = binding.tvPanenKgRh.text.toString()
                var ip = binding.tvIpRh.text.toString()
                var pnjlnRp = binding.tvPenjRpRh.text.toString()
                var pakan = binding.tvPakanRh.text.toString()
                var ovk = binding.tvOvkRh.text.toString()
                if(!qty.equals("") && !panenKg.equals("") && !ip.equals("") && !pnjlnRp.equals("")
                        && !pakan.equals("") && !ovk.equals("")) {
                            var bop =(qty.toInt() * 400)
                            binding.tvBopRh.text = bop.toString()
                    var bonusIP = when (ip.toInt()) {
                        in 0..250 -> 0
                        in 251..260 -> 700
                        in 261..270 -> 750
                        in 271..280 -> 800
                        in 281..290 -> 850
                        in 291..300 -> 900
                        in 301..310 -> 950
                        in 311..320 -> 1050
                        in 321..330 -> 1150
                        in 331..340 -> 1200
                        in 341..346 -> 1250
                        else -> 0
                    }
                    binding.lbIpRh.text = bonusIP.toString()
                    var ipbonus =bonusIP * qty.toInt()
                    binding.tvBonusIpRh.text = ipbonus.toString()
                    var pendapatan =(pnjlnRp.toInt() + bop + ipbonus)
                    binding.tvTotalPndptnRh.text = pendapatan.toString()

                    var bibit = qty.toInt() * 6500
                    binding.tvBibitRh.text = bibit.toString()
                    var biaya = (bibit + pakan.toInt() + ovk.toInt())
                    binding.tvTotalBiayaRh.text = biaya.toString()
                    binding.tvRHPP.text = (pendapatan - biaya).toString()

                }
            }
        })
    }


    private fun loadRHPP() {
        db!!.collection("/users/pl/Plasma").document(args.username)
                .get()
                .addOnSuccessListener { doc ->
                    if (doc != null) {binding.tvUsernameRh.text = doc.get("nama").toString()}
                }
        var dataRef = db!!.collection("/users/pl/Plasma").document(args.username).collection("doc")
        dataRef.document(args.chickin)
                .get()
                .addOnSuccessListener { doc ->
                    if (doc != null) {binding.tvQtyRh.setText(doc.get("ekor").toString())
                    }
                }
        dataRef.document(args.chickin).collection("sales")
                .get()
                .addOnSuccessListener { document ->
                    var totalKg = 0.00
                    var totalRp : Long = 0
                    for (doc in document) {
                        var b = doc.get("total").toString().toLong()
                        var c = doc.get("kg").toString().toFloat()
                        totalRp += b
                        totalKg += c
                        var x = Math.round(((totalKg)*100.0))/100.0
                        binding.tvPanenKgRh.text = x.toString()
                        binding.tvPenjRpRh.text = totalRp.toString()
                    }
                }
        dataRef.document(args.chickin).collection("ip")
                .document(args.chickin)
                .get()
                .addOnSuccessListener { doc ->
                    if(doc != null){binding.tvIpRh.setText(doc.get("ip").toString())}
                }
        dataRef.document(args.chickin).collection("feed")
                .get()
                .addOnSuccessListener { document ->
                    var totalRp : Long = 0
                    for (doc in document) {
                        var c = doc.get("total").toString().toLong()
                        totalRp += c
                        binding.tvPakanRh.text = totalRp.toString()
                    }
                }
        dataRef.document(args.chickin).collection("ovk")
                .get()
                .addOnSuccessListener { document ->
                    var totalRp : Long = 0
                    for (doc in document) {
                        var c = doc.get("total").toString().toLong()
                        totalRp += c
                        binding.tvOvkRh.text = totalRp.toString()
                    }
                }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}