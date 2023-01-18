package amsi.dei.estg.ipleiria.projecto_standauto;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import amsi.dei.estg.ipleiria.projecto_standauto.Modelo.Veiculos.SingletonVeiculos;
import amsi.dei.estg.ipleiria.projecto_standauto.Modelo.Venda.Venda;

public class InfoVeiculoActivity extends AppCompatActivity {

    private TextView tvMarca, tvModelo, tvSerie, tvSegemento, tvCombustivel,
            tvAno, tvCaixa, tvCV, tvMotor, tvCor, tvQuilometros, tvMatricula, tvNome, tvNif, tvMorada, tvValor;
    private ImageView ivCarro;
    private Venda venda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_veiculo);

        tvMarca = findViewById(R.id.tvMarcaInfo);
        tvModelo = findViewById(R.id.tvModeloInfo);
        tvSerie = findViewById(R.id.tvSerieInfo);
        tvSegemento = findViewById(R.id.tvSegementoInfo);
        tvCombustivel = findViewById(R.id.tvCombustivelInfo);
        tvAno = findViewById(R.id.tvAnoInfo);
        tvCaixa = findViewById(R.id.tvCaixaInfo);
        tvCV = findViewById(R.id.tvCVInfo);
        tvMotor = findViewById(R.id.tvMotorInfo);
        tvCor = findViewById(R.id.tvCorInfo);
        tvQuilometros = findViewById(R.id.tvQuilometrosInfo);
        tvMatricula = findViewById(R.id.tvMatriculaInfo);
        tvNome = findViewById(R.id.tvNomeInfo);
        tvNif = findViewById(R.id.tvNifInfo);
        tvMorada = findViewById(R.id.tvMoradaInfo);
        tvValor = findViewById(R.id.tvValorInfo);
        ivCarro = findViewById(R.id.ivVeiculoInfo);

        int id = getIntent().getIntExtra(MenuVeiculoActivity.ID_VENDA, -1);
        venda = SingletonVeiculos.getInstance(this).getVenda(id);

        if (venda != null) {
            carregarVenda(venda);
        }
    }

    @SuppressLint("SetTextI18n")
    private void carregarVenda(Venda venda) {
        tvMarca.setText(venda.getVeiculo().getMarca());
        tvModelo.setText(venda.getVeiculo().getModelo());
        tvSerie.setText(venda.getVeiculo().getSerie());
        tvSegemento.setText(venda.getVeiculo().getSegmento());
        tvCombustivel.setText(venda.getVeiculo().getCombustivel());
        tvAno.setText("" + venda.getVeiculo().getAno());
        tvMotor.setText("" + venda.getVeiculo().getMotor());
        tvCaixa.setText(venda.getVeiculo().getTipoCaixa());
        tvCV.setText("" + venda.getVeiculo().getCv());
        tvCor.setText(venda.getVeiculo().getCor());
        tvQuilometros.setText(venda.getVeiculo().getQuilometros());
        tvMatricula.setText(venda.getVeiculo().getMatricula());
        tvNome.setText(venda.getNome());
        tvNif.setText(venda.getNif());
        tvMorada.setText(venda.getMorada());
        tvValor.setText("" + venda.getValor());

        String url = venda.getVeiculo().getImagem();
        url = url.replace("http://backendstand.test/", "http://10.0.2.2:80/");
        Glide.with(InfoVeiculoActivity.this).load(url).placeholder(R.drawable.ic_car).diskCacheStrategy(DiskCacheStrategy.ALL).into(ivCarro);
    }
}