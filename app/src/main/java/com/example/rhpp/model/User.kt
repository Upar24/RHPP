package com.example.rhpp.model

class User {
    var id: String? = null
    var jabatan: String? = null
    var nama: String? = null
    var password: String? = null

    constructor() {}

    constructor(id: String, jabatan: String, nama: String, password:String) {
        this.id = id
        this.jabatan = jabatan
        this.nama = nama
        this.password = password
    }

    constructor( jabatan: String, nama: String, password: String) {
        this.id = id
        this.jabatan = jabatan
        this.nama = nama
        this.password = password
    }

    fun toMap(): Map<String, Any> {

        val result = HashMap<String, Any>()
        result.put("jabatan", jabatan!!)
        result.put("nama", nama!!)
        result.put("password", password!!)

        return result
    }
}