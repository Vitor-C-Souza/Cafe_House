package br.me.vitorcsouza.cafe_house.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.me.vitorcsouza.cafe_house.R
import br.me.vitorcsouza.cafe_house.databinding.ViewholderCartBinding
import br.me.vitorcsouza.cafe_house.domain.Item
import br.me.vitorcsouza.cafe_house.help.ChangeNumberItemsListener
import br.me.vitorcsouza.cafe_house.help.ManagmentCart
import com.bumptech.glide.Glide
import java.util.Locale

class CartAdapter(
    private val listItemSelected: ArrayList<Item>,
    context: Context,
    var changeNumberItemsListener: ChangeNumberItemsListener? = null
) : RecyclerView.Adapter<CartAdapter.Viewholder>() {
    class Viewholder(val binding: ViewholderCartBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val managmentCart = ManagmentCart(context)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Viewholder {
        val binding =
            ViewholderCartBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        return Viewholder(binding)
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        val item = listItemSelected[position]
        holder.binding.apply {
            titleTxt.text = item.title
            feeEachItem.text = holder.itemView.context.getString(
                R.string.price_format,
                String.format(Locale.getDefault(), "%.2f", item.price)
            )
            numberItemTxt.text = item.numberInCart.toString()
            totalEachItem.text = holder.itemView.context.getString(
                R.string.price_format,
                String.format(Locale.getDefault(), "%.2f", item.price * item.numberInCart)
            )

            Glide.with(holder.itemView.context)
                .load(item.picUrl[0])
                .into(picCart)

            plusCartBtn.setOnClickListener {
                val adapterPosition = holder.bindingAdapterPosition
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    managmentCart.plusItem(listItemSelected, adapterPosition, object : ChangeNumberItemsListener {
                        override fun onChanged() {
                            notifyItemChanged(adapterPosition)
                            changeNumberItemsListener?.onChanged()
                        }
                    })
                }
            }

            minusCartBtn.setOnClickListener {
                val adapterPosition = holder.bindingAdapterPosition
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    val currentQuantity = listItemSelected[adapterPosition].numberInCart
                    managmentCart.minusItem(listItemSelected, adapterPosition, object : ChangeNumberItemsListener {
                        override fun onChanged() {
                            if (currentQuantity == 1) {
                                notifyItemRemoved(adapterPosition)
                                notifyItemRangeChanged(adapterPosition, listItemSelected.size)
                            } else {
                                notifyItemChanged(adapterPosition)
                            }
                            changeNumberItemsListener?.onChanged()
                        }
                    })
                }
            }
        }
    }

    override fun getItemCount(): Int = listItemSelected.size
}
