package com.example.rhpp

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.launch



class PlasmaViewModel: ViewModel() {
    var username : String =""
    var idDocc    : String = ""
    private val db = FirebaseFirestore.getInstance()



//    lateinit var idUsers : String
    init {
        db.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
    }
//    fun lol()=viewModelScope.launch {
//        db.collection("users").document(username).collection("doc")
//                .whereEqualTo("siap", true)
//                .get()
//                .addOnSuccessListener { documents ->
//                    for (document in documents) {
//                        idDocc = document.id
//                    }
//                }
//                .addOnFailureListener { exception ->
//                    Log.w(TAG, "Error getting documents: ", exception)
//                }
//    }
//    fun checkIdDoc ()=viewModelScope.launch {
//        db.collection("users").document(username).collection("doc")
//                .whereEqualTo("siap",true)
//                .get()
//                .addOnSuccessListener { document ->
//                   if ( document != null){
////                       Log.d(TAG, "DocumentSnapshot data: ${document.data}")
//
////                       val u = document.data
//                        idDoc = document.id
//                    }
//                }
//    }
//    fun check()=viewModelScope.launch{
//        db.collection("users").document(username)
//                .get()
//                .addOnSuccessListener { documents ->
//                    for (document in documents){
//                        val c = documents.id
//                    }
//                }
//                .addOnFailureListener{exeption ->
//                    Log.w(TAG,"Error Getting documents",exeption)
//                }
//    }
    fun saveDoc(tgl:String,strain:String,ekor:Int,bobot:Int,siap:Boolean)=viewModelScope.launch{
    var docRef = db.collection("users").document(username).collection("doc")
    docRef.document(tgl).set(mapOf(
                "id" to tgl,
                "strain" to strain,
                "ekor" to ekor,
                "bobot" to bobot,
                "siap" to siap
        ))
    }

    fun saveDaily(tgl:String, mati:Int, afkir:Int, konsumsi:Int)=viewModelScope.launch {
 var dailyRef = db.collection("users").document(username).collection("doc").document(idDocc).collection("daily")
        dailyRef.document(tgl).set(mapOf(
                "id" to tgl,
                "mati" to mati,
                "afkir" to afkir,
                "konsumsi" to konsumsi,
        ))
    }
    fun saveFeed(invoice:String,tgl:String,pakan:String,jenis:String,jmlh:Int)=viewModelScope.launch {
        var feedRef = db.collection("users").document(username).collection("doc").document(idDocc).collection("feed")
            feedRef.document(invoice).set(mapOf(
                    "id" to invoice,
                    "tgl" to tgl,
                    "namaPakan" to pakan,
                    "jenis" to jenis,
                    "jumlah" to jmlh
            ))
    }
    fun saveOvk(invoice:String,tgl:String,ovk:String,harga:Int,jmlh:Int)=viewModelScope.launch {
        var ovkRef = db.collection("users").document(username).collection("doc").document(idDocc).collection("ovk")
        ovkRef.document(invoice).set(mapOf(
                "id" to invoice,
                "tgl" to tgl,
                "namaOvk" to ovk,
                "harga" to harga,
                "jumlah" to jmlh)
        )
    }
    fun saveSale(tgl: String,invoice: String,pmbl:String,ekor: Int,kg:Double)=viewModelScope.launch {
        var saleRef = db.collection("users").document(username).collection("doc").document(idDocc).collection("sales")
        saleRef.document(invoice).set(mapOf(
                "tgl" to tgl,
                "invoice" to invoice,
                "pembeli" to pmbl,
                "ekor" to ekor,
                "kg" to kg)
        )
    }


}