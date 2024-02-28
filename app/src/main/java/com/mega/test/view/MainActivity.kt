package com.mega.test.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mega.test.R
import com.mega.test.databinding.ActivityMainBinding
import com.mega.test.utils.Utils

class MainActivity: AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var mainAdapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(binding.root)
        toolbar()
        mainAdapter()
        viewModel()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.cancel()
    }

    private fun toolbar() {
        with(binding) {
            with(toolbar) {
                title.text          = resources.getString(R.string.al_quran)
                back.visibility     = View.GONE

                title.setPadding(Utils.gap(this@MainActivity, 16), 0, 0, 0)
            }
        }
    }

    private fun viewModel() {
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        request()
        viewModel.observerData().observe(this) { data -> mainAdapter.data(data) }
        viewModel.observerProgress().observe(this) { flag -> progress(flag) }
        viewModel.observerFailure().observe(this) { flag -> failure(flag) }
    }

    private fun progress(flag: Boolean) {
        with(binding) {
            loading.visibility  = if (flag) View.VISIBLE else View.GONE
            recycler.visibility = if (!flag) View.VISIBLE else View.GONE
        }
    }

    private fun failure(flag: Boolean) {
        if (flag) request()
    }

    private fun mainAdapter() {
        mainAdapter = MainAdapter(this)

        binding.recycler.apply {
            layoutManager   = LinearLayoutManager(context)
            adapter         = mainAdapter
        }
    }

    private fun request() {
        viewModel.cancel()
        viewModel.data()
    }
}