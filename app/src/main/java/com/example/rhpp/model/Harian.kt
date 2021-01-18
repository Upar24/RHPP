package com.example.rhpp.model

data class Harian(var id: String? = null,
             var afkir: String? = null,
             var mati: String? = null,
             var konsumsi: String? = null)
//{
//
//        var id: String? = null
//        var afkir: String? = null
//        var mati: String? = null
//        var konsumsi: String? = null
//
//        constructor() {}
//
//        constructor(id: String, afkir: String, mati: String, konsumsi:String) {
//                this.id = id
//                this.afkir = afkir
//                this.mati = mati
//                this.konsumsi = konsumsi
//        }
//
//        constructor(afkir: String, mati: String, konsumsi:String) {
//                this.id = id
//                this.afkir = afkir
//                this.mati = mati
//                this.konsumsi = konsumsi
//        }
//
//        fun toMap(): Map<String, Any> {
//
//                val result = HashMap<String, Any>()
//                result.put("afkir", afkir!!)
//                result.put("mati", mati!!)
//                result.put("konsumsi", konsumsi!!)
//
//                return result
//        }
//}