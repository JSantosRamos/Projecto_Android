package amsi.dei.estg.ipleiria.projecto_standauto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuVeiculoActivity extends AppCompatActivity {

    private Button btnInfo, btnRegistos;
    private int idVeiculo, idVenda;
    public final static String ID_VEICULO = "ID_VEICULO";
    public final static String ID_VENDA = "ID_VENDA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_veiculo);

        btnInfo = findViewById(R.id.btnMenuVeiculoInfo);
        btnRegistos = findViewById(R.id.btnMenuVeiculoRegistos);
        idVenda = (int) getIntent().getLongExtra(ListaMeusVeiculosFragament.VENDA, -1);

        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent infoVeiculo = new Intent(MenuVeiculoActivity.this, InfoVeiculoActivity.class);
                infoVeiculo.putExtra(ID_VENDA, idVenda);
                startActivity(infoVeiculo);
            }
        });

        btnRegistos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent infoVeiculo = new Intent(MenuVeiculoActivity.this, InfoVeiculoActivity.class);
                infoVeiculo.putExtra(ID_VEICULO, idVeiculo);
                startActivity(infoVeiculo);*/
            }
        });
    }
}