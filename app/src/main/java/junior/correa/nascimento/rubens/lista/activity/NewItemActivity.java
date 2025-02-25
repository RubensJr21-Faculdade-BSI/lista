package junior.correa.nascimento.rubens.lista.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import java.util.Objects;

import junior.correa.nascimento.rubens.lista.R;
import junior.correa.nascimento.rubens.lista.model.NewItemActivityViewModel;

public class NewItemActivity extends AppCompatActivity {

    static int PHOTO_PICKER_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_item);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        NewItemActivityViewModel vm = new ViewModelProvider( this ).get(NewItemActivityViewModel.class );

        /*
        * Para o caso de a tela estar sendo reconstruída após uma rotação por exemplo
        * é necessário preencher o "source" do elemento de preview da imagem, pois pela implementação
        * padrão, ele não consegue lidar com ciclo de vida da aplicação, ao contrário do faz as caixas de texto
        * */
        Uri selectPhotoLocation = vm.getSelectPhotoLocation();
        if(selectPhotoLocation != null) {
            ImageView imvfotoPreview = findViewById(R.id.imvfotoPreview);
            imvfotoPreview.setImageURI(selectPhotoLocation);
        }

        ImageButton imgCI = findViewById(R.id.imbCI);

        imgCI.setOnClickListener(v -> {
            Intent photoPickerIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            photoPickerIntent.setType("image/*");
            //noinspection deprecation
            startActivityForResult(photoPickerIntent, PHOTO_PICKER_REQUEST);
        });

        Button btnAddItem = findViewById(R.id.btnAddItem);

        btnAddItem.setOnClickListener(v -> {

            Uri selectPhotoLocation1 = vm.getSelectPhotoLocation();

            if (selectPhotoLocation1 == null) {
                Toast.makeText(NewItemActivity.this, "É necessário selecionar uma imagem!", Toast.LENGTH_LONG).show();
                return;
            }

            EditText etTitle = findViewById(R.id.etTitle);
            String title = etTitle.getText().toString();
            if (title.isEmpty()) {
                Toast.makeText(NewItemActivity.this, "É necessário inserir um título", Toast.LENGTH_LONG).show();
                return;
            }

            EditText etDesc = findViewById(R.id.etDesc);
            String description = etDesc.getText().toString();
            if (description.isEmpty()) {
                Toast.makeText(NewItemActivity.this, "É necessário inserir uma descrição", Toast.LENGTH_LONG).show();
                return;
            }

            Intent i = new Intent();
            i.setData(selectPhotoLocation1);
            i.putExtra("title", title);
            i.putExtra("description", description);
            setResult(Activity.RESULT_OK, i);
            finish();
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // https://stackoverflow.com/questions/30572261/using-data-from-context-providers-or-requesting-google-photos-read-permission/30909105#30909105
        /*
            O erro:
                java.lang.SecurityException: Permission Denial: opening provider com.android.providers.media.MediaDocumentsProvider from ProcessRecord{f647f72 7628:junior.correa.nascimento.rubens.lista/u0a214} (pid=7628, uid=10214) requires that you obtain access using ACTION_OPEN_DOCUMENT or related APIs
            acontece pois estamos tentando acessar um recurso de outro aplicativo.
            Mas por quê conseguimos ver no preview, e o erro só aparece quando fazemos o scroll no recycleview?
                Quando vizualizamos no preview usamos uma URI temporária, a mesma coisa quando o os itens forem inseridos no recycleview. Porém quando fazemos o scroll ele busca pela URI como recurso de outro aplicativo.
        */
        if(requestCode == PHOTO_PICKER_REQUEST) {
            if(resultCode == Activity.RESULT_OK) {
                Uri photoSelected = Objects.requireNonNull(data).getData();
                ImageView imvfotoPreview = findViewById(R.id.imvfotoPreview);
                imvfotoPreview.setImageURI(photoSelected);
                NewItemActivityViewModel vm = new ViewModelProvider(this).get(NewItemActivityViewModel.class);
                vm.setSelectPhotoLocation(photoSelected);
            }
        }
    }
}