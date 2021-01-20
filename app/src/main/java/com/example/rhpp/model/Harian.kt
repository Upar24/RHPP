package com.example.rhpp.model

class Harian {

        var id: String? = null
        var afkir: Int? = null
        var mati: Int? = null
        var konsumsi: Int? = null
        var check : Boolean = true

        constructor() {}

        constructor(id: String, afkir: Int, mati: Int, konsumsi:Int, check:Boolean) {
                this.id = id
                this.afkir = afkir
                this.mati = mati
                this.konsumsi = konsumsi
                this.check = check
        }

        constructor( afkir: Int, mati: Int, konsumsi:Int, check: Boolean) {
                this.id = id
                this.afkir = afkir
                this.mati = mati
                this.konsumsi = konsumsi
                this.check = check
        }

        fun toMap(): Map<String, Any> {

                val result = HashMap<String, Any>()
                result.put("afkir", afkir!!)
                result.put("mati", mati!!)
                result.put("konsumsi", konsumsi!!)
                result.put("check",check!!)

                return result
        }
}