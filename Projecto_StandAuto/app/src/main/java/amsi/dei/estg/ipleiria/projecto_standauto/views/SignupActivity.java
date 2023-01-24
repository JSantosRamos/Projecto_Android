package amsi.dei.estg.ipleiria.projecto_standauto.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import amsi.dei.estg.ipleiria.projecto_standauto.Listener.SignupListener;
import amsi.dei.estg.ipleiria.projecto_standauto.Modelo.Veiculo.SingletonVeiculos;
import amsi.dei.estg.ipleiria.projecto_standauto.R;

public class SignupActivity extends AppCompatActivity implements SignupListener {

    private Button btnSignup;
    private EditText etEmail, etPwd, etNome, etPwdConf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etNome = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmailSignup);
        etPwd = findViewById(R.id.etPasswordSignup);
        etPwdConf = findViewById(R.id.etPasswordSignupConf);
        btnSignup = findViewById(R.id.btnSignupForm);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarSignup();
            }
        });

        SingletonVeiculos.getInstance(this).setSignupListener(this);
    }

    private void validarSignup() {
        String nome = etNome.getText().toString();
        String email = etEmail.getText().toString();
        String password = etPwd.getText().toString();
        String passwordConf = etPwdConf.getText().toString();


        if (!isNomeValido(nome)) {
            etNome.setError(getString(R.string.erroNome));
            return;
        }

        if (!isEmailValido(email)) {
            etEmail.setError(getString(R.string.erroEmail));
            return;
        }

        if (!isPasswordValida(password)) {
            etPwd.setError(getString(R.string.erroPass));
            return;
        }

        if (!isPasswordConfValida(password, passwordConf)) {
            etPwdConf.setError(getString(R.string.erroPassConf));
            return;
        }

        SingletonVeiculos.getInstance(this).signupAPI(nome, email, password, this);
    }

    private boolean isNomeValido(String nome) {

        if (nome.isEmpty()) {
            return false;
        }
        return nome.length() >= 4;
    }

    private boolean isEmailValido(String email) {

        if (email == null || email.isEmpty()) {
            return false;
        }
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isPasswordValida(String password) {

        if (password == null) {
            return false;
        }

        return password.length() >= 8;
    }

    private boolean isPasswordConfValida(String password, String passwordConf) {

        if (passwordConf == null || passwordConf.isEmpty()) {
            return false;
        }

        if (!password.equals(passwordConf)) {
            return false;
        }

        return passwordConf.length() >= 8;
    }

    @Override
    public void onValidateSignup(boolean success, String message, Context context) {
        if (success) {
            Toast.makeText(context, "Registo com sucesso", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        } else {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }
}