package com.example.rhpp

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
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


    init {
        db.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
    }
    fun saveDoc(tgl:String,strain:String,ekor:Int,bobot:Int,siap:Boolean)=viewModelScope.launch{
    db.collection("users").document(username).collection("doc").document(tgl).set(mapOf(
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
                "check" to false
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
    fun saveOvk(invoice:String,tgl:String,ovk:String,harga:Int,jmlh:Int,total:Int)=viewModelScope.launch {
        var ovkRef = db.collection("users").document(username).collection("doc").document(idDocc).collection("ovk")
        ovkRef.document(invoice).set(mapOf(
                "id" to invoice,
                "tgl" to tgl,
                "namaOvk" to ovk,
                "harga" to harga,
                "jumlah" to jmlh,
                "total" to total)
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