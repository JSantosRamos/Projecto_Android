package amsi.dei.estg.ipleiria.projecto_standauto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.Objects;

import amsi.dei.estg.ipleiria.projecto_standauto.Adaptadores.SliderAdapter;
import amsi.dei.estg.ipleiria.projecto_standauto.Modelo.Veiculos.SingletonVeiculos;
import amsi.dei.estg.ipleiria.projecto_standauto.Modelo.Veiculos.SliderData;
import amsi.dei.estg.ipleiria.projecto_standauto.Modelo.Veiculos.Veiculo;

public class DetalhesVeiculoActivity extends AppCompatActivity {

    private TextView tvMarca, tvModelo, tvAno, tvCombustivel, tvKM, tvMotor, tvCV, tvCaixa, tvPreco, tvDescricao;
    private Veiculo veiculo;
    private SliderView sliderView;
    private FloatingActionButton fabFavorito;
    private boolean isFavorito;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_veiculo);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        tvMarca = findViewById(R.id.tvMarcaDetalhe);
        tvAno = findViewById(R.id.tvAnoDetalhe);
        tvModelo = findViewById(R.id.tvModeloDetalhe);
        tvCombustivel = findViewById(R.id.tvCombustivelDetalhe);
        tvKM = findViewById(R.id.tvQuilometrosDetalhe);
        tvMotor = findViewById(R.id.tvMotorDetalhe);
        tvCV = findViewById(R.id.tvCvDetalhe);
        tvCaixa = findViewById(R.id.tvCaixaDetallhe);
        tvPreco = findViewById(R.id.tvPrecoDetalhe);
        tvDescricao = findViewById(R.id.tvDescricaoDetalhe);
        sliderView = findViewById(R.id.slider);

        fabFavorito = findViewById(R.id.floatingFavorito);
        ExtendedFloatingActionButton extendedFabTestdrive = findViewById(R.id.fabTestdrive);

        int id = (int) getIntent().getLongExtra(ListaVeiculosFragment.VEICULO, -1);
        veiculo = SingletonVeiculos.getInstance(this).getVeiculo(id);

        if (veiculo != null) {
            carregarVeiculo(veiculo);

            if (validarFavorito()) { //valida como deve preencher o floatingButton
                fabFavorito.setImageResource(R.drawable.ic_favorito_adicionado);
                isFavorito = true; //para evitar chamar outra vez a bd
            } else {
                fabFavorito.setImageResource(R.drawable.ic_favorito_nao_dicionado);
                isFavorito = false;
            }
        }

        fabFavorito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFavorito) {
                    removerVeiculoFavoritos();
                    fabFavorito.setImageResource(R.drawable.ic_favorito_nao_dicionado);
                    isFavorito = false;

                } else {
                    adicionarVeiculoFavoritos();
                    fabFavorito.setImageResource(R.drawable.ic_favorito_adicionado);
                    isFavorito = true;
                }
            }
        });

        extendedFabTestdrive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentTestdrive = new Intent(DetalhesVeiculoActivity.this, DetalhesTestdriveActivity.class);
                intentTestdrive.putExtra(DetalhesTestdriveActivity.ID_VEICULO, veiculo.getId());
                intentTestdrive.putExtra(DetalhesTestdriveActivity.OP_CODE, 101);
                startActivity(intentTestdrive);
            }
        });
    }

    private void adicionarVeiculoFavoritos() {
        if (veiculo != null) {
            SingletonVeiculos.getInstance(this).adicionarVeiculoFavoritosBD(veiculo);
        }
    }

    private void removerVeiculoFavoritos() {
        SingletonVeiculos.getInstance(this).removerVeiculoFavoritosBD(veiculo);
    }

    @SuppressLint("SetTextI18n")
    private void carregarVeiculo(Veiculo veiculo) {
        tvMarca.setText(veiculo.getMarca());
        tvAno.setText("" + veiculo.getAno());
        tvModelo.setText(veiculo.getModelo());
        tvCombustivel.setText(veiculo.getCombustivel());
        tvKM.setText(veiculo.getQuilometros());
        tvMotor.setText(veiculo.getMotor());
        tvCV.setText("" + veiculo.getCv());
        tvCaixa.setText(veiculo.getTipoCaixa());
        tvPreco.setText("" + veiculo.getPreco() + " €");
        tvDescricao.setText(Html.fromHtml(veiculo.getDescricao()));

        //inicializar slider
        ArrayList<SliderData> sliderImages = new ArrayList<>();

        String urlCapa = veiculo.getImagem();
        urlCapa = urlCapa.replace("http://backendstand.test/", "http://10.0.2.2:80/");

        sliderImages.add(new SliderData(urlCapa));
        if (veiculo.getImagensLista().size() > 0) {
            for (String imagem : veiculo.getImagensLista()) {
                imagem = imagem.replace("http://backendstand.test/", "http://10.0.2.2:80/");
                sliderImages.add(new SliderData(imagem));
            }
        }

        SliderAdapter sliderAdapter = new SliderAdapter(this, sliderImages);
        sliderView.setSliderAdapter(sliderAdapter);
        //sliderView.setScrollTimeInSec(3);
        //sliderView.setAutoCycle(true);
        //sliderView.startAutoCycle();
    }

    private boolean validarFavorito() {
        return SingletonVeiculos.getInstance(this).verificarVeiculoFavoritoBD(veiculo.getId());
    }
}