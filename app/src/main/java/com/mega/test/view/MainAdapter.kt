package com.mega.test.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mega.test.R
import com.mega.test.databinding.ItemMainBinding
import com.mega.test.model.Data
import com.mega.test.utils.Utils
import com.mega.test.view.detail.DetailActivity

@SuppressLint("NotifyDataSetChanged", "SetTextI18n")
class MainAdapter(private val context: Context): RecyclerView.Adapter<MainAdapter.MyHolder>() {
    private var data = ArrayList<Data>()

    inner class MyHolder(val binding: ItemMainBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder = MyHolder(ItemMainBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    override fun getItemCount(): Int                                            = data.size

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        with(holder) {
            with(binding) {
                number.text         = data[position].nomor.toString()
                name.text           = data[position].nama
                latin.text          = data[position].namaLatin
                other.text          = data[position].tempatTurun + " • " + data[position].arti + " | " + data[position].jumlahAyat.toString() + " " + context.resources.getString(R.string.ayat)
                divider.visibility  = if (position == itemCount - 1) View.GONE else View.VISIBLE

                itemView.setPadding(0, if (position == 0) Utils.gap(context, 4) else 0, 0, if (position == itemCount - 1) Utils.gap(context, 4) else 0)
                itemView.setOnClickListener {
                    val intent = Intent(context, DetailActivity::class.java)

                    intent.putExtra("number", data[position].nomor)
                    (context as Activity).startActivity(intent)
                }
            }
        }
    }

    fun data(data: ArrayList<Data>) {
        this.data = data

        notifyDataSetChanged()
    }
}