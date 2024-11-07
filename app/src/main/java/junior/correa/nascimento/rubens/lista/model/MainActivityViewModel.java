package junior.correa.nascimento.rubens.lista.model;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class MainActivityViewModel extends ViewModel {
    ArrayList<MyItem> itens = new ArrayList<>();

    public ArrayList<MyItem> getItens() {
        return itens;
    }
}
