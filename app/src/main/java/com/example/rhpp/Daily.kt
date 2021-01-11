package com.example.rhpp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.rhpp.databinding.FragmentDailyBinding

class Daily: Fragment(R.layout.fragment_daily) {
    private var _binding : FragmentDailyBinding? = null
    private val binding get() = _binding!!
    private val viewModel :  PlasmaViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentDailyBinding.inflate(inflater,container,false)
        binding.viewmodel = viewModel
        val view = binding.root
        return view}


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}