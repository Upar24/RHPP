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
import com.example.rhpp.databinding.FragmentFeedBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class Feed: Fragment(R.layout.fragment_feed) {
    private var _binding : FragmentFeedBinding? = null
    private val binding get() = _binding!!
    private val args : FeedArgs by navArgs()
    private val viewModel :  PlasmaViewModel by viewModels()
    private val db = FirebaseFirestore.getInstance()
    private var idDoc = ""

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentFeedBinding.inflate(inflater,container,false)
        binding.viewmodel = viewModel
        val view = binding.root
        return view}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.edDate.transformIntoDatePicker(requireContext(),"MM-dd-yyyy", Date())
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
        binding.fabFeed.setOnClickListener{
            viewModel.saveFeed(binding.etInvoice.text.toString(),
                binding.edDate.text.toString(),
                binding.etFeed.text.toString(),
                binding.spFeed.selectedItem.toString(),
                binding.edTotalFeed.text.toString().toInt())
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}