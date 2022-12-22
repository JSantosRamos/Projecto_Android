package amsi.dei.estg.ipleiria.projecto_standauto;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import amsi.dei.estg.ipleiria.projecto_standauto.Modelo.Veiculos.SingletonVeiculos;
import amsi.dei.estg.ipleiria.projecto_standauto.Modelo.Veiculos.Veiculo;

public class DetalhesVeiculoActivity extends AppCompatActivity {

    private TextView tvMarca, tvModelo, tvAno;
    private ImageView ivImage;
    private Veiculo veiculo;
    private FloatingActionButton fabFavorito;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_veiculo);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvMarca = findViewById(R.id.tvMarcaDetalhe);
        tvAno = findViewById(R.id.tvAnoDetalhe);
        ivImage = findViewById(R.id.ivImageDetalhe);

        fabFavorito = findViewById(R.id.floatingFavorito);

        int id = (int) getIntent().getLongExtra(ListaVeiculosFragment.VEICULO, -1);
        veiculo = SingletonVeiculos.getInstance(this).getVeiculo(id);

        if (veiculo != null) {
            carregarVeiculo(veiculo);
        }

       fabFavorito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addVeiculoFavoritos();
            }
        });
    }

    private void addVeiculoFavoritos() {
        if (veiculo != null) {
            if(SingletonVeiculos.getInstance(this).adicionarVeiculoFavoritosBD(veiculo)){
                Toast.makeText(this, "TRUE", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "FALSE", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void carregarVeiculo(Veiculo veiculo) {
        tvMarca.setText(veiculo.getMarca());
        tvAno.setText("" + veiculo.getAno());

        byte[] decodedString = Base64.decode(veiculo.getImagem(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        ivImage.setImageBitmap(decodedByte);
    }
}