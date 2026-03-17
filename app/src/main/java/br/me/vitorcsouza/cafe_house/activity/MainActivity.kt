package br.me.vitorcsouza.cafe_house.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import br.me.vitorcsouza.cafe_house.adapter.CategoryAdapter
import br.me.vitorcsouza.cafe_house.adapter.PopularAdapter
import br.me.vitorcsouza.cafe_house.adapter.SpecialAdapter
import br.me.vitorcsouza.cafe_house.databinding.ActivityMainBinding
import br.me.vitorcsouza.cafe_house.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Ativa o modo de tela cheia (Edge-to-Edge)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configura o ajuste automático para as barras do sistema (StatusBar e NavigationBar)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initCategory()
        initPopular()
        initSpecial()
        initBottomMenu()
    }

    private fun initSpecial() {
        binding.progressBarSpecial.visibility = View.VISIBLE
        viewModel.loadSpecial().observe(this) { itens ->
            binding.recyclerViewSpecial.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            binding.recyclerViewSpecial.adapter = SpecialAdapter(itens)
            binding.progressBarSpecial.visibility = View.GONE
        }
    }


    /**
     * Função responsável por carregar a lista de cafés populares
     */
    private fun initPopular() {
        // 1. Torna o ProgressBar visível para indicar que o carregamento começou
        binding.progressBarPopular.visibility = View.VISIBLE

        // 2. Chama a função no ViewModel para buscar os dados no Firebase e "observa" o resultado
        // .observe(this) garante que a Activity só receba os dados se estiver ativa, evitando crashes
        viewModel.loadPopular().observe(this) { itens ->
            // 3. Define como a lista será organizada (neste caso, na vertical)
            binding.recyclerViewPopular.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

            // 4. Conecta o Adapter com a lista de itens recebida do Firebase para exibir na tela
            binding.recyclerViewPopular.adapter = PopularAdapter(itens)

            // 5. Esconde o ProgressBar, pois o carregamento já terminou
            binding.progressBarPopular.visibility = View.GONE
        }
    }

    private fun initCategory() {
        // Exibe o ProgressBar antes de começar o carregamento
        binding.progressBarCategory.visibility = View.VISIBLE

        // Observa os dados do ViewModel. .observe(this) é melhor que observeForever por segurança
        viewModel.loadCategory().observe(this) { categorias ->
            // Configura o RecyclerView como uma lista horizontal
            binding.recyclerViewCategory.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

            // Define o Adapter com a lista recebida do Firebase
            binding.recyclerViewCategory.adapter = CategoryAdapter(categorias)

            // Esconde o ProgressBar após o carregamento terminar
            binding.progressBarCategory.visibility = View.GONE
        }
    }

    private fun initBottomMenu() {
        binding.cartBtn.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }
    }
}
