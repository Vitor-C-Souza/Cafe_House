package br.me.vitorcsouza.cafe_house.help

import android.content.Context
import android.widget.Toast
import br.me.vitorcsouza.cafe_house.domain.Item

class ManagmentCart(val context: Context) {
    private val tinyDB = TinyDB(context)

    fun insertItems(item: Item) {
        val listItem = getListCart()
        val index = listItem.indexOfFirst { it.title == item.title }

        if (index != -1) {
            listItem[index].numberInCart = item.numberInCart
        } else {
            listItem.add(item)
        }

        tinyDB.putListObject("CartList", listItem)
        Toast.makeText(context, "Adicionado ao seu carrinho", Toast.LENGTH_SHORT).show()
    }

    fun getListCart(): ArrayList<Item> {
        return tinyDB.getListObject("CartList") ?: arrayListOf()
    }

    fun minusItem(
        listItems: ArrayList<Item>,
        position: Int,
        listener: ChangeNumberItemsListener
    ) {
        if (listItems[position].numberInCart == 1) {
            listItems.removeAt(position)
        } else {
            listItems[position].numberInCart--
        }
        tinyDB.putListObject("CartList", listItems)
        listener.onChanged()
    }

    fun plusItem(
        listItems: ArrayList<Item>,
        position: Int,
        listener: ChangeNumberItemsListener
    ) {
        listItems[position].numberInCart++
        tinyDB.putListObject("CartList", listItems)
        listener.onChanged()
    }

    fun getTotalFee(): Double {
        val listItem = getListCart()
        var fee = 0.0
        for (i in 0 until listItem.size) {
            fee += (listItem[i].price * listItem[i].numberInCart)
        }
        return fee
    }
}
