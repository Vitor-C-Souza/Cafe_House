package br.me.vitorcsouza.cafe_house.activity

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.recyclerview.widget.GridLayoutManager
import br.me.vitorcsouza.cafe_house.adapter.PopularAdapter
import br.me.vitorcsouza.cafe_house.databinding.ActivityItemListBinding
import br.me.vitorcsouza.cafe_house.viewmodel.MainViewModel

class ItemListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityItemListBinding
    private val viewModel = MainViewModel()
    private var id: String = ""
    private var title: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityItemListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configuração para ocultar as barras automaticamente (Modo Imersivo)
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        getBundle()
        initList()
    }

    private fun getBundle() {
        id = intent.getStringExtra("id") ?: ""
        title = intent.getStringExtra("title") ?: ""

        binding.categoryTxt.text = title
    }

    private fun initList() {
        binding.apply {
            progressBar.visibility = View.VISIBLE
            viewModel.loadItems(id).observe(this@ItemListActivity) { items ->
                listView.layoutManager = GridLayoutManager(this@ItemListActivity, 2)
                listView.adapter = PopularAdapter(items)
                progressBar.visibility = View.GONE
            }
            backBtn.setOnClickListener { finish() }
        }
    }
}
