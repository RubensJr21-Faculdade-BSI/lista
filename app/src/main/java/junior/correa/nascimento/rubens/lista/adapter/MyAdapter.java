package junior.correa.nascimento.rubens.lista.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import junior.correa.nascimento.rubens.lista.R;
import junior.correa.nascimento.rubens.lista.activity.MainActivity;
import junior.correa.nascimento.rubens.lista.model.MyItem;

/** @noinspection rawtypes*/
public class MyAdapter extends RecyclerView.Adapter {
    MainActivity mainActivity;
    List<MyItem> itens;

    public MyAdapter(MainActivity mainActivity, List<MyItem> itens) {
        this.mainActivity = mainActivity;
        this.itens = itens;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // A classe LayoutInflater é usada para transformar arquivos xml em objetos de view
        LayoutInflater inflater = LayoutInflater.from(mainActivity);
        // Aqui nós inflamos o layout item_list.xml em um objeto view
        View v = inflater.inflate(R.layout.item_list, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyItem myItem = itens.get(position);

        View v = holder.itemView;

        ImageView imvfoto = v.findViewById(R.id.imvfoto);
        imvfoto.setImageBitmap(myItem.photo);

        TextView tvTitle = v.findViewById(R.id.tvTitle);
        tvTitle.setText(myItem.title);

        TextView tvdesc = v.findViewById(R.id.tvdesc);
        tvdesc.setText(myItem.description);
    }


    @Override
    public int getItemCount() {
        return itens.size();
    }
}