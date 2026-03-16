package br.me.vitorcsouza.cafe_house.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import br.me.vitorcsouza.cafe_house.domain.Category
import br.me.vitorcsouza.cafe_house.repository.MainRepository

class MainViewModel : ViewModel() {

    private val repository = MainRepository()

    fun loadCategory(): LiveData<MutableList<Category>> {
        return repository.loadCategory()
    }

}