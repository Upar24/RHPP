package com.example.rhpp

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import kotlinx.coroutines.launch



class PlasmaViewModel: ViewModel() {
    var username : String =""
    var idDocc    : String = ""
    private val db = FirebaseFirestore.getInstance()


    init {
        db.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
    }
    fun saveDoc(tgl:String,strain:String,ekor:Int,bobot:Int,siap:Boolean)=viewModelScope.launch{
    db.collection("/users/pl/Plasma").document(username).collection("doc").document(tgl).set(mapOf(
                "id" to tgl,
                "strain" to strain,
                "ekor" to ekor,
                "bobot" to bobot,
                "siap" to siap
        ))
    }

    fun saveDaily(tgl:String, mati:Int, afkir:Int, konsumsi:Int)=viewModelScope.launch {
 var dailyRef = db.collection("/users/pl/Plasma").document(username).collection("doc").document(idDocc).collection("daily")
        dailyRef.document(tgl).set(mapOf(
                "id" to tgl,
                "mati" to mati,
                "afkir" to afkir,
                "konsumsi" to konsumsi,
                "check" to false
        ))
    }
    fun saveFeed(invoice: String, tgl: String, pakan: String, jenis: String, jmlh: String, total: String)=viewModelScope.launch {
        var feedRef = db.collection("/users/pl/Plasma").document(username).collection("doc").document(idDocc).collection("feed")
            feedRef.document(invoice).set(mapOf(
                    "id" to invoice,
                    "tgl" to tgl,
                    "namaPakan" to pakan,
                    "jenis" to jenis,
                    "jumlah" to jmlh,
                    "total" to total
            ))
    }
    fun saveOvk(invoice:String,tgl:String,ovk:String,harga:Int,jmlh:Int,total:Int)=viewModelScope.launch {
        var ovkRef = db.collection("/users/pl/Plasma").document(username).collection("doc").document(idDocc).collection("ovk")
        ovkRef.document(invoice).set(mapOf(
                "id" to invoice,
                "tgl" to tgl,
                "namaOvk" to ovk,
                "harga" to harga,
                "jumlah" to jmlh,
                "total" to total)
        )
    }
    fun saveSale(tgl: String,invoice: String,pmbl:String,ekor: String,kg:String,
                umur:String, abw:String, hgaransi:String, total:String)=viewModelScope.launch {
        var saleRef = db.collection("/users/pl/Plasma").document(username).collection("doc").document(idDocc).collection("sales")
        saleRef.document(invoice).set(mapOf(
                "tgl" to tgl,
                "id" to invoice,
                "pembeli" to pmbl,
                "ekor" to ekor,
                "kg" to kg,
                "umur" to umur,
                "abw" to abw,
                "hgaransi" to hgaransi,
                "total" to total)
        )

    }
    fun saveIP(fcr:String, abw:String, live:String, umur:String, ip:String, chickIn:String)=viewModelScope.launch {
        var ipRef = db.collection("/users/pl/Plasma").document(username).collection("doc").document(idDocc).collection("ip")
        ipRef.document(chickIn).set(mapOf(
                "fcr" to fcr,
                "abw" to abw,
                "live" to live,
                "umur" to umur,
                "ip" to ip,
                "validAdmin" to true
        ))
    }
    fun saveRHPP(id:String,penj:String, bop:String, bonus_ip: String, bibit:String, pakan:String, ovk:String){
        var rhppRef = db.collection("/users/pl/Plasma").document(username).collection("doc").document(idDocc).collection("rhpp")
        rhppRef.document(id).set(mapOf(
                "id" to id,
                "penj_ayam" to penj,
                "bop" to bop,
                "bonus_ip" to bonus_ip,
                "bibit" to bibit,
                "pakan" to pakan,
                "ovk" to ovk,
                "validAdmin" to true
        ))
    }
}