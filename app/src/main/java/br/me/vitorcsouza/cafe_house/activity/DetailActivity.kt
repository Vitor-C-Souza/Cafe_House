package br.me.vitorcsouza.cafe_house.activity

import android.os.Build
import android.os.Bundle
import android.view.ViewOutlineProvider
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import br.me.vitorcsouza.cafe_house.R
import br.me.vitorcsouza.cafe_house.adapter.SizeAdapter
import br.me.vitorcsouza.cafe_house.databinding.ActivityDetailBinding
import br.me.vitorcsouza.cafe_house.domain.Item
import br.me.vitorcsouza.cafe_house.help.ManagmentCart
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import java.util.Locale

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var item: Item
    private lateinit var managmentCart: ManagmentCart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configuração para ocultar as barras automaticamente (Modo Imersivo)
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.let { controller ->
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            controller.hide(WindowInsetsCompat.Type.systemBars())
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        managmentCart = ManagmentCart(this)

        bundle()
        setBlurEffect()
        initSizeList()
    }

    private fun initSizeList() {
        val sizeList = ArrayList<String>()
        sizeList.add("1")
        sizeList.add("2")
        sizeList.add("3")
        sizeList.add("4")

        binding.sizeList.adapter = SizeAdapter(sizeList)
        binding.sizeList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        if (item.picUrl.isNotEmpty()) {
            Glide.with(this)
                .load(item.picUrl[0])
                .apply(RequestOptions.bitmapTransform(RoundedCorners(16)))
                .into(binding.picMain)
        }
    }

    private fun setBlurEffect() {
        val radius = 20f
        val decorView = window.decorView
        val windowBackground = decorView.background

        binding.blurView.setupWith(binding.blurTarget)
            .setFrameClearDrawable(windowBackground)
            .setBlurRadius(radius)

        binding.blurView.outlineProvider = ViewOutlineProvider.BACKGROUND
        binding.blurView.clipToOutline = true
    }


    private fun bundle() {
        binding.apply {
            item = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getSerializableExtra("object", Item::class.java)!!
            } else {
                @Suppress("DEPRECATION")
                intent.getSerializableExtra("object") as Item
            }

            if (item.picUrl.isNotEmpty()) {
                Glide.with(this@DetailActivity)
                    .load(item.picUrl[0])
                    .into(picMain)
            }

            titleTxt.text = item.title
            descriptionTxt.text = item.description
            priceTxt.text = getString(R.string.price_format, String.format(Locale.getDefault(), "%.2f", item.price))
            ratingTxt.text = String.format(Locale.getDefault(), "%.1f", item.rating)
            extraTxt.text = item.extra

            addToCartBtn.setOnClickListener {
                item.numberInCart = numberOrderTxt.text.toString().toInt()
                managmentCart.insertItems(item)
            }

            backBtn.setOnClickListener { finish() }

            plusBtn.setOnClickListener {
                val currentNumber = numberOrderTxt.text.toString().toInt()
                val nextNumber = currentNumber + 1
                numberOrderTxt.text = String.format(Locale.getDefault(), "%d", nextNumber)
                
                // Aplica cor laranja no plus e remove do minus
                plusBtn.setBackgroundResource(R.drawable.orange_bg)
                plusBtn.setColorFilter(ContextCompat.getColor(this@DetailActivity, R.color.white))
                
                minusBtn.background = null
                minusBtn.setColorFilter(ContextCompat.getColor(this@DetailActivity, R.color.white))
            }

            minusBtn.setOnClickListener {
                val currentNumber = numberOrderTxt.text.toString().toInt()
                if (currentNumber > 1) {
                    val prevNumber = currentNumber - 1
                    numberOrderTxt.text = String.format(Locale.getDefault(), "%d", prevNumber)
                    
                    // Aplica cor laranja no minus e remove do plus
                    minusBtn.setBackgroundResource(R.drawable.orange_bg)
                    minusBtn.setColorFilter(ContextCompat.getColor(this@DetailActivity, R.color.white))
                    
                    plusBtn.background = null
                    plusBtn.setColorFilter(ContextCompat.getColor(this@DetailActivity, R.color.white))
                }
            }
        }
    }
}
