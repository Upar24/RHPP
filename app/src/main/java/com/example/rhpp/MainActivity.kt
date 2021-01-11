package com.example.rhpp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.rhpp.databinding.ActivityMainBinding
import com.google.firebase.firestore.FirebaseFirestore


class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val db = FirebaseFirestore.getInstance()
    }
}