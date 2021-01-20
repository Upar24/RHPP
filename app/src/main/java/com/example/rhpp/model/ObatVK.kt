package com.example.rhpp.model

class ObatVK {
    var id: String? = null
    var tgl: String? = null
    var namaOvk: String? = null
    var harga: Int? = 0
    var jumlah : Int? = 0
    var total : Int? = 0
    constructor() {}

    constructor(id:String, tgl: String, namaOvk:String, harga:Int, jumlah:Int, total:Int) {
        this.id = id
        this.tgl = tgl
        this.namaOvk = namaOvk
        this.harga = harga
        this.jumlah = jumlah
        this.total = total
    }

    constructor( tgl: String, namaOvk:String, harga:Int, jumlah:Int, total:Int) {
        this.id = id
        this.tgl = tgl
        this.namaOvk = namaOvk
        this.harga = harga
        this.jumlah = jumlah
        this.total = total
    }

    fun toMap(): Map<String, Any> {

        val result = HashMap<String, Any>()
        result.put("tgl", tgl!!)
        result.put("namaOvk", namaOvk!!)
        result.put("harga", harga!!)
        result.put("jumlah",jumlah!!)
        result.put("total",total!!)

        return result
    }
}