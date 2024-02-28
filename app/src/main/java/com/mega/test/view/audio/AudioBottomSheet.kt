package com.mega.test.view.audio

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mega.test.databinding.BottomSheetAudioBinding
import com.mega.test.model.Audio
import com.mega.test.view.OnSingleClickListener

@SuppressLint("NotifyDataSetChanged")
class AudioBottomSheet(private val context: Context, private val audio: Audio): BottomSheetDialogFragment() {
    private lateinit var binding: BottomSheetAudioBinding
    private lateinit var audioAdapter: AudioAdapter

    private val audios      = ArrayList<String>()
    private val isPlay      = ArrayList<Boolean>()
    private val isStop      = ArrayList<Boolean>()
    private var mediaPlayer = MediaPlayer()
    private var lastIndex   = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = BottomSheetAudioBinding.inflate(inflater, container, false)

        audioAdapter()
        data()
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog?.setOnShowListener { it ->
            val d           = it as BottomSheetDialog
            val bottomSheet = d.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)

            bottomSheet?.let {
                val behavior    = BottomSheetBehavior.from(it)
                behavior.state  = BottomSheetBehavior.STATE_EXPANDED
            }
        }

        return super.onCreateDialog(savedInstanceState)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        mediaPlayer.stop()
    }

    private fun audioAdapter() {
        audioAdapter = AudioAdapter(context)

        audioAdapter.listener(object: OnSingleClickListener {
            override fun onClick(view: View) {
                for (i in 0 until isPlay.size) {
                    if (i == audioAdapter.index() && isPlay[i]) isPlay[i] = false
                    else isPlay[i] = i == audioAdapter.index()
                }
                mediaPlayer()
            }
        })
        audioAdapter.stopListener(object: OnSingleClickListener {
            override fun onClick(view: View) {
                stop()
                stopUpdate()
            }
        })
        binding.recycler.apply {
            layoutManager   = LinearLayoutManager(context)
            adapter         = audioAdapter
        }
    }

    private fun data() {
        audios.add(audio.one!!)
        audios.add(audio.two!!)
        audios.add(audio.three!!)
        audios.add(audio.four!!)
        audios.add(audio.five!!)

        for (ignored in audios) {
            isPlay.add(false)
            isStop.add(true)
        }
        audioAdapter.data(audios, isPlay, isStop)
    }

    private fun mediaPlayer() {
        if (lastIndex == audioAdapter.index()) {
            if (mediaPlayer.isPlaying) mediaPlayer.pause()
            else mediaPlayer.start()
        } else {
            if (lastIndex != -1) isStop[lastIndex]   = true
            lastIndex           = audioAdapter.index()
            isStop[lastIndex]   = false

            stop()

            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
            mediaPlayer.setDataSource(audios[lastIndex])

            mediaPlayer.isLooping = false

            mediaPlayer.setVolume(1f, 1f,)
            mediaPlayer.prepare()
            mediaPlayer.start()
        }
        audioAdapter.notifyDataSetChanged()
    }

    private fun stop() {
        mediaPlayer.stop()
        mediaPlayer.release()

        mediaPlayer = MediaPlayer()
        mediaPlayer.reset()

        mediaPlayer.setOnCompletionListener {
            stopUpdate()
        }
    }

    private fun stopUpdate() {
        lastIndex = -1

        for (i in 0 until isPlay.size) {
            isPlay[i] = false
            isStop[i] = true
        }
        audioAdapter.notifyDataSetChanged()
    }
}