package com.mega.test.view.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.mega.test.R
import com.mega.test.databinding.ActivityDetailBinding
import com.mega.test.model.Audio
import com.mega.test.model.Data
import com.mega.test.model.PreviousNext
import com.mega.test.view.OnSingleClickListener
import com.mega.test.view.audio.AudioBottomSheet

@SuppressLint("SetTextI18n")
class DetailActivity: AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: DetailViewModel
    private lateinit var suratAdapter: SuratAdapter
    private lateinit var tafsirAdapter: TafsirAdapter

    private var surat: Data?    = null
    private var tafsir: Data?   = null
    private var number          = 0
    private var count           = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(binding.root)
        bundle()
        toolbar()
        listener()
        suratAdapter()
        tafsirAdapter()
        viewModel()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.cancel()
    }

    private fun bundle() {
        val bundle  = intent.extras
        number      = bundle?.getInt("number", 0)!!
    }

    private fun toolbar() {
        with(binding) {
            with(toolbar) {
                title.text = resources.getString(R.string.al_quran)

                back.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
            }
        }
    }

    private fun listener() {
        surat(true)
        with(binding) {
            buttonSurat.setOnClickListener { surat(true) }
            buttonTafsir.setOnClickListener { surat(false) }
        }
    }

    private fun information() {
        with(binding) {
            with(information) {
                latin.text  = surat?.namaLatin
                other.text  = surat?.tempatTurun + " • " + surat?.arti + " | " + surat?.jumlahAyat.toString() + " " + resources.getString(R.string.ayat)
                desc.text   = Html.fromHtml(surat?.deskripsi, HtmlCompat.FROM_HTML_MODE_LEGACY)
                name.text   = surat?.nama

                audio.setOnClickListener {
                    audioBottomSheet(surat?.audioFull!!)
                }
            }
        }
    }

    private fun previous() {
        with(binding) {
            with(previous) {
                if (surat?.suratSebelumnya != false) {
                    // variable Nama tidak bisa di generate
                    val previous            = Gson().fromJson(surat?.suratSebelumnya.toString().trim(), PreviousNext::class.java)
                    latin.text              = previous.namaLatin
                    name.text               = previous.nama
                    parent.visibility       = View.VISIBLE
                } else parent.visibility    = View.GONE
            }
        }
    }

    private fun next() {
        with(binding) {
            with(next) {
                if (surat?.suratSelanjutnya != false) {
                    // variable Nama tidak bisa di generate
                    val next                = Gson().fromJson(surat?.suratSelanjutnya.toString().trim(), PreviousNext::class.java)
                    latin.text              = next.namaLatin
                    name.text               = next.nama
                    parent.visibility       = View.VISIBLE
                } else parent.visibility    = View.GONE
            }
        }
    }

    private fun viewModel() {
        viewModel = ViewModelProvider(this)[DetailViewModel::class.java]

        reset()
        viewModel.observerSurat().observe(this) { data ->
            run {
                surat = data

                information()
//                previous()
//                next()
                suratAdapter.data(data.ayat!!)
            }
        }
        viewModel.observerTafsir().observe(this) { data ->
            run {
                tafsir = data

                tafsirAdapter.data(data.tafsir!!)
            }
        }
        viewModel.observerProgress().observe(this) { flag -> progress(flag) }
        viewModel.observerFailure().observe(this) { flag -> failure(flag) }
    }

    private fun progress(flag: Boolean) {
        with(binding) {
            if (!flag) count++
            if (!flag && count == 2) {
                loading.visibility  = View.GONE
                data.visibility     = View.VISIBLE
            } else {
                loading.visibility  = View.VISIBLE
                data.visibility     = View.GONE
            }
        }
    }

    private fun failure(flag: Boolean) {
        if (flag) request()
    }

    private fun suratAdapter() {
        suratAdapter = SuratAdapter(this)

        binding.recyclerSurat.apply {
            layoutManager   = LinearLayoutManager(context)
            adapter         = suratAdapter
        }

        suratAdapter.listener(object: OnSingleClickListener {
            override fun onClick(view: View) {
                audioBottomSheet(surat?.ayat?.get(suratAdapter.index())?.audio!!)
            }
        })
    }

    private fun tafsirAdapter() {
        tafsirAdapter = TafsirAdapter(this)

        binding.recyclerTafsir.apply {
            layoutManager   = LinearLayoutManager(context)
            adapter         = tafsirAdapter
        }
    }

    private fun reset() {
        surat   = null
        tafsir  = null
        count   = 0

        request()
    }

    private fun request() {
        viewModel.cancel()

        if (surat == null) viewModel.surat(number)
        if (tafsir == null) viewModel.tafsir(number)
    }

    private fun surat(isFlag: Boolean) {
        with(binding) {
            recyclerSurat.visibility  = if (isFlag) View.VISIBLE else View.GONE
            recyclerTafsir.visibility = if (isFlag) View.GONE else View.VISIBLE
        }
    }

    private fun audioBottomSheet(audio: Audio) {
        val audio = AudioBottomSheet(this@DetailActivity, audio)

        supportFragmentManager.let { audio.show(it, "ModalBottomSheetDialog") }
    }
}