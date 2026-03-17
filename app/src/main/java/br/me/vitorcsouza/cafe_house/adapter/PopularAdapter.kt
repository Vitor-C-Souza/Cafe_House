package br.me.vitorcsouza.cafe_house.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.me.vitorcsouza.cafe_house.R
import br.me.vitorcsouza.cafe_house.activity.DetailActivity
import br.me.vitorcsouza.cafe_house.databinding.ViewholderPopularBinding
import br.me.vitorcsouza.cafe_house.domain.Item
import com.bumptech.glide.Glide

class PopularAdapter(private val items: MutableList<Item>) :
    RecyclerView.Adapter<PopularAdapter.Viewholder>() {

    class Viewholder(val binding: ViewholderPopularBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        val binding =
            ViewholderPopularBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Viewholder(binding)
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        val item = items[position]
        val context = holder.itemView.context

        holder.binding.titleTxt.text = item.title
        holder.binding.extraTxt.text = item.extra
        holder.binding.priceTxt.text = context.getString(R.string.price_format, item.price.toString())

        if (item.picUrl.isNotEmpty()) {
            Glide.with(context)
                .load(item.picUrl[0])
                .into(holder.binding.pic)
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java).apply {
                putExtra("object", item)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = items.size
}
