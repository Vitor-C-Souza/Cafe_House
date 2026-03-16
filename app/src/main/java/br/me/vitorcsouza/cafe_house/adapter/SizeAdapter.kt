package br.me.vitorcsouza.cafe_house.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.me.vitorcsouza.cafe_house.R
import br.me.vitorcsouza.cafe_house.databinding.ViewholderSizeBinding

class SizeAdapter(val items: MutableList<String>) :
    RecyclerView.Adapter<SizeAdapter.Viewholder>() {

    private var selectedPosition = -1
    private var lastSelectedPosition = -1
    private lateinit var context: Context

    inner class Viewholder(val binding: ViewholderSizeBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Viewholder {
        context = parent.context
        val binding = ViewholderSizeBinding.inflate(LayoutInflater.from(context), parent, false)
        return Viewholder(binding)
    }

    override fun onBindViewHolder(holder: Viewholder, @SuppressLint("RecyclerView") position: Int) {

        val imageSize = when (position) {
            0 -> 45.dpToPx(context)
            1 -> 50.dpToPx(context)
            2 -> 55.dpToPx(context)
            3 -> 60.dpToPx(context)
            else -> 65.dpToPx(context)
        }
        val layoutParams = holder.binding.img.layoutParams
        layoutParams.width = imageSize
        layoutParams.height = imageSize
        holder.binding.img.layoutParams = layoutParams

        holder.binding.root.setOnClickListener {
            lastSelectedPosition = selectedPosition
            selectedPosition = position
            notifyItemChanged(lastSelectedPosition)
            notifyItemChanged(selectedPosition)
        }
        if (selectedPosition == position) {
            holder.binding.img.setBackgroundResource(R.drawable.orange_bg)
        } else {
            holder.binding.img.setBackgroundResource(R.drawable.stroke_bg)
        }
    }

    override fun getItemCount(): Int = items.size

    private fun Int.dpToPx(context: Context): Int {
        return (this * context.resources.displayMetrics.density).toInt()
    }
}
