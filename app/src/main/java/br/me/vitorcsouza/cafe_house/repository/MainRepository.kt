package br.me.vitorcsouza.cafe_house.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.me.vitorcsouza.cafe_house.domain.Category
import br.me.vitorcsouza.cafe_house.domain.Item
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

/**
 * O Repository é a classe responsável por gerenciar a origem dos dados (neste caso, o Firebase).
 */
class MainRepository {

    // 1. Instanciamos o acesso ao Firebase Database. É como abrir a porta do seu banco de dados online.
    private val firebaseDatabase = FirebaseDatabase.getInstance()

    /**
     * Função para carregar as categorias da tela principal.
     */
    fun loadCategory(): LiveData<MutableList<Category>> {
        // 2. Criamos um 'MutableLiveData'. Pense nele como um balde que a Activity fica vigiando.
        // Quando colocamos algo dentro desse balde (value), a tela percebe e se atualiza.
        val listData = MutableLiveData<MutableList<Category>>()

        // 3. Criamos uma referência. Aqui estamos dizendo ao Firebase: "Quero olhar para a pasta 'Category'".
        val ref = firebaseDatabase.getReference("Category")

        // 4. Adicionamos um Listener (ouvinte). Ele fica monitorando essa pasta em tempo real.
        ref.addValueEventListener(object : ValueEventListener {

            // 5. 'onDataChange' é chamado sempre que o Firebase envia os dados com sucesso.
            override fun onDataChange(snapshot: DataSnapshot) {
                // 6. Criamos uma lista vazia aqui no código para organizar os itens que virão do Firebase.
                val list = mutableListOf<Category>()

                // 7. O 'snapshot' contém todos os dados da pasta. Vamos percorrer item por item (children).
                for (child in snapshot.children) {
                    // 8. Pegamos o dado bruto e tentamos converter para a nossa classe 'Category'.
                    val item = child.getValue(Category::class.java)

                    // 9. Se a conversão deu certo (não é nulo), adicionamos na nossa lista local.
                    item?.let { list.add(it) }
                }

                // 10. Colocamos a lista finalizada dentro do nosso "balde" (listData.value).
                // Isso "avisa" automaticamente a ViewModel e a Activity que os dados chegaram.
                listData.value = list
            }

            // 11. 'onCancelled' é chamado se houver erro (ex: internet ruim ou falta de permissão no Firebase).
            override fun onCancelled(error: DatabaseError) {
                // Apenas logamos o erro para saber o que aconteceu durante o desenvolvimento.
                Log.e("MainRepository", "Erro ao carregar categorias: ${error.message}")
            }
        })

        // 12. Retornamos o LiveData. No início ele está vazio, mas será preenchido assim que o Firebase responder.
        return listData
    }

    /**
     * Função para carregar os itens populares (Cafés). O processo é idêntico ao de cima.
     */
    fun loadPopular(): LiveData<MutableList<Item>> {
        val listData = MutableLiveData<MutableList<Item>>()

        // Apontamos para a pasta "Items" no banco de dados.
        val ref = firebaseDatabase.getReference("Items")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<Item>()
                for (child in snapshot.children) {
                    // Convertemos os dados para a nossa classe 'Item' (domínio).
                    val itemObj = child.getValue(Item::class.java)
                    itemObj?.let { list.add(it) }
                }
                // Entregamos a lista completa para quem estiver observando este LiveData.
                listData.value = list
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("MainRepository", "Erro ao carregar itens populares: ${error.message}")
            }
        })
        return listData
    }


    fun loadSpecial(): LiveData<MutableList<Item>> {
        val listData = MutableLiveData<MutableList<Item>>()
        val ref = firebaseDatabase.getReference("Special")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<Item>()
                for (child in snapshot.children) {
                    val item = child.getValue(Item::class.java)
                    item?.let { list.add(it) }

                    listData.value = list
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("MainRepository", "Erro ao carregar itens especiais: ${error.message}")
            }
        })
        return listData
    }

    fun loadCategoryItems(categoryId: String): LiveData<MutableList<Item>> {
        val listData = MutableLiveData<MutableList<Item>>()
        val ref = firebaseDatabase.getReference("Items")

        val query = ref.orderByChild("categoryId").equalTo(categoryId)

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<Item>()
                for (child in snapshot.children) {
                    val item = child.getValue(Item::class.java)
                    item?.let { list.add(it) }
                }
                listData.value = list
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("MainRepository", "Erro ao carregar itens por categoria: ${error.message}")
            }
        })
        return listData
    }
}
