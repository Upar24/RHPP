package com.example.rhpp

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

class InternalViewModel: ViewModel() {
    var username : String =""
    var idDocc    : String = ""
    private val db = FirebaseFirestore.getInstance()
    init {
        db.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
    }
}