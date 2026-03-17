package br.me.vitorcsouza.cafe_house.activity

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import br.me.vitorcsouza.cafe_house.R
import br.me.vitorcsouza.cafe_house.adapter.CartAdapter
import br.me.vitorcsouza.cafe_house.databinding.ActivityCartBinding
import br.me.vitorcsouza.cafe_house.help.ChangeNumberItemsListener
import br.me.vitorcsouza.cafe_house.help.ManagmentCart
import java.util.Locale

class CartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding
    private lateinit var managmentCart: ManagmentCart
    private var tax: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Ativa o modo Edge-to-Edge
        enableEdgeToEdge()

        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configuração para ocultar as barras automaticamente (Modo Imersivo)
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        
        // Esconde as barras de sistema (Status e Navegação)
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())

        // Mantemos o Listener para garantir que, quando as barras aparecerem, 
        // elas não fiquem em cima dos botões importantes
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        managmentCart = ManagmentCart(context = this)

        calculateCart()
        setVariable()
        initCartList()
    }

    private fun calculateCart() {
        val percentTax = 0.02
        val delivery = 10.0

        val itemTotal = managmentCart.getTotalFee()
        tax = itemTotal * percentTax
        val total = itemTotal + tax + delivery

        binding.apply {
            totalItemTxt.text = getString(
                R.string.price_format,
                String.format(Locale.getDefault(), "%.2f", itemTotal)
            )
            taxTxt.text =
                getString(R.string.price_format, String.format(Locale.getDefault(), "%.2f", tax))
            deliveryTxt.text = getString(
                R.string.price_format,
                String.format(Locale.getDefault(), "%.2f", delivery)
            )
            totalTxt.text =
                getString(R.string.price_format, String.format(Locale.getDefault(), "%.2f", total))

            val isEmpty = managmentCart.getListCart().isEmpty()
            emptyTxt.visibility = if (isEmpty) View.VISIBLE else View.GONE
            constraintLayout4.visibility = if (isEmpty) View.GONE else View.VISIBLE
        }
    }

    private fun setVariable() {
        binding.backBtn.setOnClickListener { finish() }
    }

    private fun initCartList() {
        binding.apply {
            cartView.layoutManager =
                LinearLayoutManager(this@CartActivity, LinearLayoutManager.VERTICAL, false)
            cartView.adapter = CartAdapter(
                managmentCart.getListCart(),
                this@CartActivity,
                object : ChangeNumberItemsListener {
                    override fun onChanged() {
                        calculateCart()
                    }
                })
        }
    }
}
