package com.example.rhpp.model

class Pakan {
    var id: String? = null
    var tgl: String? = null
    var namaPakan: String? = null
    var jenis: String? = null
    var jumlah : String? = null
    var total : String? = null

    constructor() {}

    constructor(id: String, tgl: String, namaPakan: String,jenis:String,jumlah:String, total:String) {
        this.id = id
        this.tgl = tgl
        this.namaPakan = namaPakan
        this.jenis = jenis
        this.jumlah = jumlah
        this.total = total
    }

    constructor( tgl: String, namaPakan: String, jenis:String, jumlah:String, total:String) {
        this.id = id
        this.tgl = tgl
        this.namaPakan = namaPakan
        this.jenis = jenis
        this.jumlah = jumlah
        this.total = total
    }

    fun toMap(): Map<String, Any> {

        val result = HashMap<String, Any>()
        result.put("tgl", tgl!!)
        result.put("namaPakan", namaPakan!!)
        result.put("jenis",jenis!!)
        result.put("jumlah", jumlah!!)
        result.put("total",total!!)

        return result
    }
}