package com.mega.test.view.detail

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mega.test.R
import com.mega.test.databinding.ItemTafsirBinding
import com.mega.test.model.Tafsir
import com.mega.test.utils.Utils

@SuppressLint("NotifyDataSetChanged", "SetTextI18n")
class TafsirAdapter(private val context: Context): RecyclerView.Adapter<TafsirAdapter.MyHolder>() {
    private var tafsir = ArrayList<Tafsir>()

    inner class MyHolder(val binding: ItemTafsirBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder = MyHolder(ItemTafsirBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    override fun getItemCount(): Int                                            = tafsir.size

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        with(holder) {
            with(binding) {
                ayat.text = context.resources.getString(R.string.ayat) + " " + tafsir[position].ayat.toString()
                teks.text = tafsir[position].teks

                itemView.setPadding(Utils.gap(context, 16), Utils.gap(context, if (position == 0) 16 else 12), Utils.gap(context, 16), Utils.gap(context, if (position == itemCount - 1) 20 else 12))
            }
        }
    }

    fun data(tafsir: ArrayList<Tafsir>) {
        this.tafsir = tafsir

        notifyDataSetChanged()
    }
}