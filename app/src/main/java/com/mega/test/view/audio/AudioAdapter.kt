package com.mega.test.view.audio

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mega.test.R
import com.mega.test.databinding.ItemAudioBinding
import com.mega.test.utils.Utils
import com.mega.test.view.OnSingleClickListener

@SuppressLint("NotifyDataSetChanged", "SetTextI18n")
class AudioAdapter(private val context: Context): RecyclerView.Adapter<AudioAdapter.MyHolder>() {
    private var listener: OnSingleClickListener?    = null
    private var stopListener: OnSingleClickListener?= null
    private var audios                              = ArrayList<String>()
    private var isPlay                              = ArrayList<Boolean>()
    private var isStop                              = ArrayList<Boolean>()
    private var index                               = 0

    inner class MyHolder(val binding: ItemAudioBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder = MyHolder(ItemAudioBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    override fun getItemCount(): Int                                            = audios.size

    override fun onBindViewHolder(holder: MyHolder, @SuppressLint("RecyclerView") position: Int) {
        with(holder) {
            with(binding) {
                audio.text          = context.resources.getString(R.string.audio) + " " + (position + 1).toString()
                stop.visibility     = if (isStop[position]) View.GONE else View.VISIBLE
                divider.visibility  = if (position == itemCount - 1) View.GONE else View.VISIBLE

                image.setImageResource(if (isPlay[position]) R.drawable.ic_pause else R.drawable.ic_play)
                itemView.setPadding(0, if (position == 0) Utils.gap(context, 4) else 0, 0, if (position == itemCount - 1) Utils.gap(context, 4) else 0)
                stop.setOnClickListener {
                    stopListener?.onClick(it)
                }
                image.setOnClickListener {
                    index = position

                    listener?.onClick(it)
                }
            }
        }
    }

    fun index() = index

    fun listener(listener: OnSingleClickListener) { this.listener = listener }
    fun stopListener(stopListener: OnSingleClickListener) { this.stopListener = stopListener }

    fun data(audios: ArrayList<String>, isPlay: ArrayList<Boolean>, isStop: ArrayList<Boolean>) {
        this.audios = audios
        this.isPlay = isPlay
        this.isStop = isStop

        notifyDataSetChanged()
    }
}