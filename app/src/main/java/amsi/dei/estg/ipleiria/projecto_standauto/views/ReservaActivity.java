package amsi.dei.estg.ipleiria.projecto_standauto.views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import amsi.dei.estg.ipleiria.projecto_standauto.Modelo.Testdrive.SingletonTestdrive;
import amsi.dei.estg.ipleiria.projecto_standauto.Modelo.Testdrive.Testdrive;
import amsi.dei.estg.ipleiria.projecto_standauto.R;

public class ReservaActivity extends AppCompatActivity {

    private EditText etMorada, etNumero, etNIF;
    private Button btnUpload;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserva);

        etMorada = findViewById(R.id.etMorada);
        etNumero = findViewById(R.id.etNumero);
        etNIF = findViewById(R.id.etNIF);
        btnUpload = findViewById(R.id.btnCarregarImagem);
    }

    private void adicionarReserva() {

      /*  if (!isMotivoValido()) {
            Toast.makeText(this, "Motivo inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isDataValida()) {
            Toast.makeText(this, "Data inválida", Toast.LENGTH_SHORT).show();
            return;
        }
*/

    }
}