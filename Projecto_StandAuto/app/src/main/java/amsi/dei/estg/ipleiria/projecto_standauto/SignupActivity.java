package amsi.dei.estg.ipleiria.projecto_standauto;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import amsi.dei.estg.ipleiria.projecto_standauto.Modelo.Veiculos.SingletonVeiculos;

public class SignupActivity extends AppCompatActivity {

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
    }

    private void validarSignup() {
        String nome = etNome.getText().toString();
        String email = etEmail.getText().toString();
        String password = etPwd.getText().toString();
        String passwordConf = etPwdConf.getText().toString();


        if (!isNomeValido(nome)) {
            etNome.setError(getString(R.string.erroNome));
        }

        if (!isEmailValido(email)) {
            etEmail.setError(getString(R.string.erroEmail));
        }

        if (!isPasswordValida(password)) {
            etPwd.setError(getString(R.string.erroPass));
        }

        if (!isPasswordConfValida(password, passwordConf)) {
            etPwdConf.setError(getString(R.string.erroPassConf));
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

        if (email.isEmpty()) {
            return false;
        }
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isPasswordValida(String password) {

        if (password.isEmpty()) {
            return false;
        }

        return password.length() >= 8;
    }

    private boolean isPasswordConfValida(String password, String passwordConf) {

        if (password.isEmpty() || passwordConf.isEmpty()) {
            return false;
        }

        if (!password.equals(passwordConf)) {
            return false;
        }

        return passwordConf.length() >= 8;
    }
}