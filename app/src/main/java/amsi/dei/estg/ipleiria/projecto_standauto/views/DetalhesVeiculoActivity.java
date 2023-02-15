package amsi.dei.estg.ipleiria.projecto_standauto.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.Objects;

import amsi.dei.estg.ipleiria.projecto_standauto.Adaptadores.SliderAdapter;
import amsi.dei.estg.ipleiria.projecto_standauto.Modelo.Veiculo.SingletonVeiculos;
import amsi.dei.estg.ipleiria.projecto_standauto.Modelo.Veiculo.SliderData;
import amsi.dei.estg.ipleiria.projecto_standauto.Modelo.Veiculo.Veiculo;
import amsi.dei.estg.ipleiria.projecto_standauto.R;

public class DetalhesVeiculoActivity extends AppCompatActivity {

    private TextView tvMarca, tvModelo, tvAno, tvCombustivel, tvKM, tvMotor, tvCV, tvCaixa, tvPreco, tvDescricao, tvTitulo;
    private Veiculo veiculo;
    private SliderView sliderView;
    private Button btnTestdrive, btnReserva;
    private boolean isFavorito;
    private int idUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_veiculo);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        tvTitulo = findViewById(R.id.tvTituloDetalhe);
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
        btnTestdrive = findViewById(R.id.btnTestdriveDetalheVeiculo);
        btnReserva = findViewById(R.id.btnReserva);

        int id = (int) getIntent().getLongExtra(ListaVeiculosFragment.VEICULO, -1);
        veiculo = SingletonVeiculos.getInstance(this).getVeiculo(id);

        if (veiculo != null) {
            carregarVeiculo(veiculo);

            //valida como deve preencher o item favorito da toolbar
            isFavorito = validarFavorito();
        }

        btnTestdrive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentTestdrive = new Intent(DetalhesVeiculoActivity.this, DetalhesTestdriveActivity.class);
                intentTestdrive.putExtra(DetalhesTestdriveActivity.ID_VEICULO, veiculo.getId());
                intentTestdrive.putExtra(DetalhesTestdriveActivity.CAPA, veiculo.getImagem());
                intentTestdrive.putExtra(DetalhesTestdriveActivity.OP_CODE, DetalhesTestdriveActivity.OP_CODE_ADICIONAR);
                startActivity(intentTestdrive);
            }
        });

        btnReserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentReserva = new Intent(DetalhesVeiculoActivity.this, ReservaActivity.class);
                intentReserva.putExtra(DetalhesTestdriveActivity.ID_VEICULO, veiculo.getId());
                intentReserva.putExtra(DetalhesTestdriveActivity.CAPA, veiculo.getImagem());
                startActivity(intentReserva);

               /* String email = getString(R.string.standEmail);

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
                intent.putExtra(Intent.EXTRA_SUBJECT, veiculo.toString());
                intent.putExtra(Intent.EXTRA_TEXT, "");
                startActivity(Intent.createChooser(intent, ""));*/
            }
        });
    }

    private void adicionarVeiculoFavoritos() {
        if (veiculo != null) {
            SingletonVeiculos.getInstance(this).adicionarVeiculoFavoritosBD(veiculo.getId(), idUser);
        }
    }

    private void removerVeiculoFavoritos() {
        SingletonVeiculos.getInstance(this).removerVeiculoFavoritosBD(veiculo.getId(), idUser);
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
        tvTitulo.setText(veiculo.getTitulo());

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
    }

    private boolean validarFavorito() {

        SharedPreferences sharedPref = this.getSharedPreferences(LoginActivity.DADOS_USER, Context.MODE_PRIVATE);
        idUser = sharedPref.getInt(LoginActivity.ID_KEY, -1);

        return SingletonVeiculos.getInstance(this).verificarVeiculoFavoritoBD(veiculo.getId(), idUser);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isFavorito) {
            getMenuInflater().inflate(R.menu.menu_detalhe_veiculo_fv_adicionado, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_detalhe_veiculo, menu);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.itemPartilhar:
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");

                String webpage = getString(R.string.standwebpage);
                String shareBody = webpage + veiculo.getId();
                String shareSubject = "StandAuto";

                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, shareSubject);
                startActivity(Intent.createChooser(sharingIntent, "Share using"));
                break;

            case R.id.itemFavorito:
                if (isFavorito) {
                    removerVeiculoFavoritos();
                    item.setIcon(R.drawable.ic_favorito_nao_dicionado);
                    isFavorito = false;

                } else {
                    adicionarVeiculoFavoritos();
                    item.setIcon(R.drawable.ic_favorito_adicionado);
                    isFavorito = true;
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
