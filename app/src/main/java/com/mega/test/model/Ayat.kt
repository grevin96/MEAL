package com.mega.test.model

data class Ayat(
    val nomorAyat: Int,
    val teksArab: String?,
    val teksLatin: String?,
    val teksIndonesia: String?,
    val audio: Audio?
)
