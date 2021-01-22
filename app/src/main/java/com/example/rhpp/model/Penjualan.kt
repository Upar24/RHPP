package com.example.rhpp.model

import java.math.BigInteger

class Penjualan {
    var id: String? = null
    var tgl: String? = null
    var pembeli: String? = null
    var ekor :  Int? = 0
    var kg : Double? = 0.00
    var umur : String? = null
    var abw : Double? = 0.00
    var hgaransi : Int? = 0
    var totalsales : Double? = 0.00
    var umurpanen : Int? = 0

    constructor() {}

    constructor(id:String, tgl: String, pembeli:String, ekor: Int, kg:Double, umur:String, abw:Double,
    hgaransi:Int, totalsales:Double, umurpanen : Int) {
        this.id = id
        this.tgl = tgl
        this.pembeli = pembeli
        this.ekor = ekor
        this.kg = kg
        this.umur = umur
        this.abw = abw
        this.hgaransi= hgaransi
        this.totalsales = totalsales
        this.umurpanen = umurpanen
    }

    constructor( tgl: String, pembeli:String, ekor: Int, kg:Double, umur:String, abw:Double,
                 hgaransi:Int, totalsales:Double, umurpanen : Int) {
        this.id = id
        this.tgl = tgl
        this.pembeli = pembeli
        this.ekor = ekor
        this.kg = kg
        this.umur = umur
        this.abw = abw
        this.hgaransi= hgaransi
        this.totalsales = totalsales
        this.umurpanen = umurpanen
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
        result.put("totalsales",totalsales!!)
        result.put("umurpanen",umurpanen!!)

        return result
    }
}