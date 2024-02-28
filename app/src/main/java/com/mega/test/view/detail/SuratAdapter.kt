package com.mega.test.view.detail

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutParams
import com.mega.test.R
import com.mega.test.databinding.ItemSuratBinding
import com.mega.test.model.Ayat
import com.mega.test.utils.Utils
import com.mega.test.view.OnSingleClickListener

@SuppressLint("NotifyDataSetChanged", "SetTextI18n")
class SuratAdapter(private val context: Context): RecyclerView.Adapter<SuratAdapter.MyHolder>() {
    private var listener: OnSingleClickListener?    = null
    private var surat                               = ArrayList<Ayat>()
    private var index                               = 0

    inner class MyHolder(val binding: ItemSuratBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder = MyHolder(ItemSuratBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    override fun getItemCount(): Int                                            = surat.size

    override fun onBindViewHolder(holder: MyHolder, @SuppressLint("RecyclerView") position: Int) {
        with(holder) {
            with(binding) {
                ayat.text       = context.resources.getString(R.string.ayat) + " " + surat[position].nomorAyat.toString()
                nama.text       = surat[position].teksArab
                latin.text      = surat[position].teksLatin
                indonesia.text  = surat[position].teksIndonesia

                val param = itemView.layoutParams as LayoutParams

                param.setMargins(Utils.gap(context, 16), Utils.gap(context, if (position == 0) 16 else 12), Utils.gap(context, 16), Utils.gap(context, if (position == itemCount - 1) 20 else 12))
                audio.setOnClickListener {
                    index = position

                    listener?.onClick(it)
                }
            }
        }
    }

    fun index() = index

    fun listener(listener: OnSingleClickListener) { this.listener = listener }

    fun data(surat: ArrayList<Ayat>) {
        this.surat = surat

        notifyDataSetChanged()
    }
}