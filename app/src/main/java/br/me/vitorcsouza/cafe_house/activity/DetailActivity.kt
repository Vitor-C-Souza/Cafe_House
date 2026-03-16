package br.me.vitorcsouza.cafe_house.activity

import android.os.Build
import android.os.Bundle
import android.view.ViewOutlineProvider
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import br.me.vitorcsouza.cafe_house.R
import br.me.vitorcsouza.cafe_house.adapter.SizeAdapter
import br.me.vitorcsouza.cafe_house.databinding.ActivityDetailBinding
import br.me.vitorcsouza.cafe_house.domain.Item
import br.me.vitorcsouza.cafe_house.help.ManagmentCart
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var item: Item
    private lateinit var managmentCart: ManagmentCart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
            priceTxt.text = getString(R.string.price_format, item.price.toString())
            ratingTxt.text = item.rating.toString()
            extraTxt.text = item.extra

            addToCartBtn.setOnClickListener {
                item.numberInCart = numberOrderTxt.text.toString().toInt()
                managmentCart.insertItems(item)
            }

            backBtn.setOnClickListener { finish() }

            plusBtn.setOnClickListener {
                val currentNumber = numberOrderTxt.text.toString().toInt()
                numberOrderTxt.text = (currentNumber + 1).toString()
            }

            minusBtn.setOnClickListener {
                val currentNumber = numberOrderTxt.text.toString().toInt()
                if (currentNumber > 1) {
                    numberOrderTxt.text = (currentNumber - 1).toString()
                }
            }
        }
    }
}
