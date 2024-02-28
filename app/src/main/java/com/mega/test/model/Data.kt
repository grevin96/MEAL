package com.mega.test.model

data class Data(
    val nomor: Int                  = 0,
    val nama: String?               = null,
    val namaLatin: String?          = null,
    val jumlahAyat: Int             = 0,
    val tempatTurun: String?        = null,
    val arti: String?               = null,
    val deskripsi: String?          = null,
    val audioFull: Audio?           = null,
    val ayat: ArrayList<Ayat>?      = null,
    val tafsir: ArrayList<Tafsir>?  = null,
    val suratSebelumnya: Any?       = null,
    val suratSelanjutnya: Any?      = null
)
