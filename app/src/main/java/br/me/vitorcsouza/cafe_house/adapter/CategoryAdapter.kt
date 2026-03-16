package br.me.vitorcsouza.cafe_house.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.me.vitorcsouza.cafe_house.R
import br.me.vitorcsouza.cafe_house.activity.ItemListActivity
import br.me.vitorcsouza.cafe_house.databinding.ViewholderCategoryBinding
import br.me.vitorcsouza.cafe_house.domain.Category

/**
 * O Adapter é o "garçom" do RecyclerView: ele pega os dados (a lista) e os serve para as visualizações (o layout).
 */
class CategoryAdapter(val items: MutableList<Category>) :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    // Contexto da aplicação, necessário para inflar layouts e iniciar atividades
    private lateinit var context: Context
    
    // Posição do item selecionado no momento (-1 significa nenhum selecionado)
    private var selectedPosition = -1
    
    // Posição do item que estava selecionado anteriormente (para atualizar sua cor ao desmarcar)
    private var lastSelectedPosition = -1

    /**
     * O ViewHolder é uma "caixa" que guarda as referências para as views de um único item da lista.
     * Isso evita que o sistema tenha que procurar as views pelo ID toda a vez que a lista rolar.
     */
    class ViewHolder(val binding: ViewholderCategoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    /**
     * Passo 1: Criar a visualização. Este método cria o "esqueleto" de um item da lista.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Guardamos o contexto do componente pai (o próprio RecyclerView)
        context = parent.context
        
        // Usamos o ViewBinding para transformar o arquivo XML (viewholder_category) em um objeto de código
        val binding = ViewholderCategoryBinding.inflate(LayoutInflater.from(context), parent, false)
        
        // Retornamos o ViewHolder com o layout inflado
        return ViewHolder(binding)
    }

    /**
     * Passo 2: Preencher os dados. Este método coloca as informações corretas no "esqueleto" criado acima.
     */
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        // Pegamos o objeto de dados correspondente a esta posição na lista
        val item = items[position]
        
        // Colocamos o nome da categoria no TextView do layout
        holder.binding.titleCat.text = item.title

        /**
         * Lógica de clique: o que acontece quando o usuário toca em uma categoria.
         */
        holder.binding.root.setOnClickListener {
            // Guardamos a posição que estava selecionada antes da nova ser clicada
            lastSelectedPosition = selectedPosition
            
            // Definimos que a nova posição selecionada é a atual (onde o usuário clicou)
            selectedPosition = position
            
            // Avisamos o RecyclerView que esses dois itens mudaram, para ele redesenhar a cor de fundo deles
            notifyItemChanged(lastSelectedPosition)
            notifyItemChanged(selectedPosition)

            /**
             * Handler(Looper.getMainLooper()).postDelayed:
             * Cria um pequeno atraso (0.5 segundos) antes de abrir a próxima tela.
             * Isso serve para que o usuário consiga ver a mudança de cor do botão antes da tela mudar.
             */
            Handler(Looper.getMainLooper()).postDelayed({
                // Intent: Intenção de ir para a tela 'ItemListActivity'
                val intent = Intent(context, ItemListActivity::class.java).apply {
                    // Passamos informações extras (ID e Título) para a próxima tela usar
                    putExtra("id", item.id.toString())
                    putExtra("title", item.title)
                }
                // Executamos a navegação
                context.startActivity(intent)
            }, 500)
        }

        /**
         * Lógica Visual: Muda o fundo do item dependendo se ele está selecionado ou não.
         */
        if (selectedPosition == position) {
            // Se esta posição for a selecionada, coloca o fundo marrom claro
            holder.binding.titleCat.setBackgroundResource(R.drawable.brown_bg)
        } else {
            // Se não for a selecionada, mantém o fundo marrom escuro
            holder.binding.titleCat.setBackgroundResource(R.drawable.dark_brown_bg)
        }
    }

    /**
     * Método obrigatório: informa ao RecyclerView quantos itens existem no total na lista.
     */
    override fun getItemCount(): Int = items.size
}
