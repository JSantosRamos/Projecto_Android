package amsi.dei.estg.ipleiria.projecto_standauto.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import amsi.dei.estg.ipleiria.projecto_standauto.Modelo.Reserva.Reserva;
import amsi.dei.estg.ipleiria.projecto_standauto.Modelo.Testdrive.SingletonTestdrive;
import amsi.dei.estg.ipleiria.projecto_standauto.Modelo.Testdrive.Testdrive;
import amsi.dei.estg.ipleiria.projecto_standauto.Modelo.Veiculo.SingletonVeiculos;
import amsi.dei.estg.ipleiria.projecto_standauto.R;

public class ReservaActivity extends AppCompatActivity {

    private EditText etMorada, etNumero, etNIF;
    private Button btnUpload;
    private ImageView ivCC;
    private String imageEncoded = null;
    private FloatingActionButton fabAdicionar;
    private int idVeiculo;
    public static final String ID_VEICULO = "ID_VEICULO";

    public static final String ADICIONADO = "ADICIONADO";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserva);

        etMorada = findViewById(R.id.etMorada);
        etNumero = findViewById(R.id.etNumero);
        etNIF = findViewById(R.id.etNIF);
        btnUpload = findViewById(R.id.btnCarregarImagem);
        ivCC = findViewById(R.id.ivCC);
        fabAdicionar = findViewById(R.id.floatingAdicionarReserva);

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                int PICK_IMAGE = 1;
                startActivityForResult(intent, PICK_IMAGE);
            }
        });

        fabAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adicionarReserva();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int PICK_IMAGE = 1;
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                ivCC.setImageBitmap(bitmap);

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();

                imageEncoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void adicionarReserva() {

        if (!isNumeroValido()) {
            Toast.makeText(this, "Número inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isNifValida()) {
            Toast.makeText(this, "Nif inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isMoradaValida()) {
            Toast.makeText(this, "Morada inválida", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isCCValida()) {
            Toast.makeText(this, "CC inválido", Toast.LENGTH_SHORT).show();

            return;
        }

        idVeiculo = getIntent().getIntExtra(ID_VEICULO, -1);

        if(idVeiculo != -1){
            Reserva reserva = new Reserva(0, idVeiculo, etNumero.getText().toString(), etNIF.getText().toString(), etMorada.getText().toString(), imageEncoded);
            SingletonVeiculos.getInstance(this).adicionarReserva(reserva, this);

            Intent menu = new Intent(this, MenuMainActivity.class);
            menu.putExtra(ADICIONADO, true);
            startActivity(menu);
            finish();
        }
    }

    private boolean isCCValida() {

        return imageEncoded != null && !imageEncoded.isEmpty();
    }

    private boolean isMoradaValida() {
        String morada = etMorada.getText().toString();
        if (morada.isEmpty()) {
            return false;
        }
        return morada.length() > 4;
    }

    private boolean isNifValida() {

        String nif = etNIF.getText().toString();
        if (nif.isEmpty()) {
            return false;
        }
        return nif.length() == 9;
    }

    private boolean isNumeroValido() {
        String numero = etNumero.getText().toString();
        if (numero.isEmpty()) {
            return false;
        }
        return numero.length() == 9;
    }
}