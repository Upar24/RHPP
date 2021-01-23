package com.example.rhpp.model

import java.math.BigInteger

class Penjualan {
    var id: String? = null
    var tgl: String? = null
    var pembeli: String? = null
    var ekor :  String? = null
    var kg : String? = null
    var umur : String? = null
    var abw : String? = null
    var hgaransi : String? = null
    var total : String? = null

    constructor() {}

    constructor(id:String, tgl: String, pembeli:String, ekor: String, kg:String, umur:String, abw:String,
    hgaransi:String, total:String) {
        this.id = id
        this.tgl = tgl
        this.pembeli = pembeli
        this.ekor = ekor
        this.kg = kg
        this.umur = umur
        this.abw = abw
        this.hgaransi= hgaransi
        this.total = total
    }

    constructor( tgl: String,
                 pembeli:String,
                 ekor: String, kg:String,
                 umur:String,
                 abw:String,
                 hgaransi:String,total: String) {
        this.id = id
        this.tgl = tgl
        this.pembeli = pembeli
        this.ekor = ekor
        this.kg = kg
        this.umur = umur
        this.abw = abw
        this.hgaransi= hgaransi
        this.total = total
    }

    fun toMap(): Map<String, Any> {

        val result = HashMap<String, Any>()
        result.put("tgl", tgl!!)
        result.put("pembeli", pembeli!!)
        result.put("ekor", ekor!!)
        result.put("kg",kg!!)
        result.put("umur",umur!!)
        result.put("abw",abw!!)
        result.put("hgaransi",hgaransi!!)
        result.put("total",total!!)

        return result
    }
}