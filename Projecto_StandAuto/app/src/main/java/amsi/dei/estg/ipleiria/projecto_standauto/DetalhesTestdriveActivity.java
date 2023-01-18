package amsi.dei.estg.ipleiria.projecto_standauto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import amsi.dei.estg.ipleiria.projecto_standauto.Adaptadores.ListaTestdriveAdaptador;
import amsi.dei.estg.ipleiria.projecto_standauto.Listener.TestdriveListener;
import amsi.dei.estg.ipleiria.projecto_standauto.Modelo.Testdrive.Testdrive;
import amsi.dei.estg.ipleiria.projecto_standauto.Modelo.Veiculos.SingletonVeiculos;

public class DetalhesTestdriveActivity extends AppCompatActivity implements TestdriveListener, AdapterView.OnItemSelectedListener {

    private int idVeiculo;
    private EditText etMotivo;
    private Spinner spinnerHora;
    private String hora;
    private Button btnData;

    private Testdrive testdrive;
    private DatePickerDialog datePickerDialog;

    public static final String ID_VEICULO = "ID_VEICULO";
    public static final String OP_CODE = "OPERATION_CODE";
    public static final int OP_CODE_ADICIONAR = 101;
    public static final int OP_CODE_EDITAR = 100;
    public static final int OP_CODE_APAGAR = 50;

    private String dataAtual;
    private ArrayAdapter<CharSequence> adapterSpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_testdrive);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        spinnerHora = findViewById(R.id.spinnerHoraTestdrive);
        etMotivo = findViewById(R.id.etMotivoTestdrive);
        btnData = findViewById(R.id.btnDataTestedrive);
        FloatingActionButton fabAddTestdrive = findViewById(R.id.floatingAdicionarTestdrive);


        //region Spinner Hora
        adapterSpinner = ArrayAdapter.createFromResource(this,
                R.array.testdriveshora_array, android.R.layout.simple_spinner_item);

        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHora.setAdapter(adapterSpinner);
        spinnerHora.setOnItemSelectedListener(this);

        //endregion

        //region DatePicker Data
        Calendar calendar = Calendar.getInstance();
        int ano = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH);
        int dia = calendar.get(Calendar.DAY_OF_MONTH);

        dataAtual = dia + "-" + mes + 1 + "-" + ano;

        btnData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(DetalhesTestdriveActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        String date = dayOfMonth + "-" + month + "-" + year;
                        btnData.setText(date);
                    }
                }, ano, mes, dia);
                dialog.show();
            }
        });

        //endregion

        int operation = getIntent().getIntExtra(OP_CODE, -1);

        if (operation == OP_CODE_ADICIONAR) {
            idVeiculo = getIntent().getIntExtra(ID_VEICULO, -1);
            btnData.setText(dataAtual);
        } else {
            int id = (int) getIntent().getLongExtra(ListaTestdrivesFragment.TESTDRIVE, -1);
            if (id > -1) {
                testdrive = SingletonVeiculos.getInstance(this).getTestdrive(id);
                if (testdrive != null) {
                    carregarTestdrive(testdrive);
                }
            }
        }

        fabAddTestdrive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adicionarTestdrive();
            }
        });
    }

    private void carregarTestdrive(Testdrive testdrive) {
        btnData.setText(testdrive.getData());
        etMotivo.setText(testdrive.getMotivo());
        if (!testdrive.getHora().isEmpty()) { //nunca é vazio, mas para evitar erros.
            int spinnerPosition = adapterSpinner.getPosition(testdrive.getHora());
            spinnerHora.setSelection(spinnerPosition);
        }
    }

    private void adicionarTestdrive() {

        if (!isMotivoValido()) {
            Toast.makeText(this, "Motivo inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isDataValida()) {
            Toast.makeText(this, "Data inválida", Toast.LENGTH_SHORT).show();
            return;
        }

        if (testdrive == null) {
            Testdrive novoTeste = new Testdrive(0, btnData.getText().toString(), hora, etMotivo.getText().toString(), Testdrive.POR_VER, idVeiculo); //o id é sempre atribuido quando é criado um novo registo pela api.
            SingletonVeiculos.getInstance(this).adicionarTestdriveAPI(novoTeste, this);
            setResult(RESULT_OK);
            finish();
        } else {
            Testdrive novosDadosTeste = new Testdrive(testdrive.getId(), btnData.getText().toString(), hora, etMotivo.getText().toString(), testdrive.getEstado(), testdrive.getIdVeiculo());
            SingletonVeiculos.getInstance(this).editarTestdriveAPI(novosDadosTeste, this);
            setResult(RESULT_OK);
            finish();
        }
    }


    //region Validar Campos
    public boolean isDataValida() {
        String data = btnData.getText().toString();
        try {
            @SuppressLint("SimpleDateFormat") Date date1 = new SimpleDateFormat("dd-MM-yyyy").parse(dataAtual);
            @SuppressLint("SimpleDateFormat") Date date2 = new SimpleDateFormat("dd-MM-yyyy").parse(data);

            return !Objects.requireNonNull(date2).before(date1);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isMotivoValido() {
        String motivo = etMotivo.getText().toString();

        if (motivo.isEmpty()) {
            return false;
        }

        return motivo.length() > 5;
    }
    //endregion

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (testdrive != null) {
            getMenuInflater().inflate(R.menu.menu_detalhe_testedrive_remover, menu);
            return super.onCreateOptionsMenu(menu);
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.itemRemover) {
            dialogRemover();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void dialogRemover() {
        AlertDialog builder = new AlertDialog.Builder(this).
                setTitle("Apagar").
                setIcon(R.drawable.ic_remover).
                setMessage("Tem a certeza que quer apagar?").
                setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SingletonVeiculos.getInstance(getApplicationContext()).removerTestdriveAPI(testdrive, getApplicationContext());
                        Intent intent = new Intent();
                        intent.putExtra(OP_CODE, OP_CODE_APAGAR);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
    }

    @Override
    public void onRefreshDetalhes(int op) {
        Intent intent = new Intent();
        intent.putExtra(OP_CODE, op);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        hora = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}