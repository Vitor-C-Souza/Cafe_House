package br.me.vitorcsouza.cafe_house.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.me.vitorcsouza.cafe_house.domain.Category
import br.me.vitorcsouza.cafe_house.domain.Item
import br.me.vitorcsouza.cafe_house.repository.MainRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener

class MainViewModel : ViewModel() {

    private val repository = MainRepository()

    fun loadCategory(): LiveData<MutableList<Category>> {
        return repository.loadCategory()
    }

    fun loadPopular(): LiveData<MutableList<Item>> {
        return repository.loadPopular()
    }

}