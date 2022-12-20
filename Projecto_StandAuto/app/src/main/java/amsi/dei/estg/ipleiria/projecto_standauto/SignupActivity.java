package amsi.dei.estg.ipleiria.projecto_standauto;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SignupActivity extends AppCompatActivity {

    private Button btnSignup;
    private EditText etEmail, etPwd, etNome, etNumero, etNif, etPwdConf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etNome = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmailSignup);
        etNumero = findViewById(R.id.etNumber);
        etNif = findViewById(R.id.etNif);
        etPwd = findViewById(R.id.etPasswordSignup);
        etPwdConf = findViewById(R.id.etPasswordSignupConf);
        btnSignup = findViewById(R.id.btnSignupForm);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarSignup();
            }
        });
    }

    private void validarSignup() {
        if (!isNomeValido()) {
            etNome.setError(getString(R.string.erroNome));
        }

        if (!isEmailValido()) {
            etEmail.setError(getString(R.string.erroEmail));
        }

        if (!isNumeroValido()) {
            etNumero.setError(getString(R.string.erroNumero));
        }

        if (!isNifValido()) {
            etNif.setError(getString(R.string.erroNif));
        }

        if(!isPasswordValida()){
            etPwd.setError(getString(R.string.erroNumero));
        }

        if(!isPasswordConfValida()){
            etPwdConf.setError(getString(R.string.erroPassConf));
        }
    }

    private boolean isNomeValido() {
        String nome = etNome.getText().toString();

        if (nome.isEmpty()) {
            return false;
        }
        return nome.length() >= 4;
    }

    private boolean isEmailValido() {
        String email = etEmail.getText().toString();

        if (email.isEmpty()) {
            return false;
        }
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isNumeroValido() {
        String numero = etNumero.getText().toString();
        if (numero.isEmpty()) {
            return false;
        }
        return numero.length() == 9;
    }

    private boolean isNifValido() {
        String nif = etNif.getText().toString();

        if (nif.isEmpty()) {
            return false;
        }

        return nif.length() == 9;
    }

    private boolean isPasswordValida() {
        String password = etPwd.getText().toString();

        if (password.isEmpty()) {
            return false;
        }

        return password.length() >= 8;
    }

    private boolean isPasswordConfValida() {

        String password = etPwd.getText().toString();
        String passwordConf = etPwdConf.getText().toString();

        if (password.isEmpty() || passwordConf.isEmpty()) {
            return false;
        }

        if (!password.equals(passwordConf)) {
            return false;
        }

        return passwordConf.length() >= 8;
    }
}