package junior.correa.nascimento.rubens.lista.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import junior.correa.nascimento.rubens.lista.R;
import junior.correa.nascimento.rubens.lista.adapter.MyAdapter;
import junior.correa.nascimento.rubens.lista.model.MainActivityViewModel;
import junior.correa.nascimento.rubens.lista.model.MyItem;
import junior.correa.nascimento.rubens.lista.util.Util;

public class MainActivity extends AppCompatActivity {

    static int NEW_ITEM_REQUEST = 1;
    MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        FloatingActionButton fabAddNewItem = findViewById(R.id.fabAddNewItem);
        fabAddNewItem.setOnClickListener(v -> {
            // Cria uma intenção de enviar algo
            Intent i = new Intent(MainActivity.this, NewItemActivity.class);

            /*
                NEW_ITEM_REQUEST representa a "minha rota",
                quando alguma tela retornar algo eu poderei verificar qual tela retornou
            */
            //noinspection deprecation
            startActivityForResult(i, NEW_ITEM_REQUEST);
        });

        // RecycleView é o ViewGroup que contém as vizualizações correspondentes aos seus dados.
        /*
         * Cada elemento individual da lista é definido por um objeto fixador de visualização.
         * Quando o fixador de visualização é criado, ele não tem dados associados a si mesmo.
         * Depois que o fixador de visualização é criado, o RecyclerView vincula-o a seus dados.
         * Para definir o fixador de visualização, estenda RecyclerView.ViewHolder.
         * Logo o ViewHolder é faz o link do objeto na memória com a estrutura que foi criada a partir
         * do xml. Isso evita que seja necessário ficar recriando as Views todas as vezes que um elemento
         * for aparecer em tela.
         * */
        RecyclerView rvItens = findViewById(R.id.rvItens);

        // Provedor de ViewModel, retornando o MainActivityViewModel que não é destruído quando a Acticity é destruída, ele sobrevive ao ciclo de vida
        MainActivityViewModel vm = new ViewModelProvider(this).get(MainActivityViewModel.class);
        // Dentro do ViewModel tem um método 'getItens' que retorna uma ArrayList<MyItem>
        ArrayList<MyItem> itens = vm.getItens();

        // Instancia a classe MyAdapter passando a lista que foi recuperada a o reiniciar o ciclo de vida
        myAdapter = new MyAdapter(this,itens);
        rvItens.setAdapter(myAdapter);

        rvItens.setHasFixedSize(true);

        // LinearLayoutManager é uma implementação de RecyclerView.LayoutManager que fornece funcionalidade semelhante a android.widget.ListView
        /*
        * LinearLayout é um gerenciador de Layout que organiza os itens de for linear (em uma única direção)
        * Ele também é responsável por destruir e reutilizar os itens fora de vista
        * Também é possível configura-lo para que a rolagem seja vertical ou horizontal
        *
        * Propriedades Principais do LinearLayoutManager:
        * orientation:   Define a orientação do layout (VERTICAL ou HORIZONTAL).
        * reverseLayout: Se true, inverte a direção padrão. Em uma lista vertical, por exemplo, a
        *                rolagem normal coloca os itens mais recentes embaixo, mas com reverseLayout,
        *                os itens novos apareceriam no topo.
        * stackFromEnd:  Se true, inicia a lista a partir do fim. É útil em listas de mensagens ou
        *                bate-papo, onde você quer que o último item (mensagem mais recente) apareça no final.
        * */
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvItens.setLayoutManager(layoutManager);

        // Basciamente é a linha que separa os itens da recyclerView
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvItens.getContext(), DividerItemDecoration.VERTICAL);
        rvItens.addItemDecoration(dividerItemDecoration);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // função é chamada quando a activity que foi chamada retorna algum resultado
        super.onActivityResult(requestCode, resultCode, data);

        // Para o caso de terem várias chamadas à várias activities
        if(requestCode == NEW_ITEM_REQUEST) {
            if(resultCode == Activity.RESULT_OK) {

                MyItem myItem = new MyItem();
                myItem.title = data.getStringExtra("title");
                myItem.description = data.getStringExtra("description");
                Uri selectedPhotoURI = data.getData();

                try {
                    // Usa a função getBitmap do pacote Util para converter criar um Bitmap usando uma URI como base
                    myItem.photo = Util.getBitmap(MainActivity.this, selectedPhotoURI, 100, 100 );
                } catch (FileNotFoundException e) {
                    //noinspection CallToPrintStackTrace
                    e.printStackTrace();
                }

                MainActivityViewModel vm = new ViewModelProvider( this ).get(MainActivityViewModel.class );
                List<MyItem> itens = vm.getItens();

                itens.add(myItem);
                // Notifica o adapter que o um item foi inserido
                myAdapter.notifyItemInserted(itens.size()-1);
            }
        }
    }
}